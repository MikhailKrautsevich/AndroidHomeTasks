package com.example.fridge_project.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.fridge_project.repoData.IngrData;

import java.util.List;

@Dao
public interface IngredientDao {

    @Query("SELECT * from recipe_ingredients WHERE recipe_id = :recipe_id")
    LiveData<List<IngrData>> getAllNeededIngredientsByRecipeId(int recipe_id);

    @Query("SELECT * from recipe_ingredients WHERE id = :id")
    LiveData<Ingredients> getIngeredientById(int id);

    @Query("SELECT * from recipe_ingredients")
    LiveData<List<Ingredients>> getAllIn() ;

    @Update
    void updateIngredient(Ingredients ingredient);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addIngredient(Ingredients ingredient);

    @Delete
    void deleteIngredient(Ingredients ingredient);
}
