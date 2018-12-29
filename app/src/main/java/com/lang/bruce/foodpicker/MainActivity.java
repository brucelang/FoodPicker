package com.lang.bruce.foodpicker;

import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.Fragment;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
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

    public SQLHelper sql = new SQLHelper(this);

    //Fragments
    final Fragment homeFragment = new HomeFragment();
    final Fragment rankFragment = new RankFragment();
    final Fragment overviewFragment = new OverviewFragment();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = homeFragment;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_ranking:
                    fm.beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                            .replace(R.id.main_container, rankFragment).commit();
                    active = rankFragment;
                    return true;
                case R.id.navigation_home:
                    fm.beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                            .replace(R.id.main_container, homeFragment).commit();
                    active = homeFragment;
                    return true;
                case R.id.navigation_dummy:
                    fm.beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                            .replace(R.id.main_container, overviewFragment).commit();
                    active = overviewFragment;
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_home);
        MakeIconsBigger(navigation);

        GetExcelData();
    }

    private void MakeIconsBigger(BottomNavigationView navigation) {
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

    private void GetExcelData() {
        InputStream myInput;
        AssetManager assetManager = getAssets();

        SQLiteDatabase db = sql.getWritableDatabase();

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

                        switch (colno)
                        {
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
                                food.setKcal(Double.parseDouble(myCell.toString()));
                                break;
                            case 4:
                                food.setProtein(Double.parseDouble(myCell.toString()));
                            case 5:
                                food.setFat(Double.parseDouble(myCell.toString()));
                            case 6:
                                food.setCarbs(Double.parseDouble(myCell.toString()));
                            case 7:
                                food.setVegetarian(Double.parseDouble(myCell.toString()));
                                break;
                        }

                        colno++;
                        Log.e(TAG, " Index :" + myCell.getColumnIndex() + " -- " + myCell.toString());
                    }
                    if(!sql.hasFood(food)) {
                        sql.addFood(food);
                    }
                }
                rowno++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        db.close();
    }

    @Override
    protected void onDestroy() {
        sql.close();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
