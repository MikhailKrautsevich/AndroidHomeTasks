package com.example.hometask_08_weather.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CityDao {

    @Query("SELECT cityEntity.name from CityEntity")
    public LiveData<List<String>> getAllCityNames() ;

    @Query("Delete from CityEntity where name =:name")
    public void deleteCityByName (String name) ;

    @Query("SELECT * from CITYENTITY where name =:name")
    public CityEntity getCityByName(String name) ;

    @Insert
    public void addCity(CityEntity city) ;
}
