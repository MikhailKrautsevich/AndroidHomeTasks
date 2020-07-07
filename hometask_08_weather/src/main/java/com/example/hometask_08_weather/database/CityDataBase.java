package com.example.hometask_08_weather.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {CityEntity.class}, version = 1)
public abstract class CityDataBase extends RoomDatabase {

    private static volatile CityDataBase INSTANCE;

    public abstract CityDao getDao() ;

    public static CityDataBase getDataBase(@NonNull final Context context) {
        if (INSTANCE == null) {
            synchronized (CityDataBase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room
                            .databaseBuilder(context, CityDataBase.class , "dataBase").build() ;
                }
            }
        }
        return INSTANCE;
    }
}
