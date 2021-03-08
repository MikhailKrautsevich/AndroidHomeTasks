package com.example.hometask_02_view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Custom custom = new Custom(this) ;
        setContentView(custom);

        custom.setListener(new Custom.CustomListener(){
            @Override
            public void viewClicked(int x, int y) {
                if (toast != null){
                    toast.cancel();
                }
                 toast = Toast.makeText(MainActivity.this, getCoordString(x, y), Toast.LENGTH_SHORT);
                 toast.show();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (toast != null) {
            toast.cancel();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (toast != null) {
            toast = null;
        }
    }

    @SuppressLint("DefaultLocale")
    private static String getCoordString (int x, int y) {
        return String.format("Нажаты координаты [%d; %d]", x, y) ;
    }
}
