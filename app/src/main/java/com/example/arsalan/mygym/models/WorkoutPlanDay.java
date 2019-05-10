package com.example.arsalan.mygym.models;

import java.util.ArrayList;

public class WorkoutPlanDay {
    int dayOfWeek;
    ArrayList<WorkoutRow> workoutRows;



    public WorkoutPlanDay(int dayOfWeek, ArrayList<WorkoutRow> workoutRows) {
        this.dayOfWeek = dayOfWeek;
        this.workoutRows = workoutRows;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public ArrayList<WorkoutRow> getWorkoutRows() {
        return workoutRows;
    }

    public void setWorkoutRows(ArrayList<WorkoutRow> workoutRows) {
        this.workoutRows = workoutRows;
    }
}