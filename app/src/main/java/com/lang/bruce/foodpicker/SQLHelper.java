package com.lang.bruce.foodpicker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SQLHelper extends SQLiteOpenHelper {

    private static final String TAG = "SQLHELPER";

    private static final int DATABASE_VERSION = 17;
    private static final String DATABASE_NAME = "Food.db";
    static String[] projectionFood = {
            Constants.COLUMN_FOODS_ID,
            Constants.COLUMN_FOODS_NAME,
            Constants.COLUMN_FOODS_TYPE,
            Constants.COLUMN_FOODS_TIME,
            Constants.COLUMN_FOODS_KCAL,
            Constants.COLUMN_FOODS_PROTEIN,
            Constants.COLUMN_FOODS_FAT,
            Constants.COLUMN_FOODS_CARBS,
            Constants.COLUMN_FOODS_VEGETARIAN,
            Constants.COLUMN_FOODS_RANK
    };
    static String[] projectionDates = {
            Constants.COLUMN_DATES_ID,
            Constants.COLUMN_DATES_FOODS_ID,
            Constants.COLUMN_DATES_DATE
    };

    static String sortOrder = Constants.COLUMN_FOODS_RANK + " DESC";

    SQLHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Constants.SQL_CREATE_TABLE_FOODS);
        db.execSQL(Constants.SQL_CREATE_TABLE_DATES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(Constants.SQL_DELETE_FOODS);
        db.execSQL(Constants.SQL_DELETE_DATES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    void addFood(Food food) {
        Log.d(TAG + "Add", "AddFood: " + food.toString());

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constants.COLUMN_FOODS_NAME, food.getName());
        values.put(Constants.COLUMN_FOODS_TYPE, food.getType());
        values.put(Constants.COLUMN_FOODS_TIME, food.getTime());
        values.put(Constants.COLUMN_FOODS_KCAL, food.getKcal());
        values.put(Constants.COLUMN_FOODS_PROTEIN, food.getProtein());
        values.put(Constants.COLUMN_FOODS_FAT, food.getFat());
        values.put(Constants.COLUMN_FOODS_CARBS, food.getCarbs());
        values.put(Constants.COLUMN_FOODS_VEGETARIAN, food.getVegetarian());
        values.put(Constants.COLUMN_FOODS_RANK, 0);

        long result = db.insert(Constants.TABLE_FOODS,
                null,
                values);
        db.close();

        Log.d(TAG, "Inserted = " + result);
    }

    void updateFood(Food food) {
        Log.d(TAG, "Update = " + food.toString());

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.COLUMN_FOODS_NAME, food.getName());
        values.put(Constants.COLUMN_FOODS_TYPE, food.getType());
        values.put(Constants.COLUMN_FOODS_TIME, food.getTime());
        values.put(Constants.COLUMN_FOODS_KCAL, food.getKcal());
        values.put(Constants.COLUMN_FOODS_PROTEIN, food.getProtein());
        values.put(Constants.COLUMN_FOODS_FAT, food.getFat());
        values.put(Constants.COLUMN_FOODS_CARBS, food.getCarbs());
        values.put(Constants.COLUMN_FOODS_RANK, food.getRank());
        values.put(Constants.COLUMN_FOODS_VEGETARIAN, food.getVegetarian());

        int i = db.update(Constants.TABLE_FOODS,
                values,
                Constants.COLUMN_FOODS_ID + " = ?",
                new String[]{String.valueOf(food.getId())});

        db.close();

        Log.d(TAG, "Updated = " + i);
    }

    void addTimeStamp(Food food) {
        SimpleDateFormat s = new SimpleDateFormat("ddMMyyyyhhmmss");
        String date = s.format(new Date());
        Log.d(TAG + "Date", "AddDate: " + date);

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.COLUMN_DATES_FOODS_ID, food.getId());
        values.put(Constants.COLUMN_DATES_DATE, date);

        long result = db.insert(Constants.TABLE_DATES,
                null,
                values);
        db.close();

        Log.d(TAG, "Inserted = " + result);
    }

    Food getFood(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor =
                db.query(Constants.TABLE_FOODS,
                        projectionFood,
                        " id = ?",
                        new String[]{String.valueOf(id)},
                        null,
                        null,
                        null,
                        null);

        if (cursor != null)
            cursor.moveToFirst();

        Food food = new Food();
        food.setId(Integer.parseInt(cursor.getString(0)));
        food.setName(cursor.getString(1));
        food.setType(cursor.getString(2));
        food.setTime(Double.parseDouble(cursor.getString(3)));
        food.setKcal(Double.parseDouble(cursor.getString(4)));
        food.setProtein(Double.parseDouble(cursor.getString(5)));
        food.setFat(Double.parseDouble(cursor.getString(6)));
        food.setCarbs(Double.parseDouble(cursor.getString(7)));
        food.setVegetarian(Double.parseDouble(cursor.getString(8)));
        food.setRank(Integer.parseInt(cursor.getString(9)));

        Log.d("getFood(" + id + ")", food.toString());

        cursor.close();
        return food;
    }

    boolean hasFood(Food food) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =
                db.query(Constants.TABLE_FOODS,
                        projectionFood,
                        " name = ?",
                        new String[]{food.getName()},
                        null,
                        null,
                        null,
                        null);

        if (cursor != null && cursor.moveToFirst()) {
            Log.d(TAG, "HasFood" + food.toString());
            CheckOtherValues(food, cursor);
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }

    private void CheckOtherValues(Food food, Cursor cursor) {
        cursor.moveToFirst();

        food.id = Integer.parseInt(cursor.getString(0));

        Food dbfood = new Food();
        dbfood.setId(Integer.parseInt(cursor.getString(0)));
        dbfood.setName(cursor.getString(1));
        dbfood.setType(cursor.getString(2));
        dbfood.setTime(Double.parseDouble(cursor.getString(3)));
        dbfood.setKcal(Double.parseDouble(cursor.getString(4)));
        dbfood.setProtein(Double.parseDouble(cursor.getString(5)));
        dbfood.setFat(Double.parseDouble(cursor.getString(6)));
        dbfood.setCarbs(Double.parseDouble(cursor.getString(7)));
        dbfood.setVegetarian(Double.parseDouble(cursor.getString(8)));
        dbfood.setRank(Integer.parseInt(cursor.getString(9)));

        food.rank = dbfood.getRank();

        if (!food.equals(dbfood)) {
            updateFood(food);
        }
    }

    ArrayList<Food> getAllFoods(String ordering) {
        ArrayList<Food> foods = new ArrayList<>();
        String query = "SELECT  * FROM " + Constants.TABLE_FOODS + ordering;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        Food food;
        if (cursor.moveToFirst()) {
            do {
                food = new Food();
                food.setId(Integer.parseInt(cursor.getString(0)));
                food.setName(cursor.getString(1));
                food.setType(cursor.getString(2));
                food.setTime(Double.parseDouble(cursor.getString(3)));
                food.setKcal(Double.parseDouble(cursor.getString(4)));
                food.setProtein(Double.parseDouble(cursor.getString(5)));
                food.setFat(Double.parseDouble(cursor.getString(6)));
                food.setCarbs(Double.parseDouble(cursor.getString(7)));
                food.setVegetarian(Double.parseDouble(cursor.getString(8)));
                food.setRank(Integer.parseInt(cursor.getString(9)));
                foods.add(food);
            } while (cursor.moveToNext());
        }

        Log.d(TAG + "GetAll", "Get All Foods: " + foods.toString());
        cursor.close();
        return foods;
    }

    ArrayList<TimeStamp> getAllDates(String filter) {
        Log.d(TAG + "GetAll", "Get All TimeStamps ordering: " + filter);
        ArrayList<TimeStamp> timeStamps = new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor =
                db.query(Constants.TABLE_DATES,
                        projectionDates,
                        Constants.COLUMN_DATES_DATE + " LIKE ?",
                        new String[]{filter + "%"},
                        null,
                        null,
                        Constants.COLUMN_DATES_ID + " DESC",
                        null);

        TimeStamp timeStamp;
        if (cursor.moveToFirst()) {
            do {
                timeStamp = new TimeStamp();
                timeStamp.setId(Integer.parseInt(cursor.getString(0)));
                timeStamp.setFoodId(Integer.parseInt(cursor.getString(1)));
                timeStamp.setDate((cursor.getString(2)));
                timeStamps.add(timeStamp);
            } while (cursor.moveToNext());
        }

        Log.d(TAG + "GetAll", "Get All TimeStamps: " + timeStamps.toString());
        cursor.close();
        return timeStamps;
    }

    public boolean deleteFood(Food food) {
        SQLiteDatabase db = this.getWritableDatabase();

        int x = db.delete(Constants.TABLE_FOODS,
                Constants.COLUMN_FOODS_ID + " = ?",
                new String[]{String.valueOf(food.getId())});

        db.close();
        Log.d(TAG, "Deleted: " + food.toString());

        return x > 0;
    }
}
