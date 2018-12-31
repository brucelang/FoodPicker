package com.lang.bruce.foodpicker;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class RankFragment extends Fragment {
    private final String TAG = "RANKFRAGMENT";

    private SQLHelper sql;
    private ListView listView;
    private FoodAdapter adapter;
    private ArrayList foods;
    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.rank_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        listView = getView().findViewById(R.id.listViewFood);
        sql = new SQLHelper(getContext());
        foods = sql.getAllFoods(" ORDER BY " + Constants.COLUMN_FOODS_RANK + " DESC");
        adapter = new FoodAdapter(getContext(), foods);
        listView.setAdapter(adapter);
        listView.setLongClickable(true);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                Food clickedFood = (Food) listView.getItemAtPosition(pos);
                Log.d(TAG, "Clicked: " + clickedFood.toString());
                EatFood(clickedFood);
                return true;
            }
        });
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

    private void EatFood(final Food clickedFood) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle("Eat food");
        alert.setMessage("Have you eaten " + clickedFood.getName() + " today?");
        alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                UpdateRank(clickedFood.getId());

                Toast.makeText(getContext(), clickedFood.getName() + " successfully eaten", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Eaten " + clickedFood.toString());
            }
        });
        alert.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alert.show();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.removeItem(R.id.action_info);
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

            queryTextListener = new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextChange(String newText) {
                    Log.i(TAG, "Search for " + newText);
                    adapter.getFilter().filter(newText.toLowerCase());
                    adapter.notifyDataSetChanged();
                    return true;
                }
                @Override
                public boolean onQueryTextSubmit(String query) {
                    Log.i(TAG, "Search submit " + query);
                    return true;
                }
            };
            searchView.setOnQueryTextListener(queryTextListener);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        sql.close();
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        RefreshListView();
        super.onResume();
    }

    private void UpdateRank(int id) {
        Food food = sql.getFood(id);
        food.rank++;
        sql.updateFood(food);
        sql.addTimeStamp(food);
        MainActivity.CountKcal(sql);
        Log.d(TAG, "Updated: " + food.toString());

        RefreshListView();
    }

    private void RefreshListView() {
        foods = sql.getAllFoods(" ORDER BY " + Constants.COLUMN_FOODS_RANK + " DESC");
        adapter.clear();
        adapter.addAll(foods);
        adapter.notifyDataSetChanged();
    }
}
