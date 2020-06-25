package com.example.fridge_project.repoData;

public class FoodData {

    private String name;
    private Double amount;

    public FoodData(String name, Double amount) {
        this.name = name;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public Double getAmount() {
        return amount;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAmount(Double amount) {
        this.amount = amount ;
    }
}
