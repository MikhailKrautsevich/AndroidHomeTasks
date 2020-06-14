package com.example.fridge_project.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface FridgeDao {

    @Query("SELECT * from fridge WHERE food_id = :food_id ")
    int getCurrentAmountById(int food_id);

    @Update
    void updateCurrentAmountById(Food food);

    @Delete
    void deleteNote(Food food);
}
