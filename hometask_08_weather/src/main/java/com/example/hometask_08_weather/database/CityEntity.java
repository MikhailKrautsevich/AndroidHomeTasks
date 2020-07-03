package com.example.hometask_08_weather.database;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices ={@Index(value = {"name"}, unique = true)} )
public class CityEntity {

    @PrimaryKey(autoGenerate = true)
    private int id ;
    private String name ;

    public CityEntity(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }
}
