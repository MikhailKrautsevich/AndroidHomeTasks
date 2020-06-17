package com.example.fridge_project.repo;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.fridge_project.database.Food;
import com.example.fridge_project.database.FoodDao;
import com.example.fridge_project.database.Fridge;
import com.example.fridge_project.database.FridgeDao;
import com.example.fridge_project.database.FridgeDataBase;
import com.example.fridge_project.database.IngredientDao;
import com.example.fridge_project.database.Recipe;
import com.example.fridge_project.database.RecipeDao;
import com.example.fridge_project.repoData.FoodData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

public class FridgeRepository {

    private ExecutorService executorService;
    private FoodDao foodDao;
    private FridgeDao fridgeDao;
    private IngredientDao ingredientDao;
    private RecipeDao recipeDao;

    private LiveData<List<Food>> livedataOfAllFood ;
    private LiveData<List<Recipe>> listLiveDataRecipes;

    private static final String LOG_TAG = "MY_LOG_TAG" ;
    private static String MY_LOG = "123q";

    public FridgeRepository(@NonNull final Context context){
        FridgeDataBase fridgeDataBase = FridgeDataBase.getFridgedataBase(context) ;
        executorService = fridgeDataBase.getExecutorService();
        foodDao = fridgeDataBase.getFoodDao() ;
        fridgeDao = fridgeDataBase.getFridgeDao() ;
        ingredientDao = fridgeDataBase.getIngredientDao() ;
        recipeDao = fridgeDataBase.getRecipeDao() ;
    }

    public MutableLiveData<List<FoodData>> getFoodDataList() {
        if (livedataOfAllFood == null) {
            livedataOfAllFood = foodDao.getAllFood();
        }
        MutableLiveData<List<FoodData>> results = (MutableLiveData<List<FoodData>>) Transformations
                .map(livedataOfAllFood, new Function<List<Food>, List<FoodData>>() {
            @Override
            public List<FoodData> apply(List<Food> input) {
                return input.stream().map(new java.util.function.Function<Food, FoodData>() {
                    @Override
                    public FoodData apply(Food food) {
                        String name = food.getName() ;
                        Log.d(MY_LOG, "FrRepo - name = " + name) ;
                        int id = food.getId() ;
                        Log.d(MY_LOG, "FrRepo - id = " + id) ;
                        Double amount = 0.0 ;
//                        amount = fridgeDao.getCurrentAmountInDoubleById(id) ;
                        FoodData foodData = new FoodData(name, amount) ;
                        Log.d(MY_LOG, "FrRepo - amount = " + amount) ;
                        return foodData;
                    }
                }).collect(Collectors.<FoodData>toList());                   // Хоспади, сколько я намучился с этой строчкой, пока нашед куда каст вставить
            }
        });
        return results ;
    }

//    public MutableLiveData<List<FoodData>> getFoodDataList123() {
//        if (livedataOfAllFood == null) {
//            livedataOfAllFood = foodDao.getAllFood();
//        }
//        MutableLiveData<List<FoodData>> results = (MutableLiveData<List<FoodData>>) Transformations
//                .switchMap(livedataOfAllFood, new Function<List<Food>, LiveData<List<FoodData>>>() {
//                    @Override
//                    public MutableLiveData<List<FoodData>> apply(List<Food> input) {
//                        List<FoodData> f =  input.stream().map(new java.util.function.Function<Food, FoodData>() {
//                            @Override
//                            public FoodData apply(Food food) {
//                                String name = food.getName() ;
//                                Log.d(MY_LOG, "FrRepo - name = " + name) ;
//                                final int id = food.getId() ;
//                                Log.d(MY_LOG, "FrRepo - id = " + id) ;
//
//                                FoodData foodData = new FoodData(name, amountLD.getValue()) ;
//                                Log.d(MY_LOG, "FrRepo - amount = " + amountLD.getValue()) ;
//                                return foodData;
//                            }
//                        }).collect(Collectors.<FoodData>toList());                   // Хоспади, сколько я намучился с этой строчкой, пока нашед куда каст вставить
//                        MutableLiveData <List<FoodData>> res = new MutableLiveData<>() ;
//                        res.postValue(f);
//                        return res ;
//                    }
//                });
//        return results ;
//    }

//    public LiveData<List<FoodData>> getFoodDataList() {
//        if (livedataOfAllFood == null) {
//            livedataOfAllFood = foodDao.getAllFood() ;
//        }
//        MutableLiveData<List<FoodData>> resultLiveData = new MutableLiveData<>();
//        List<FoodData> results = new ArrayList<>() ;
//        for (Food food : livedataOfAllFood.getValue()) {
//            String name = food.getName() ;
//            int id = food.getId() ;
//            LiveData<Double> amount = fridgeDao.getCurrentAmountById(id) ;
//            FoodData foodData = new FoodData(name, amount.getValue()) ;
//            results.add(foodData) ;
//        }
//        resultLiveData.postValue(results);
//        return resultLiveData ;
//    }

    public void changeAmount(FoodData foodData) {
        final String name  = foodData.getName() ;
        final Double amount = foodData.getAmount() ;
        Log.d(LOG_TAG, "Repo - Обновляю количество еды " + name + " на " + amount) ;
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                int food_id  = foodDao.getFoodIdByName(name) ;
                Fridge frNote = fridgeDao.getFridgeNoteById(food_id) ;
                frNote.setAmount(amount);
                fridgeDao.updateAmount(frNote);
            }
        });
    }

    public void addNewFood(final FoodData foodData) {
        final Food food = new Food(foodData.getName()) ;
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                foodDao.addNewFood(food);
                int food_id = foodDao.getFoodIdByName(foodData.getName()) ;
                Fridge fridge = new Fridge(foodData.getAmount() , food_id) ;
                fridgeDao.addNewFood(fridge);
            }
        });
    }

    public LiveData<List<FoodData>> getAllFoodDataInList() {
        return foodDao.getAllFoodDataInList() ;
    }

    public List<Food> getFoodList() {
        final ArrayList<Food> foods = new ArrayList<>() ;
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                foods.add(new Food("ZEgg")) ;
                foods.addAll(foodDao.getListOfAllFood())  ;
            }
        });
        return foods ;
    }

    public List<Fridge> getAllFrN() {
        final ArrayList<Fridge> fr = new ArrayList<>() ;
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                fr.addAll(fridgeDao.getAllFridgeNote()) ;
            }
        });
        return fr ;
    }
}
