package com.example.arsalan.mygym.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RetTutorialVideoList {
    @SerializedName("Records")
    List<TutorialVideo> records;
    @SerializedName("RecordCount")
    int recordsCount;
    public RetTutorialVideoList() {
    }

    public List<TutorialVideo> getRecords() {
        return records;
    }

    public int getRecordsCount() {
        return recordsCount;
    }
}
