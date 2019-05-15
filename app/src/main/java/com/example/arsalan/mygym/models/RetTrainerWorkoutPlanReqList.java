package com.example.arsalan.mygym.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RetTrainerWorkoutPlanReqList {
    @SerializedName("Records")
    List<WorkoutPlanReq> records;

    @SerializedName("RecordsCount")
    int recordsCount;

    public RetTrainerWorkoutPlanReqList() {
    }

    public List<WorkoutPlanReq> getRecords() {
        return records;
    }

    public int getRecordsCount() {
        return recordsCount;
    }
}
