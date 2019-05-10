package com.example.arsalan.mygym.models;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class UserCredit {

    @PrimaryKey
    @SerializedName("UserId")
    long userId;
    @SerializedName("Credit")
    private int credit;
    @SerializedName("Result")
    private String result;

    public UserCredit() {
    }

    public int getCredit(){
        return credit;
    }
    public String getCreditFromatted() {
        String formatedAmount = "0";

            DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.JAPAN);
            DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
            symbols.setGroupingSeparator(',');
            formatter.setDecimalFormatSymbols(symbols);
            formatedAmount = formatter.format(credit);
        return formatedAmount;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
