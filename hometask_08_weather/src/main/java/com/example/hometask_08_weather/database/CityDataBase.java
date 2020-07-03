package com.example.hometask_08_weather.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {CityEntity.class}, version = 1)
public abstract class CityDataBase extends RoomDatabase {

    private final static int CORE_NUMBER = Runtime.getRuntime().availableProcessors();
    private static volatile CityDataBase INSTANCE;

    private ExecutorService executorService = Executors.newFixedThreadPool(CORE_NUMBER);

    public ExecutorService getExecutorService() {
        return executorService;
    }

    public abstract CityDao getDao() ;

    public static CityDataBase getFridgeDataBase(@NonNull final Context context) {
        if (INSTANCE == null) {
            synchronized (CityDataBase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context, CityDataBase.class, "data_base")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
