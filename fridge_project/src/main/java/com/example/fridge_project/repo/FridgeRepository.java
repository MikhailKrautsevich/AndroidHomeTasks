package com.example.fridge_project.repo;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.fridge_project.database.FoodDao;
import com.example.fridge_project.database.FridgeDao;
import com.example.fridge_project.database.FridgeDataBase;
import com.example.fridge_project.database.IngredientDao;
import com.example.fridge_project.database.RecipeDao;

import java.util.concurrent.ExecutorService;

public class FridgeRepository {

    private ExecutorService executorService;
    private FoodDao foodDao;
    private FridgeDao fridgeDao;
    private IngredientDao ingredientDao;
    private RecipeDao recipeDao;

    public FridgeRepository(@NonNull final Context context){
        FridgeDataBase fridgeDataBase = FridgeDataBase.getFridgedataBase(context) ;
        executorService = fridgeDataBase.getExecutorService();
        foodDao = fridgeDataBase.getFoodDao() ;
        fridgeDao = fridgeDataBase.getFridgeDao() ;
        ingredientDao = fridgeDataBase.getIngredientDao() ;
        recipeDao = fridgeDataBase.getRecipeDao() ;
    }
}
