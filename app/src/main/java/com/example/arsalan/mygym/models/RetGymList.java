package com.example.arsalan.mygym.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RetGymList {
    @SerializedName("Records")

    List<Gym> records;
    @SerializedName("RecordCount")

    int recordsCount;
    public RetGymList() {
    }

    public List<Gym> getRecords() {
        return records;
    }

    public int getRecordsCount() {
        return recordsCount;
    }
}
