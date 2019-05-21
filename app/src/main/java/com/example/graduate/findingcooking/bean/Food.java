package com.example.graduate.findingcooking.bean;


import java.io.Serializable;

public class Food implements Serializable {
    private Long id;
    private String foodName;
    private String foodURL;
    private String foodType;
    private String date;
    private String active;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodURL() {
        return foodURL;
    }

    public void setFoodURL(String foodURL) {
        this.foodURL = foodURL;
    }


    public String getFoodType() {
        return foodType;
    }

    public void setFoodType(String foodType) {
        this.foodType = foodType;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }
}
