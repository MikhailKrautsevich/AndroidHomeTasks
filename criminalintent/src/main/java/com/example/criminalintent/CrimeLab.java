package com.example.criminalintent;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CrimeLab {

    private static CrimeLab sCrimeLab ;
    private static final String LOG = "CrimeLab" ;

    private List<Crime> mCrimes ;

    private CrimeLab(Context context) {
        Log.d(LOG, "Constructor started") ;
        mCrimes = new ArrayList<>() ;
        for (int i = 0; i < 100; i++) {
            Crime crime = new Crime();
            crime.setTitle("Crime # " + i);
            crime.setSolved(i % 2 == 0);
            mCrimes.add(crime) ;
            Log.d(LOG, "Crime #" + i + " added.") ;
        }
        Log.d(LOG, "Constructor finished, mCrimes.size() = " + mCrimes.size()) ;
    }

    static CrimeLab get(Context context) {
        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab(context) ;
        }
        Log.d(LOG, "CrimeLab get(Context context)") ;
        return sCrimeLab ;
    }

    List<Crime> getCrimes(){
        return mCrimes ;
    }

    public Crime getCrime(UUID uuid) {
        for (Crime crime: mCrimes) {
            if (crime.getID().equals(uuid)) {
                return crime ;
            }
        }
        return null ;
    }
}
