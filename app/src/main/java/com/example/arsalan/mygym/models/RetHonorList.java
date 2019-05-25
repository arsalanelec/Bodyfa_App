package com.example.arsalan.mygym.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RetHonorList {
    @SerializedName("Records")
    List<Honor> records;
    @SerializedName("RecordsCount")
    int recordsCount;
    public RetHonorList() {
    }

    public List<Honor> getRecords() {
        return records;
    }

    public int getRecordsCount() {
        return recordsCount;
    }
}
