package com.example.fridge_project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fridge_project.repo.FridgeRepository;
import com.example.fridge_project.repoData.RecipeShortD;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RecipesActivity extends AppCompatActivity {

    private RecyclerView recipeRecycler ;
    private ImageButton addRecipe ;
    private FridgeRepository fridgeRepository;
    private LiveData<List<RecipeShortD>> recipesList;

    private static String MY_LOG = "123q";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);
        Log.d(MY_LOG, "RecipeActivity - OnCreate") ;

//        initTestList();

        fridgeRepository = new FridgeRepository(RecipesActivity.this) ;

        addRecipe = findViewById(R.id.addRecipe);
        addRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecipesActivity.this, RecipesAddActivity.class) ;
                startActivity(intent);
            }
        });
        recipeRecycler = findViewById(R.id.recyclerRecipes);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false) ;
        recipeRecycler.setLayoutManager(linearLayoutManager);
        recipeRecycler.addItemDecoration(new DividerItemDecoration(this, linearLayoutManager.getOrientation()));
        recipeRecycler.setAdapter(new RecipesAdapter(new ArrayList<RecipeShortD>()));

        recipesList = fridgeRepository.getAllRecipes() ;
        recipesList.observe(this, new Observer<List<RecipeShortD>>() {
            @Override
            public void onChanged(List<RecipeShortD> newList) {
                if (newList != null) {
                    Log.d(MY_LOG, "RecipeActivity - recipeListData: Observe " + newList.size()) ;
                    newList.sort(new Comparator<RecipeShortD>() {
                        @Override
                        public int compare(RecipeShortD o1, RecipeShortD o2) {
                            return (o1.getName().toLowerCase()).compareTo(o2.getName().toLowerCase());
                        }
                    });
                    recipeRecycler.setAdapter(new RecipesAdapter(newList));}
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeRepo();
    }

    private void closeRepo() {
        fridgeRepository = null ;
    }


    class RecipesAdapter extends RecyclerView.Adapter<RecipesActivity.RecipesAdapter.RecipesViewHolder>{

        List<RecipeShortD> rRecipesList ;

        RecipesAdapter(List<RecipeShortD> list) {
            rRecipesList = list ;
        }

        @NonNull
        @Override
        public RecipesActivity.RecipesAdapter.RecipesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recipe_element, parent, false) ;
            return new RecipesActivity.RecipesAdapter.RecipesViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecipesActivity.RecipesAdapter.RecipesViewHolder holder, int position) {
            holder.bindData(rRecipesList.get(position));
        }

        @Override
        public int getItemCount() {
            return rRecipesList.size();
        }

        class RecipesViewHolder extends RecyclerView.ViewHolder {

            private TextView recipeName ;
            private ImageView possibilityPic;

            public RecipesViewHolder(@NonNull View itemView) {
                super(itemView);
                recipeName = itemView.findViewById(R.id.recipeName) ;
//                itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(ProductsActivity.this , ProductAddOrEdit.class) ;
//                        intent.putExtra(TITLE_KEY , productName.getText()) ;
//                        intent.putExtra(AMOUNT_KEY , productAmount.getText()) ;
//                        startActivity(intent);
//                    }
//                });
            }

            void bindData(RecipeShortD shortD) {
                final String name = shortD.getName() ;
                recipeName.setText(name);
            }
        }
    }
}
