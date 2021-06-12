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
    private String mSolved ;

    public CrimeEntity(Crime crime) {
        mID = crime.getID() ;
        mTitle = crime.getTitle() ;
        mDate = crime.getDate() ;
        mSolved = String.valueOf(crime.getSolved());
    }

    public CrimeEntity(){}

    public UUID getID() {
        return mID;
    }

    public void setID(UUID ID) {
        mID = ID;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public String getSolved() {
        return mSolved;
    }

    public void setSolved(String solved) {
        mSolved = solved;
    }
}
