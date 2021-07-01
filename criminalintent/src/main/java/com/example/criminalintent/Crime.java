package com.example.criminalintent;

import android.util.Log;

import java.util.Date;
import java.util.UUID;

public class Crime {

    private static final String LOG = "Crime_log" ;

    private UUID mID ;
    private String mTitle ;
    private Date mDate ;
    private String mSuspect ;
    private boolean mSolved ;
    
    Crime() {
        this(UUID.randomUUID());
        Log.d(LOG, " Crime()") ;
    }

    public Crime(UUID id) {
        mID = id ;
        mDate = new Date();
        Log.d(LOG, " Crime(UUID id) : mDate = " + mDate.toString()) ;
    }

    UUID getID() {
        return mID;
    }

    String getTitle() {
        return mTitle;
    }

    Date getDate() {
        return mDate;
    }

    String getSuspect() {return mSuspect; }

    boolean getSolved() {
        return mSolved;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public void setSuspect(String suspect) {mSuspect = suspect; }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }

    public String getPhotoFilename() {
        return "IMG_" + getID().toString() + ".jpg";
    }
}
