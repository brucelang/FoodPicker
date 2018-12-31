package com.lang.bruce.foodpicker;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class FoodAdapter extends ArrayAdapter<Food> {

    public boolean rank = true;
    TextView txtName;
    TextView txtType;
    TextView txtRankKcal;
    ImageView image;
    Food food;

    FoodAdapter(Context context, ArrayList<Food> food) {
        super(context, 0, food);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        food = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.food_item, parent, false);
        }

        txtName = convertView.findViewById(R.id.textViewName);
        txtType = convertView.findViewById(R.id.textViewType);
        txtRankKcal = convertView.findViewById(R.id.textViewRankOrKcal);
        image = convertView.findViewById(R.id.imageViewVeggie);

        assert food != null;
        txtName.setText(food.getName());
        txtType.setText(food.getType());
        if (rank)
        {
            txtRankKcal.setText(food.getRank() + "x");
            AdjustWidth();
        } else
        {
            txtRankKcal.setVisibility(View.GONE);
            txtName.setText(food.getName() + " (" + Math.round(food.getKcal()) + " kcal)");
        }

        if(food.getVegetarian() == 0)
        {
            image.setVisibility(View.GONE);
        }

        return convertView;
    }

    private void AdjustWidth() {
        if (food.getRank() > 100) {
            txtRankKcal.setWidth(70);
        }
        if (food.getRank() > 1000) {
            txtRankKcal.setWidth(90);
        }
        if (food.getRank() > 10000) {
            txtRankKcal.setWidth(100);
        }
    }
}