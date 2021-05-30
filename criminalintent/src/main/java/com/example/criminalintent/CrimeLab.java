package com.example.criminalintent;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

class CrimeLab {

    private static CrimeLab sCrimeLab ;
    private static final String LOG = "CrimeLab" ;

    private LinkedHashMap<UUID, Crime> mCrimes ;

    private CrimeLab(Context context) {
        Log.d(LOG, "Constructor started") ;
        mCrimes = new LinkedHashMap<>() ;
        for (int i = 0; i < 10; i++) {
            Crime crime = new Crime();
            crime.setTitle("Crime # " + i);
            crime.setSolved(i % 2 == 0);
            mCrimes.put(crime.getID(), crime) ;
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

    void addCrime(Crime crime) {
        if (mCrimes != null) {
            mCrimes.put(crime.getID(), crime) ;
        }
    }

    List<Crime> getCrimes(){
        return new ArrayList<>(mCrimes.values()) ;
    }

    Crime getCrime(UUID uuid) {
        return mCrimes.get(uuid) ;
    }
}
