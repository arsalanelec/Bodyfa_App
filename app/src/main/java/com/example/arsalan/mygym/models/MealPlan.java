package com.example.arsalan.mygym.models;

import androidx.room.Entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

@Entity(primaryKeys = {"trainerMealPlanId","athleteMealPlanId"}  )
public class MealPlan implements Parcelable {
    /*"TrainerWorkoutPlanId": null,
        "Title": null,
        "DateFa": null,
        "DateEn": null,
        "UserId": null,
        "Description": null,
        "Price": null*/
    @SerializedName("AthleteMealPlanId")
    private long athleteMealPlanId;

    @SerializedName("TrainerMealPlanId")
    private long trainerMealPlanId;


    private String title;

    @SerializedName("DateFa")
    private String date;

    @SerializedName(value="AthleteUserId", alternate={"UserId"})
    private long userId;
    @SerializedName("Description")
    private String description;

    private String trainerName;

    public MealPlan() {
    }

    protected MealPlan(Parcel in) {
        athleteMealPlanId = in.readLong();
        trainerMealPlanId = in.readLong();
        title = in.readString();
        date = in.readString();
        userId = in.readLong();
        description = in.readString();
        trainerName = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(athleteMealPlanId);
        dest.writeLong(trainerMealPlanId);
        dest.writeString(title);
        dest.writeString(date);
        dest.writeLong(userId);
        dest.writeString(description);
        dest.writeString(trainerName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MealPlan> CREATOR = new Creator<MealPlan>() {
        @Override
        public MealPlan createFromParcel(Parcel in) {
            return new MealPlan(in);
        }

        @Override
        public MealPlan[] newArray(int size) {
            return new MealPlan[size];
        }
    };

    public void setAthleteMealPlanId(long athleteMealPlanId) {
        this.athleteMealPlanId = athleteMealPlanId;
    }

    public void setTrainerMealPlanId(long trainerMealPlanId) {
        this.trainerMealPlanId = trainerMealPlanId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTrainerName(String trainerName) {
        this.trainerName = trainerName;
    }

    public String getTrainerName() {
        return trainerName;
    }

    public long getTrainerMealPlanId() {
        return trainerMealPlanId;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public long getUserId() {
        return userId;
    }

    public String getDescription() {
        return description;
    }

    public long getAthleteMealPlanId() {
        return athleteMealPlanId;
    }

    @Override
    public String toString() {
        return "{" +
                "mealPlanIdSt='" + trainerMealPlanId + '\'' +
                ", trainerMealPlanId=" + trainerMealPlanId +
                ", title='" + title + '\'' +
                ", date='" + date + '\'' +
                ", userId=" + userId +
                ", description='" + description + '\'' +
                '}';
    }

}
