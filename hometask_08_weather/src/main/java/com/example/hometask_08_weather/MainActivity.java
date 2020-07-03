package com.example.hometask_08_weather;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    private ImageButton settings ;
    private ImageButton addCity ;
    private TextView cityNameTV ;
    private TextView temperature ;
    private TextView weatherDescription ;
    private RecyclerView weatherRecycler ;

    private final int REQUEST_FOR_PERM = 123;
    private static final String LOG_TAG = "myLogs";

    private String cityWanted ;
    private String degreesType ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        settings = findViewById(R.id.setting);
        addCity = findViewById(R.id.addNewCity) ;
        cityNameTV = findViewById(R.id.cityName) ;
        temperature = findViewById(R.id.temperature) ;
        weatherDescription = findViewById(R.id.weatherDescr) ;
        weatherRecycler = findViewById(R.id.recyclerWeather) ;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        getSettings();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            getWeatherData();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED) {
                getWeatherData() ;
            }
            else {requestPermissions(new String[]{
                    Manifest.permission.INTERNET}, REQUEST_FOR_PERM);
            }
        }

    }

    private void getWeatherData() {
        String forCurrent = makeTextOfCurrentWeatherCall() ;

    }

    private String makeTextOfCurrentWeatherCall(){
        final String API_KEY = "1df12535dec466abefcb634971ab0b15" ;
        final String example = "api.openweathermap.org/data/2.5/weather?q={%s}&appid={%s}" ;
        Log.d(LOG_TAG, "MA - Метод makeTextOfCurrentWeatherCall()");
        return String.format(example, cityWanted, API_KEY) ;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_FOR_PERM) {
            if (grantResults.length > 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getWeatherData();
            } else {
                Toast.makeText(getApplicationContext(), "I need permissions", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void getSettings() {
        String degreesKey = "degreesKey" ;
        String celsius = "Celsius" ;
        String fahrenheit = "Fahrenheit" ;
        String cityKey = "cityKey" ;
        String london = "London" ;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this) ;
        if (preferences.getString(degreesKey, celsius).equals(celsius)) {
            degreesType = celsius ;
        } else {
            degreesType = fahrenheit ;
        }
        if (preferences.getString(cityKey, london).equals(london)) {
            cityWanted = london ;
        } else {
            cityWanted = preferences.getString(cityKey, london) ;
        }
        Log.d(LOG_TAG, "MA - Метод getSettings() degreesType = " + degreesType + " , City = " + cityWanted);
    }


}
