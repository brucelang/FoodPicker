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

    FoodAdapter(Context context, ArrayList<Food> food) {
        super(context, 0, food);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        Food food = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.food_item, parent, false);
        }

        TextView txtName = convertView.findViewById(R.id.textViewName);
        TextView txtType =  convertView.findViewById(R.id.textViewType);
        TextView txtRank =  convertView.findViewById(R.id.textViewRank);
        ImageView image =  convertView.findViewById(R.id.imageViewVeggie);

        assert food != null;
        txtName.setText(food.getName());
        txtType.setText(food.getType());
        txtRank.setText(food.getRank() +"x");

        if(food.getRank() > 100)
        {
            txtRank.setWidth(70);
        }
        if(food.getRank() > 1000)
        {
            txtRank.setWidth(90);
        }
        if(food.getRank() > 10000)
        {
            txtRank.setWidth(100);
        }

        if(food.getVegetarian() == 0)
        {
            image.setVisibility(View.GONE);
        }

        return convertView;
    }
}