package com.example.hometask_08_weather.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CityDao {

    @Query("SELECT cityEntity.name from CityEntity")
    LiveData<List<String>> getAllCityNames() ;

    @Query("SELECT * from CITYENTITY where name =:name")
    CityEntity getCityByName(String name) ;

    @Insert
    void addCity(CityEntity city) ;
}
