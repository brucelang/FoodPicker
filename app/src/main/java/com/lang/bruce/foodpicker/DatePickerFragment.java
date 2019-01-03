package com.lang.bruce.foodpicker;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.DatePicker;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment {
    private static final String TAG = "DATEPICKERFRAGMENT";

    private final DatePickerDialog.OnDateSetListener dateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int month, int day) {
                    Log.d(TAG, "OnDataSet " + day + "." + (month + 1) + "." + year);
                    OverviewFragment.updateViews(day, month + 1, year);
                }
            };

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        Log.d(TAG, "CreateDialog " + day + "." + (month + 1) + "." + year);
        return new DatePickerDialog(getActivity(), dateSetListener, year, month, day);
    }
}