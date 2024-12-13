package com.example.hometask_06;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.room.Room;

import com.example.hometask_06.database.ConDataBase;
import com.example.hometask_06.database.ContactDao;
import com.example.hometask_06.database.ContactEntity;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ActivityEdit extends Activity {

    private TextView phoneNumEdit ;
    private TextView emailEdit ;
    private TextView nameEdit;
    private Intent answerIntent ;

    private ConDataBase dataBase ;
    private ContactDao contactDao ;
    private ContactEntity entity ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Intent gettedIntend = getIntent();
        answerIntent = new Intent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        dataBase = Room.databaseBuilder(getApplication(), ConDataBase.class, "database")
                .build() ;
        contactDao = dataBase.getConDao() ;

        ImageButton backFromEdit = findViewById(R.id.backFromEdit);
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

        final String name = gettedIntend.getStringExtra(EXTRAS.EXTRA_FOR_CONTACT_NAME) ;
        final boolean isItEmail = gettedIntend.getBooleanExtra(EXTRAS.EXTRA_FOR_CONTACT_IS, false) ;
        final String text = gettedIntend.getStringExtra(EXTRAS.EXTRA_FOR_CONTACT_INFO) ;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                entity = contactDao.getByNameAndText(name, text) ;
            }
        }) ;
        thread.start();

        nameEdit.setText(name);
        if (isItEmail) {emailEdit.setText(text);
                        phoneNumEdit.setVisibility(View.INVISIBLE);}
        else {emailEdit.setVisibility(View.INVISIBLE);
                        phoneNumEdit.setText(text);}
 //       Toast.makeText(this, "pos getted " + positionToRemove, Toast.LENGTH_SHORT).show();


        Button removeContact = findViewById(R.id.removeButton);
        removeContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityEdit.this) ;
                builder.setMessage(R.string.remove_contact)
                        .setPositiveButton(R.string.remove, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setResult(RESULT_OK, answerIntent);
                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                contactDao.deleteContact(entity);
                            }
                        }) ;
                        thread.start();
                        ActivityEdit.this.finish();
                    }
                })
                        .setNeutralButton(R.string.cancel, null);
                AlertDialog dialog = builder.create() ;
                dialog.show();

                DisplayMetrics metrics = new DisplayMetrics(); //get metrics of screen
                ActivityEdit.this.getWindowManager().getDefaultDisplay().getMetrics(metrics);
                int height = (int) (metrics.heightPixels*0.2);
                int width = (int) (metrics.widthPixels*0.75);

                dialog.getWindow().setLayout(width, height);
            }
        });

        Button editContact = findViewById(R.id.editButton);
        editContact.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                CompletableFuture.supplyAsync(new Supplier<ContactEntity>() {
                    @Override
                    public ContactEntity get() {
                        String newName = nameEdit.getText().toString().trim() ;
                        String newNumber = phoneNumEdit.getText().toString().trim() ;
                        String newEmail = emailEdit.getText().toString().trim() ;
                        if (!newName.isEmpty()) {
                            entity.setName(newName);
                        }
                        if (isItEmail) {
                            if (!newEmail.isEmpty()) {
                                entity.setNumberOrEmail(newEmail);
                            }
                        }
                        else if (!isItEmail) {
                            if (!newNumber.isEmpty()) {
                                entity.setNumberOrEmail(newNumber);
                            }
                        }
                        return entity;
                    }
                }).thenAcceptAsync(new Consumer<ContactEntity>() {
                    @Override
                    public void accept(ContactEntity contactEntity) {
                        contactDao.updateContact(entity);
                        Toast.makeText(ActivityEdit.this, "Contact was edited", Toast.LENGTH_SHORT).show();
                    }
                }) ;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        contactDao = null ;
        dataBase.close();
        dataBase = null ;
    }
}
