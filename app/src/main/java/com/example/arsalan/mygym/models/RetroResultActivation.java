package com.example.arsalan.mygym.models;

import com.google.gson.annotations.SerializedName;

public class RetroResultActivation {
    @SerializedName("Result")
    private String result;
    @SerializedName("UserId")
    private long userId;

    public RetroResultActivation() {
    }

    public String getResult() {
        return result;
    }

    public long getUserId() {
        return userId;
    }
}
