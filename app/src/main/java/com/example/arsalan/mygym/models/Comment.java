package com.example.arsalan.mygym.models;

import com.google.gson.annotations.SerializedName;

public class Comment {
    /*            "NewsCommentId": 9,
            "UserId": 1,
            "SenderName": "NameFamily",
            "NewsId": 5,
            "CommentDate": "2018-04-14T12:57:34.923",
            "CommentDateFa": "1397/01/25",
            "CommentTimeFa": "12:57:34",
            "Title": "",
            "Comment": "سلام این یک پیام نمونه است."*/
    @SerializedName("NewsCommentId")
    long id;
    String senderName;
    long newsId;
    String commentDateFa;
    String commentTimeFa;
    String comment;
    public Comment() {
    }

    public long getId() {
        return id;
    }

    public String getSenderName() {
        return senderName;
    }

    public long getNewsId() {
        return newsId;
    }

    public String getCommentDateFa() {
        return commentDateFa;
    }

    public String getCommentTimeFa() {
        return commentTimeFa;
    }

    public String getComment() {
        return comment;
    }
}
