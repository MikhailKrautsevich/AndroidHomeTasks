package com.example.fridge_project.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface FridgeDao {

    @Query("SELECT amount from fridge WHERE food_id = :food_id ")
    LiveData<Double> getCurrentAmountById(int food_id);

    @Query("SELECT amount from fridge WHERE food_id = :food_id ")
    Double getCurrentAmountInDoubleById(int food_id);

    @Query("SELECT * from fridge WHERE food_id = :food_id ")
    Fridge getFridgeNoteById(int food_id);

    @Query("SELECT * from fridge")
    List<Fridge> getAllFridgeNote() ;

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addNewFood(Fridge fridge);

    @Delete
    void deleteNote(Fridge fridge);

    @Update
    void updateAmount(Fridge fridge);

    @Update
    void updateFridgeByFridge(Fridge fridge);
}
