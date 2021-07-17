package com.example.criminalintent;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.Date;
import java.util.UUID;

public class Crime {

    private static final String LOG = "Crime_log";

    private UUID mID;
    private String mTitle;
    private Date mDate;
    private String mSuspect;
    private boolean mSolved;

    Crime() {
        this(UUID.randomUUID());
        Log.d(LOG, " Crime()");
    }

    public Crime(UUID id) {
        mID = id;
        mDate = new Date();
        Log.d(LOG, " Crime(UUID id) : mDate = " + mDate.toString());
    }

    @NonNull
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("mID = ").append(mID).append(", ")
                .append("mTitle = ");
        if (mTitle == null) {
            stringBuilder.append("null");
        } else stringBuilder.append(mTitle) ;
        stringBuilder.append(", mDate = ").append(mDate);
        return stringBuilder.toString();
    }

    UUID getID() {
        return mID;
    }

    String getTitle() {
        return mTitle;
    }

    Date getDate() {
        Log.d(LOG, "Crime: mTitle " + mTitle + ", Date is " + mDate.toString());
        return mDate;
    }

    String getSuspect() {
        return mSuspect;
    }

    boolean getSolved() {
        return mSolved;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public void setSuspect(String suspect) {
        mSuspect = suspect;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }

    public String getPhotoFilename() {
        return "IMG_" + getID().toString() + ".jpg";
    }
}
