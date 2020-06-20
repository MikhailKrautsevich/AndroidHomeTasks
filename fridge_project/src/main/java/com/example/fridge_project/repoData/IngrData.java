package com.example.fridge_project.repoData;

public class IngrData {

    private int id ;
    private String name ;
    private int recipe_id ;
    private double amount ;

    public IngrData(int id, String name, int recipe_id, double amount) {
        this.id = id;
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

    public void setName(String name) {
        this.name = name;
    }

    public void setRecipe_id(int recipe_id) {
        this.recipe_id = recipe_id;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
