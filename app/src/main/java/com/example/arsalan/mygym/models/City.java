package com.example.arsalan.mygym.models;


/**
 * Created by Arsalan on 03-10-2017.
 */

public class City {

    private int id;

    private String name;

    private String provinceId;

    public City() {
    }

    @Override
    public String toString() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getProvinceId() {
        return provinceId;
    }

}
