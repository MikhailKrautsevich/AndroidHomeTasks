package com.example.fridge_project;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fridge_project.repo.FridgeRepository;
import com.example.fridge_project.repoData.FoodData;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RecipesAddActivity extends AppCompatActivity {

    private EditText repTitle ;
    private EditText repDescription ;
    private EditText ingredientName ;
    private EditText ingredientAmount ;
    private RecyclerView ingredientRec ;
    private Button cancel ;
    private Button save ;
    private Button addIngredient ;
    private ArrayList<FoodData> ingredientsList;
    private ArrayList<String> ingredientsNames ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_add);

        repTitle = findViewById(R.id.recipeName) ;
        repDescription = findViewById(R.id.recipeDescription) ;
        ingredientName = findViewById(R.id.ingredient) ;
        ingredientAmount = findViewById(R.id.ingredient_amount) ;

        ingredientsList = new ArrayList<>() ;
        ingredientsNames = new ArrayList<>() ;
        ingredientRec = findViewById(R.id.ingredients_recycler) ;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL , false) ;
        ingredientRec.setLayoutManager(linearLayoutManager);
        ingredientRec.setAdapter(new IngredientsAdapter(ingredientsList)) ;

        addIngredient = findViewById(R.id.addIngredient) ;
        addIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ingredientAmount.getText().toString().matches("[0-9]+?[\\.\\,]?[0-9]*") && !getNameFromEdit().isEmpty() ) {
                    String name = getNameFromEdit() ;
                    Double amount = getAmountFromEdit() ;
                    if (!ingredientsNames.contains(name)) {
                        saveIngredientToList(name , amount) ;
                    } else {
                        for (FoodData f : ingredientsList) {
                            if (f.getName().equals(name)) {
                                ingredientsList.remove(f) ;
                                ingredientsNames.remove(name) ;
                                saveIngredientToList(name , amount) ;
                                showToast("I deleted smth");
                                ingredientRec.setAdapter(new IngredientsAdapter(ingredientsList));
                            }
                        }
                    }
                } else {
                    showToast("Введите корректные значения");
                }
            }
        });

        save = findViewById(R.id.save_btn) ;
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ingredientsList != null && ingredientsList.size() != 0 && !getRecipeNameFromEdit().isEmpty()) {
                    FridgeRepository fridgeRepository = new FridgeRepository(RecipesAddActivity.this) ;
                    fridgeRepository.addRecipe(getRecipeNameFromEdit() , getDescriptionFromEdit(), ingredientsList) ;
                    ingredientsList.clear();
                    ingredientsNames.clear();
                    repTitle.setText("");
                    ingredientRec.setAdapter(new IngredientsAdapter(ingredientsList));

                }
            }
        });
        cancel = findViewById(R.id.cancel) ;
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void saveIngredientToList(String name, Double amount) {
        ingredientsList.add(new FoodData(name , amount)) ;
        ingredientsNames.add(name) ;
        String saveMessage = String.format(Locale.getDefault() ,"Product %s in amount %.1f was saved", name, amount) ;
        showToast(saveMessage);
        ingredientName.setText("");
        ingredientAmount.setText("");
    }

    class IngredientsAdapter extends RecyclerView.Adapter<RecipesAddActivity.IngredientsAdapter.IngredViewHolder>{

        List<FoodData> rFoodList ;

        IngredientsAdapter(ArrayList<FoodData> list) {
            rFoodList = list ;
        }

        @NonNull
        @Override
        public RecipesAddActivity.IngredientsAdapter.IngredViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.product_element, parent, false) ;
            return new RecipesAddActivity.IngredientsAdapter.IngredViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecipesAddActivity.IngredientsAdapter.IngredViewHolder holder, int position) {
            holder.bindData(rFoodList.get(position));
        }

        @Override
        public int getItemCount() {
            return rFoodList.size();
        }

        class IngredViewHolder extends RecyclerView.ViewHolder {

            private TextView productName ;
            private TextView productAmount ;

            IngredViewHolder(@NonNull View itemView) {
                super(itemView);
                productName = itemView.findViewById(R.id.productName) ;
                productAmount = itemView.findViewById(R.id.productAmount) ;
            }

            @SuppressLint("SetTextI18n")
            void bindData(FoodData foodData) {
                final String name = foodData.getName() ;
                productName.setText(name);
                if (foodData.getAmount() != null)
                {productAmount.setText(foodData.getAmount().toString());}
                else {
                    productAmount.setText("0.0") ;
                }
            }
        }
    }

    private void showToast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }

    private double getAmountFromEdit() {
        return Double.parseDouble(ingredientAmount.getText().toString().replace(',' , '.')) ;
    }

    private String getNameFromEdit() {
        return ingredientName.getText().toString().trim();
    }

    private String getRecipeNameFromEdit() {
        return repTitle.getText().toString().trim();
    }

    private String getDescriptionFromEdit() {
        return repDescription.getText().toString().trim() ;
    }
}
