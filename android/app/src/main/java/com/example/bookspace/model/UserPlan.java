package com.example.bookspace.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserPlan {

    @SerializedName("plan")
    @Expose
    private int plan;
    @SerializedName("count")
    @Expose
    private int count;
    @SerializedName("percent")
    @Expose
    private String percent;

    public int getPlan() {
        return plan;
    }

    public void setPlan(int plan) {
        this.plan = plan;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }
}
