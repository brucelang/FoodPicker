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

    private SQLHelper sql;
    private final String TAG = "RANKFRAGMENT";
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
                Log.d(TAG, "Long Clicked: " + clickedFood.toString());

                DeleteFood(clickedFood);
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

    private void DeleteFood(final Food clickedFood) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle("Delete entry");
        alert.setMessage("Are you sure you want to delete " + clickedFood.getName() + "?");
        alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                if (sql.deleteFood(clickedFood)) {
                    //Refresh
                    foods = sql.getAllFoods(" ORDER BY " + Constants.COLUMN_FOODS_RANK + " DESC");
                    adapter.clear();
                    adapter.addAll(foods);
                    adapter.notifyDataSetChanged();
                    listView.setAdapter(adapter);
                    Toast.makeText(getContext(), clickedFood.getName() + " successfully deleted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Couldn't delete " + clickedFood.getName(), Toast.LENGTH_SHORT).show();
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
                    Log.i("onQueryTextChange", newText);
                    adapter.getFilter().filter(newText.toLowerCase());
                    adapter.notifyDataSetChanged();
                    return true;
                }
                @Override
                public boolean onQueryTextSubmit(String query) {
                    Log.i("onQueryTextSubmit", query);

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
        searchView.setOnQueryTextListener(queryTextListener);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        sql.close();
    }
}
