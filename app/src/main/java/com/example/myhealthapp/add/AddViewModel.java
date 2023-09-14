package com.example.myhealthapp.add;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myhealthapp.Constants;
import com.example.myhealthapp.network.FoodDatabaseInterface;
import com.example.myhealthapp.network.model.Data;
import com.example.myhealthapp.network.model.Food;
import com.example.myhealthapp.network.model.Hint;
//import com.example.myhealthapp.network.model.Hint;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddViewModel extends ViewModel {
    private MutableLiveData<Food> foodScanned;

    private final FoodDatabaseInterface inf;

    public AddViewModel() {
        Retrofit rf = new Retrofit.Builder()
                .baseUrl(Constants.EDAMAM_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        this.inf = rf.create(FoodDatabaseInterface.class);
    }

    public LiveData<Food> getSearched(String what) {
        if (foodScanned == null) {
            foodScanned = new MutableLiveData<>();
        }
        getFood(what);

        return foodScanned;
    }

    public void getFood(String what) {
        Call<Data> d = this.inf.getFood(what, Constants.EDAMAM_ID, Constants.EDAMAM_KEY);
        d.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(@NonNull Call<Data> call, @NonNull Response<Data> response) {
                ArrayList<Food> data = new ArrayList<>();
                if (!response.isSuccessful()) {
                    Log.d("IMAD", "Response error: " + response.message());
                    foodScanned.setValue(null);
                } else {
                    if (response.body() != null && response.body().getHints().size() != 0) {
                        for (Hint it: response.body().getHints()) {
                            if (it.getFood().getLabel().equalsIgnoreCase(what)) {
                                foodScanned.setValue(it.getFood());
                                break;
                            }
                        }
                    } else if (response.body() != null && response.body().getParsed().size() != 0) {
                        foodScanned.setValue(response.body().getParsed().get(0).getFood());
                    } else {
                        foodScanned.setValue(null);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Data> call, @NonNull Throwable t) {
                Log.d("IMAD", "Error connecting " + t.getMessage());
            }
        });
    }
}
