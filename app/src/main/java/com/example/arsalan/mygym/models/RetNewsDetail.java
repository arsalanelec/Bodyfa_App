package com.example.arsalan.mygym.models;

import com.google.gson.annotations.SerializedName;

public class RetNewsDetail {

    @SerializedName("Record")
    News record;
    public RetNewsDetail() {
    }

    public News getRecord() {
        return record;
    }

}
