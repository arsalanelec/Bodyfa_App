package com.example.arsalan.mygym.models;

import android.os.Parcel;
import android.os.Parcelable;

public class MealRow implements Parcelable {
    int type;
    String desc;

    public MealRow() {
        type =0;
    }

    protected MealRow(Parcel in) {
        type = in.readInt();
        desc = in.readString();
    }

    public static final Creator<MealRow> CREATOR = new Creator<MealRow>() {
        @Override
        public MealRow createFromParcel(Parcel in) {
            return new MealRow(in);
        }

        @Override
        public MealRow[] newArray(int size) {
            return new MealRow[size];
        }
    };

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "MealRow{" +
                "type=" + type +
                ", desc='" + desc + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(type);
        parcel.writeString(desc);
    }
}
