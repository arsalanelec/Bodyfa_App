package com.example.arsalan.mygym.models;

import com.google.gson.annotations.SerializedName;

public class RetWorkoutPlan {
    @SerializedName("Record")
    WorkoutPlan record;
    public RetWorkoutPlan() {

    }

    public WorkoutPlan getRecord() {
        return record;
    }

}
