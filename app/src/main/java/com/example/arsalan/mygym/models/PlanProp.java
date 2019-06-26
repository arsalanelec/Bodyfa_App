package com.example.arsalan.mygym.models;

public class PlanProp {
    private int height;
    private int weight;
    private int bloodType;
    private int waist;

    public PlanProp() {
        height = 170;
        weight = 65;
        bloodType = 0;
        waist = 70;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getBloodType() {
        return bloodType;
    }

    public void setBloodType(int bloodType) {
        this.bloodType = bloodType;
    }

    public String getBloodTypeString() {
        switch (bloodType) {
            case 0:
                return "A+";
            case 1:
                return "A-";
            case 2:
                return "B+";
            case 3:
                return "B-";
            case 4:
                return "AB+";
            case 5:
                return "AB-";
            case 6:
                return "O+";
            case 7:
                return "O-";
            default:
                return "A+";
        }
    }

    public int getWaist() {
        return waist;
    }

    public void setWaist(int waist) {
        this.waist = waist;
    }


}
