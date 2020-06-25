package com.example.fridge_project.database;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "recipe", indices = {@Index(value = {"name"}, unique = true)})
public class Recipe {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String description;

    public Recipe(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.name = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
