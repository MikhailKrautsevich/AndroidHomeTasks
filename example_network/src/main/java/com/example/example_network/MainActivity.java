package com.example.example_network;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity {

    private static final String API_KEY = "3491520b1da85a557a8b092551c46a2d";
    private static final String CITY = "Minsk";

    private static final String URL = "http://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s";

    private OkHttpClient okHttpClient = new OkHttpClient() ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
