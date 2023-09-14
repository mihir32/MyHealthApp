package com.example.myhealthapp.search;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myhealthapp.Constants;
import com.example.myhealthapp.network.FoodDatabaseInterface;
import com.example.myhealthapp.network.model.Data;
import com.example.myhealthapp.network.model.Food;
import com.example.myhealthapp.network.model.Hint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchViewModel extends ViewModel {
    private MutableLiveData<ArrayList<ArrayList<String>>> data;
    private String userID;

    public void setUserID(String name) {
        userID = name;
    }

    private FoodDatabaseInterface inf;

    private MutableLiveData<ArrayList<Food>> foodSearched;

    private MutableLiveData<Boolean> dataSearched;

    public SearchViewModel() {
        Retrofit rf = new Retrofit.Builder()
                .baseUrl(Constants.EDAMAM_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        this.inf = rf.create(FoodDatabaseInterface.class);
        dataSearched = new MutableLiveData<>(true);
    }

    public LiveData<ArrayList<Food>> getSearched(String what) {
        if (foodSearched == null) {
            foodSearched = new MutableLiveData<>();
        }
        getFood(what);

        return foodSearched;
    }

    public void getFood(String what) {
        dataSearched.setValue(false);
        Call<Data> d = this.inf.getFood(what, Constants.EDAMAM_ID, Constants.EDAMAM_KEY);
        d.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                ArrayList<Food> data = new ArrayList<>();
                if (!response.isSuccessful()) {
                    Log.d("IMAD", response.message());
                    foodSearched.setValue(data);
                } else {
                    assert response.body() != null;
                    for (Hint hint: response.body().getHints()) {
                        data.add(hint.getFood());
                    }

                    foodSearched.setValue(data);
                }
                dataSearched.setValue(true);
            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {
                Log.d("IMAD", t.getMessage());
                dataSearched.setValue(true);
            }
        });
    }

    public LiveData<Boolean> dataStatus() {
        return dataSearched;
    }
}
