package com.lang.bruce.foodpicker;

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

}
