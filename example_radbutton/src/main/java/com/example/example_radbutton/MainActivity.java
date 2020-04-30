package com.example.example_radbutton;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.graphics.Color;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView mInfoTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mInfoTextView = (TextView)findViewById(R.id.current_pick);

        RadioButton redRadioButton = (RadioButton)findViewById(R.id.radio_red);
        redRadioButton.setOnClickListener(radioButtonClickListener);

        RadioButton greenRadioButton = (RadioButton)findViewById(R.id.radio_green);
        greenRadioButton.setOnClickListener(radioButtonClickListener);

        RadioButton blueRadioButton = (RadioButton)findViewById(R.id.radio_blue);
        blueRadioButton.setOnClickListener(radioButtonClickListener);

        RadioButton grayRadioButton = (RadioButton)findViewById(R.id.radio_gray);
        grayRadioButton.setOnClickListener(radioButtonClickListener);
    }

    View.OnClickListener radioButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RadioButton rb = (RadioButton)v;
            switch (rb.getId()) {
                case R.id.radio_red: mInfoTextView.setBackgroundColor(Color.parseColor("#ff0000"));
                    break;
                case R.id.radio_green: mInfoTextView.setBackgroundColor(Color.parseColor("#0000ff"));
                    break;
                case R.id.radio_blue: mInfoTextView.setBackgroundColor(Color.parseColor("#00ff00"));
                    break;
                case R.id.radio_gray: mInfoTextView.setBackgroundColor(Color.parseColor("#666666"));
                    break;

                default:
                    break;
            }
        }
    };
}
