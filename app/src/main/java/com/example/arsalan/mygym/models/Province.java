package com.example.arsalan.mygym.models;


import java.util.List;

/**
 * Created by Arsalan on 03-10-2017.
 */

public class Province {

    long id;

    String name;

    List<City> cities;

    public Province() {
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<City> getCities() {
        return cities;
    }

    @Override
    public String toString() {
        return name;
    }
}
