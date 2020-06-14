package com.example.fridge_project.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Food.class, Fridge.class, Ingredients.class, Recipe.class},
        version = 1)
public abstract class FridgeDataBase extends RoomDatabase {

    private final static int CORE_NUMBER = Runtime.getRuntime().availableProcessors();
    private static volatile FridgeDataBase INSTANCE;

    private ExecutorService executorService = Executors.newFixedThreadPool(CORE_NUMBER);

    public ExecutorService getExecutorService() {
        return executorService;
    }

    public abstract FoodDao getFoodDao();

    public abstract FridgeDao getFridgeDao();

    public abstract IngredientDao getIngredientDao();

    public abstract RecipeDao getRecipeDao();

    public static FridgeDataBase getFridgedataBase(@NonNull final Context context) {
        if (INSTANCE == null) {
            synchronized (FridgeDataBase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context, FridgeDataBase.class, "fr_data_base")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
