package com.example.arsalan.mygym.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class RetGalleryList {
    @SerializedName("Records")
    ArrayList<GalleryItem> records;
    @SerializedName("RecordCount")
    int recordsCount;
    public RetGalleryList() {
    }

    public ArrayList<GalleryItem> getRecords() {
        return records;
    }

    public int getRecordsCount() {
        return recordsCount;
    }
}
