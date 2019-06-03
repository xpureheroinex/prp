package com.example.bookspace.model.books;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetBooksResponse {

    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("info")
    @Expose
    private UserBook[] info;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public UserBook[] getInfo() {
        return info;
    }

    public void setInfo(UserBook[] info) {
        this.info = info;
    }
}