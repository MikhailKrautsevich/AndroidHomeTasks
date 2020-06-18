package com.example.fridge_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

    private static String TITLE_KEY = "TITLE_KEY" ;
    private static String AMOUNT_KEY = "AMOUNT_KEY" ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acrivity_add_or_edit_pr);

        name = findViewById(R.id.productName) ;
        amount = findViewById(R.id.productAmount) ;
        save = findViewById(R.id.save_btn) ;
        cancel = findViewById(R.id.cancel) ;
        remove = findViewById(R.id.remove) ;

        Intent intent = getIntent() ;

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (intent.getStringExtra(TITLE_KEY) != null) {
            String nameCur = intent.getStringExtra(TITLE_KEY) ;
            String amountCur = intent.getStringExtra(AMOUNT_KEY) ;
            Double dAmountCur = Double.parseDouble(amountCur) ;
            name.setText(nameCur);
            amount.setText(amountCur);
            save.setText(R.string.change);
            if (dAmountCur !=null)
            {FoodData foodToWorkWith = new FoodData(nameCur, dAmountCur);}
        } else {
            remove.setVisibility(View.GONE);
            save.setOnClickListener(new SaveListener());
        }
    }

    class SaveListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            saveFoodData();
        }
    }

    private void saveFoodData(){
        if (amount.getText().toString().matches("[0-9]+?[\\.\\,]?[0-9]*")) {
            String nameProductStr = name.getText().toString().trim();
            String toDouble = amount.getText().toString().trim().replace(',' , '.');
            Double amountProductDouble = Double.parseDouble(toDouble);
            if (nameProductStr != null && !nameProductStr.isEmpty()
                    && amountProductDouble != null) {
                FoodData newFood = new FoodData(nameProductStr, amountProductDouble);
                FridgeRepository fridgeRepository = new FridgeRepository(ProductAddOrEdit.this);
                fridgeRepository.addNewFood(newFood);
                String saveMessage = String.format("Product %s in amount %.1f was saved", nameProductStr, amountProductDouble) ;
                Toast.makeText(ProductAddOrEdit.this, saveMessage, Toast.LENGTH_LONG).show();
                name.setText("");
                amount.setText("");
            }
        } else {
            Toast.makeText(ProductAddOrEdit.this, "Введите корректное количество", Toast.LENGTH_LONG).show();
        }
    }
}
