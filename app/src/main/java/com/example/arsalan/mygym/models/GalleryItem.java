package com.example.arsalan.mygym.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

@Entity
public class GalleryItem implements Parcelable {
    /*"GalleryId": 1,
            "ThumbUrl": "/Content/images/thumb_97ffb3f8-fada-4161-b341-b5f8c2e3bd19.jpg",
            "PictureUrl": "/Content/images/97ffb3f8-fada-4161-b341-b5f8c2e3bd19.jpg",

            "UserId"*/

    public GalleryItem() {
    }

    @PrimaryKey
    @SerializedName("GalleryId")
    long id;
    String thumbUrl;
    String pictureUrl;
    long userId;

    protected GalleryItem(Parcel in) {
        id = in.readLong();
        thumbUrl = in.readString();
        pictureUrl = in.readString();
        userId = in.readLong();
    }

    public static final Creator<GalleryItem> CREATOR = new Creator<GalleryItem>() {
        @Override
        public GalleryItem createFromParcel(Parcel in) {
            return new GalleryItem(in);
        }

        @Override
        public GalleryItem[] newArray(int size) {
            return new GalleryItem[size];
        }
    };

    public void setId(long id) {
        this.id = id;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getId() {
        return id;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public long getUserId() {
        return userId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(thumbUrl);
        parcel.writeString(pictureUrl);
        parcel.writeLong(userId);
    }
}
