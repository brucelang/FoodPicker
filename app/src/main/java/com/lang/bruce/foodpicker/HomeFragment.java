package com.lang.bruce.foodpicker;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
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

public class HomeFragment extends Fragment {
    private static final String TAG = "HOMEFRAGMENT";

    private Spinner spinnerTime;
    private ArrayAdapter<Integer> spinnerAdapter;
    private TextView txtKcal;
    private FloatingActionButton addButton;

    private SQLHelper sql;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        SetListener();

        sql = new SQLHelper(getContext());
        txtKcal = getView().findViewById(R.id.textViewKcal);
        addButton = getView().findViewById(R.id.addButton);
        //Spinner
        spinnerTime = getView().findViewById(R.id.spinnerTime);
        Integer[] items = new Integer[]{15,30,40};
        spinnerAdapter = new ArrayAdapter<>(getContext(), R.layout.spinner_item, items);
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinnerTime.setAdapter(spinnerAdapter);
        spinnerTime.setSelection(1);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddFood.class);
                startActivity(intent);
            }
        });
    }

    private void SetListener() {
        ImageButton breakfast = getView().findViewById(R.id.imageButtonBreakfast);
        breakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"Getting Breakfast...", Toast.LENGTH_SHORT).show();
                GetFood(Constants.BREAKFAST);
            }
        });
        ImageButton lunch = getView().findViewById(R.id.imageButtonLunch);
        lunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"Getting Lunch...", Toast.LENGTH_SHORT).show();
                GetFood(Constants.LUNCH);
            }
        });
        ImageButton dinner = getView().findViewById(R.id.imageButtonDinner);
        dinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"Getting Dinner...", Toast.LENGTH_SHORT).show();
                GetFood(Constants.DINNER);
            }
        });
        ImageButton snack = getView().findViewById(R.id.imageButtonSnack);
        snack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"Getting Snack...", Toast.LENGTH_SHORT).show();
                GetFood(Constants.SNACK);
            }
        });
    }

    private void GetFood(String value) {
        Switch isVeggie = getView().findViewById(R.id.switchVegetarian);
        Spinner time = getView().findViewById(R.id.spinnerTime);

        Intent myIntent = new Intent(getContext(), ShowFood.class);
        myIntent.putExtra("type", value);
        myIntent.putExtra("isVeggie",  isVeggie.isChecked());
        myIntent.putExtra("time",  Double.parseDouble(time.getSelectedItem().toString()));
        startActivity(myIntent);
    }

    @Override
    public void onResume() {
        MainActivity.CountKcal(sql);
        UpdateKcalTextView();
        super.onResume();
    }

    private void UpdateKcalTextView() {
        int kcal = (int) Math.round(MainActivity.todaysKcal);
        txtKcal.setText(kcal + " kcal");
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        if (hour <= 12 && kcal < 300
                || hour > 12 && hour <= 18 && kcal < 1500
                || hour > 18 && hour <= 24 && kcal < 2300) {
            txtKcal.setTextColor(Color.RED);
        }

        if (hour <= 12 && kcal < 500 && kcal >= 300
                || hour > 12 && hour <= 18 && kcal >= 1500 && kcal < 1800
                || hour > 18 && hour <= 24 && kcal >= 2300 && kcal < 2500) {
            txtKcal.setTextColor(Color.rgb(244, 182, 66));
        }
        if (hour <= 12 && kcal >= 500
                || hour > 12 && hour <= 18 && kcal >= 1800
                || hour > 18 && hour <= 24 && kcal >= 2500) {
            txtKcal.setTextColor(Color.parseColor("#33b247"));
        }
    }
}
