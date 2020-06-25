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
                deferred = true)} )
public class Ingredients {
    @PrimaryKey(autoGenerate = true)
    private int id ;
    private String name ;
    private int recipe_id ;
    private double amount ;

    public Ingredients(String name, int recipe_id, double amount) {
        this.name = name;
        this.recipe_id = recipe_id;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getRecipe_id() {
        return recipe_id;
    }

    public double getAmount() {
        return amount;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIngredient_name(String ingredient_name) {
        this.name = ingredient_name;
    }

    public void setRecipe_id(int recipe_id) {
        this.recipe_id = recipe_id;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
