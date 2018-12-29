package com.lang.bruce.foodpicker;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class FoodOverview extends AppCompatActivity {
    private static final String TAG = "FOODOVERVIEW";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_overview);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Food food = (Food) getIntent().getSerializableExtra("Food");

        getSupportActionBar().setTitle(food.getName());
        setPieChart(food);
        setData(food);
    }

    public void setPieChart(final Food food) {
        final float sum = (float) (food.getProtein() + food.getCarbs() + food.getFat());
        final PieChart pieChart = findViewById(R.id.piechart);
        pieChart.setCenterText(food.getKcal() + " kcal");
        pieChart.setCenterTextSize(20);
        pieChart.setCenterTextTypeface(Typeface.DEFAULT_BOLD);
        //pieChart.setCenterTextColor(Color.RED); //Todo maybe if much cal red green yellow,...
        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {

            @Override
            public void onValueSelected(Entry e, Highlight h) {
                PieEntry pieE = (PieEntry) e;
                pieChart.setCenterText(pieE.getLabel() + "\n" + Math.round(pieE.getValue() / sum * 100) + "%");
            }

            @Override
            public void onNothingSelected() {
                pieChart.setCenterText(food.getKcal() + " kcal");
            }
        });

        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5, 10, 5, 10);
        pieChart.animateY(1000, Easing.EaseOutBounce);
        pieChart.setDragDecelerationFrictionCoef(1f);

        ArrayList<PieEntry> yValues = new ArrayList<>();
        yValues.add(new PieEntry((float) food.getProtein(), "Protein"));
        yValues.add(new PieEntry((float) food.getCarbs(), "Carbs"));
        yValues.add(new PieEntry((float) food.getFat(), "Fat"));

        PieDataSet dataSet = new PieDataSet(yValues, "");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(3f);
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueTypeface(Typeface.DEFAULT_BOLD);

        PieData pieData = new PieData((dataSet));
        pieData.setValueFormatter(new GrammFormatter());
        pieData.setValueTextSize(18);
        pieChart.setEntryLabelTextSize(0);
        pieData.setValueTextColor(Color.WHITE);
        pieData.setHighlightEnabled(true);

        pieChart.setData(pieData);

        Legend legend = pieChart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setTextSize(16);
        legend.setXEntrySpace(15);
    }

    private void setData(Food food) {
        if (!food.getType().contains(Constants.BREAKFAST)) {
            findViewById(R.id.imageViewBreak).setVisibility(View.GONE);
        }
        if (!food.getType().contains(Constants.LUNCH)) {
            findViewById(R.id.imageViewLunch).setVisibility(View.GONE);
        }
        if (!food.getType().contains(Constants.DINNER)) {
            findViewById(R.id.imageViewDinner).setVisibility(View.GONE);
        }
        if (!food.getType().contains(Constants.SNACK)) {
            findViewById(R.id.imageViewSnack).setVisibility(View.GONE);
        }
        if (food.getVegetarian() != 1) {
            findViewById(R.id.imageViewVeggie).setVisibility(View.GONE);
        }
        TextView txtMinutes = findViewById(R.id.textViewMinutes);
        int time = (int) Math.round(food.getTime());
        if (time != 1) {
            txtMinutes.setText(Math.round(food.getTime()) + " minutes");
        } else {
            txtMinutes.setText(Math.round(food.getTime()) + " minute");
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
