package com.example.fridge_project.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
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

    @Query("SELECT * from food WHERE name = :name ")
    Food getFoodByName(String name) ;

    @Query("SELECT food.name, fridge.amount from food, fridge WHERE fridge.food_id == food.id")
    LiveData<List<FoodData>> getAllFoodDataInList() ;

    @Query("DELETE from food where name = :name")
    void deleteFoodByName(String name) ;

    @Query("DELETE from food where id = :id")
    void deleteFoodById(int id) ;

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addNewFood(Food food);

    @Update
    void updateFoodByFood(Food food);

    @Query("SELECT food.name, fridge.amount from food, fridge WHERE food.name = :name")
    LiveData<FoodData> getFoodLDByName(String name);
}
