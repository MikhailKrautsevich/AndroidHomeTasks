package com.example.criminalintent;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public abstract class SingleFragmentActivity extends AppCompatActivity {

    private static final String LOG = "SingleFragmentActivity" ;

    protected abstract Fragment createFragment() ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        FragmentManager fragmentManager = getSupportFragmentManager() ;
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container) ;

        if (fragment == null) {
            fragment = createFragment() ;
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit() ;
            Log.d(LOG, "SingleFragmentActivity: if(fragment == null)-branch") ;
        }
    }
}
