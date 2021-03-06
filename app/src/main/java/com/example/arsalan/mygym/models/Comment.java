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
    private
    long id;
    private String senderName;
    private long newsId;
    private String commentDateFa;
    private String commentTimeFa;

    @SerializedName("CommentDateTs")
    private
    long commentDateTs;

    private String comment;
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

    public void setId(long id) {
        this.id = id;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public void setNewsId(long newsId) {
        this.newsId = newsId;
    }

    public void setCommentDateFa(String commentDateFa) {
        this.commentDateFa = commentDateFa;
    }

    public void setCommentTimeFa(String commentTimeFa) {
        this.commentTimeFa = commentTimeFa;
    }

    public long getCommentDateTs() {
        return commentDateTs;
    }

    public void setCommentDateTs(long commentDateTs) {
        this.commentDateTs = commentDateTs;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
