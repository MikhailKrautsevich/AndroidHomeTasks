package com.example.fridge_project.database;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "fridge", foreignKeys = @ForeignKey(entity = Food.class, parentColumns = "id", childColumns = "food_id"))
public class Fridge {
    @PrimaryKey(autoGenerate = true)
    private int id ;
    private double amount ;
    private int food_id ;

    public Fridge(double amount, int food_id) {
        this.amount = amount;
        this.food_id = food_id;
    }

    public int getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public int getFood_id() {
        return food_id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setFood_id(int food_id) {
        this.food_id = food_id;
    }
}
