package com.example.arsalan.mygym.models;

import static com.example.arsalan.mygym.MyUtil.getStringFormatOfTime;

public class DownloadStatus {
    private int percent;
    private long timeS;

    private int totalItems;
    private int itemNo;

    public DownloadStatus() {
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

    public long getTimeS() {
        return timeS;
    }

    public void setTimeS(long timeS) {
        this.timeS = timeS;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public int getItemNo() {
        return itemNo;
    }

    public void setItemNo(int itemNo) {
        this.itemNo = itemNo;
    }

    public String getTimeInString(){
  return       getStringFormatOfTime(timeS);
    }
}
