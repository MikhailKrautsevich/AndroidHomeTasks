package com.example.criminalintent;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.criminalintent.database.CrimeTypeConverter;

import java.util.Date;
import java.util.UUID;

@Entity(indices = {@Index(value = "mID")})
@TypeConverters({CrimeTypeConverter.class})
public class Crime {

    private static final String LOG = "Crime_log" ;

    @PrimaryKey
    @NonNull
    private UUID mID ;
    private String mTitle ;
    private Date mDate ;
    private boolean mSolved ;
    
    public Crime() {
        this(UUID.randomUUID());
        Log.d(LOG, " Crime()") ;
    }

    private Crime(UUID id) {
        mID = id ;
        mDate = new Date();
        Log.d(LOG, " Crime(UUID id) : mDate = " + mDate.toString()) ;
    }

//    static CrimeEntity createEntity(Crime crime) {
//        return new CrimeEntity(crime) ;
//    }

    public UUID getID() {
        return mID;
    }

    public String getTitle() {
        return mTitle;
    }

    public Date getDate() {
        return mDate;
    }

    public boolean getSolved() {
        return mSolved;
    }

    public void setID(UUID id) {mID = id ;}

    public void setTitle(String title) {
        mTitle = title;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }
}
