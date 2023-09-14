package com.example.myhealthapp.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Food {
    @SerializedName("foodId")
    @Expose
    private String foodId;
    @SerializedName("label")
    @Expose
    private String label;
    @SerializedName("knownAs")
    @Expose
    private String knownAs;
    @SerializedName("nutrients")
    @Expose
    private Nutrients nutrients;

    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getKnownAs() {
        return knownAs;
    }

    public void setKnownAs(String knownAs) {
        this.knownAs = knownAs;
    }

    public Nutrients getNutrients() {
        return nutrients;
    }

    public void setNutrients(Nutrients nutrients) {
        this.nutrients = nutrients;
    }

    public String toString() {
        return this.label;
    }

}