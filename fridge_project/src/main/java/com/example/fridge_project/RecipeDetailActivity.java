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
import com.example.fridge_project.repoData.IngrData;

import java.util.List;
import java.util.Locale;

public class RecipeDetailActivity extends AppCompatActivity {

    private TextView recName;
    private TextView repDescription;
    private TextView haveProducts ;
    private TextView notProducts ;
    private Button remove ;
    private Button cancel ;
    private Button useProducts ;
    private FridgeRepository fridgeRepository ;

    private static final String TITLE_KEY = "TITLE_KEY" ;
    private static final String MY_LOG = "123q";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        recName = findViewById(R.id.recipeDetName) ;
        repDescription = findViewById(R.id.recipeDetDescription) ;
        haveProducts = findViewById(R.id.actual_product) ;
        notProducts = findViewById(R.id.not_have_product) ;
        remove = findViewById(R.id.delete_rep) ;
        cancel = findViewById(R.id.cancel) ;
        useProducts = findViewById(R.id.use_products) ;
        Intent intent = getIntent() ;
        final String repTitle = intent.getStringExtra(TITLE_KEY) ;
        recName.setText(repTitle);

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
                Log.d(MY_LOG, integer + "--");
                LiveData<List<IngrData>> lidr = fridgeRepository.getAllIngrsByRecipeId(integer) ;
                lidr.observe(RecipeDetailActivity.this, new Observer<List<IngrData>>() {
                    @Override
                    public void onChanged(List<IngrData> ingrData) {
                            haveProducts.setText(ingrData.get(0).getName() + "" + ingrData.get(0).getAmount());
                    }
                });
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
