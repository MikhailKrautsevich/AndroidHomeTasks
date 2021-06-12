package com.example.criminalintent.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;


@Database(entities = {CrimeEntity.class}, version = 1)
@TypeConverters(CrimeTypeConverter.class)
public abstract class CrimeDataBase extends RoomDatabase {
    public abstract CrimeDao getDao() ;
}
