package com.example.arsalan.mygym.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RetWorkoutPlanRequestList {
    @SerializedName("Records")
    private List<WorkoutPlanReq> records;

    @SerializedName("RecordsCount")
    private int count;

    public RetWorkoutPlanRequestList() {
    }

    public List<WorkoutPlanReq> getRecords() {
        return records;
    }

    public int getCount() {
        return count;
    }
}
