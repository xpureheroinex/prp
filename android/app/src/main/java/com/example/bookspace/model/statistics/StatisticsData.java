package com.example.bookspace.model.statistics;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StatisticsData {
    @SerializedName("count")
    @Expose
    private int count;
    @SerializedName("fav_author")
    @Expose
    private String fav_author;
    @SerializedName("fav_genre")
    @Expose
    private String fav_genre;


    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getFav_author() {
        return fav_author;
    }

    public void setFav_author(String fav_author) {
        this.fav_author = fav_author;
    }

    public String getFav_genre() {
        return fav_genre;
    }

    public void setFav_genre(String fav_genre) {
        this.fav_genre = fav_genre;
    }
}

