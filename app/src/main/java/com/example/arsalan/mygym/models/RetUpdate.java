package com.example.arsalan.mygym.models;

import com.google.gson.annotations.SerializedName;

public class RetUpdate {
    @SerializedName("Record")
    Update record;
    public RetUpdate() {
    }

    public Update getRecord() {
        return record;
    }

}
