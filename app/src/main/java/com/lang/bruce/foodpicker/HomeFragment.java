package com.lang.bruce.foodpicker;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import static java.lang.String.format;

public class HomeFragment extends Fragment {
    private static final String TAG = "HOMEFRAGMENT";

    private static SQLHelper sql;
    private static ArrayAdapter<Integer> spinnerAdapter;
    //View
    private Spinner spinnerDuration;
    private Switch switchVeggie;
    private TextView txtKcal;
    private FloatingActionButton addButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        return inflater.inflate(R.layout.home_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated");
        sql = SQLHelper.getInstance(getContext());

        setListener();
        txtKcal = getView().findViewById(R.id.textViewKcal);
        addButton = getView().findViewById(R.id.addButton);
        switchVeggie = getView().findViewById(R.id.switchVegetarian);
        //Spinner
        spinnerDuration = getView().findViewById(R.id.spinnerTime);
        Integer[] items = new Integer[]{15,30,40};
        spinnerAdapter = new ArrayAdapter<>(getContext(), R.layout.spinner_item, items);
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinnerDuration.setAdapter(spinnerAdapter);
        spinnerDuration.setSelection(1);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Clicked AddButton");
                Intent intent = new Intent(getContext(), AddFood.class);
                startActivity(intent);
            }
        });
    }

    private void setListener() {
        //todo maybe remove toast?
        Log.d(TAG, "Set Listener");
        ImageButton breakfast = getView().findViewById(R.id.imageButtonBreakfast);
        breakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"Getting Breakfast...", Toast.LENGTH_SHORT).show();
                getFood(Constants.BREAKFAST);
            }
        });
        ImageButton lunch = getView().findViewById(R.id.imageButtonLunch);
        lunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"Getting Lunch...", Toast.LENGTH_SHORT).show();
                getFood(Constants.LUNCH);
            }
        });
        ImageButton dinner = getView().findViewById(R.id.imageButtonDinner);
        dinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"Getting Dinner...", Toast.LENGTH_SHORT).show();
                getFood(Constants.DINNER);
            }
        });
        ImageButton snack = getView().findViewById(R.id.imageButtonSnack);
        snack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"Getting Snack...", Toast.LENGTH_SHORT).show();
                getFood(Constants.SNACK);
            }
        });
    }

    private void getFood(String value) {
        boolean isVeggie = switchVeggie.isChecked();
        double time = Double.parseDouble(spinnerDuration.getSelectedItem().toString());
        Log.d(TAG, "getFood " + value + " Veggie = " + isVeggie + " time = " + time);

        Intent myIntent = new Intent(getContext(), ShowFood.class);
        myIntent.putExtra("type", value);
        myIntent.putExtra("isVeggie", isVeggie);
        myIntent.putExtra("time", time);
        startActivity(myIntent);
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume");
        HelperMethods.countKcal(sql);
        updateKcalTextView();
        super.onResume();
    }

    private void updateKcalTextView() {
        int kcal = (int) Math.round(HelperMethods.todaysKcal);
        txtKcal.setText(format("%d kcal", kcal));
        Log.d(TAG, "updateKcalTextView " + kcal + " kcal");

        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        if (hour <= 12 && kcal < 300
                || hour > 12 && hour <= 18 && kcal < 1500
                || hour > 18 && hour <= 24 && kcal < 2300) {
            txtKcal.setTextColor(Color.RED);
            Log.d(TAG, "Red  " + kcal + " kcal at hour " + hour);
        }

        if (hour <= 12 && kcal < 500 && kcal >= 300
                || hour > 12 && hour <= 18 && kcal >= 1500 && kcal < 1800
                || hour > 18 && hour <= 24 && kcal >= 2300 && kcal < 2500) {
            txtKcal.setTextColor(Color.rgb(244, 182, 66));
            Log.d(TAG, "Yellow  " + kcal + " kcal at hour " + hour);
        }
        if (hour <= 12 && kcal >= 500
                || hour > 12 && hour <= 18 && kcal >= 1800
                || hour > 18 && hour <= 24 && kcal >= 2500) {
            txtKcal.setTextColor(Color.parseColor("#33b247"));
            Log.d(TAG, "Green  " + kcal + " kcal at hour " + hour);
        }
    }
}
