package com.example.arsalan.mygym.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Arsalan on 10-02-2018.
 */
@Entity
public class NewsHead {
    @PrimaryKey
    @SerializedName("NewsId")
    private
    long id;
    @SerializedName("Title")
    private String title;

    @SerializedName("NewsDateFa")
    private
    String date;

    @SerializedName("ViewCount")
    private
    int visitcnt;

    @SerializedName("UserId")
    private
    long publisherId;

    @SerializedName("LikeCount")
    private
    int likeCnt;

    @SerializedName("NewsTypeId")
    private int typeId;

    private  String UserThumbUrl;

    @SerializedName("CommentCount")

    private
    int commentCnt;
    @SerializedName("ThumbUrl")
    private String thumbUrl;



    public NewsHead() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getVisitcnt() {
        return visitcnt;
    }

    public void setVisitcnt(int visitcnt) {
        this.visitcnt = visitcnt;
    }

    public int getLikeCnt() {
        return likeCnt;
    }

    public void setLikeCnt(int likeCnt) {
        this.likeCnt = likeCnt;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getUserThumbUrl() {
        return UserThumbUrl;
    }

    public void setUserThumbUrl(String userThumbUrl) {
        UserThumbUrl = userThumbUrl;
    }

    public int getCommentCnt() {
        return commentCnt;
    }

    public void setCommentCnt(int commentCnt) {
        this.commentCnt = commentCnt;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public long getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(long publisherId) {
        this.publisherId = publisherId;
    }
}
