package com.example.arsalan.mygym.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RetNewsList {
    @SerializedName("Records")
    List<News> records;
    @SerializedName("RecordCount")
    int recordsCount;
    public RetNewsList() {
    }

    public List<News> getRecords() {
        return records;
    }

    public int getRecordsCount() {
        return recordsCount;
    }
}
