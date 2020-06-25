package com.example.fridge_project.database;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;


@Entity(tableName = "food", indices = {@Index(value = {"name"}, unique = true)})
public class Food {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;

    public Food(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
