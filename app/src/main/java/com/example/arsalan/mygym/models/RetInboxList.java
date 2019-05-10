package com.example.arsalan.mygym.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RetInboxList {
    @SerializedName("Records")
    List<InboxItem> records;
    @SerializedName("RecordCount")
    int recordsCount;
    public RetInboxList() {
    }

    public List<InboxItem> getRecords() {
        return records;
    }

    public int getRecordsCount() {
        return recordsCount;
    }
}
