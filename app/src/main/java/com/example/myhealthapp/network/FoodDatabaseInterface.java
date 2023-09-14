package com.example.myhealthapp.network;

import com.example.myhealthapp.network.model.Data;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.Call;
import retrofit2.http.Query;

public interface FoodDatabaseInterface {

    @GET("parser?")
    Call<Data> getFood(@Query("ingr") String food,
                             @Query("app_id") String app_id,
                             @Query("app_key") String app_key);
}
