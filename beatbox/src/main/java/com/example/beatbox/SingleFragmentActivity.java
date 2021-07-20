package com.example.beatbox;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public abstract class SingleFragmentActivity extends AppCompatActivity {

    private static final String LOG = "SingleFragmentActivity" ;

    protected abstract Fragment createFragment() ;

    @LayoutRes
    protected int getLayoutResId(){
        return R.layout.activity_fragment ;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());

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
