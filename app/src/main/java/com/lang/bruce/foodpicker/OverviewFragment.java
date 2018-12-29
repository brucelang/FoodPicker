package com.lang.bruce.foodpicker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class OverviewFragment extends Fragment {

    private static final String TAG = "OVERVIEWFRAGMENT";
    private ListView listView;
    private CalendarView calendarView;
    private SQLHelper sql;
    private FoodAdapter adapter;
    private ArrayList foods;
    private ArrayList dates;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.overview_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        calendarView = getView().findViewById(R.id.calendarView);
        listView = getView().findViewById(R.id.listViewFoodsDate);
        sql = new SQLHelper(getContext());
        foods = new ArrayList();
        adapter = new FoodAdapter(getContext(), foods);

        calendarView.setDate(new Date().getTime());
        dates = sql.getAllDates(Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "" +
                (Calendar.getInstance().get(Calendar.MONTH) + 1) + "" +
                Calendar.getInstance().get(Calendar.YEAR));
        setData();

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                Log.d(TAG, dayOfMonth + " " + month + " " + year);
                dates = sql.getAllDates(dayOfMonth + "" + (month + 1) + "" + year);
                setData();
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
    }

    private void setData() {
        foods.clear();

        for (int i = 0; i < dates.size(); i++) {
            foods.add(sql.getFood(((TimeStamp) dates.get(i)).getFoodId()));

        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        calendarView.setDate(new Date().getTime());
        super.onResume();
    }
}
