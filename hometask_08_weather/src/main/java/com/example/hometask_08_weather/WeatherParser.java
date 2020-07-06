package com.example.hometask_08_weather;

import android.annotation.SuppressLint;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WeatherParser {

    private static final String LOG_TAG = "myLogs";

    private String json ;

    public WeatherParser(String json) {
        this.json = json;
        Log.d(LOG_TAG, "WeatherParser - create new WP");
    }

    public WeatherData parseData() {
        try {
            String description = null;
            String temperature = null;

            JSONObject jsonObject = new JSONObject(json) ;
            JSONObject weatherObject = jsonObject.getJSONArray("weather").getJSONObject(0) ;
            JSONObject mainObject = jsonObject.getJSONObject("main") ;

            if (weatherObject != null) {
                description = weatherObject.getString("description");
            }
            if (mainObject != null) {
                temperature = mainObject.getString("temp") ;
            }
            return new WeatherData(Double.parseDouble(temperature), description) ;
        } catch (JSONException e) {
            e.printStackTrace();
            return new WeatherData(1000.0, "Smth wrong" );
        }
    }

    public String getTestString() throws JSONException {
        JSONObject jsonObject = new JSONObject(json) ;
        JSONObject coordObject = jsonObject.getJSONObject("coord") ;

        return coordObject.getString("lon") ;
    }

    public double[] getCoords() throws JSONException {
        JSONObject jsonObject = new JSONObject(json) ;
        JSONObject coordObject = jsonObject.getJSONObject("coord") ;
        double lat = coordObject.getDouble("lat") ;
        Log.d(LOG_TAG, "WeatherParser - getCoords() : lat = " + lat);
        double lon = coordObject.getDouble("lon") ;
        Log.d(LOG_TAG, "WeatherParser - getCoords() : lon = " + lon);

        return new double[]{lat, lon};
    }

    public HourlyWeather[] getHourly() throws JSONException {
        Log.d(LOG_TAG, "WeatherParser - getHourly() : STARTED");
        HourlyWeather[] result = new HourlyWeather[12] ;

        JSONObject jsonObject = new JSONObject(json) ;
        JSONArray jsonObjArray = jsonObject.getJSONArray("hourly") ;
        for (int i = 0 ; i<12 ;i++) {
            JSONObject object = jsonObjArray.getJSONObject(i);
            long unix = object.getLong("dt") ;
            Date date = new Date(unix*1000L);
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("HH:mm") ;
            String time = sdf.format(date) ;
            Log.d(LOG_TAG, "WeatherParser - getHourly() : time = " + time);

            Double degreesCelvin = object.getDouble("temp") ;
            Double degreesCelcius = degreesCelvin - 273 ;
            Double degreesFahrenheit =  9*(degreesCelvin -273)/5 + 32 ;

            String sDegreesCelvin = String.format("%.2f", degreesCelvin) ;
            String sDegreesCelcius = String.format("%.2f", degreesCelcius).concat("\u2103") ;
            String sDegreesFahrenheit = String.format("%.2f", degreesFahrenheit).concat("\u2109") ;

            Log.d(LOG_TAG, "WeatherParser - getHourly() : sDegreesCelvin = " + sDegreesCelvin);
            Log.d(LOG_TAG, "WeatherParser - getHourly() : sDegreesCelcius = " + sDegreesCelcius);
            Log.d(LOG_TAG, "WeatherParser - getHourly() : sDegreesFahrenheit = " + sDegreesFahrenheit);


            String description = object.getJSONArray("weather").getJSONObject(0).getString("description") ;

            Log.d(LOG_TAG, "WeatherParser - getHourly() : description = " + description);

            result[i] = new HourlyWeather(time, sDegreesCelcius, sDegreesFahrenheit + "", description) ;
        }

        return result ;
    }
}
