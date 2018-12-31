package com.lang.bruce.foodpicker;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment {

    private DatePickerDialog.OnDateSetListener dateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int month, int day) {
                    SQLHelper sql = new SQLHelper(getContext());
                    String filter = day + "" + (month + 1) + "" + year;

                    OverviewFragment.dates = sql.getAllDates(filter);
                    OverviewFragment.setData();

                    MainActivity.CountKcal(sql, filter);
                    OverviewFragment.txtOverview.setText(Math.round(MainActivity.todaysKcal) + " kcal on "
                            + day + "." + (month + 1) + "." + year);
                    sql.close();
                }
            };

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), dateSetListener, year, month, day);
    }
}