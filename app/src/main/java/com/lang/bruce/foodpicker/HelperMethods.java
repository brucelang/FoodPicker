package com.lang.bruce.foodpicker;

import android.graphics.Color;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;

class HelperMethods {
    private static final String TAG = "HELPERMETHODS";

    public static double todaysKcal = 0;
    public static ArrayList<TimeStamp> dates;

    public HelperMethods() {
    }

    public static void countKcal(SQLHelper sql) {
        countKcal(sql, Calendar.getInstance().get(Calendar.DAY_OF_MONTH),
                (Calendar.getInstance().get(Calendar.MONTH) + 1),
                Calendar.getInstance().get(Calendar.YEAR));
    }

    public static void countKcal(SQLHelper sql, int day, int month, int year) {
        Log.d(TAG, "Count Kcal on " + day + "." + month + "." + year);
        todaysKcal = 0;
        dates = sql.getAllTimeStamps(day, month, year);

        for (int i = 0; i < dates.size(); i++) {
            todaysKcal += sql.getFood((dates.get(i)).getFoodId()).getKcal();
        }
        Log.d(TAG, "Todays Kcal = " + todaysKcal);
    }

    public static void updateRank(SQLHelper sql, Food food) {
        Log.d(TAG, "Update " + food.toString());
        food.rank++;
        sql.updateFood(food);
        sql.addTimeStamp(food);
        HelperMethods.countKcal(sql);
        Log.d(TAG, "Updated " + food.toString());
    }

    public static int getKcalColor() {
        int kcal = (int) Math.round(HelperMethods.todaysKcal);
        Log.d(TAG, "getKcalColor " + kcal + " kcal");

        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        if (hour <= 12 && kcal < 300
                || hour > 12 && hour <= 18 && kcal < 1500
                || hour > 18 && hour <= 24 && kcal < 2300) {
            Log.d(TAG, "Red  " + kcal + " kcal at hour " + hour);
            return Color.RED;
        }

        if (hour <= 12 && kcal < 500 && kcal >= 300
                || hour > 12 && hour <= 18 && kcal >= 1500 && kcal < 1800
                || hour > 18 && hour <= 24 && kcal >= 2300 && kcal < 2500) {
            Log.d(TAG, "Yellow  " + kcal + " kcal at hour " + hour);
            return Color.rgb(244, 182, 66);
        }
        if (hour <= 12 && kcal >= 500
                || hour > 12 && hour <= 18 && kcal >= 1800
                || hour > 18 && hour <= 24 && kcal >= 2500) {
            Log.d(TAG, "Green  " + kcal + " kcal at hour " + hour);
            return Color.parseColor("#33b247");
        }
        return Color.BLACK;
    }
}
