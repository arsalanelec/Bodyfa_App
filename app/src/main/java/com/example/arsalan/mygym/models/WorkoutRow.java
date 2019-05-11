package com.example.arsalan.mygym.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

public class WorkoutRow implements Parcelable {
    private int set;
    private int rep;
    private int rest;
    private int setDuration;

    protected WorkoutRow(Parcel in) {
        set = in.readInt();
        rep = in.readInt();
        rest = in.readInt();
        setDuration = in.readInt();
        groupId = in.readInt();
        workoutId = in.readInt();
        workoutName = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(set);
        dest.writeInt(rep);
        dest.writeInt(rest);
        dest.writeInt(setDuration);
        dest.writeInt(groupId);
        dest.writeInt(workoutId);
        dest.writeString(workoutName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<WorkoutRow> CREATOR = new Creator<WorkoutRow>() {
        @Override
        public WorkoutRow createFromParcel(Parcel in) {
            return new WorkoutRow(in);
        }

        @Override
        public WorkoutRow[] newArray(int size) {
            return new WorkoutRow[size];
        }
    };

    public int getSetDuration() {
        return setDuration;
    }

    public void setSetDuration(int setDuration) {
        this.setDuration = setDuration;
    }

    private int groupId;
    private int workoutId;
    private String workoutName;

    public int getSet() {
        return set;
    }

    public int getRep() {
        return rep;
    }

    public int getRest() {
        return rest;
    }

    public int getGroupId() {
        return groupId;
    }

    public int getWorkoutId() {
        return workoutId;
    }

    public void setSet(int set) {
        this.set = set;
    }

    public void setRep(int rep) {
        this.rep = rep;
    }

    public void setRest(int rest) {
        this.rest = rest;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public void setWorkoutId(int workoutId) {
        this.workoutId = workoutId;
    }

    public String getWorkoutName() {
        return workoutName;
    }

    public void setWorkoutName(String workoutName) {
        this.workoutName = workoutName;
    }

    public WorkoutRow() {
        groupId = 1;
        workoutId = 1;
        rep = 1;
        rest = 1;
        set = 1;
    }

    public interface OnWorkoutRowEventListener {
        void onClick(int index);
    }
}
