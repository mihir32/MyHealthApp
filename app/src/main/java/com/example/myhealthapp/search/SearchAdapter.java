package com.example.myhealthapp.search;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myhealthapp.MainActivity;
import com.example.myhealthapp.R;
import com.example.myhealthapp.network.model.Food;

import java.text.DecimalFormat;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {
    List<Food> foodList;
    Context context;
    DecimalFormat df = new DecimalFormat("#.##");
    Activity curActivity;
    String type;
    String date;

    public SearchAdapter(Context context, List<Food> foodList, String type, String date) {
        this.foodList = foodList;
        this.context = context;
        this.type = type;
        this.date = date;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_item, parent, false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        Food food = foodList.get(position);

        holder.itemName.setText(food.getKnownAs());
        holder.itemKcal.setText(df.format(food.getNutrients().getEnercKcal()));
        holder.curAc = (MainActivity) curActivity;
        holder.f = foodList.get(position);
        holder.type = type;
        holder.date = date;
    }

    public void setOnClickListener(Activity ac) {
        this.curActivity = ac;
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public static class SearchViewHolder extends RecyclerView.ViewHolder {
        Food f;
        TextView itemName, itemKcal;
        View.OnClickListener onClickListener;
        MainActivity curAc;
        String type;
        String date;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);

            itemName = (TextView)itemView.findViewById(R.id.search_item_name);
            itemKcal = (TextView)itemView.findViewById(R.id.search_item_kcal);
            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    curAc.addFood(f, type, date);
                }
            });
        }
    }
}
