package com.example.myhealthapp.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Links {

    @SerializedName("next")
    @Expose
    private Next next;

    public Next getNext() {
        return next;
    }

    public void setNext(Next next) {
        this.next = next;
    }
}