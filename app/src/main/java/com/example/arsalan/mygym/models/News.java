package com.example.arsalan.mygym.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Arsalan on 10-02-2018.
 */
@Entity
public class News {
    @PrimaryKey
    @SerializedName("NewsId")
    private
    long id;

    private String title;

    @SerializedName("Description")
    private
    String desc;

    @SerializedName("NewsDateFa")
    private
    String date;

    @SerializedName("ViewCount")
    private
    int visitcnt;

    @SerializedName("LikeCount")
    private
    int likeCnt;

    @SerializedName("NewsTypeId")
    private int typeId;

    private  String UserThumbUrl;

    @SerializedName("CommentCount")
    private
    int commentCnt;
    private String thumbUrl;

    private String pictureUrl;

    private boolean isLiked;

    public News() {
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setUserThumbUrl(String userThumbUrl) {
        UserThumbUrl = userThumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public String getUserThumbUrl() {
        return UserThumbUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getId() {
        return id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
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

    public int getCommentCnt() {
        return commentCnt;
    }

    public void setCommentCnt(int commentCnt) {
        this.commentCnt = commentCnt;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }
}
