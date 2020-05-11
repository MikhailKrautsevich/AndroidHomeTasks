package com.example.hometask_02_view;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Custom custom = new Custom(this) ;
        setContentView(custom);
        custom.setListener(new Custom.CustomListener(){
            @Override
            public void viewClicked(int x, int y) {
                Toast.makeText(MainActivity.this, getCoordString(x, y), Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    private static String getCoordString (int x, int y) {
        return String.format("Нажаты координаты [%d; %d]", x, y) ;
    }
}
