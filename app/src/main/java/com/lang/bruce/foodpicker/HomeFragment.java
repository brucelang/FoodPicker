package com.lang.bruce.foodpicker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

public class HomeFragment extends Fragment {

    private static final String TAG = "HOMEFRAGMENT";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        SetListener();

        //Spinner
        Spinner timeSpinner = getView().findViewById(R.id.spinnerTime);
        Integer[] items = new Integer[]{15,30,40};
        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(getContext(),R.layout.spinner_item, items);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        timeSpinner.setAdapter(adapter);
        timeSpinner.setSelection(1);
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
        //todo use rank
        Intent myIntent = new Intent(getContext(), ShowFood.class);
        myIntent.putExtra("type", value);
        myIntent.putExtra("isVeggie",  isVeggie.isChecked());
        myIntent.putExtra("time",  Double.parseDouble(time.getSelectedItem().toString()));
        startActivity(myIntent);
    }
}
