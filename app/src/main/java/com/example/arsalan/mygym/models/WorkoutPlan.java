package com.example.arsalan.mygym.models;

import androidx.room.Entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

@Entity(primaryKeys = {"trainerWorkoutPlanId","athleteWorkoutPlanId"}  )
public class WorkoutPlan implements Parcelable {
    /*"TrainerWorkoutPlanId": null,
        "Title": null,
        "DateFa": null,
        "DateEn": null,
        "UserId": null,
        "Description": null,
        "Price": null*/
    @SerializedName("AthleteWorkoutPlanId")
    private long athleteWorkoutPlanId;

    @SerializedName("TrainerWorkoutPlanId")
    private long trainerWorkoutPlanId;
    @SerializedName("Title")
    private String title;

    @SerializedName("DateFa")
    private String date;

    @SerializedName(value="AthleteUserId", alternate={"UserId"})
    private long userId;
    @SerializedName("Description")
    private String description;
    @SerializedName("TrainerName")
    private String trainerName;

    public WorkoutPlan() {
    }

    protected WorkoutPlan(Parcel in) {
        athleteWorkoutPlanId = in.readLong();
        trainerWorkoutPlanId = in.readLong();
        title = in.readString();
        date = in.readString();
        userId = in.readLong();
        description = in.readString();
        trainerName = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(athleteWorkoutPlanId);
        dest.writeLong(trainerWorkoutPlanId);
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

    public static final Creator<WorkoutPlan> CREATOR = new Creator<WorkoutPlan>() {
        @Override
        public WorkoutPlan createFromParcel(Parcel in) {
            return new WorkoutPlan(in);
        }

        @Override
        public WorkoutPlan[] newArray(int size) {
            return new WorkoutPlan[size];
        }
    };

    public void setTrainerWorkoutPlanId(long trainerWorkoutPlanId) {
        this.trainerWorkoutPlanId = trainerWorkoutPlanId;
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

    public long getTrainerWorkoutPlanId() {
        return trainerWorkoutPlanId;
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

    public long getAthleteWorkoutPlanId() {
        return athleteWorkoutPlanId;
    }

    public String getTrainerName() {
        return trainerName;
    }

    public void setAthleteWorkoutPlanId(long athleteWorkoutPlanId) {
        this.athleteWorkoutPlanId = athleteWorkoutPlanId;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "{" +

                " trainerWorkoutPlanId=" + trainerWorkoutPlanId +
                ", title='" + title + '\'' +
                ", date='" + date + '\'' +
                ", userId=" + userId +
                ", description='" + description + '\'' +
                '}';
    }

}
