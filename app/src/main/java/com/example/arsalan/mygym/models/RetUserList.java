package com.example.arsalan.mygym.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RetUserList {
    @SerializedName("Records")

    List<User> records;
    @SerializedName("RecordCount")

    int recordsCount;
    public RetUserList() {
    }

    public List<User> getRecords() {
        return records;
    }

    public int getRecordsCount() {
        return recordsCount;
    }
}
