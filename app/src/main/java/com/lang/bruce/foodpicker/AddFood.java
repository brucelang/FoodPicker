package com.lang.bruce.foodpicker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class AddFood extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_food);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
