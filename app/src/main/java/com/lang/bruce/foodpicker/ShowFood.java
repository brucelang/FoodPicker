package com.lang.bruce.foodpicker;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.Arrays;

import static com.lang.bruce.foodpicker.HelperMethods.updateRank;
import static java.lang.String.format;

public class ShowFood extends AppCompatActivity {
    private static final String TAG = "SHOWFOOD";

    private static SQLHelper sql;

    //Filter
    private static String type;
    private static double time;
    private static boolean isVeggie;
    private static Food food1;
    private static Food food2;

    //View
    private Button button1;
    private Button button2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_food);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sql = SQLHelper.getInstance(this);

        button1 = findViewById(R.id.buttonFirst);
        button2 = findViewById(R.id.buttonSecond);

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

        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!button1.getText().equals(getResources().getString(R.string.no_data)))
                {
                    Log.d(TAG, food1.toString() + " choosen");
                    updateRank(sql, food1);
                }
                finish();
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!button2.getText().equals(getResources().getString(R.string.no_data)))
                {
                    Log.d(TAG, food2.toString() + " choosen");
                    updateRank(sql, food2);
                }
                finish();
            }
        });
    }

    @SuppressLint("DefaultLocale")
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

        Log.d(TAG, "Set food: Selection = " + selection + " Params = " + Arrays.toString(paramArray));

        ArrayList<Food> foods = sql.getAllFoods(selection, paramArray, "RANDOM() LIMIT 2");
        Log.d(TAG, "Set foods " + foods.toString());

        if (!foods.isEmpty()) {
            Log.d(TAG, "Food not empty");
            food1 = foods.get(0);
            button1.setText(format("1. %s (%d)", food1.getName(), food1.getRank()));
            if (food1.getVegetarian() != 1)
                findViewById(R.id.imageViewFirst).setVisibility(View.INVISIBLE);
            else findViewById(R.id.imageViewFirst).setVisibility(View.VISIBLE);

            if (foods.size() >= 2) {
                Log.d(TAG, "Second food found");
                food2 = foods.get(1);
                button2.setText(format("2. %s (%d)", food2.getName(), food2.getRank()));
                if (food2.getVegetarian() != 1)
                    findViewById(R.id.imageViewSecond).setVisibility(View.INVISIBLE);
                else findViewById(R.id.imageViewSecond).setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id==android.R.id.home) {
            Log.d(TAG, "Go Back");
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
