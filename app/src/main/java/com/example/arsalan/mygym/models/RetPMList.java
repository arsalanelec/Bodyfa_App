package com.example.arsalan.mygym.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RetPMList {
    @SerializedName("Records")
    List<PrivateMessage> records;
    @SerializedName("RecordCount")
    int recordsCount;
    public RetPMList() {
    }

    public List<PrivateMessage> getRecords() {
        return records;
    }

    public int getRecordsCount() {
        return recordsCount;
    }
}
