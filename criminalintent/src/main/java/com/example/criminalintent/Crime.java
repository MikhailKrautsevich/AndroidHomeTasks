package com.example.criminalintent;

import java.util.Date;
import java.util.UUID;

public class Crime {
    private UUID mID ;
    private String mTitle ;
    private Date mDate ;
    private boolean mSolved ;
    
    Crime() {
        this(UUID.randomUUID());
    }

    public Crime(UUID id) {
        mID = id ;
        mDate = new Date() ;
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

    boolean getSolved() {
        return mSolved;
    }

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
