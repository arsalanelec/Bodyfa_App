package com.example.arsalan.mygym.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import static com.example.arsalan.mygym.MyUtil.getStringFormatOfDate;

@Entity
public class TrainerAthlete {
    @PrimaryKey
    @SerializedName("TrainerAthletsId")
    @Expose
    private long id;



    @SerializedName(value = "AthleteUserId",alternate = {"AthleteId"})
    @Expose
    private long athleteId;
    @SerializedName(value = "ParentId",alternate = {"TrainerUserId"})
    @Expose
    private long parentId;

    @SerializedName("ParentUsername")
    @Expose
    private long parentUsername;

    @SerializedName("ParentName")
    @Expose
    private String parentName;
    @SerializedName("ParentFamily")
    @Expose
    private String parentFamily;

    @SerializedName("ParentPicture")
    @Expose
    private String parentThumbUrl;

    @SerializedName("AthleteThumbPicture")
    @Expose
    private String athleteThumbPicture;
    @SerializedName("AthleteUsername")
    @Expose
    private String athleteUsername;
    @SerializedName("AthleteName")
    @Expose
    private String athleteName;
    @SerializedName("AthleteFamily")
    @Expose
    private String athleteFamily;
    @SerializedName("RegisterDate")
    @Expose
    private String registerDate;
    @SerializedName("RegisterDateTs")
    @Expose
    private long registerDateTs;
    @SerializedName("RegisterDateFa")
    @Expose
    private String registerDateFa;
    @SerializedName("FromDate")
    @Expose
    private String fromDate;
    @SerializedName("FromDateTs")
    @Expose
    private long fromDateTs;
    @SerializedName("FromDateFa")
    @Expose
    private String fromDateFa;
    @SerializedName("ToDate")
    @Expose
    private String toDate;
    @SerializedName("ToDateTs")
    @Expose
    private long toDateTs;
    @SerializedName("ToDateFa")
    @Expose
    private String toDateFa;
    @SerializedName("MembershipType")
    @Expose
    private String membershipType;
    @SerializedName("MembershipDescription")
    @Expose
    private String membershipDescription;
    @SerializedName("Type")
    @Expose
    private String type;
    @SerializedName("Status")
    @Expose
    private String status;
    @SerializedName("StatusFa")
    @Expose
    private String statusFa;

    public TrainerAthlete() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getAthleteId() {
        return athleteId;
    }

    public void setAthleteId(long athleteId) {
        this.athleteId = athleteId;
    }

    public String getAthleteThumbPicture() {
        return athleteThumbPicture;
    }

    public void setAthleteThumbPicture(String athleteThumbPicture) {
        this.athleteThumbPicture = athleteThumbPicture;
    }

    public String getAthleteUsername() {
        return athleteUsername;
    }

    public void setAthleteUsername(String athleteUsername) {
        this.athleteUsername = athleteUsername;
    }

    public String getAthleteName() {
        return athleteName;
    }

    public void setAthleteName(String athleteName) {
        this.athleteName = athleteName;
    }

    public String getAthleteFamily() {
        return athleteFamily;
    }

    public void setAthleteFamily(String athleteFamily) {
        this.athleteFamily = athleteFamily;
    }

    public String getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(String registerDate) {
        this.registerDate = registerDate;
    }

    public long getRegisterDateTs() {
        return registerDateTs;
    }

    public void setRegisterDateTs(long registerDateTs) {
        this.registerDateTs = registerDateTs;
    }

    public String getRegisterDateFa() {
        return registerDateFa;
    }

    public void setRegisterDateFa(String registerDateFa) {
        this.registerDateFa = registerDateFa;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public long getFromDateTs() {
        return fromDateTs;
    }

    public void setFromDateTs(long fromDateTs) {
        this.fromDateTs = fromDateTs;
    }

    public String getFromDateFa() {
        return fromDateFa;
    }

    public void setFromDateFa(String fromDateFa) {
        this.fromDateFa = fromDateFa;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public long getToDateTs() {
        return toDateTs;
    }

    public void setToDateTs(long toDateTs) {
        this.toDateTs = toDateTs;
    }

    public String getToDateFa() {
        return toDateFa;
    }

    public void setToDateFa(String toDateFa) {
        this.toDateFa = toDateFa;
    }

    public String getMembershipType() {
        return membershipType;
    }

    public void setMembershipType(String membershipType) {
        this.membershipType = membershipType;
    }

    public String getMembershipDescription() {
        return membershipDescription;
    }

    public void setMembershipDescription(String membershipDescription) {
        this.membershipDescription = membershipDescription;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusFa() {
        return statusFa;
    }

    public void setStatusFa(String statusFa) {
        this.statusFa = statusFa;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public String getRequestDatePersianString(){
        return getStringFormatOfDate(registerDateTs);
    }
    public String getRequestEndDatePersianString(){
        return getStringFormatOfDate(toDateTs);
    }

    public long getParentUsername() {
        return parentUsername;
    }

    public void setParentUsername(long parentUsername) {
        this.parentUsername = parentUsername;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getParentFamily() {
        return parentFamily;
    }

    public void setParentFamily(String parentFamily) {
        this.parentFamily = parentFamily;
    }

    public String getParentThumbUrl() {
        return parentThumbUrl;
    }

    public void setParentThumbUrl(String parentThumbUrl) {
        this.parentThumbUrl = parentThumbUrl;
    }
}