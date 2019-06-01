package com.example.bookspace.model.statistics;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StatisticsResponse {
    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("info")
    @Expose
    private StatisticsData info;
    @SerializedName("plan")
    @Expose
    private UserPlan plan;

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

    public StatisticsData getInfo() {
        return info;
    }

    public void setInfo(StatisticsData info) {
        this.info = info;
    }

    public UserPlan getPlan() {
        return plan;
    }

    public void setPlan(UserPlan plan) {
        this.plan = plan;
    }
}
