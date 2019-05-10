package com.example.arsalan.mygym.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RetTransactionList {
    @SerializedName("Records")
    List<Transaction> records;
    @SerializedName("RecordsCount")
    int recordsCount;
    public RetTransactionList() {
    }

    public List<Transaction> getRecords() {
        return records;
    }

    public int getRecordsCount() {
        return recordsCount;
    }
}
