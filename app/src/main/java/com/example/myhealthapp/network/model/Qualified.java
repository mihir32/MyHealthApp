package com.example.myhealthapp.network.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Qualified {

    @SerializedName("qualifiers")
    @Expose
    private List<Qualifier> qualifiers = null;

    public List<Qualifier> getQualifiers() {
        return qualifiers;
    }

    public void setQualifiers(List<Qualifier> qualifiers) {
        this.qualifiers = qualifiers;
    }

}