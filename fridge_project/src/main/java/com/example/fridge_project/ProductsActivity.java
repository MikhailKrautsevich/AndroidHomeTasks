package com.example.fridge_project;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fridge_project.repo.FridgeRepository;
import com.example.fridge_project.repoData.FoodData;

import java.util.ArrayList;
import java.util.List;

public class ProductsActivity extends AppCompatActivity {

    private RecyclerView productsRecycler ;
    private ImageButton addProduct ;
    private FridgeRepository fridgeRepository;
    private MutableLiveData<List<FoodData>> foodListData ;
    private ArrayList<FoodData> testList ;

    private static String MY_LOG = "123q";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        Log.d(MY_LOG, "ProductsActivity - OnCreate") ;

        initTestList();

        fridgeRepository = new FridgeRepository(ProductsActivity.this) ;

        addProduct = findViewById(R.id.addProduct);
        productsRecycler = findViewById(R.id.recyclerProducts);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        productsRecycler.setLayoutManager(linearLayoutManager);
        productsRecycler.setAdapter(new ProductsAdapter(testList));


        foodListData = fridgeRepository.getFoodDataList() ;
        foodListData.observe(this, new Observer<List<FoodData>>() {
            @Override
            public void onChanged(List<FoodData> newList) {
                 if (newList != null) {
                 Log.d(MY_LOG, "ProductsActivity - foodListData: Observe " + newList.size()) ;
                    productsRecycler.setAdapter(new ProductsAdapter(newList));}
            }
        });


//        fillFridge();
 //       exampleLiveDataMethod(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeRepo();
    }

    private void fillFridge() {
        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                fridgeRepository = new FridgeRepository(ProductsActivity.this) ;
                Log.d(MY_LOG, "ProductActivity - Put products in fridge") ;
                fridgeRepository.AddNewFood(new FoodData("Хлеб" , 1.0));
                fridgeRepository.AddNewFood(new FoodData("Мясо" , 1.0));
            }
        });
        thread.start();
    }

    private void exampleLiveDataMethod(Context context) {
        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                List<FoodData> foodList = new ArrayList<>() ;
                foodList.add(new FoodData("qwe" , 2.1)) ;
                try {
                   Thread.currentThread().sleep(2000);
                } catch (InterruptedException e) {
                   e.printStackTrace();
                }
                foodListData.postValue(foodList);
            }
        }) ;
        thread.start();
    }

    private void closeRepo() {
        fridgeRepository = null ;
    }

    class AddPrListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {

        }
    }

    private void initTestList() {
        FoodData foodData1 = new FoodData("Name1", 2.0) ;
        FoodData foodData2 = new FoodData("Name2", 2.0) ;
        FoodData foodData3 = new FoodData("Name3", 2.0) ;
        testList = new ArrayList<>();
        testList.add(foodData1) ;
        testList.add(foodData2) ;
        testList.add(foodData3) ;
    }

    class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductsViewHolder>{

        List<FoodData> rFoodList ;

        ProductsAdapter(List<FoodData> list) {
            rFoodList = list ;
        }

        @NonNull
        @Override
        public ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.product_element, parent, false) ;
            return new ProductsViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ProductsViewHolder holder, int position) {
            holder.bindData(rFoodList.get(position));
        }

        @Override
        public int getItemCount() {
            return rFoodList.size();
        }

        public void changeList(LiveData<ArrayList<FoodData>> newList) {
            rFoodList = newList.getValue();
        }

        class ProductsViewHolder extends RecyclerView.ViewHolder {

            private TextView productName ;
            private TextView productAmount ;

            public ProductsViewHolder(@NonNull View itemView) {
                super(itemView);
                productName = itemView.findViewById(R.id.productName) ;
                productAmount = itemView.findViewById(R.id.productAmount) ;
            }

            void bindData(FoodData foodData) {
                productName.setText(foodData.getName());
                if (foodData.getAmount() != null)
                {productAmount.setText(foodData.getAmount().toString());}
                else {
                    productAmount.setText("11.0") ;
                }
            }
        }
    }
}
