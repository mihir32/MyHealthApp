package com.example.myhealthapp.network.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Hint {

    @SerializedName("food")
    @Expose
    private Food food;
    @SerializedName("measures")
    @Expose
    private List<Measure> measures = null;

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }

    public List<Measure> getMeasures() {
        return measures;
    }

    public void setMeasures(List<Measure> measures) {
        this.measures = measures;
    }

}