package com.example.myhealthapp.network.model;

public class DailyLimit {


    int daily_limit;
    int consumption;


    public DailyLimit(int daily_limit, int consumption) {
        this.daily_limit=daily_limit ;
        this.consumption=consumption ;
    }

    public DailyLimit(){

    }

    public int getDaily_limit() {
        return daily_limit;
    }

    public void setDaily_limit(int daily_limit) {
        this.daily_limit = daily_limit;
    }

    public int getConsumption() {
        return consumption;
    }

    public void setConsumption(int consumption) {
        this.consumption = consumption;
    }
}
