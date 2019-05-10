package com.example.arsalan.mygym.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RetMealPlanList {
    @SerializedName("Records")
    List<MealPlan> records;
    @SerializedName("RecordCount")
    int recordsCount;
    public RetMealPlanList() {
    }

    public List<MealPlan> getRecords() {
        return records;
    }

    public int getRecordsCount() {
        return recordsCount;
    }
}
