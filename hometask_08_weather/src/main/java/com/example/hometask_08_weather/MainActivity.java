package com.example.hometask_08_weather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hometask_08_weather.database.CityDao;
import com.example.hometask_08_weather.database.CityDataBase;
import com.example.hometask_08_weather.database.CityEntity;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private TextView cityNameTV ;
    private TextView temperature ;
    private TextView weatherDescription ;
    private RecyclerView weatherRecycler ;
    private ImageView weatherPic ;

    private static final String LOG_TAG = "myLogs";
    private static final String CELSIUS = "Celsius" ;
    private static final String FAHRENHEIT = "Fahrenheit" ;

    static final String API_KEY = "1df12535dec466abefcb634971ab0b15" ;

    private String cityWanted ;
    private String degreesType ;

    private OkHttpClient okHttpClient = new OkHttpClient() ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView settings = findViewById(R.id.setting);
        ImageButton addCity = findViewById(R.id.addNewCity);
        cityNameTV = findViewById(R.id.cityName) ;
        temperature = findViewById(R.id.temperatureText) ;
        weatherDescription = findViewById(R.id.weatherDescription) ;
        weatherPic = findViewById(R.id.weatherPic) ;
        weatherRecycler = findViewById(R.id.recyclerWeather) ;
        weatherRecycler.setLayoutManager(new LinearLayoutManager(this));
        weatherRecycler.setAdapter(new WeatherAdapter(new HourlyWeather[0]));

        addCity.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CityAddActivity.class) ;
            intent.putExtra("cityWanted", cityWanted) ;
            startActivity(intent);
        });

        settings.setOnClickListener(v -> {
            Log.d(LOG_TAG, "MA - Settings was clicked" );
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class) ;
            intent.putExtra("degreesKey", degreesType) ;
            startActivity(intent);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        getSettings();
        getWeatherData();
        getHourlyWeather();
        cityNameTV.setText(cityWanted);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this) ;
        preferences.edit().putString("cityKey", cityWanted).apply();
    }

    private void getWeatherData() {
        final String urlForCurrent = makeTextOfCurrentWeatherCall() ;
        final Request curRequest = new Request.Builder().url(urlForCurrent).build() ;
        Log.d(LOG_TAG, "MA - Метод getWeatherData() - создал реквест");

        CompletableFuture.supplyAsync(() -> {
            try {
                Log.d(LOG_TAG, "MA - Метод makeTextOfCurrentWeatherCall() = 1" );
                return okHttpClient.newCall(curRequest).execute();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }).thenApply(response -> {
            if (response != null && response.isSuccessful()) {
                try {
                    Log.d(LOG_TAG, "MA - Метод makeTextOfCurrentWeatherCall() = 2" );
                    return response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }).thenApply(response -> {
            if (response != null) {
                try {
                    Log.d(LOG_TAG, "MA - Метод makeTextOfCurrentWeatherCall() = 3" );
                    WeatherParser weatherParser = new WeatherParser(response);
                    Log.d(LOG_TAG, "MA - Метод makeTextOfCurrentWeatherCall() = 3 : get lon" + weatherParser.getTestString());
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

                weatherDescription.setText(weather.getDescription());
                String temprText ;
                if (degreesType.equals(CELSIUS)) {
                    temprText = " \u2103" ;
                } else {
                    temprText = " \u2109" ;
                }
                temperature.setText(weather.getTemperature() + temprText);
                Picasso.with(MainActivity.this)
                        .load(weather.getIconURL())
                        .placeholder(R.drawable.weather_icon)
                        .error(R.drawable.weather_icon)
                        .into(weatherPic);
            }
        },ContextCompat.getMainExecutor(this));
    }

    private String makeTextOfCurrentWeatherCall(){
        final String example = "http://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s&units=%s" ;
        String type = null ;
        if (degreesType.equals(CELSIUS)) {
            type = "metric" ;
        } else if (degreesType.equals(FAHRENHEIT)) {
            type = "imperial" ;
        }
        Log.d(LOG_TAG, "MA - Метод makeTextOfCurrentWeatherCall() = " + String.format(example, cityWanted, API_KEY, type) );
        return String.format(example, cityWanted, API_KEY, type) ;
    }

    private void getHourlyWeather() {
        CompletableFuture.supplyAsync(new Supplier<Coordinations>() {
            @Override
            public Coordinations get() {
                if (cityWanted.equals("London")) {
                    return new Coordinations(-0.13, 51.51) ;
                } else {
                    CityDataBase dataBase = CityDataBase.getDataBase(MainActivity.this) ;
                    CityDao dao = dataBase.getDao() ;
                    CityEntity city = dao.getCityByName(cityWanted) ;
                    Log.d(LOG_TAG, "MA - Метод getHourlyWeather() = 1 , city = " + city.getName());
                    return new Coordinations(city.getLon(), city.getLat()) ;
                }
            }
        }).thenApply(new Function<Coordinations, Response>() {
            @Override
            public Response apply(Coordinations coordinations) {
                try {
                    Log.d(LOG_TAG, "MA - Метод getHourlyWeather() = 2");
                    return okHttpClient.newCall(makeHourlyRequest(coordinations)).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                    return null ;
                }
            }
        }).thenApply(new Function<Response, HourlyWeather[]>() {
            @Override
            public HourlyWeather[] apply(Response response) {
                if (response != null) {
                    try {
                        Log.d(LOG_TAG, "MA - Метод getHourlyWeather() = 3");
                        WeatherParser parser = new WeatherParser(response.body().string()) ;
                        return parser.getHourly() ;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return new HourlyWeather[0];
                    }
                } else {
                    return new HourlyWeather[0];
                }
            }
        }).thenAcceptAsync(new Consumer<HourlyWeather[]>() {
            @Override
            public void accept(HourlyWeather[] hourlyWeathers) {
                Log.d(LOG_TAG, "MA - Метод getHourlyWeather() = 4");
                weatherRecycler.setAdapter(new WeatherAdapter(hourlyWeathers));
            }
        }, ContextCompat.getMainExecutor(this)) ;
    }

    private Request makeHourlyRequest (Coordinations coordinations){
        String exampleHourly = "http://api.openweathermap.org/data/2.5/onecall?lat=%f&lon=%f&exclude=current,minutely,daily&appid=%s" ;
        Log.d(LOG_TAG, "!!!!MA - Метод makeHourlyRequest (Coordinations coordinations) , STARTED");
        double lat = coordinations.getLat() ;
        Log.d(LOG_TAG, "!!!!MA - Метод makeHourlyRequest (Coordinations coordinations) , lat = " + lat);
        double lon = coordinations.getLon() ;
        Log.d(LOG_TAG, "!!!!MA - Метод makeHourlyRequest (Coordinations coordinations) , lon = " + lon);
        String url = String.format(exampleHourly, lat, lon, API_KEY) ;
        Log.d(LOG_TAG, "!!!!MA - Метод makeHourlyRequest (Coordinations coordinations) , make = " + url);
        return new Request.Builder().url(url).build() ;
    }

    private void getSettings() {
        String degreesKey = "degreesKey" ;
        String cityKey = "cityKey" ;
        String london = "London" ;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this) ;
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
        Log.d(LOG_TAG, "MA - Метод getSettings() degreesType = " + degreesType + " , City = " + cityWanted);
    }

    class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherHolder>{

        private HourlyWeather[] weathers ;

        public WeatherAdapter(HourlyWeather[] weathers) {
            this.weathers = weathers;
        }

        @NonNull
        @Override
        public WeatherHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.weather_element, parent, false);
            return new WeatherHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull WeatherHolder holder, int position) {
            if (weathers[position] != null) {
                holder.bindData(weathers[position]);
            }
        }

        @Override
        public int getItemCount() {
            return weathers.length;
        }

        class WeatherHolder extends RecyclerView.ViewHolder{

            private TextView timeTextView ;
            private TextView descrTextView ;
            private TextView tempTextView ;
            private ImageView iconWeatherRec ;

            WeatherHolder(@NonNull View itemView) {
                super(itemView);

                timeTextView = itemView.findViewById(R.id.timeRec) ;
                descrTextView = itemView.findViewById(R.id.weatherDescrRec) ;
                tempTextView = itemView.findViewById(R.id.temperatureRec) ;
                iconWeatherRec = itemView.findViewById(R.id.iconWeatherRec) ;
            }

            void bindData(HourlyWeather weather) {
                timeTextView.setText(weather.getTime());
                descrTextView.setText(weather.getDescription());
                if (degreesType.equals(FAHRENHEIT)) {
                    tempTextView.setText(weather.getDegreesFahrenheit());
                } else {tempTextView.setText(weather.getDegreesCelcius());}
                Picasso.with(MainActivity.this)
                        .load(weather.getIconUrl())
                        .placeholder(R.drawable.weather_icon)
                        .error(R.drawable.weather_icon)
                        .into(iconWeatherRec);
            }
        }
    }
}
