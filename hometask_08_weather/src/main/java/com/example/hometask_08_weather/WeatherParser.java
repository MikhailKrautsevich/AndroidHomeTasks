package com.example.hometask_08_weather;

import android.annotation.SuppressLint;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

class WeatherParser {

    private static final String LOG_TAG = "myLogs";

    private String json ;

    WeatherParser(String json) {
        this.json = json;
        Log.d(LOG_TAG, "WeatherParser - create new WP");
    }

    WeatherData parseData() {
        try {
            String description = null;
            String temperature = null;
            String iconUrl = null;
            String date = null ;

            JSONObject jsonObject = new JSONObject(json) ;
            JSONObject weatherObject = jsonObject.getJSONArray("weather").getJSONObject(0) ;
            JSONObject mainObject = jsonObject.getJSONObject("main") ;

            if (jsonObject != null) {
                long unix = jsonObject.getLong("dt") ;
                date = getTimeAndDate(unix) ;
            }

            if (weatherObject != null) {
                description = weatherObject.getString("description");
                iconUrl =  "http://openweathermap.org/img/wn/" + weatherObject.getString("icon") + "@2x.png";
                Log.d(LOG_TAG, "WeatherParser - getCoords() : iconUrl = " + iconUrl);
            }
            if (mainObject != null) {
                temperature = mainObject.getString("temp") ;
            }
            return new WeatherData(Double.parseDouble(temperature), description, iconUrl, date) ;
        } catch (JSONException e) {
            e.printStackTrace();
            return new WeatherData(1000.0, "Smth wrong", "" , "");
        }
    }

    String getTestString() throws JSONException {
        JSONObject jsonObject = new JSONObject(json) ;
        JSONObject coordObject = jsonObject.getJSONObject("coord") ;

        return coordObject.getString("lon") ;
    }

    double[] getCoords() throws JSONException {
        JSONObject jsonObject = new JSONObject(json) ;
        JSONObject coordObject = jsonObject.getJSONObject("coord") ;
        double lat = coordObject.getDouble("lat") ;
        Log.d(LOG_TAG, "WeatherParser - getCoords() : lat = " + lat);
        double lon = coordObject.getDouble("lon") ;
        Log.d(LOG_TAG, "WeatherParser - getCoords() : lon = " + lon);

        return new double[]{lat, lon};
    }

    HourlyWeather[] getHourly() throws JSONException {
        Log.d(LOG_TAG, "WeatherParser - getHourly() : STARTED");
        HourlyWeather[] result = new HourlyWeather[24] ;

        JSONObject jsonObject = new JSONObject(json) ;
        JSONArray jsonObjArray = jsonObject.getJSONArray("hourly") ;
        for (int i = 0 ; i<24 ;i++) {
            JSONObject object = jsonObjArray.getJSONObject(i);
            long unix = object.getLong("dt") ;
            String time = getTime(unix) ;
            Log.d(LOG_TAG, "WeatherParser - getHourly() : time = " + time);

            double degreesCelvin = object.getDouble("temp") ;
            double degreesCelcius = degreesCelvin - 273 ;
            double degreesFahrenheit =  9*(degreesCelvin -273)/5 + 32 ;

            String sDegreesCelcius = String.format("%.2f", degreesCelcius).concat("\u2103") ;
            String sDegreesFahrenheit = String.format("%.2f", degreesFahrenheit).concat("\u2109") ;

//            Log.d(LOG_TAG, "WeatherParser - getHourly() : sDegreesCelvin = " + sDegreesCelvin);
//            Log.d(LOG_TAG, "WeatherParser - getHourly() : sDegreesCelcius = " + sDegreesCelcius);
//            Log.d(LOG_TAG, "WeatherParser - getHourly() : sDegreesFahrenheit = " + sDegreesFahrenheit);

            JSONObject weatherObject = object.getJSONArray("weather").getJSONObject(0) ;

            String description = weatherObject.getString("description") ;
            String iconUrl = "http://openweathermap.org/img/wn/" + weatherObject.getString("icon") + "@2x.png";

            Log.d(LOG_TAG, "WeatherParser - getHourly() : description = " + description);

            result[i] = new HourlyWeather(time, sDegreesCelcius, sDegreesFahrenheit, description, iconUrl) ;
        }
        return result ;
    }

    private String getTime(long unix) {
        Date date = new Date(unix*1000L);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("HH:mm") ;
        return sdf.format(date) ;
    }

    private String getTimeAndDate(long unix) {
        Date date = new Date(unix*1000L);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm") ;
        return sdf.format(date) ;
    }
}
