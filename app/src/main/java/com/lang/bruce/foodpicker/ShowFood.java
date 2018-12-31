package com.lang.bruce.foodpicker;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.ArrayList;

public class ShowFood extends AppCompatActivity {
    private static final String TAG = "SHOWFOOD";

    private SQLHelper sql;
    private SQLiteDatabase db;

    private String type;
    private double time;
    private boolean isVeggie;

    private Button food1;
    private Button food2;

    private int id1;
    private int id2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_food);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sql = new SQLHelper(getApplicationContext());
        db = sql.getReadableDatabase();

        food1 = findViewById(R.id.buttonFirst);
        food2 = findViewById(R.id.buttonSecond);

        type = getIntent().getStringExtra("type");
        isVeggie = getIntent().getBooleanExtra("isVeggie", false);
        time = getIntent().getDoubleExtra("time", -1);

        getSupportActionBar().setTitle(type);

        setFood();

        ImageButton refresh = findViewById(R.id.imageButtonRefresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFood();
            }
        });

        food1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(!food1.getText().equals(getResources().getString(R.string.no_data)))
                {
                    UpdateRank(id1);
                }
                finish();
            }
        });

        food2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(!food2.getText().equals(getResources().getString(R.string.no_data)))
                {
                    UpdateRank(id2);
                }
                finish();
            }
        });
    }

    private void UpdateRank(int id) {
        Food food = sql.getFood(id);
        food.rank++;
        sql.updateFood(food);
        sql.addTimeStamp(food);
        MainActivity.CountKcal(sql);
        Log.d(TAG, "Updated: " + food.toString());
    }

    private void setFood() {
        String selection = Constants.COLUMN_FOODS_TYPE + " LIKE ?";
        ArrayList<String> params = new ArrayList<>();
        params.add("%" + type + "%");

        if (isVeggie) {
            selection += " AND " + Constants.COLUMN_FOODS_VEGETARIAN + " = ?";
            params.add("1.0");
        }
        if (time != -1) {
            selection += " AND " + Constants.COLUMN_FOODS_TIME + " <= ?";
            params.add(time + "");
        }

        String[] paramArray = new String[params.size()];
        paramArray = params.toArray(paramArray);

        Cursor cursor = db.query(
                Constants.TABLE_FOODS,
                SQLHelper.projectionFood,
                selection,
                paramArray,
                null,
                null,
                "RANDOM() LIMIT 2"
        );

        if (cursor.moveToFirst()) {
            id1 = (Integer.parseInt(cursor.getString(0)));
            Food food = sql.getFood(id1);
            food1.setText("1. " + food.getName() + " (" + food.getRank() + ")");
            if(food.getVegetarian() != 1) findViewById(R.id.imageViewFirst).setVisibility(View.INVISIBLE);
            else findViewById(R.id.imageViewFirst).setVisibility(View.VISIBLE);

            if (cursor.moveToNext()) {
                id2 = (Integer.parseInt(cursor.getString(0)));
                food = sql.getFood(id2);
                food2.setText("2. " + food.getName() + " (" + food.getRank() + ")");
                if(food.getVegetarian() != 1) findViewById(R.id.imageViewSecond).setVisibility(View.INVISIBLE);
                else findViewById(R.id.imageViewSecond).setVisibility(View.VISIBLE);
            }
        }

        cursor.close();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
        sql.close();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id==android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
