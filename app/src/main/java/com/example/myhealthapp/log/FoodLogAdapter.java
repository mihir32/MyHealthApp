package com.example.myhealthapp.log;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myhealthapp.R;
import com.example.myhealthapp.network.model.FoodDataBase;

import java.util.ArrayList;

public class FoodLogAdapter extends RecyclerView.Adapter<FoodLogAdapter.ViewHolder> {
    Context context;
    ArrayList<FoodDataBase> food;

    public FoodLogAdapter(Context context, ArrayList<FoodDataBase> foodList) {
        this.context = context;
        this.food = foodList;
        this.food = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_item, parent, false);
        return new FoodLogAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FoodDataBase food = this.food.get(position);

        holder.tv.setText(food.getDish_name());
        holder.tv1.setText(String.valueOf(food.getDish_calorie()));
    }

    public void setFood(ArrayList<FoodDataBase> food) {
        this.food = food;
    }

    @Override
    public int getItemCount() {
        return food.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv, tv1;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.search_item_name);
            tv1 = itemView.findViewById(R.id.search_item_kcal);
        }
    }
}
