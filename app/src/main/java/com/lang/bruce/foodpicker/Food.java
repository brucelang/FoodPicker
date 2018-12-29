package com.lang.bruce.foodpicker;

import java.io.Serializable;

public class Food implements Serializable {

    public int id;
    private String name;
    private String type;
    private double time;
    private double kcal;
    private double protein;
    private double fat;
    private double carbs;
    private double vegetarian = 0;
    int rank;

    Food() {
        super();
    }

    @Override
    public String toString() {//todo
        return id + " " + name + " " + type + " " + time + " " + kcal + " " + protein + " " + fat + " " + carbs + " " + vegetarian + " " + rank;
    }

    boolean equals(Food obj) {
        return this.name.equals(obj.getName()) && this.type.equals(obj.getType())
                && this.kcal == obj.getKcal() && this.time == obj.getTime()
                && this.protein == obj.getProtein() && this.fat == obj.getFat()
                && this.carbs == obj.getCarbs() && this.vegetarian == obj.getVegetarian();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String getType() {
        return type;
    }

    void setType(String type) {
        this.type = type;
    }

    double getKcal() {
        return kcal;
    }

    void setKcal(double kcal) {
        this.kcal = kcal;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    double getVegetarian() {
        return vegetarian;
    }

    void setVegetarian(double vegetarian) {
        this.vegetarian = vegetarian;
    }

    int getRank() {
        return rank;
    }

    void setRank(int rank) {
        this.rank = rank;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getProtein() {
        return protein;
    }

    public void setProtein(double protein) {
        this.protein = protein;
    }

    public double getFat() {
        return fat;
    }

    public void setFat(double fat) {
        this.fat = fat;
    }

    public double getCarbs() {
        return carbs;
    }

    public void setCarbs(double carbs) {
        this.carbs = carbs;
    }
}
