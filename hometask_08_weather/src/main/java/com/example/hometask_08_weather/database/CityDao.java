package com.example.hometask_08_weather.database;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CityDao {

    @Query("SELECT name from CityEntity")
    public List<String> getAllCities() ;

    @Query("Delete from CityEntity where name =:name")
    public void deleteCityByName (String name) ;
}
