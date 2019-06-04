package com.example.bookspace.model.books;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TopResponse {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("books")
    @Expose
    private MainPageBook[] books;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public MainPageBook[] getBooks() {
        return books;
    }

    public void setBooks(MainPageBook[] books) {
        this.books = books;
    }
}