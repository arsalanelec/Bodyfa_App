package com.example.arsalan.mygym.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Arsalan on 10-02-2018.
 */
@Entity
public class Trainer implements Parcelable {

    @PrimaryKey
    @SerializedName("UserId")
    @Expose
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
    @SerializedName("TrainerDetailsId")
    @Expose
    private int trainerDetailsId;
    @SerializedName("ProvinceId")
    @Expose
    private int provinceId;
    @SerializedName("CityId")
    @Expose
    private int cityId;
    @SerializedName("GymId")
    @Expose
    private long gymId;
    @SerializedName("GymName")
    @Expose
    private String  gymName;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("Family")
    @Expose
    private String  family;
    @SerializedName("Rate")
    @Expose
    private long rate;
    @SerializedName("Point")
    @Expose
    private int point;
    @SerializedName("ChampionshipCount")
    @Expose
    private int championshipCount;
    @SerializedName("MealPlanPrice")
    @Expose
    private int mealPlanPrice;
    @SerializedName("WorkoutPlanPrice")
    @Expose
    private int workoutPlanPrice;
    @SerializedName("PictureUrl")
    @Expose
    private String pictureUrl;
    @SerializedName("ThumbUrl")
    @Expose
    private String  thumbUrl;
    @SerializedName("DocumentsPictureUrl")
    @Expose
    private String  docPictureUrl;
    @SerializedName("DocumentsThumbUrl")
    @Expose
    private String  docThumbUrl;
    @SerializedName("NationalCardPictureUrl")
    @Expose
    private String  nationalCardPictureUrl;
    @SerializedName("NationalCardThumbUrl")
    @Expose
    private String  nationalCardThumbUrl;
    @SerializedName("IsConfirmed")
    @Expose
    private boolean isConfirmed;
    @SerializedName("CardNumber")
    @Expose
    private String  cardNumber;
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
    @SerializedName("QrCode")
    @Expose
    private String  qrCode;
    /*@SerializedName("Honors")
    @Expose
    private Object honors;*/


    public Trainer() {
    }

    protected Trainer(Parcel in) {
        id = in.readLong();
        trainerDetailsId = in.readInt();
        provinceId = in.readInt();
        cityId = in.readInt();
        gymId = in.readLong();
        gymName = in.readString();
        name = in.readString();
        family = in.readString();
        rate = in.readLong();
        point = in.readInt();
        championshipCount = in.readInt();
        mealPlanPrice = in.readInt();
        workoutPlanPrice = in.readInt();
        pictureUrl = in.readString();
        thumbUrl = in.readString();
        docPictureUrl = in.readString();
        docThumbUrl = in.readString();
        nationalCardPictureUrl = in.readString();
        nationalCardThumbUrl = in.readString();
        isConfirmed = in.readByte() != 0;
        cardNumber = in.readString();
        oneDayFee = in.readInt();
        weeklyFee = in.readInt();
        twelveDaysFee = in.readInt();
        halfMonthFee = in.readInt();
        monthlyFee = in.readInt();
        credit = in.readInt();
        qrCode = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeInt(trainerDetailsId);
        dest.writeInt(provinceId);
        dest.writeInt(cityId);
        dest.writeLong(gymId);
        dest.writeString(gymName);
        dest.writeString(name);
        dest.writeString(family);
        dest.writeLong(rate);
        dest.writeInt(point);
        dest.writeInt(championshipCount);
        dest.writeInt(mealPlanPrice);
        dest.writeInt(workoutPlanPrice);
        dest.writeString(pictureUrl);
        dest.writeString(thumbUrl);
        dest.writeString(docPictureUrl);
        dest.writeString(docThumbUrl);
        dest.writeString(nationalCardPictureUrl);
        dest.writeString(nationalCardThumbUrl);
        dest.writeByte((byte) (isConfirmed ? 1 : 0));
        dest.writeString(cardNumber);
        dest.writeInt(oneDayFee);
        dest.writeInt(weeklyFee);
        dest.writeInt(twelveDaysFee);
        dest.writeInt(halfMonthFee);
        dest.writeInt(monthlyFee);
        dest.writeInt(credit);
        dest.writeString(qrCode);
    }

    @Override
    public int describeContents() {
        return 0;
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getTrainerDetailsId() {
        return trainerDetailsId;
    }

    public void setTrainerDetailsId(int trainerDetailsId) {
        this.trainerDetailsId = trainerDetailsId;
    }


    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public long getGymId() {
        return gymId;
    }

    public void setGymId(long gymId) {
        this.gymId = gymId;
    }

    public String getGymName() {
        return gymName;
    }

    public void setGymName(String gymName) {
        this.gymName = gymName;
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

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public int getChampionshipCount() {
        return championshipCount;
    }

    public void setChampionshipCount(int championshipCount) {
        this.championshipCount = championshipCount;
    }

    public int getMealPlanPrice() {
        return mealPlanPrice;
    }

    public void setMealPlanPrice(int mealPlanPrice) {
        this.mealPlanPrice = mealPlanPrice;
    }

    public int getWorkoutPlanPrice() {
        return workoutPlanPrice;
    }

    public void setWorkoutPlanPrice(int workoutPlanPrice) {
        this.workoutPlanPrice = workoutPlanPrice;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public String getDocPictureUrl() {
        return docPictureUrl;
    }

    public void setDocPictureUrl(String docPictureUrl) {
        this.docPictureUrl = docPictureUrl;
    }

    public String getDocThumbUrl() {
        return docThumbUrl;
    }

    public void setDocThumbUrl(String docThumbUrl) {
        this.docThumbUrl = docThumbUrl;
    }

    public String getNationalCardPictureUrl() {
        return nationalCardPictureUrl;
    }

    public void setNationalCardPictureUrl(String nationalCardPictureUrl) {
        this.nationalCardPictureUrl = nationalCardPictureUrl;
    }

    public String getNationalCardThumbUrl() {
        return nationalCardThumbUrl;
    }

    public void setNationalCardThumbUrl(String nationalCardThumbUrl) {
        this.nationalCardThumbUrl = nationalCardThumbUrl;
    }

    public boolean isConfirmed() {
        return isConfirmed;
    }

    public void setConfirmed(boolean confirmed) {
        isConfirmed = confirmed;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
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

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public static Creator<Trainer> getCREATOR() {
        return CREATOR;
    }


}
