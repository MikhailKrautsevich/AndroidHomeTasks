package com.example.fridge_project.repoData;

import androidx.lifecycle.LiveData;

import java.util.List;

public class RecipeFullD {
    private int id ;
    private String name ;
    private String description ;
    private LiveData<List<FoodData>>  ingredients ;

    public RecipeFullD(int id, String name, String description, LiveData<List<FoodData>> ingredients) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.ingredients = ingredients;
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

    public LiveData<List<FoodData>> getIngredients() {
        return ingredients;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIngredients(LiveData<List<FoodData>> ingredients) {
        this.ingredients = ingredients;
    }
}
