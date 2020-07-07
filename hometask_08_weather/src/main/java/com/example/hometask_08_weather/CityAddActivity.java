package com.example.hometask_08_weather;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hometask_08_weather.database.CityDao;
import com.example.hometask_08_weather.database.CityDataBase;
import com.example.hometask_08_weather.database.CityEntity;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class CityAddActivity extends AppCompatActivity {

    private RecyclerView cityRecycler ;

    private String cityToObserve ;

    private LiveData<List<String>> cityList ;
    private CityDataBase dataBase ;
    private CityDao cityDao ;
    private SharedPreferences preferences ;

    private static final String LOG_TAG = "myLogs";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_add);

        cityToObserve = getIntent().getStringExtra("cityWanted") ;
        preferences = PreferenceManager.getDefaultSharedPreferences(this) ;

        ImageButton addCityBtn = findViewById(R.id.addNewCity2);
        addCityBtn.setOnClickListener(v -> {
            Log.d(LOG_TAG, "addCityBtn - was clicked");

            LayoutInflater inflater = LayoutInflater.from(CityAddActivity.this) ;
            View view = inflater.inflate(R.layout.dialog_layout, null) ;
            AlertDialog.Builder builder = new AlertDialog.Builder(CityAddActivity.this) ;
            builder.setView(view) ;
            final EditText input = view.findViewById(R.id.textEditForCity) ;
            builder.setMessage(R.string.enter_the_city)
                    .setPositiveButton(R.string.done, (dialog, which) -> {
                        String fromEdit = input.getText().toString().trim() ;
                        if (!fromEdit.isEmpty()) {
                            String fromEditCapitalised = capitalise(fromEdit) ;
                            final OkHttpClient okHttpClient = new OkHttpClient() ;
                            final String url = makeTextCallForCity(fromEditCapitalised) ;
                            final Request request = new Request.Builder().url(url).build() ;

                            CompletableFuture.supplyAsync(() -> {
                                try {
                                    Log.d(LOG_TAG, "CAA - Метод onClick(DialogInterface dialog, int which) - 1");
                                    return okHttpClient.newCall(request).execute();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    Log.d(LOG_TAG, "CAA - Метод onClick(DialogInterface dialog, int which - 1 !EXCEPTION!");
                                    return null ;
                                }
                            }).thenApplyAsync(response -> {
                                try {
                                    if (response != null) {
                                        WeatherParser parser = new WeatherParser(response.body().string()) ;
                                        Log.d(LOG_TAG, "CAA - Метод onClick(DialogInterface dialog, int which) - 2");
                                        return parser.getCoords() ;
                                    } else {
                                        Log.d(LOG_TAG, "CAA - Метод onClick(DialogInterface dialog, int which) - 2 - !RETURN NULL!");
                                        return null ;
                                    }
                                } catch (IOException | JSONException e) {
                                    e.printStackTrace();
                                    Log.d(LOG_TAG, "CAA - Метод onClick(DialogInterface dialog, int which - 2 !EXCEPTION!");
                                    return null ;
                                }
                            }).thenAccept(doubles -> {
                                cityToObserve = fromEditCapitalised ;
                                CityEntity toAdd = new CityEntity(fromEditCapitalised, doubles[0], doubles[1]) ;
                                Log.d(LOG_TAG, "CAA - Метод onClick(DialogInterface dialog, int which) - 3 : city to add = " + toAdd.getName());
                                cityDao.addCity(toAdd);
                            }) ;
                        } else {
                            Toast.makeText(CityAddActivity.this , R.string.city_not_correct, Toast.LENGTH_SHORT)
                                    .show();
                        }
                    })
                    .setNeutralButton(R.string.cancel, null);
            AlertDialog dialog = builder.create() ;
            dialog.show();
        });

        cityRecycler = findViewById(R.id.cityRecycler) ;
        cityRecycler.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        cityRecycler.setLayoutManager(manager);
        cityRecycler.addItemDecoration(new DividerItemDecoration(getBaseContext(), DividerItemDecoration.VERTICAL));

        dataBase = CityDataBase.getDataBase(this);
        cityDao = dataBase.getDao() ;
        cityList = cityDao.getAllCityNames();
    }

    @Override
    protected void onResume() {
        super.onResume();

        cityList.observe(CityAddActivity.this, strings -> cityRecycler.setAdapter(new CityAdapter((ArrayList<String>) strings)));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        preferences.edit().putString("cityKey", cityToObserve).apply();

        cityDao = null ;
        dataBase.close();
        dataBase = null ;
    }

    private String makeTextCallForCity(String city){
        final String example = "http://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s&units=metric" ;
        String url = String.format(example, city, MainActivity.API_KEY) ;
        Log.d(LOG_TAG, "CAA - makeTextCallForCity( " + city + " ) = " + url);
        return url ;
    }

    private String capitalise(String string) {
        String toReturn = string ;
        if (string.matches("[a-z]")) {
            toReturn =  string.toUpperCase() ;
            Log.d(LOG_TAG, "CAA - метод capitalise возвращаю [a-z]");
        } else if (string.matches("[a-zA-Z]{2,}")) {
            Log.d(LOG_TAG, "CAA - метод capitalise возвращаю \"[A-Za-z]{2,}\"");
            string = string.substring(0,1).toUpperCase() + string.substring(1).toLowerCase();
        }
        Log.d(LOG_TAG, "CAA - метод capitalise возвращаю " + toReturn);
        return toReturn;
    }

    class CityAdapter extends RecyclerView.Adapter<CityAdapter.CityHolder>{
        ArrayList<String> list = new ArrayList() ;

        CityAdapter(ArrayList<String> strings) {
            if (strings != null) {
                this.list = strings ;
                if (!strings.contains("London")) {
                    list.add("London") ;
                    list.sort(String::compareTo);
                }
            } else {
                list = new ArrayList<>();
                list.add("London") ;
            }
        }

        @NonNull
        @Override
        public CityHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.city_element, parent, false);
            return new CityHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CityHolder holder, int position) {
            holder.bindData(list.get(position));
        }

        @Override
        public int getItemCount() {
            if (list != null) {
                return list.size();
            } else {
                Log.d(LOG_TAG, "CAA - Метод getItemCount() - что-то с list");
                return 0 ;
            }
        }

        class CityHolder extends RecyclerView.ViewHolder{
            TextView cityName ;
            ImageView check ;

            CityHolder(@NonNull View itemView) {
                super(itemView);

                cityName = itemView.findViewById(R.id.cityNameRec) ;
                check = itemView.findViewById(R.id.check) ;
                itemView.setOnClickListener(v -> {
                    cityToObserve = cityName.getText().toString();
                    preferences.edit().putString("cityKey", cityToObserve).apply();
                    cityRecycler.setAdapter(new CityAdapter((ArrayList<String>) cityList.getValue()));
                });
            }

            void bindData(String name){
                cityName.setText(name);
                if (name.equals(cityToObserve)) {
                    check.setVisibility(View.VISIBLE);
                } else {check.setVisibility(View.GONE);}
            }
        }
    }
}
