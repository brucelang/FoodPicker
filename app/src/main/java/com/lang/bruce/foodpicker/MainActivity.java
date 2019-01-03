package com.lang.bruce.foodpicker;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MAINACTIVITY";

    //Fragments
    private static final Fragment rankFragment = new RankFragment();
    private static final Fragment homeFragment = new HomeFragment();
    private static final Fragment overviewFragment = new OverviewFragment();
    private static SQLHelper sql;
    private final FragmentManager fm = getSupportFragmentManager();

    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_ranking:
                    fm.beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                            .replace(R.id.main_container, rankFragment).commit();
                    Log.d(TAG, "Open Ranking Fragment");
                    return true;
                case R.id.navigation_home:
                    fm.beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                            .replace(R.id.main_container, homeFragment).commit();
                    Log.d(TAG, "Open Home Fragment");
                    return true;
                case R.id.navigation_overview:
                    fm.beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                            .replace(R.id.main_container, overviewFragment).commit();
                    Log.d(TAG, "Open Navigation Fragment");
                    return true;
            }
            return false;
        }
    };

    private void makeIconsBigger(BottomNavigationView navigation) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) navigation.getChildAt(0);
        for (int i = 0; i < menuView.getChildCount(); i++) {
            final View iconView = menuView.getChildAt(i).findViewById(android.support.design.R.id.icon);
            final ViewGroup.LayoutParams layoutParams = iconView.getLayoutParams();
            final DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 38, displayMetrics);
            layoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 38, displayMetrics);
            iconView.setLayoutParams(layoutParams);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "Create Main");
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_home);
        makeIconsBigger(navigation);

        sql = SQLHelper.getInstance(this);
        getExcelData();
    }

    private void getExcelData() {
        //fixme ?
        Log.d(TAG, "getExcelData");
        InputStream myInput;
        AssetManager assetManager = getAssets();

        try {
            myInput = assetManager.open("Essen.xls");
            POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);
            HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);
            HSSFSheet mySheet = myWorkBook.getSheetAt(0);

            Iterator<Row> rowIter = mySheet.rowIterator();
            int rowno = 0;

            while (rowIter.hasNext()) {
                Log.e(TAG, "Row nr. "+ rowno);
                HSSFRow myRow = (HSSFRow) rowIter.next();

                if(rowno !=0) {
                    Iterator<Cell> cellIter = myRow.cellIterator();
                    int colno = 0;

                    Food food = new Food();
                    while (cellIter.hasNext()) {
                        HSSFCell myCell = (HSSFCell) cellIter.next();

                        switch (colno) {
                            case 0:
                                food.setName(myCell.toString());
                                break;
                            case 1:
                                food.setType( myCell.toString());
                                break;
                            case 2:
                                food.setTime(Double.parseDouble(myCell.toString()));
                                break;
                            case 3:
                                food.setKcal(Double.parseDouble(myCell.toString().replace(',', '.')));
                                break;
                            case 4:
                                food.setProtein(Double.parseDouble(myCell.toString().replace(',', '.')));
                            case 5:
                                food.setFat(Double.parseDouble(myCell.toString().replace(',', '.')));
                            case 6:
                                food.setCarbs(Double.parseDouble(myCell.toString().replace(',', '.')));
                            case 7:
                                food.setVegetarian(Double.parseDouble(myCell.toString()));
                                break;
                        }

                        colno++;
                        Log.e(TAG, " Index :" + myCell.getColumnIndex() + " -- " + myCell.toString());
                    }
                    if (food.getName() != null && !sql.hasFood(food)) {
                        sql.addFood(food);
                    }
                }
                rowno++;
            }

        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu");
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_info) {
            Log.d(TAG, "Menu --> Info");
            //todo labr
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
