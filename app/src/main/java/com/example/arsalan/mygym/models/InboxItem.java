package com.example.arsalan.mygym.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;
@Entity
public class InboxItem {
    /* "UsersMessagesId": 13,
            "UserId": 40,
            "PartyId": 45,
            "PartyName": "باشگاه اول 1",
            "PartyFamily": "",
            "PartyThumbUrl": "/Content/images/thumb_2ff4b87f-e2bb-4a23-a035-64d36ea87855",
            "Message": "test1",
            "SendDate": "2018-06-17T12:20:15.92",
            "SendDateFa": "1397/03/27",
            "SendTime": "",
            "SeenDate": null,
            "SeenDateFa": "",
            "SeenTime": "",
            "IsSeen": false*/
    @PrimaryKey
    @SerializedName("UsersMessagesId")
    private long id;

    private long userId;
    private long partyId;
    private String partyName;

    private String partyThumbUrl;
    private String message;
    private String sendDate;

    public InboxItem() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getPartyId() {
        return partyId;
    }

    public void setPartyId(long partyId) {
        this.partyId = partyId;
    }

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

    public String getPartyThumbUrl() {
        return partyThumbUrl;
    }

    public void setPartyThumbUrl(String partyThumbUrl) {
        this.partyThumbUrl = partyThumbUrl;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSendDate() {
        return sendDate;
    }

    public void setSendDate(String sendDate) {
        this.sendDate = sendDate;
    }
}
