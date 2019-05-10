package com.example.arsalan.mygym.models;

import com.google.gson.annotations.SerializedName;

public class Token {
    @SerializedName("access_token")
    private String token;
    @SerializedName("expires_in")
    private int exprieIn;

    public Token() {
    }

    public Token(String token) {
       this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTokenBearer() {
        return "bearer " + token;
    }

    public int getExprieIn() {
        return exprieIn;
    }

    public void setExprieIn(int exprieIn) {
        this.exprieIn = exprieIn;
    }
}
