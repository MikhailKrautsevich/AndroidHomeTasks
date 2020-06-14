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
public interface RecipeDao {

    @Query("SELECT * from recipe")
    LiveData<List<Recipe>> getAllRecipes();

    @Query("SELECT * from recipe WHERE id = :id")
    Recipe getRecipeById(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addRecipe(Recipe recipe) ;

    @Update
    void updateRecipe(Recipe recipe) ;

    @Delete
    void deleteRecipe(Recipe recipe) ;
}
