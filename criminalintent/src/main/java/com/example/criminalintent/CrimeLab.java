package com.example.criminalintent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.criminalintent.database.CrimeBaseHelper;
import com.example.criminalintent.database.CrimeCursorWrapper;
import com.example.criminalintent.database.CrimeDbSchema.CrimeTable.Cols;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.example.criminalintent.database.CrimeDbSchema.*;

class CrimeLab {

    private static CrimeLab sCrimeLab ;
    private static final String LOG = "CrimeLab" ;

    private Context mContext ;
    private SQLiteDatabase mDatabase ;

    private CrimeLab(Context context) {
        Log.d(LOG, "Constructor started") ;
        mContext = context.getApplicationContext() ;
        mDatabase = new CrimeBaseHelper(mContext)
                .getReadableDatabase() ;
    }

    static CrimeLab get(Context context) {
        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab(context) ;
        }
        Log.d(LOG, "CrimeLab get(Context context)") ;
        return sCrimeLab ;
    }

    private static ContentValues getContentValues(Crime crime) {
        ContentValues values = new ContentValues() ;
        values.put(Cols.UUID, crime.getID().toString());
        values.put(Cols.TITLE, crime.getTitle());
        values.put(Cols.DATE, crime.getDate().getTime());
        values.put(Cols.SOLVED, crime.getSolved() ? 1 : 0);
        return values ;
    }

    private CrimeCursorWrapper queryCrime(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(CrimeTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null) ;
        return new CrimeCursorWrapper(cursor) ;
    }

    void addCrime(Crime crime) {
        ContentValues values = getContentValues(crime) ;

        mDatabase.insert(CrimeTable.NAME, null, values ) ;
    }

    void updateCrime (Crime crime) {
        String uuidString = crime.getID().toString() ;
        ContentValues values = getContentValues(crime) ;

        mDatabase.update(CrimeTable.NAME, values,
                Cols.UUID + " = ?",
                new String[] {uuidString}) ;
    }

    List<Crime> getCrimes(){
        ArrayList<Crime> crimes = new ArrayList<>() ;

        try (CrimeCursorWrapper cursor = queryCrime(null, null)) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                crimes.add(cursor.getCrime());
                cursor.moveToNext();
            }
        }
        return crimes ;
    }

    Crime getCrime(UUID uuid) {
        try (CrimeCursorWrapper cursor = queryCrime(
                Cols.UUID + " = ?",
                new String[]{uuid.toString()})) {
            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getCrime();
        }
    }

    void deleteCrime(UUID id) {
        mDatabase.delete( CrimeTable.NAME,
                Cols.UUID + " = ?",
                new String[] {id.toString()} ) ;
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
