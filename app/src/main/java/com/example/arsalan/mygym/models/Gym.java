package com.example.arsalan.mygym.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
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
    @Expose
    private long id;
    @SerializedName("GymId")
    @Expose
    private long gymId;
    @SerializedName("Title")
    @Expose
    private String  title;
    @SerializedName("CityId")
    @Expose
    private int cityId;
    @SerializedName("CityName")
    @Expose
    private String  cityName;
    @SerializedName("Address")
    @Expose
    private String  address;
    @SerializedName("Phone1")
    @Expose
    private String  phone1;
    @SerializedName("Phone2")
    @Expose
    private String phone2;
    @SerializedName("ThumbUrl")
    @Expose
    private String thumbUrl;
    @SerializedName("PictureUrl")
    @Expose
    private String pictureUrl;
    @SerializedName("Long")
    @Expose
    private double lng;
    @SerializedName("Lat")
    @Expose
    private double lat;
    @SerializedName("Intro")
    @Expose
    private String intro;

    @SerializedName("Name")
    @Expose
    private String  name;
    @SerializedName("Family")
    @Expose
    private String  family;
    @SerializedName("Rate")
    @Expose
    private long rate;
    @SerializedName("Point")
    @Expose
    private long point;
    @SerializedName("Status")
    @Expose
    private String  status;
    @SerializedName("Features")
    @Expose
    private String  features;
    @SerializedName("Description")
    @Expose
    private String  description;
    @SerializedName("OneDayFee")
    @Expose
    private int oneDayFee;
    @SerializedName("WeeklyFee")
    @Expose
    private int weeklyFee;
    @SerializedName("TwelveDaysFee")
    @Expose
    private int twelveDaysFee;
    @SerializedName("HalfMonthFee")
    @Expose
    private int halfMonthFee;
    @SerializedName("MonthlyFee")
    @Expose
    private int monthlyFee;
    @SerializedName("Credit")
    @Expose
    private int credit;
    @SerializedName("CardNumber")
    @Expose
    private String cardNumber;
    @SerializedName("IsConfirmed")
    @Expose
    private boolean isConfirmed;
    @SerializedName("QrCode")
    @Expose
    private String qrCode;


    public Gym() {

    }

    protected Gym(Parcel in) {
        gymId = in.readLong();
        title = in.readString();
        cityId = in.readInt();
        cityName = in.readString();
        address = in.readString();
        phone1 = in.readString();
        phone2 = in.readString();
        thumbUrl = in.readString();
        pictureUrl = in.readString();
        lng = in.readDouble();
        lat = in.readDouble();
        intro = in.readString();
        id = in.readLong();
        name = in.readString();
        family = in.readString();
        rate = in.readLong();
        point = in.readLong();
        status = in.readString();
        features = in.readString();
        description = in.readString();
        oneDayFee = in.readInt();
        weeklyFee = in.readInt();
        twelveDaysFee = in.readInt();
        halfMonthFee = in.readInt();
        monthlyFee = in.readInt();
        credit = in.readInt();
        cardNumber = in.readString();
        isConfirmed = in.readByte() != 0;
        qrCode = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(gymId);
        dest.writeString(title);
        dest.writeInt(cityId);
        dest.writeString(cityName);
        dest.writeString(address);
        dest.writeString(phone1);
        dest.writeString(phone2);
        dest.writeString(thumbUrl);
        dest.writeString(pictureUrl);
        dest.writeDouble(lng);
        dest.writeDouble(lat);
        dest.writeString(intro);
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(family);
        dest.writeLong(rate);
        dest.writeLong(point);
        dest.writeString(status);
        dest.writeString(features);
        dest.writeString(description);
        dest.writeInt(oneDayFee);
        dest.writeInt(weeklyFee);
        dest.writeInt(twelveDaysFee);
        dest.writeInt(halfMonthFee);
        dest.writeInt(monthlyFee);
        dest.writeInt(credit);
        dest.writeString(cardNumber);
        dest.writeByte((byte) (isConfirmed ? 1 : 0));
        dest.writeString(qrCode);
    }

    @Override
    public int describeContents() {
        return 0;
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

    public long getGymId() {
        return gymId;
    }

    public void setGymId(long gymId) {
        this.gymId = gymId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public long getRate() {
        return rate;
    }

    public void setRate(long rate) {
        this.rate = rate;
    }

    public long getPoint() {
        return point;
    }

    public void setPoint(long point) {
        this.point = point;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFeatures() {
        return features;
    }

    public void setFeatures(String features) {
        this.features = features;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getOneDayFee() {
        return oneDayFee;
    }

    public void setOneDayFee(int oneDayFee) {
        this.oneDayFee = oneDayFee;
    }

    public int getWeeklyFee() {
        return weeklyFee;
    }

    public void setWeeklyFee(int weeklyFee) {
        this.weeklyFee = weeklyFee;
    }

    public int getTwelveDaysFee() {
        return twelveDaysFee;
    }

    public void setTwelveDaysFee(int twelveDaysFee) {
        this.twelveDaysFee = twelveDaysFee;
    }

    public int getHalfMonthFee() {
        return halfMonthFee;
    }

    public void setHalfMonthFee(int halfMonthFee) {
        this.halfMonthFee = halfMonthFee;
    }

    public int getMonthlyFee() {
        return monthlyFee;
    }

    public void setMonthlyFee(int monthlyFee) {
        this.monthlyFee = monthlyFee;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public boolean isConfirmed() {
        return isConfirmed;
    }

    public void setConfirmed(boolean confirmed) {
        isConfirmed = confirmed;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }
}
