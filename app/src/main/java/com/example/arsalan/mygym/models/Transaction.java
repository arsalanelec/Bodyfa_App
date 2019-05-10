package com.example.arsalan.mygym.models;

import com.google.gson.annotations.SerializedName;

import java.text.DecimalFormat;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * received or payed transaction object
 */
    /*"TransactionId": 59,
            "SenderId": 33,
            "SenderName": "ارسلان",
            "ReceiverId": 33,
            "ReceiverName": "ارسلان",
            "TransactionDateTimeEn": "2019-03-09T10:44:59.803",
            "TransactionDateTimeEnTs": 1552128299,
            "TransactionDateFa": "1397/12/18",
            "TransactionTimeFa": "10:44:59",
            "TransactionAmount": 10000,
            "Description": "",
            "HasPaid": false,
            "TransactionType": "increase",
            "ReferenceNumber": "",
            "ReservationNumber": "1552128299",
            "Status": "waiting",
            "StateCode": "",
            "SenderCardNumber": "",
            "TransactionConfirmedDateTimeEn": null,
            "TransactionConfirmedDateTimeEnTs": 0,
            "TransactionConfirmedDateFa": null,
            "TransactionConfirmedTimeFa": null*/
@Entity(tableName = "uTransaction")
public class Transaction {
    @PrimaryKey
    @SerializedName("TransactionId")
    private int id;
    private int senderId;
    @SerializedName("TransactionDateFa")
    private String dateCreated;
    @SerializedName("TransactionTimeFa")
    private String timeCreated;
    @SerializedName("TransactionAmount")
    private int amount;
    private String description;
    private String senderName;
    private int receiverId;
    private String receiverName;
    @SerializedName("TransactionType")
    private String type;
    private String status;

    public Transaction() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(String timeCreated) {
        this.timeCreated = timeCreated;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
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
    public String getAmountFormatted(){
        DecimalFormat formatter = new DecimalFormat("###,###");
        return formatter.format(amount);
    }

    public String getDateFormatted(){
        return  dateCreated+" "+timeCreated;
    }
}
