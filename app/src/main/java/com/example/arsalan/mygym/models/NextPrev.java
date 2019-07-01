package com.example.arsalan.mygym.models;

public class NextPrev {
    private int step;
    private int day;
    public NextPrev(){}

    public NextPrev(int day, int step) {
        this.day=day;
        this.step=step;
    }

    public int getDay() {
        return day;
    }

    public int getStep() {
        return step;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setStep(int step) {
        this.step = step;
    }
}
