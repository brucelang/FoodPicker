package com.lang.bruce.foodpicker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class AddFood extends AppCompatActivity {
    private static final String TAG = "ADDFOOD";

    private static SQLHelper sql;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.add_food);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sql = SQLHelper.getInstance(this);
        //todo
    }
}
