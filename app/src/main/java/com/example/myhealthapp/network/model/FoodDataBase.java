package com.example.myhealthapp.network.model;

public class FoodDataBase {
    private String dish_name;
    private  int dish_calorie;
    private int  dish_quantity;

    public FoodDataBase() { }

    public FoodDataBase(String dish_name, int dish_calorie , int dish_quantity) {
        this.dish_name=dish_name;
        this.dish_calorie=dish_calorie;
        this.dish_quantity=dish_quantity;
    }

    public String getDish_name() {
        return dish_name;
    }

    public void setDish_name(String dish_name) {
        this.dish_name = dish_name;
    }

    public int getDish_calorie() {
        return dish_calorie;
    }

    public void setDish_calorie(int dish_calorie) {
        this.dish_calorie = dish_calorie;
    }

    public int getDish_quantity() {
        return dish_quantity;
    }

    public void setDish_quantity(int dish_quantity) {
        this.dish_quantity = dish_quantity;
    }
}
