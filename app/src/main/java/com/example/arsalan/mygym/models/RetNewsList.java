package com.example.arsalan.mygym.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RetNewsList {
    @SerializedName("Records")
    List<NewsHead> records;
    @SerializedName("RecordCount")
    int recordsCount;
    public RetNewsList() {
    }

    public List<NewsHead> getRecords() {
        return records;
    }

    public int getRecordsCount() {
        return recordsCount;
    }
}
