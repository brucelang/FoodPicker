package com.lang.bruce.foodpicker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class AddFood extends AppCompatActivity {
    private static final String TAG = "ADDFOOD";

    private static SQLHelper sql;
    private static ArrayAdapter<String> spinnerAdapter;
    //View
    private Spinner spinnerCategory;
    private CheckBox checkBoxVeggie;
    private EditText txtName;
    private EditText txtTime;
    private EditText txtKcal;
    private EditText txtProtein;
    private EditText txtFats;
    private EditText txtCarbs;

    public static double checkText(String text) {
        Log.d(TAG, "checkText " + text);
        if (text.isEmpty() || text.equals("")) {
            Log.d(TAG, "Text is empty");
            return 0;
        } else {
            return Double.parseDouble(text);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.add_food);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sql = SQLHelper.getInstance(this);

        txtName = findViewById(R.id.editTextName);
        txtTime = findViewById(R.id.editTextTime);
        txtKcal = findViewById(R.id.editTextCalories);
        txtProtein = findViewById(R.id.editTextProtein);
        txtFats = findViewById(R.id.editTextFats);
        txtCarbs = findViewById(R.id.editTextCarbs);
        checkBoxVeggie = findViewById(R.id.checkBoxVeggie);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        String[] items = new String[]{Constants.BREAKFAST, Constants.LUNCH, Constants.DINNER, Constants.SNACK};
        spinnerAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, items);
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinnerCategory.setAdapter(spinnerAdapter);
        spinnerCategory.setSelection(1);
    }

    private void saveFood() {
        Food food = new Food();
        if (txtName.getText().toString().equals("")) {
            Toast.makeText(this, "Field Name cannot be empty!", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "EditText Name is empty");
        } else {
            food.setName(txtName.getText().toString());
            food.setType(spinnerCategory.getSelectedItem().toString());
            if (checkBoxVeggie.isChecked()) food.setVegetarian(1);
            else food.setVegetarian(0);
            food.setTime(checkText(txtTime.getText().toString()));
            food.setKcal(checkText(txtKcal.getText().toString()));
            food.setProtein(checkText(txtProtein.getText().toString()));
            food.setFat(checkText(txtFats.getText().toString()));
            food.setCarbs(checkText(txtCarbs.getText().toString()));
            if (!sql.hasFood(food)) {
                sql.addFood(food);
                finish();
                Toast.makeText(this, food.getName() + " successfully added.", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Added " + food.toString());
            } else {
                Toast.makeText(this, food.getName() + " already exist. \nUpdating values...", Toast.LENGTH_SHORT).show();
            }

            Log.d(TAG, "Add Food " + food.toString());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu");
        getMenuInflater().inflate(R.menu.save_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                Log.d(TAG, "Go Back");
                finish();
                break;
            case R.id.save:
                Log.d(TAG, "Save");
                saveFood();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
