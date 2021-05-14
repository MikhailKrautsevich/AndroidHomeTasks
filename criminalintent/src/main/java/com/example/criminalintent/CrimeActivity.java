package com.example.criminalintent;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class CrimeActivity extends AppCompatActivity {

    private static final String LOG = "CrimeActivity_log" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager() ;
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container) ;

        if (fragment == null) {
            fragment = new CrimeFragment() ;
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit() ;
            Log.d(LOG, "CrimeActivity: if(fragment == null)-branch") ;
        }
    }
}
