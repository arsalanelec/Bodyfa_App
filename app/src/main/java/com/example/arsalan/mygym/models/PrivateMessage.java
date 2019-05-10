package com.example.arsalan.mygym.models;

import com.google.gson.annotations.SerializedName;

public class PrivateMessage {
    /*"UsersMessagesId": 9,
            "UserIdSender": 45,
            "SenderName": "باشگاه اول 1",
            "SenderFamily": "",
            "UserIdReceiver": 40,
            "ReceiverName": "مربی اول",
            "ReceiverFamily": null,
            "Message": "test",
            "SendDate": "2018-06-16T12:20:15.92",
            "SendDateFa": "1397/03/26",
            "SendTime": "",
            "SeenDate": null,
            "SeenDateFa": "",
            "SeenTime": "",
            "IsSeen": false*/
    @SerializedName("UsersMessagesId")
    long id;
    String senderName;
    @SerializedName("UserIdSender")
    long senderId;

    String receiverName;

    @SerializedName("UserIdReceiver")
    long receiverId;

    String message;

    String sendDate;

    public PrivateMessage() {
    }

    public long getId() {
        return id;
    }

    public String getSenderName() {
        return senderName;
    }

    public long getSenderId() {
        return senderId;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public long getReceiverId() {
        return receiverId;
    }

    public String getMessage() {
        return message;
    }

    public String getSendDate() {
        return sendDate;
    }
}
