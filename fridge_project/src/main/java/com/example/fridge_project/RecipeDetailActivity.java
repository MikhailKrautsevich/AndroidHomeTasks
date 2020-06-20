package com.example.fridge_project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.example.fridge_project.repo.FridgeRepository;
import com.example.fridge_project.repoData.FoodData;
import com.example.fridge_project.repoData.IngrData;

import java.util.List;
import java.util.Locale;

public class RecipeDetailActivity extends AppCompatActivity {

    private TextView repName;
    private TextView repDescription;
    private TextView haveProducts ;
    private TextView haveNotProducts ;
    private Button remove ;
    private Button cancel ;
    private Button useProducts ;
    private FridgeRepository fridgeRepository ;

    private static final String TITLE_KEY = "TITLE_KEY" ;
    private static final String START_OF_UPPER_MESSAGE = "You need : ";
    private static final String START_OF_DOWN_MESSAGE = "You have : ";
    private static final String YOU_HAVE_NO_PRODUCTS = "You have no products for this recipe.";
    private static final String TIRE = " - ";
    private static final String DOT = ".";
    private static final String ADD_EXAMPLE_FORMAT = " %s - %s ,";
    private static final String MY_LOG = "123q";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        repName = findViewById(R.id.recipeDetName) ;
        repDescription = findViewById(R.id.recipeDetDescription) ;
        haveProducts = findViewById(R.id.actual_product) ;
        haveNotProducts = findViewById(R.id.not_have_product) ;
        remove = findViewById(R.id.delete_rep) ;
        cancel = findViewById(R.id.cancel) ;
        useProducts = findViewById(R.id.use_products) ;
        Intent intent = getIntent() ;
        final String repTitle = intent.getStringExtra(TITLE_KEY) ;
        repName.setText(repTitle);

        fridgeRepository = new FridgeRepository(this) ;

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fridgeRepository != null) {
                    fridgeRepository.deleteRecipe(repTitle) ;
                    String delNote = String.format(Locale.getDefault(), "I remove %s recipe", repTitle) ;
                    showToast(delNote);
                    finish();
                }
            }
        });

        LiveData<String> descrLiveData = fridgeRepository.getDescrRecByName(repTitle);
        descrLiveData.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (!(s == null)) {
                    if (!s.isEmpty()) {
                        repDescription.setText(s);
                    }
                }
            }
        });

        LiveData<Integer> idRec = fridgeRepository.getRecipeIdByName(repTitle);
        idRec.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (integer != null) {
                    Log.d(MY_LOG, "RecipeDetailActivity - get id = " + integer);
                    LiveData<List<IngrData>> lidr = fridgeRepository.getAllIngrsByRecipeId(integer) ;
                    lidr.observe(RecipeDetailActivity.this, new Observer<List<IngrData>>() {
                        @Override
                        public void onChanged(List<IngrData> listOfIngredients) {
                            if (listOfIngredients != null) {
                                StringBuffer upperTextMessage = new StringBuffer() ;
                                String delimeter = "";
                                upperTextMessage.append(START_OF_UPPER_MESSAGE) ;
                                final StringBuffer downTextMessage = new StringBuffer() ;
                                downTextMessage.append(START_OF_DOWN_MESSAGE) ;
                                for (IngrData ingredient : listOfIngredients) {
                                    upperTextMessage.append(delimeter)
                                            .append(ingredient.getName())
                                            .append(TIRE).
                                            append((ingredient.getAmount() + "")) ;
                                    delimeter = " , " ;
                                    LiveData<FoodData> actualProduct = fridgeRepository.getProductLDByName(ingredient.getName()) ;
                                    actualProduct.observe(RecipeDetailActivity.this, new Observer<FoodData>() {
                                        @Override
                                        public void onChanged(FoodData foodData) {
                                            if (foodData != null) {
                                                if ((downTextMessage.charAt(downTextMessage.length() - 1)) == '.' ) {
                                                    downTextMessage.deleteCharAt(downTextMessage.length() -1) ;
                                                    downTextMessage.append(", ");
                                                }
                                                String toAppend = String
                                                        .format(Locale.getDefault() , ADD_EXAMPLE_FORMAT, foodData.getName() , foodData.getAmount().toString()) ;
                                                Log.d(MY_LOG, "downTextMessage toAppend - " + toAppend);
                                                downTextMessage.append(toAppend) ;
                                                Log.d(MY_LOG, "downTextMessage = " + downTextMessage);
                                            }
                                            if (downTextMessage.length() == START_OF_DOWN_MESSAGE.length()) {
                                                haveNotProducts.setText(YOU_HAVE_NO_PRODUCTS);
                                            } else  if (downTextMessage.length() != START_OF_DOWN_MESSAGE.length()) {
                                                downTextMessage.deleteCharAt(downTextMessage.length() -1) ;
                                                downTextMessage.append(DOT);
                                                haveNotProducts.setText(downTextMessage);
                                            }
                                        }
                                    });
                                }
                                upperTextMessage.append(DOT) ;
                                haveProducts.setText(upperTextMessage);
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fridgeRepository = null ;
    }

    private void showToast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }
}
