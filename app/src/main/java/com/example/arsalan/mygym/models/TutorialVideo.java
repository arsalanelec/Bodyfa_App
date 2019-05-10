package com.example.arsalan.mygym.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity
public class TutorialVideo {
    /* "TutorialId": 17,
            "UserId": 1,
            "Name": "NameName",
            "ThumbUrl": "/Content/images/thumb_4ad32b5c-bd34-473b-b1f6-10b45279a450.jpg",
            "PictureUrl": "/Content/images/4ad32b5c-bd34-473b-b1f6-10b45279a450.mp4",
            "TutorialSubCategoryId": 1,
            "TutorialSubCategoryTitle": "پشت پا ایستاده",
            "ViewCount": 0,*/
    @PrimaryKey
    @SerializedName("TutorialId")
    private long id;

    @SerializedName("UserId")
    private long userId;

    @SerializedName("Name")
    private String name;

    @SerializedName("ThumbUrl")
    private String thumbUrl;

    @SerializedName("PictureUrl")
    private String pictureUrl;

    @SerializedName("TutorialSubCategoryId")
    private long subCatId;

    @SerializedName("TutorialSubCategoryTitle")
    private String subCatName;

    @SerializedName("ViewCount")
    private int viewCount;

    public TutorialVideo() {
    }

    public long getId() {
        return id;
    }

    public long getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public long getSubCatId() {
        return subCatId;
    }

    public String getSubCatName() {
        return subCatName;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public void setSubCatId(long subCatId) {
        this.subCatId = subCatId;
    }

    public void setSubCatName(String subCatName) {
        this.subCatName = subCatName;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }
}
