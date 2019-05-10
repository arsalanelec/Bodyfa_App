package com.example.arsalan.mygym.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Arsalan on 22-02-2018.
 */
@Entity
public class Gym implements Parcelable {
    /*"GymFeatures": null,
            "GymId": 1,
            "Title": "1",
            "CityId": 1,
            "CityName": null,
            "Address": "1",
            "Phone1": "1",
            "Phone2": "1",
            "ThumbUrl": "1",
            "PictureUrl": "/Content/images/1.jpg",
            "Long": "1",
            "Lat": "1",
            "Intro": "1",
            "UserId": 1,
            "Name": null,
            "Family": null,
            "rate": 1,
            "point": 1,
            "Status": "active"
            description
            features*/
    @PrimaryKey
    @SerializedName("UserId")
    int id;
    @SerializedName("Title")
    private String title;

    private String address;

    @SerializedName("rate")
    private double rate;
    private int point;
    private String status;
    private int cityId;
    private double lat;

    @SerializedName("ThumbUrl")
    private String thumbUrl;
    @SerializedName("PictureUrl")
    private String pictureUrl;
    @SerializedName("Long")
    private double lon;

    @SerializedName("Phone1")
    private String phone1;

    @SerializedName("Phone2")
    private String phone2;

    private String description;

    private String features;

    @SerializedName("HalfMonthFee")
    private int halfMonthFee;

    @SerializedName("MonthlyFee")
    private int monthlyFee;

    public Gym() {

    }

    protected Gym(Parcel in) {
        id = in.readInt();
        title = in.readString();
        address = in.readString();
        rate = in.readDouble();
        point = in.readInt();
        status = in.readString();
        cityId = in.readInt();
        lat = in.readDouble();
        thumbUrl = in.readString();
        pictureUrl = in.readString();
        lon = in.readDouble();
        phone1 = in.readString();
        phone2 = in.readString();
        description = in.readString();
        features = in.readString();
        halfMonthFee = in.readInt();
        monthlyFee = in.readInt();
    }

    public static final Creator<Gym> CREATOR = new Creator<Gym>() {
        @Override
        public Gym createFromParcel(Parcel in) {
            return new Gym(in);
        }

        @Override
        public Gym[] newArray(int size) {
            return new Gym[size];
        }
    };

    public int getHalfMonthFee() {
        return halfMonthFee;
    }

    public int getMonthlyFee() {
        return monthlyFee;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setFeatures(String features) {
        this.features = features;
    }

    public void setHalfMonthFee(int halfMonthFee) {
        this.halfMonthFee = halfMonthFee;
    }

    public void setMonthlyFee(int monthlyFee) {
        this.monthlyFee = monthlyFee;
    }

    public String getDescription() {
        return description;
    }

    public String getFeatures() {
        return features;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAddress() {
        return address;
    }

    public double getRate() {
        return rate;
    }

    public int getPoint() {
        return point;
    }

    public String getStatus() {
        return status;
    }

    public int getCityId() {
        return cityId;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public String getPhone1() {
        return phone1;
    }

    public String getPhone2() {
        return phone2;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeString(address);
        parcel.writeDouble(rate);
        parcel.writeInt(point);
        parcel.writeString(status);
        parcel.writeInt(cityId);
        parcel.writeDouble(lat);
        parcel.writeString(thumbUrl);
        parcel.writeString(pictureUrl);
        parcel.writeDouble(lon);
        parcel.writeString(phone1);
        parcel.writeString(phone2);
        parcel.writeString(description);
        parcel.writeString(features);
        parcel.writeInt(halfMonthFee);
        parcel.writeInt(monthlyFee);
    }
}
