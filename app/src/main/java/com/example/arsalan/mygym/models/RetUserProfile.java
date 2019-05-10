package com.example.arsalan.mygym.models;

import com.google.gson.annotations.SerializedName;

public class RetUserProfile {

    @SerializedName("Record")
    User record;
    public RetUserProfile() {
    }

    public User getRecord() {
        return record;
    }

}
