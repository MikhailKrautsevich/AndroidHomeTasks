    package com.example.fridge_project;

    import android.content.Context;
    import android.os.Bundle;
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
    private MutableLiveData<List<FoodData>> foodList ;
    private ArrayList<FoodData> testList ;
    private ProductsAdapter productsAdapter ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        initTestList();

        addProduct = findViewById(R.id.addProduct);
        productsRecycler = findViewById(R.id.recyclerProducts);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        productsRecycler.setAdapter(new ProductsAdapter(testList));
        productsRecycler.setLayoutManager(linearLayoutManager);

        initRepo(this);

    //        foodList = new MutableLiveData<>();
    //        ArrayList<FoodData> m = new ArrayList<>();
    //        foodList.setValue(m);
    //        ArrayList<FoodData> n = (ArrayList<FoodData>) foodList.getValue();
    //        productsRecycler.setAdapter(new ProductsAdapter(n));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeRepo();
    }

    private void initRepo(final Context context) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                fridgeRepository = new FridgeRepository(ProductsActivity.this) ;
                foodList = fridgeRepository.getFoodDataList();

                List<FoodData> foodData = new ArrayList<>() ;
                MutableLiveData<List<FoodData>> k = new MutableLiveData<>();
                k.postValue(foodData);
                ArrayList<FoodData> foodData1 = (ArrayList<FoodData>) k.getValue() ;
                productsAdapter = new ProductsAdapter(foodData1) ;
                productsRecycler.setAdapter(productsAdapter);
            }
        });
        thread.start();
    }

    private void closeRepo() {
        fridgeRepository = null ;
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
                productAmount.setText(foodData.getAmount().toString());
            }
        }
    }
    }
