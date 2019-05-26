package com.example.arsalan.mygym.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RetTrainerAthleteList {
    @SerializedName("Records")

    List<TrainerAthlete> records;
    @SerializedName("RecordsCount")

    int recordsCount;
    public RetTrainerAthleteList() {
    }

    public List<TrainerAthlete> getRecords() {
        return records;
    }

    public int getRecordsCount() {
        return recordsCount;
    }
}
