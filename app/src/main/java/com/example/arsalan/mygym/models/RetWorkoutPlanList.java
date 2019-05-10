package com.example.arsalan.mygym.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RetWorkoutPlanList {
    @SerializedName("Records")
    List<WorkoutPlan> records;
    @SerializedName("RecordCount")
    int recordsCount;
    public RetWorkoutPlanList() {
    }

    public List<WorkoutPlan> getRecords() {
        return records;
    }

    public int getRecordsCount() {
        return recordsCount;
    }
}
