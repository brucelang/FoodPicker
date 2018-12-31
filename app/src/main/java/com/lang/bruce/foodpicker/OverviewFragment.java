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
    public static TextView txtOverview;
    public static ArrayList dates;
    private static SQLHelper sql;
    private static FoodAdapter adapter;
    private static ArrayList foods;
    private ListView listView;
    private DialogFragment datePickerFragment = new DatePickerFragment();

    public static void setData() {
        foods.clear();

        for (int i = 0; i < dates.size(); i++) {
            foods.add(sql.getFood(((TimeStamp) dates.get(i)).getFoodId()));
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.overview_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        txtOverview = getView().findViewById(R.id.textViewOverview);
        listView = getView().findViewById(R.id.listViewFoodsDate);
        sql = new SQLHelper(getContext());
        foods = new ArrayList();
        adapter = new FoodAdapter(getContext(), foods);
        adapter.rank = false;

        String today = Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "" +
                (Calendar.getInstance().get(Calendar.MONTH) + 1) + "" +
                Calendar.getInstance().get(Calendar.YEAR);
        txtOverview.setText(Math.round(MainActivity.todaysKcal) + " kcal today");
        dates = sql.getAllDates(today);
        setData();

        txtOverview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerFragment.show(getFragmentManager(), "Date Picker");
            }
        });

      /*  calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                Log.d(TAG, dayOfMonth + " " + month + " " + year);
                dates = sql.getAllDates(dayOfMonth + "" + (month + 1) + "" + year);
                setData();
            }
        });*/


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

    @Override
    public void onDestroyView() {
        sql.close();
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        //  calendarView.setDate(new Date().getTime());
        super.onResume();
    }
}
