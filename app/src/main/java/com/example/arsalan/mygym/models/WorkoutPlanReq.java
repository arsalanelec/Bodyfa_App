package com.example.arsalan.mygym.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import static com.example.arsalan.mygym.MyUtil.getStringFormatOfDate;

@Entity
public class WorkoutPlanReq {

    @SerializedName("AthleteWorkoutPlanRequestId")
    @Expose
    @PrimaryKey
    private int id;
    @SerializedName("AthleteUserId")
    @Expose
    private int athleteUserId;

    @SerializedName("AthleteThumbPicture")
    @Expose
    private String athleteThumbUrl;

    @SerializedName("AthleteName")
    @Expose
    private String athleteName;
    @SerializedName("ParentUserId")
    @Expose
    private int parentUserId;
    @SerializedName("ParentName")
    @Expose
    private String parentName;
    @SerializedName("RequestDateEn")
    @Expose
    private String requestDateEn;
    @SerializedName("RequestDateEnTs")
    @Expose
    private int requestDateEnTs;
    @SerializedName("RequestDateFa")
    @Expose
    private String requestDateFa;
    @SerializedName("RequestTimeFa")
    @Expose
    private String requestTimeFa;
    @SerializedName("Title")
    @Expose
    private String title;
    @SerializedName("Descriptions")
    @Expose
    private String descriptions;
    @SerializedName("TransactionId")
    @Expose
    private int transactionId;
    @SerializedName("ParentHasSeen")
    @Expose
    private boolean parentHasSeen;
    @SerializedName("SeenDateEn")
    @Expose
    private String seenDateEn;
    @SerializedName("SeenDateEnTs")
    @Expose
    private int seenDateEnTs;
    @SerializedName("SeenDateFa")
    @Expose
    private String seenDateFa;
    @SerializedName("SeenTimeFa")
    @Expose
    private String seenTimeFa;
    @SerializedName("ParentHasSend")
    @Expose
    private boolean parentHasSend;
    @SerializedName("SendDateEn")
    @Expose
    private String sendDateEn;
    @SerializedName("SendDateEnTs")
    @Expose
    private int sendDateEnTs;
    @SerializedName("SendDateFa")
    @Expose
    private String sendDateFa;
    @SerializedName("SendTimeFa")
    @Expose
    private String sendTimeFa;
    @SerializedName("Status")
    @Expose
    private String status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAthleteUserId() {
        return athleteUserId;
    }

    public void setAthleteUserId(int athleteUserId) {
        this.athleteUserId = athleteUserId;
    }

    public String getAthleteThumbUrl() {
        return athleteThumbUrl;
    }

    public void setAthleteThumbUrl(String athleteThumbUrl) {
        this.athleteThumbUrl = athleteThumbUrl;
    }

    public String getAthleteName() {
        return athleteName;
    }

    public void setAthleteName(String athleteName) {
        this.athleteName = athleteName;
    }

    public int getParentUserId() {
        return parentUserId;
    }

    public void setParentUserId(int parentUserId) {
        this.parentUserId = parentUserId;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getRequestDateEn() {
        return requestDateEn;
    }

    public void setRequestDateEn(String requestDateEn) {
        this.requestDateEn = requestDateEn;
    }

    public int getRequestDateEnTs() {
        return requestDateEnTs;
    }

    public void setRequestDateEnTs(int requestDateEnTs) {
        this.requestDateEnTs = requestDateEnTs;
    }

    public String getRequestDateFa() {
        return requestDateFa;
    }

    public void setRequestDateFa(String requestDateFa) {
        this.requestDateFa = requestDateFa;
    }

    public String getRequestTimeFa() {
        return requestTimeFa;
    }

    public void setRequestTimeFa(String requestTimeFa) {
        this.requestTimeFa = requestTimeFa;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public boolean isParentHasSeen() {
        return parentHasSeen;
    }

    public void setParentHasSeen(boolean parentHasSeen) {
        this.parentHasSeen = parentHasSeen;
    }

    public String getSeenDateEn() {
        return seenDateEn;
    }

    public void setSeenDateEn(String seenDateEn) {
        this.seenDateEn = seenDateEn;
    }

    public int getSeenDateEnTs() {
        return seenDateEnTs;
    }

    public void setSeenDateEnTs(int seenDateEnTs) {
        this.seenDateEnTs = seenDateEnTs;
    }

    public String getSeenDateFa() {
        return seenDateFa;
    }

    public void setSeenDateFa(String seenDateFa) {
        this.seenDateFa = seenDateFa;
    }

    public String getSeenTimeFa() {
        return seenTimeFa;
    }

    public void setSeenTimeFa(String seenTimeFa) {
        this.seenTimeFa = seenTimeFa;
    }

    public boolean isParentHasSend() {
        return parentHasSend;
    }

    public void setParentHasSend(boolean parentHasSend) {
        this.parentHasSend = parentHasSend;
    }

    public String getSendDateEn() {
        return sendDateEn;
    }

    public void setSendDateEn(String sendDateEn) {
        this.sendDateEn = sendDateEn;
    }

    public int getSendDateEnTs() {
        return sendDateEnTs;
    }

    public void setSendDateEnTs(int sendDateEnTs) {
        this.sendDateEnTs = sendDateEnTs;
    }

    public String getSendDateFa() {
        return sendDateFa;
    }

    public void setSendDateFa(String sendDateFa) {
        this.sendDateFa = sendDateFa;
    }

    public String getSendTimeFa() {
        return sendTimeFa;
    }

    public void setSendTimeFa(String sendTimeFa) {
        this.sendTimeFa = sendTimeFa;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSendDatePersianString(){
       return getStringFormatOfDate(requestDateEnTs);
    }
}