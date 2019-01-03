package com.lang.bruce.foodpicker;

import java.io.Serializable;

class TimeStamp implements Serializable {
    private int id;
    private int foodId;
    private String date;

    TimeStamp() {
        super();
    }

    @Override
    public String toString() {
        return "TimeStamp " + id + " Food " + foodId + " at " + date;
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
