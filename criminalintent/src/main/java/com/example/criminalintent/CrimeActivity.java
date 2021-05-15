package com.example.criminalintent;

import androidx.fragment.app.Fragment;

public class CrimeActivity extends SingleFragmentActivity {

    private static final String LOG = "CrimeActivity_log" ;

    @Override
    protected Fragment createFragment() {
        return new CrimeFragment();
    }
}
