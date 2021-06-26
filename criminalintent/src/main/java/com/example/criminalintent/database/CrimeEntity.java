package com.example.criminalintent.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.criminalintent.Crime;

import java.util.Date;
import java.util.UUID;

@Entity(indices = {@Index(value = "mID")})
@TypeConverters({CrimeTypeConverter.class})
public class CrimeEntity {

    @PrimaryKey
    @NonNull
    private UUID mID ;
    private String mTitle ;
    private Date mDate ;
    private boolean mSolved ;

    public CrimeEntity(Crime crime) {
        mID = crime.getID() ;
        mTitle = crime.getTitle() ;
        mDate = crime.getDate() ;
        mSolved = crime.getSolved();
    }

    @SuppressWarnings(value = "unused")
    CrimeEntity(){}

    @NonNull
    UUID getID() {
        return mID;
    }

    @SuppressWarnings(value = "unused")
    public void setID(UUID ID) {
        mID = ID;
    }

    String getTitle() {
        return mTitle;
    }

    @SuppressWarnings(value = "unused")
    public void setTitle(String title) {
        mTitle = title;
    }

    Date getDate() {
        return mDate;
    }

    @SuppressWarnings(value = "unused")
    public void setDate(Date date) {
        mDate = date;
    }

    boolean getSolved() {
        return mSolved;
    }

    @SuppressWarnings(value = "unused")
    public void setSolved(boolean solved) {
        mSolved = solved;
    }
}
