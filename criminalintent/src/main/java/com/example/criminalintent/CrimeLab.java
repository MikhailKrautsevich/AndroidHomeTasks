package com.example.criminalintent;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.room.Room;

import com.example.criminalintent.database.CrimeDao;
import com.example.criminalintent.database.CrimeDataBase;

import java.util.List;
import java.util.UUID;

public class CrimeLab {

    private static CrimeLab sCrimeLab ;
    private static final String LOG = "CrimeLab" ;
    private static final String DATABASENAME = "CrimeDataBase" ;

    private final Context mContext ;
    private final CrimeDataBase mDatabase ;
    private final CrimeDao mDao ;

    private CrimeLab(Context context) {
        Log.d(LOG, "Constructor started") ;
        mContext = context.getApplicationContext() ;
        mDatabase = Room.databaseBuilder(mContext,
                CrimeDataBase.class,
                DATABASENAME).
//                allowMainThreadQueries().
                build() ;
        mDao = mDatabase.getDao() ;
    }

    public static CrimeLab get(Context context) {
        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab(context) ;
        }
        Log.d(LOG, "CrimeLab get(Context context)") ;
        return sCrimeLab ;
    }

    void addCrime(final Crime crime) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mDao.addCrime(crime);
            }
        }).start();
            }

    void updateCrime (final Crime crime) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mDao.updateCrime(crime);
            }
        }).start();
    }

    LiveData<List<Crime>> getCrimes(){
        return mDao.getCrimes() ;
    }

    List<Crime> getListCrimes(){
        return mDao.getListOfCrimes() ;
    }

    LiveData<Crime> getCrime(UUID uuid) {
        return mDao.getLVCrime(uuid) ;
    }

    void deleteCrime(final UUID id) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Crime crime = mDao.getCrime(id) ;
                mDao.deleteCrime(crime);
            }
        }).start();
    }

//    void autoInit10Crimes(LinkedHashMap<UUID, Crime> map) {
//        for (int i = 0; i < 10; i++) {
//            Crime crime = new Crime();
//            crime.setTitle("Crime # " + i);
//            crime.setSolved(i % 2 == 0);
//            map.put(crime.getID(), crime) ;
//            Log.d(LOG, "Crime #" + i + " added.") ;
//        }
//    }
}
