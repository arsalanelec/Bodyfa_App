package com.example.arsalan.mygym.models;

import com.google.gson.annotations.SerializedName;

public class RetMealPlan {
    @SerializedName("Record")
    MealPlan record;
    public RetMealPlan() {

    }

    public MealPlan getRecord() {
        return record;
    }

}
