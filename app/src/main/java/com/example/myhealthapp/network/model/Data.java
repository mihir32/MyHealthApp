package com.example.myhealthapp.network.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("parsed")
    @Expose
    private List<Parsed> parsed = null;
    @SerializedName("hints")
    @Expose
    private List<Hint> hints = null;
    @SerializedName("_links")
    @Expose
    private Links links;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Hint> getHints() {
        return hints;
    }

    public void setHints(List<Hint> hints) {
        this.hints = hints;
    }

    public Links getLinks() {
        return links;
    }

    public void setLinks(Links links) {
        this.links = links;
    }

    public List<Parsed> getParsed() {
        return parsed;
    }

    public void setParsed(List<Parsed> parsed) {
        this.parsed = parsed;
    }
}