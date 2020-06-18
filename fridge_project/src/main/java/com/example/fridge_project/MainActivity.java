package com.example.fridge_project;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fridge_project.repo.FridgeRepository;

public class MainActivity extends AppCompatActivity {

    private FridgeRepository fridgeRepository ;
    private Button goToProducts ;
    private Button goToRecipes ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initRepo(this);
        fridgeRepository = new FridgeRepository(this);

        goToProducts = findViewById(R.id.goToProducts) ;
        goToProducts.setOnClickListener( new goToProductsListener());
        goToRecipes = findViewById(R.id.goToRecipes) ;
        goToRecipes.setOnClickListener(new goToRecipesListener());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeRepo(); ;
    }

    private void initRepo(final Context context) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                fridgeRepository = new FridgeRepository(MainActivity.this) ;
            }
        });
        thread.start();
    }

    private void closeRepo() {
        fridgeRepository = null ;
    }

    class goToProductsListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this , ProductsActivity.class) ;
            startActivity(intent);
        }
    }

    class goToRecipesListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this , RecipesActivity.class) ;
            startActivity(intent);
        }
    }
}
