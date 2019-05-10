package com.example.arsalan.mygym.models;

import com.google.gson.annotations.SerializedName;

public class RetTrainer {
    @SerializedName("Record")
    Trainer record;
    public RetTrainer() {
    }

    public Trainer getRecord() {
        return record;
    }

}
