package com.example.arsalan.mygym.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity
public class Honor {
    /*"HonorId": 11,
            "UserId": 40,
            "Title": "مقام اول مسابقات جهانی",
            "HonorCategory": null,
            "GetDate": "1991-03-21T00:00:00",
            "GetDateFa": "1370/01/01",
            "GetDateTime": "10:36:34",
            "PictureUrl": "",
            "ThumbUrl": "",
            "IsConfirmed": true*/
    @PrimaryKey
    @SerializedName("HonorId")
    private long id;

    private long userId;

    private String title;

    private String pictureUrl;
    private String thumbUrl;

    @SerializedName("HonorCategory")
    private int category;

    public Honor() {
    }

    public long getId() {
        return id;
    }

    public long getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public int getCategory() {
        return category;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public void setCategory(int category) {
        this.category = category;
    }
}

