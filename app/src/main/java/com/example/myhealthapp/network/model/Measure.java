package com.example.myhealthapp.network.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Measure {

    @SerializedName("uri")
    @Expose
    private String uri;
    @SerializedName("label")
    @Expose
    private String label;
    @SerializedName("qualified")
    @Expose
    private List<Qualified> qualified = null;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<Qualified> getQualified() {
        return qualified;
    }

    public void setQualified(List<Qualified> qualified) {
        this.qualified = qualified;
    }

}