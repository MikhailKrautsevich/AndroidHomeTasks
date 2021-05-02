package com.Android2021_TB_2017_01_geoquiz;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button mTrueButton ;
    private Button mFalseButton ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTrueButton = findViewById(R.id.true_button) ;
        mFalseButton = findViewById(R.id.false_button) ;

        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast t = Toast.makeText(MainActivity.this,
                        R.string.correct_toast,
                        Toast.LENGTH_SHORT) ;
//                t.setGravity(Gravity.TOP, 0, 250) ;
                t.show();
            }
        });

        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast t = Toast.makeText(MainActivity.this,
                        R.string.incorrect_toast,
                        Toast.LENGTH_SHORT) ;
//                t.setGravity(Gravity.TOP, 0, 250) ;
                t.show();
            }
        });
    }
}
