package com.example.arsalan.mygym.models;

import java.util.ArrayList;

public class MealPlanDay {
    int dayOfWeek;
    ArrayList<MealRow> mealRowList;

    @Override
    public String toString() {
        return "MealPlanDay{" +
                "dayOfWeek=" + dayOfWeek +
                ", mealRowList=" + mealRowList +
                '}';
    }

    public MealPlanDay(int dayOfWeek, ArrayList<MealRow> mealRowList) {
        this.dayOfWeek = dayOfWeek;
        this.mealRowList = mealRowList;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public ArrayList<MealRow> getMealRowList() {
        return mealRowList;
    }

    public void setMealRowList(ArrayList<MealRow> mealRowList) {
        this.mealRowList = mealRowList;
    }
}