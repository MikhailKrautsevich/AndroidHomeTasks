package com.example.hometask_08_weather;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class SettingsActivity extends Activity {

    private TextView degreesSettings ;

    private SharedPreferences preferences ;

    private String text2 ;

    private static final String DEGREES_KEY = "degreesKey" ;
    private static final String CELSIUS = "Celsius" ;
    private static final String FAHRENHEIT = "Fahrenheit" ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        preferences = PreferenceManager.getDefaultSharedPreferences(this) ;
        String text = preferences.getString("degreesKey", CELSIUS) ;
        if (text.equals(CELSIUS)) {
            text2 = FAHRENHEIT ;
        } else {
            text2 = CELSIUS ;
        }


        degreesSettings = findViewById(R.id.degreesSettings) ;
        degreesSettings.setText(text);
        Switch switchSettings = findViewById(R.id.switchSettings);
        switchSettings.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                degreesSettings.setText(text2);
                preferences.edit().putString(DEGREES_KEY,text2).apply();
            } else {
                degreesSettings.setText(text);
                preferences.edit().putString(DEGREES_KEY,text).apply();
            }
        });
    }
}
