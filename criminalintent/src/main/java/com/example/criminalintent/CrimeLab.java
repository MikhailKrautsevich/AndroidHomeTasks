package com.example.criminalintent;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

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
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            for (Crime crime: mCrimes) {
                if (crime.getID().equals(uuid)) {
                    return crime ;
                }
            }
            return null ;
        } else {
            return refactoredGet(uuid) ;}
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private Crime refactoredGet(UUID uuid) {
        {    mCrimes.sort(new CrimeComparator());
            int left = 0 ;
            int right = mCrimes.size();
            int center ;
            int step = 1 ;
            while ((right - left) > 2) {
                center = (right - left)/2 + left ;
                Log.d(LOG, String.format("Step = %d : left = %d, right = %d, center = %d .", step, left, right, center)) ;
                step++ ;
                if (mCrimes.get(center).getID().equals(uuid)) {
                    return mCrimes.get(center) ;
                } else if (uuid.compareTo(mCrimes.get(center).getID()) <0) {
                    right = center ;
                } else {
                    left = center ;
                }
            }
            Log.d(LOG, String.format("(right - left) > 2 : left = %d, right = %d .", left, right)) ;
            center = left + 1 ;
            if (mCrimes.get(center).getID().equals(uuid)) {
                return mCrimes.get(center) ;
            } else if ((uuid.compareTo(mCrimes.get(center).getID()) <0)) {
                return mCrimes.get(left) ;
            } else if ((uuid.compareTo(mCrimes.get(center).getID()) >0)) {
                return mCrimes.get(right) ;
            }
            return new Crime() ;
        }
    }
}
