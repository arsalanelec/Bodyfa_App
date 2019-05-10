package com.example.arsalan.mygym.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RetTutorialList {
    @SerializedName("Records")
    List<Tutorial> records;
    @SerializedName("RecordCount")
    int recordsCount;
    public RetTutorialList() {
    }

    public List<Tutorial> getRecords() {
        return records;
    }

    public int getRecordsCount() {
        return recordsCount;
    }
}
