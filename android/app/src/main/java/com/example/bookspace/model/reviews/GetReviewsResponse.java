package com.example.bookspace.model.reviews;

import com.example.bookspace.model.books.UserBook;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetReviewsResponse {

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
    private Review[] info;
    @SerializedName("can_write")
    @Expose
    private Boolean review;

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

    public Review[] getInfo() {
        return info;
    }

    public void setInfo(Review[] info) {
        this.info = info;
    }

    public Boolean getReview() { return review; }

    public void setReview(Boolean review){ this.review = review; }
}
