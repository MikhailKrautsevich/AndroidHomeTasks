package com.example.criminalintent;

import java.util.Date;
import java.util.UUID;

class Crime {
    private UUID mID ;
    private String mTitle ;
    private Date mDate ;
    private boolean mSolved ;
    
    Crime() {
        mID = UUID.randomUUID() ;
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

    void setTitle(String title) {
        mTitle = title;
    }

    void setDate(Date date) {
        mDate = date;
    }

    void setSolved(boolean solved) {
        mSolved = solved;
    }
}
