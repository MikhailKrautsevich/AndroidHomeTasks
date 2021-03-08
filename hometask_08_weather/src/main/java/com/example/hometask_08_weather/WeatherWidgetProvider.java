package com.example.hometask_08_weather;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.core.content.ContextCompat;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class WeatherWidgetProvider extends AppWidgetProvider {

    private static final String LOG_TAG = "myLogs";
    private static final String CELSIUS = "Celsius" ;
    private static final String FAHRENHEIT = "Fahrenheit" ;

    static final String API_KEY = "1df12535dec466abefcb634971ab0b15" ;

    private String cityWanted ;
    private String degreesType ;

    OkHttpClient okHttpClient = new OkHttpClient() ;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        Arrays.stream(appWidgetIds).forEach(id -> setDataToWidget(context, appWidgetManager, id));
    }

    private void setDataToWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetIds) {
        Intent intent = new Intent(context, WeatherWidgetProvider.class) ;
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds) ;

        getSettings(context);

        final String urlForCurrent = makeTextOfCurrentWeatherCall() ;
        final Request curRequest = new Request.Builder().url(urlForCurrent).build() ;
        Log.d(LOG_TAG, "WWP - Метод getWeatherData() - создал реквест");

        CompletableFuture.supplyAsync(() -> {
            try {
                Log.d(LOG_TAG, "WWP - Метод makeTextOfCurrentWeatherCall() = 1" );
                return okHttpClient.newCall(curRequest).execute();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }).thenApply(response -> {
            if (response != null && response.isSuccessful()) {
                try {
                    Log.d(LOG_TAG, "WWP - Метод makeTextOfCurrentWeatherCall() = 2" );
                    return response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }).thenApply(response -> {
            if (response != null) {
                try {
                    Log.d(LOG_TAG, "WWP - Метод makeTextOfCurrentWeatherCall() = 3" );
                    WeatherParser weatherParser = new WeatherParser(response);
                    Log.d(LOG_TAG, "WWP - Метод makeTextOfCurrentWeatherCall() = 3 : get lon" + weatherParser.getTestString());
                    return new WeatherParser(response).parseData();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }).thenAcceptAsync(weather -> {
            if (weather != null) {
                Log.d(LOG_TAG, "MA - Метод makeTextOfCurrentWeatherCall() = 4" );
                Log.d(LOG_TAG, "MA - Метод makeTextOfCurrentWeatherCall() = 4 " +  weather.getDescription() + " | " + (weather.getTemperature() + "") );

                RemoteViews remoteViews = new RemoteViews(context.getPackageName() , R.layout.widget_layout) ;
                remoteViews.setTextViewText(R.id.widgetCityTV, cityWanted);
                remoteViews.setTextViewText(R.id.widgetDateTV, weather.getDate());
                remoteViews.setTextViewText(R.id.widgetDescrTV, weather.getDescription());
                remoteViews.setTextViewText(R.id.widgetTempTV, getTemperatureString(weather));

                try {
                    Picasso.with(context)
                            .load(weather.getIconURL())
                            .into(new Target() {
                                @Override
                                public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                                    Log.d(LOG_TAG, "WWP - setDataToWidget() = onBitmapLoaded - started" );
                                    remoteViews.setImageViewBitmap(R.id.widgetIcon, bitmap);
                                    Log.d(LOG_TAG, "WWP - setDataToWidget() = onBitmapLoaded - was set" );
                                    appWidgetManager.updateAppWidget(appWidgetIds , remoteViews);
                                    Log.d(LOG_TAG, "WWP - setDataToWidget() = !!onBitmapLoaded!! - Успешно " );
                                }

                                @Override
                                public void onBitmapFailed(Drawable errorDrawable) {
                                    Log.d(LOG_TAG, "WWP - setDataToWidget() = onBitmapFailed" );
                                }

                                @Override
                                public void onPrepareLoad(Drawable placeHolderDrawable) {
                                    Log.d(LOG_TAG, "WWP - setDataToWidget() = onPrepareLoad" );
                                }
                            });
                    Log.d(LOG_TAG, "WWP - setDataToWidget() = Успешно" );
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d(LOG_TAG, "WWP - setDataToWidget() = Exception" );
                    appWidgetManager.updateAppWidget(appWidgetIds , remoteViews);

//                    appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widgetCityTV);
//                    appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widgetDateTV);
//                    appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widgetDescrTV);
//                    appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widgetTempTV);
//                    appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widgetIcon);
                }
            }
        }, ContextCompat.getMainExecutor(context));
    }

    private void getSettings(Context context) {
        String degreesKey = "degreesKey" ;
        String cityKey = "cityKey" ;
        String london = "London" ;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context) ;
        if (preferences.getString(degreesKey, CELSIUS).equals(CELSIUS)) {
            degreesType = CELSIUS ;
        } else {
            degreesType = FAHRENHEIT ;
        }
        if (preferences.getString(cityKey, london).equals(london)) {
            cityWanted = london ;
        } else {
            cityWanted = preferences.getString(cityKey, london) ;
        }
        Log.d(LOG_TAG, "WWP - Метод getSettings() degreesType = " + degreesType + " , City = " + cityWanted);
    }

    private String makeTextOfCurrentWeatherCall(){
        final String example = "http://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s&units=%s" ;
        String type = null ;
        if (degreesType.equals(CELSIUS)) {
            type = "metric" ;
        } else if (degreesType.equals(FAHRENHEIT)) {
            type = "imperial" ;
        }
        Log.d(LOG_TAG, "WWP - Метод makeTextOfCurrentWeatherCall() = " + String.format(example, cityWanted, API_KEY, type) );
        return String.format(example, cityWanted, API_KEY, type) ;
    }

    private String getTemperatureString(WeatherData weatherData) {
        String temprText ;
        if (degreesType.equals(CELSIUS)) {
            temprText = " \u2103" ;
        } else {
            temprText = " \u2109" ;
        }
        return weatherData.getTemperature() + temprText ;
    }
}
