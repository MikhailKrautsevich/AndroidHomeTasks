package com.example.hometask_06;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class ActivityAdd extends Activity {

    private EditText nameAddText ;
    private EditText phoneNumberAdd ;
    private EditText emailAdd ;
    private RadioButton phoneNumberRadButton ;
    private RadioButton emailRadButton ;
    private Intent answerIntent ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        answerIntent = new Intent() ;
        setResult(RESULT_CANCELED, answerIntent);

        phoneNumberRadButton = findViewById(R.id.phoneNumberRadButton) ;
        emailRadButton = findViewById(R.id.emailRadButton) ;

        nameAddText = findViewById(R.id.nameAddText) ;
        phoneNumberAdd = findViewById(R.id.phoneNumberAdd) ;
        emailAdd = findViewById(R.id.emailAdd) ;
        emailAdd.setVisibility(View.INVISIBLE);

        ImageButton backFromAdd = findViewById(R.id.backFromAdd);
        backFromAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED, answerIntent);
                finish();
            }
        });
        ImageButton addConfirm = findViewById(R.id.addConfirm);
        addConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameAddText.getText().toString().trim();
                String email = emailAdd.getText().toString().trim();
                String phoneNumber = phoneNumberAdd.getText().toString().trim();
                if (!phoneNumberRadButton.isChecked() && !emailRadButton.isChecked()) {
                    Toast.makeText(getApplicationContext(), R.string.choosecontype, Toast.LENGTH_SHORT)
                            .show();
                }
                if (phoneNumberRadButton.isChecked() && !emailRadButton.isChecked() ){
                    Toast.makeText(getApplicationContext(), R.string.wilsavephnumber, Toast.LENGTH_SHORT)
                            .show();
                    if (answerIntent != null && !name.isEmpty() && !phoneNumber.isEmpty() && phoneNumber.matches("\\+?[0-9]*")) {
                        answerIntent.putExtra(EXTRAS.EXTRA_FOR_CONTACT_NAME, nameAddText.getText()
                        .toString()) ;
                        answerIntent.putExtra(EXTRAS.EXTRA_FOR_CONTACT_INFO, phoneNumberAdd.getText()
                        .toString());
                        answerIntent.putExtra(EXTRAS.EXTRA_FOR_CONTACT_IS, false) ;
                    setResult(RESULT_OK, answerIntent);
                    finish();}
                    else {Toast.makeText(getApplicationContext(), R.string.somethingwrongwithc, Toast.LENGTH_SHORT)
                            .show();}
                }
                if (!phoneNumberRadButton.isChecked() &&  emailRadButton.isChecked()) {
                    Toast.makeText(getApplicationContext(), R.string.willsaveemail, Toast.LENGTH_SHORT)
                            .show();
                    if (answerIntent != null && !name.isEmpty() && !email.isEmpty() && !email.matches("[а-яёА-ЯЁ]*")) {
                        answerIntent.putExtra(EXTRAS.EXTRA_FOR_CONTACT_NAME, nameAddText.getText()
                                .toString()) ;
                        answerIntent.putExtra(EXTRAS.EXTRA_FOR_CONTACT_INFO, emailAdd.getText()
                                .toString());
                        answerIntent.putExtra(EXTRAS.EXTRA_FOR_CONTACT_IS, true) ;
                    setResult(RESULT_OK, answerIntent);
                    finish();}
                    else {Toast.makeText(getApplicationContext(), R.string.somethingwrongwithc, Toast.LENGTH_SHORT)
                            .show();}
                }
                if ( phoneNumberRadButton.isChecked() && emailRadButton.isChecked()) {
                    Toast.makeText(getApplicationContext(), R.string.youhavetochoose, Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });
    }

    public void onRadioClick(View view) {
        RadioButton rb = (RadioButton) view ;
        switch (rb.getId()) {
            case R.id.phoneNumberRadButton:
                phoneNumberAdd.setVisibility(View.VISIBLE);
                emailAdd.setVisibility(View.INVISIBLE);
                break;
            case R.id.emailRadButton:
                phoneNumberAdd.setVisibility(View.INVISIBLE);
                emailAdd.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }
}
