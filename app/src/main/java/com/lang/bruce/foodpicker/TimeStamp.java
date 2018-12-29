package com.lang.bruce.foodpicker;

import java.io.Serializable;

public class TimeStamp implements Serializable {
    private int id;
    private int foodId;
    private String date;

    TimeStamp() {
        super();
    }

    @Override
    public String toString() {//todo
        return id + " " + foodId + " " + date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
