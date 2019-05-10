package com.example.arsalan.mygym.models;

import com.google.gson.annotations.SerializedName;

public class RetroResult {
    @SerializedName("Result")
    String result;
    @SerializedName("Message")
    String message;
    public RetroResult() {
    }

    public String getResult() {
        return result;
    }

    public String getMessage() {
        return message;
    }
}
