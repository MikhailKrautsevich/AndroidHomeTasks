package com.example.hometask_06.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {ContactEntity.class}, version = 1)
public abstract class ConDataBase extends RoomDatabase {

    public abstract ContactDao getConDao() ;
}
