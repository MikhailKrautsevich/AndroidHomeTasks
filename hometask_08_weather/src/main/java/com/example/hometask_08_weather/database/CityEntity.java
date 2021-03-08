package com.example.hometask_08_weather.database;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices ={@Index(value = {"name"}, unique = true)} )
public class CityEntity {

    @PrimaryKey(autoGenerate = true)
    private int id ;
    private String name ;
    private double lat ;
    private double lon ;

    public CityEntity(String name, double lat, double lon) {
        this.name = name;
        this.lat = lat;
        this.lon = lon;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public double getLon() {
        return lon;
    }

    public double getLat() {
        return lat;
    }
}
