package com.example.fridge_project.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.fridge_project.repoData.FoodData;

import java.util.List;

@Dao
public interface FoodDao {

    @Query("SELECT * from food")
    LiveData<List<Food>> getAllFood();

    @Query("SELECT * from food")
    List<Food> getListOfAllFood();

    @Query("SELECT id from food WHERE name = :name ")
    int getFoodIdByName(String name) ;

    @Query("SELECT food.name, fridge.amount from food, fridge WHERE fridge.food_id == food.id")
    LiveData<List<FoodData>> getAllFoodDataInList() ;

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addNewFood(Food food);

    @Update
    void changeFood(Food food);

    @Delete
    void deleteFood(Food food);
}
