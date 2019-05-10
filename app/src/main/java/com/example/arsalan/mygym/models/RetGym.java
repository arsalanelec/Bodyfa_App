package com.example.arsalan.mygym.models;

import com.google.gson.annotations.SerializedName;

public class RetGym {
    @SerializedName("Record")
    Gym record;
    public RetGym() {

    }

    public Gym getRecord() {
        return record;
    }

}
