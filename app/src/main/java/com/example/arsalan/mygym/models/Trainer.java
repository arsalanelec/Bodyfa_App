package com.example.arsalan.mygym.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Arsalan on 10-02-2018.
 */
@Entity
public class Trainer implements Parcelable {

    @PrimaryKey
    @SerializedName("UserId")
    long id;

    /*     "UserId": 0,
        "ProvinceId": 0,
        "CityId": 0,
        "GymId": 0,
        "Name": null,
        "Family": null,
        "Rate": 0,
        "Point": 0,
        "ChampionshipCount": 0,
        "MealPlanPrice": 0,
        "WorkoutPlanPrice": 0,
        "PictureUrl": null,
        "ThumbUrl": null,
        "DocumentsPictureUrl": null,
        "DocumentsThumbUrl": null,
        "Honors": null
        },*/
    private String name;
    //@SerializedName("Honors")

    long rate;
    int point;

    int cityId;
    int gymId;

    int mealPlanPrice;
    int workoutPlanPrice;

    @SerializedName("PictureUrl")
    private String pictureUrl;

    @SerializedName("ThumbUrl")
    private String thumbUrl;


    @SerializedName("DocumentsPictureUrl")
    private String docPictureUrl;

    @SerializedName("DocumentsThumbUrl")
    private String DocThumbUrl;

    @SerializedName("NationalCardPictureUrl")
    private String nationalCardPictureUrl;

    @SerializedName("NationalCardThumbUrl")
    private String nationalCardThumbUrl;

    private String isConfirmed;
    public Trainer() {
    }

    protected Trainer(Parcel in) {
        id = in.readLong();
        name = in.readString();
        rate = in.readLong();
        point = in.readInt();
        cityId = in.readInt();
        gymId = in.readInt();
        mealPlanPrice = in.readInt();
        workoutPlanPrice = in.readInt();
        pictureUrl = in.readString();
        thumbUrl = in.readString();
        docPictureUrl = in.readString();
        DocThumbUrl = in.readString();
        nationalCardPictureUrl = in.readString();
        nationalCardThumbUrl = in.readString();
        isConfirmed = in.readString();
    }

    public static final Creator<Trainer> CREATOR = new Creator<Trainer>() {
        @Override
        public Trainer createFromParcel(Parcel in) {
            return new Trainer(in);
        }

        @Override
        public Trainer[] newArray(int size) {
            return new Trainer[size];
        }
    };

    public boolean isConfirmed(){return this.isConfirmed.equalsIgnoreCase("True");}

    public String getNationalCardPictureUrl() {
        return nationalCardPictureUrl;
    }

    public String getNationalCardThumbUrl() {
        return nationalCardThumbUrl;
    }

    public int getMealPlanPrice() {
        return mealPlanPrice;
    }

    public int getWorkoutPlanPrice() {
        return workoutPlanPrice;
    }

    public String getDocPictureUrl() {
        return docPictureUrl;
    }

    public String getDocThumbUrl() {
        return DocThumbUrl;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }


    public long getRate() {
        return rate;
    }

    public int getPoint() {
        return point;
    }

    public int getCityId() {
        return cityId;
    }

    public int getGymId() {
        return gymId;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRate(long rate) {
        this.rate = rate;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public void setGymId(int gymId) {
        this.gymId = gymId;
    }

    public void setMealPlanPrice(int mealPlanPrice) {
        this.mealPlanPrice = mealPlanPrice;
    }

    public void setWorkoutPlanPrice(int workoutPlanPrice) {
        this.workoutPlanPrice = workoutPlanPrice;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public void setDocPictureUrl(String docPictureUrl) {
        this.docPictureUrl = docPictureUrl;
    }

    public void setDocThumbUrl(String docThumbUrl) {
        DocThumbUrl = docThumbUrl;
    }

    public void setNationalCardPictureUrl(String nationalCardPictureUrl) {
        this.nationalCardPictureUrl = nationalCardPictureUrl;
    }

    public void setNationalCardThumbUrl(String nationalCardThumbUrl) {
        this.nationalCardThumbUrl = nationalCardThumbUrl;
    }

    public String getIsConfirmed() {
        return isConfirmed;
    }

    public void setIsConfirmed(String isConfirmed) {
        this.isConfirmed = isConfirmed;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(name);
        parcel.writeLong(rate);
        parcel.writeInt(point);
        parcel.writeInt(cityId);
        parcel.writeInt(gymId);
        parcel.writeInt(mealPlanPrice);
        parcel.writeInt(workoutPlanPrice);
        parcel.writeString(pictureUrl);
        parcel.writeString(thumbUrl);
        parcel.writeString(docPictureUrl);
        parcel.writeString(DocThumbUrl);
        parcel.writeString(nationalCardPictureUrl);
        parcel.writeString(nationalCardThumbUrl);
        parcel.writeString(isConfirmed);
    }
}
