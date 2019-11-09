package com.example.bookspace.model.books;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchBookResponse {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("books")
    @Expose
    private SearchBook[] books;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() { return message; }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public SearchBook[] getBooks() {
        return books;
    }

    public void setBooks(SearchBook[] books) {
        this.books = books;
    }


}
