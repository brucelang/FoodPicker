package com.lang.bruce.foodpicker;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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

    private static Food food;
    private static SQLHelper sql;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_overview);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sql = SQLHelper.getInstance(this);
        food = (Food) getIntent().getSerializableExtra("Food");
        Log.d(TAG, "OnCreate Food = " + food.toString());

        if (food != null) {
            getSupportActionBar().setTitle(food.getName());
            setPieChart();
            setData();
        }
    }

    private void setPieChart() {
        //fixme?
        Log.d(TAG, "PieChart " + food.toString());
        final float sum = (float) (food.getProtein() + food.getCarbs() + food.getFat());
        final PieChart pieChart = findViewById(R.id.piechart);
        pieChart.setCenterText(food.getKcal() + " kcal");
        pieChart.setCenterTextSize(20);
        pieChart.setCenterTextTypeface(Typeface.DEFAULT_BOLD);
        pieChart.setCenterTextColor(setKcalColor(food.getKcal()));
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
        pieData.setValueFormatter(new GramFormatter());
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

    private int setKcalColor(double kcal) {
        if (kcal < 200) {
            return Color.RED;
        }
        if (kcal < 500 && kcal >= 200) {
            return Color.rgb(244, 182, 66);
        }
        if (kcal >= 500) {
            return Color.parseColor("#33b247");
        }
        return Color.BLACK;
    }

    private void setData() {
        Log.d(TAG, "Set data " + food.toString());
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
            txtMinutes.setText(String.format("%d minutes", Math.round(food.getTime())));
        } else {
            txtMinutes.setText(String.format("%d minute", Math.round(food.getTime())));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu");
        getMenuInflater().inflate(R.menu.menu_food_overview, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            Log.d(TAG, "Go back");
            finish();
        }
        if (id == R.id.action_delete) {
            Log.d(TAG, "Deleting food " + food.toString());
            deleteFood();
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteFood() {
        Log.d(TAG, "deleteFood " + food.toString());
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Delete entry");
        alert.setMessage("Are you sure you want to delete " + food.getName() + "?");
        alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                if (sql.deleteFood(food)) {
                    HelperMethods.countKcal(sql);
                    Toast.makeText(getApplicationContext(), food.getName() + " successfully deleted", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Deleted " + food.toString());
                    finish();
                } else {
                    Log.d(TAG, "Couldn't delete " + food.toString());
                    Toast.makeText(getApplicationContext(), "Couldn't delete " + food.getName(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        alert.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alert.show();
    }
}
