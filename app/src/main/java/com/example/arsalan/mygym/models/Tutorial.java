package com.example.arsalan.mygym.models;

import com.google.gson.annotations.SerializedName;

public class Tutorial {

    @SerializedName("TutorialSubCategoryId")
    private int id;

    /*"TutorialCategoryId": 2,
            "TutorialCategoryTitle": "حرکات سینه",
            "UserId": 0,
            "Name": null,
            "TutorialSubTitle": "پرس سینه هالتر",*/
    @SerializedName("TutorialCategoryId")
    private
    int catId;

    @SerializedName("TutorialCategoryTitle")
    private
    String catName;

    @SerializedName("TutorialSubTitle")
    private
    String title;

    public Tutorial() {
    }

    public int getId() {
        return id;
    }

    public int getCatId() {
        return catId;
    }

    public String getCatName() {
        return catName;
    }

    public String getTitle() {
        return title;
    }
}
