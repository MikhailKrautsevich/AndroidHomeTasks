package com.example.fridge_project;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fridge_project.repo.FridgeRepository;
import com.example.fridge_project.repoData.FoodData;

public class ProductAddOrEdit extends AppCompatActivity {

    private EditText name ;
    private EditText amount ;
    private Button save ;
    private Button cancel ;
    private Button remove ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acrivity_add_or_edit_pr);

        name = findViewById(R.id.productName) ;
        amount = findViewById(R.id.productAmount) ;
        save = findViewById(R.id.save_btn) ;
        cancel = findViewById(R.id.cancel) ;
        remove = findViewById(R.id.remove) ;

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        remove.setVisibility(View.GONE);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameProductStr = name.getText().toString().trim();
                Double amountProductDouble  = Double.parseDouble(amount.getText().toString().trim()) ;
                if (nameProductStr != null && !nameProductStr.isEmpty()
                        && amountProductDouble != null ) {
                    FoodData newFood = new FoodData(nameProductStr, amountProductDouble) ;
                    FridgeRepository fridgeRepository = new FridgeRepository(ProductAddOrEdit.this) ;
                    fridgeRepository.addNewFood(newFood);
                }
            }
        });
    }
}
