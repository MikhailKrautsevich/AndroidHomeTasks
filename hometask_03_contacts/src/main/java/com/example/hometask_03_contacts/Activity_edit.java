package com.example.hometask_03_contacts;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class Activity_edit extends Activity {

    ImageButton backFromEdit;
    Button removeContact ;
    TextView phoneNumEdit, emailEdit, nameEdit;
    Intent answerIntent, gettedIntend ;
    int positionToRemove;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        gettedIntend = getIntent();
        answerIntent = new Intent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        backFromEdit = findViewById(R.id.backFromEdit);
        backFromEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED, answerIntent);
                finish();
            }
        });

        nameEdit = findViewById(R.id.nameEdit);
        emailEdit = findViewById(R.id.emailEdit);
        phoneNumEdit = findViewById(R.id.phoneNumEdit);

        nameEdit.setText(gettedIntend.getStringExtra(EXTRAS.EXTRA_FOR_CONTACT_NAME));
        boolean isItEmail = gettedIntend.getBooleanExtra(EXTRAS.EXTRA_FOR_CONTACT_IS, false) ;
        if (isItEmail) {emailEdit.setText((gettedIntend.getStringExtra(EXTRAS.EXTRA_FOR_CONTACT_INFO)));
                        phoneNumEdit.setVisibility(View.INVISIBLE);}
        else {emailEdit.setVisibility(View.INVISIBLE);
                        phoneNumEdit.setText(gettedIntend.getStringExtra(EXTRAS.EXTRA_FOR_CONTACT_INFO));}
        positionToRemove = gettedIntend.getIntExtra(EXTRAS.EXTRA_FOR_CON_REMOVE, 99) ;
 //       Toast.makeText(this, "pos getted " + positionToRemove, Toast.LENGTH_SHORT).show();


                removeContact = findViewById(R.id.removeButton) ;
        removeContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK, answerIntent);
                answerIntent.putExtra(EXTRAS.EXTRA_FOR_CON_REMOVE, positionToRemove) ;
                finish();
            }
        });
    }
}
