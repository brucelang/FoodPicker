package com.lang.bruce.foodpicker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

public class OverviewFragment extends Fragment {
    private static final String TAG = "OVERVIEWFRAGMENT";

    private static SQLHelper sql;
    private static FoodAdapter adapter;
    private static ArrayList<Food> foods;

    //View
    private static TextView txtOverview;
    private static int currentDay;
    private static int currentMonth;
    private static int currentYear;
    private final DialogFragment datePickerFragment = new DatePickerFragment();
    private ListView listView;

    private static void setData() {
        Log.d(TAG, "SetData");
        foods.clear();
        for (int i = 0; i < HelperMethods.dates.size(); i++) {
            foods.add(sql.getFood((HelperMethods.dates.get(i)).getFoodId()));
        }
        adapter.notifyDataSetChanged();
    }

    private static void updateViews() {
        Log.d(TAG, "Update Views " + currentDay + "." + currentMonth + "." + currentYear);
        HelperMethods.countKcal(sql, currentDay, currentMonth, currentYear);
        HelperMethods.dates = sql.getAllTimeStamps(currentDay, currentMonth, currentYear);
        setData();
        txtOverview.setText(String.format("%d kcal on %d.%d.%d", Math.round(HelperMethods.todaysKcal), currentDay, currentMonth, currentYear));
    }

    public static void updateViews(int day, int month, int year) {
        Log.d(TAG, "Update Views " + day + "." + month + "." + year);
        currentDay = day;
        currentMonth = month;
        currentYear = year;
        updateViews();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        return inflater.inflate(R.layout.overview_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated");
        txtOverview = getView().findViewById(R.id.textViewOverview);
        listView = getView().findViewById(R.id.listViewFoodsDate);

        sql = SQLHelper.getInstance(getContext());
        foods = new ArrayList();
        adapter = new FoodAdapter(getContext(), foods);
        adapter.rank = false;

        currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        currentMonth = (Calendar.getInstance().get(Calendar.MONTH) + 1);
        currentYear = Calendar.getInstance().get(Calendar.YEAR);

        txtOverview.setText(String.format("%d kcal today", Math.round(HelperMethods.todaysKcal)));
        HelperMethods.dates = sql.getAllTimeStamps(currentDay, currentMonth, currentYear);
        setData();

        txtOverview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerFragment.show(getFragmentManager(), "Date Picker");
                updateViews();
            }
        });

        listView.setAdapter(adapter);
        listView.setLongClickable(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Food clickedFood = (Food) listView.getItemAtPosition(i);
                Log.d(TAG, "Clicked: " + clickedFood.toString());
                Intent intent = new Intent(getContext(), FoodOverview.class);
                intent.putExtra("Food", clickedFood);
                startActivity(intent);
            }
        });

        getView().findViewById(R.id.imageButtonArrowLeft).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCurrentDate(-1);
            }
        });

        getView().findViewById(R.id.imageButtonArrowRight).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCurrentDate(1);
            }
        });
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume");
        updateViews();
        super.onResume();
    }

    private void setCurrentDate(int cnt) {
        switch (currentMonth) {
            case 1:
                setDay(31, cnt, 1);
                break;
            case 2:
                setDay(28, cnt, 2);
                break;
            case 3:
                setDay(31, cnt, 3);
                break;
            case 4:
                setDay(30, cnt, 4);
                break;
            case 5:
                setDay(31, cnt, 5);
                break;
            case 6:
                setDay(30, cnt, 6);
                break;
            case 7:
                setDay(31, cnt, 7);
                break;
            case 8:
                setDay(31, cnt, 8);
                break;
            case 9:
                setDay(30, cnt, 9);
                break;
            case 10:
                setDay(31, cnt, 10);
                break;
            case 11:
                setDay(30, cnt, 11);
                break;
            case 12:
                setDay(31, cnt, 12);
                break;
        }
        updateViews();
    }

    private void setDay(int max, int cnt, int month) {
        Log.d(TAG, "SetDay " + currentDay + "." + currentMonth + "." + currentYear);
        if (currentDay <= 1 && cnt < 0) {
            if (max == 31) {
                currentDay = 30;
            }
            if (max == 30) {
                currentDay = 31;
            }
            if (month == 3) {
                currentDay = 28; //February
            }

            if (currentMonth <= 1 && cnt < 0) {
                currentMonth = 12;
                currentYear += cnt;
            } else if (currentMonth >= 12 && cnt > 0) {
                currentMonth = 1;
                currentYear += cnt;
            } else {
                currentMonth += cnt;
            }

        } else if (currentDay >= max && cnt > 0) {
            currentDay = 1;
            if (currentMonth <= 1 && cnt < 0) {
                currentMonth = 12;
                currentYear += cnt;
            } else if (currentMonth >= 12 && cnt > 0) {
                currentMonth = 1;
                currentYear += cnt;
            } else {
                currentMonth += cnt;
            }
        } else {
            currentDay += cnt;
        }
    }

}
