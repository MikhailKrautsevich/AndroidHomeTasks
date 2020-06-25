package com.example.fridge_project.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.fridge_project.repoData.RecipeShortD;

import java.util.List;

@Dao
public interface RecipeDao {

    @Query("SELECT * from recipe")
    LiveData<List<RecipeShortD>> getAllRecipes();

    @Query("SELECT * from recipe WHERE id = :id")
    Recipe getRecipeById(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long addRecipe(Recipe recipe) ;

    @Update
    void updateRecipe(Recipe recipe) ;

    @Delete
    void deleteRecipe(Recipe recipe) ;

    @Query("DELETE from recipe WHERE name = :repTitle")
    void deleteRecipeByName(String repTitle);

    @Query("SELECT * from recipe WHERE name = :name")
    LiveData<RecipeShortD> getRecipeByName(String name);

    @Query("SELECT description from recipe WHERE name = :name")
    LiveData<String> getDescrByName(String name);

    @Query("SELECT id from recipe WHERE name = :name")
    LiveData<Integer> getIdRecipeByName(String name);

}
