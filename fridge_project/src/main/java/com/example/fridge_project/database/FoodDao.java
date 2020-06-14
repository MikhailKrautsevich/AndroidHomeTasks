package com.example.fridge_project.database;

import androidx.lifecycle.LiveData;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

public interface FoodDao {

    @Query("SELECT * from food")
    LiveData<List<Food>> getAllFood();

    @Query("SELECT id from food WHERE name = :name ")
    int getFoodIdByName(String name) ;

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addNewFood(Food food);

    @Update
    void changeFood(Food food);

    @Delete
    void deleteFood(Food food);
}
