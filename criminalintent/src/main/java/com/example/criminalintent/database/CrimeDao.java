package com.example.criminalintent.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.criminalintent.Crime;

import java.util.List;
import java.util.UUID;

@Dao
public interface CrimeDao {

    @Query("SELECT mID, mTitle, mDate, mSolved from CrimeEntity")
    List<Crime> getCrimes() ;

    @Query("SELECT * from CrimeEntity where mId = :mId")
    Crime getCrime(UUID mId) ;

    @Delete
    void deleteCrime(Crime crime) ;

    @Update
    void  updateCrime(Crime crime) ;

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addCrime(Crime crime) ;
}
