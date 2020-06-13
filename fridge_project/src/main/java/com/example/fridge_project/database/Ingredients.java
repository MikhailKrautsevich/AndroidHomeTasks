package com.example.fridge_project.database;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity (tableName = "recipe_ingredients",
        foreignKeys = { @ForeignKey
                (entity = Recipe.class ,
                parentColumns = "id",
                childColumns = "recipe_id",
                onDelete = ForeignKey.CASCADE,
                deferred = true),
                @ForeignKey(entity = Food.class ,
                parentColumns = "id",
                childColumns = "food_id" ,
                onDelete = ForeignKey.CASCADE,
                deferred = true) } )
public class Ingredients {
    @PrimaryKey(autoGenerate = true)
    private int id ;
    private int food_id ;
    private int recipe_id ;
    private double amount ;

    public Ingredients(int food_id, int recipe_id, double amount) {
        this.food_id = food_id;
        this.recipe_id = recipe_id;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public int getFood_id() {
        return food_id;
    }

    public int getRecipe_id() {
        return recipe_id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFood_id(int food_id) {
        this.food_id = food_id;
    }

    public void setRecipe_id(int recipe_id) {
        this.recipe_id = recipe_id;
    }
}
