package com.lang.bruce.foodpicker;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

class GramFormatter implements IValueFormatter {
    private final DecimalFormat mFormat;

    public GramFormatter() {
        mFormat = new DecimalFormat("###,###,##0.0"); 
    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        return mFormat.format(value) + "g";
    }
}

