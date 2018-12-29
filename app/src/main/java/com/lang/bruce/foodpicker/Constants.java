package com.lang.bruce.foodpicker;

final class Constants {

    static final String TABLE_FOODS = "foods";
    static final String COLUMN_FOODS_ID = "id";
    static final String COLUMN_FOODS_NAME = "name";
    static final String COLUMN_FOODS_TYPE = "type";
    static final String COLUMN_FOODS_TIME = "time";
    static final String COLUMN_FOODS_KCAL = "kcal";
    static final String COLUMN_FOODS_PROTEIN = "protein";
    static final String COLUMN_FOODS_FAT = "fat";
    static final String COLUMN_FOODS_CARBS = "carbs";
    static final String COLUMN_FOODS_VEGETARIAN = "vegetarian";
    static final String COLUMN_FOODS_RANK = "rank";

    static final String TABLE_DATES = "dates";
    static final String COLUMN_DATES_ID = "id";
    static final String COLUMN_DATES_FOODS_ID = "foodid";
    static final String COLUMN_DATES_DATE = "date";

    static final String BREAKFAST = "Breakfast";
    static final String LUNCH = "Lunch";
    static final String DINNER = "Dinner";
    static final String SNACK = "Snack";

    static final String SQL_CREATE_TABLE_FOODS =
            "CREATE TABLE " + TABLE_FOODS + " (" +
                    COLUMN_FOODS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    COLUMN_FOODS_NAME + " TEXT," +
                    COLUMN_FOODS_TYPE + " TEXT," +
                    COLUMN_FOODS_TIME + " REAL," +
                    COLUMN_FOODS_KCAL + " REAL," +
                    COLUMN_FOODS_PROTEIN + " REAL," +
                    COLUMN_FOODS_FAT + " REAL," +
                    COLUMN_FOODS_CARBS + " REAL," +
                    COLUMN_FOODS_VEGETARIAN + " TEXT," +
                    COLUMN_FOODS_RANK + " INTEGER)";

    static final String SQL_CREATE_TABLE_DATES =
            "CREATE TABLE " + TABLE_DATES + " (" +
                    COLUMN_DATES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    COLUMN_DATES_FOODS_ID + " INTEGER NOT NULL," +
                    COLUMN_DATES_DATE + " TEXT," +
                    " FOREIGN KEY ("+COLUMN_DATES_FOODS_ID+") REFERENCES "+TABLE_FOODS+"("+COLUMN_FOODS_ID+"));";

    static final String SQL_DELETE_FOODS =
            "DROP TABLE IF EXISTS " + TABLE_FOODS;
    static final String SQL_DELETE_DATES =
            "DROP TABLE IF EXISTS " + TABLE_DATES;
}
