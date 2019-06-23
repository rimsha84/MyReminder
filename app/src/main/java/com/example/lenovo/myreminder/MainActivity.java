package com.example.lenovo.myreminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.lenovo.myreminder.Fragments.ExpenseFragment;
import com.example.lenovo.myreminder.Fragments.IncomeFragment;
import com.example.lenovo.myreminder.Fragments.LoanFragment;
import com.example.lenovo.myreminder.Services.LoanService;
import com.example.lenovo.myreminder.Services.ReminderService;
import com.example.lenovo.myreminder.alarm_manager.DailyAlarm;
import com.example.lenovo.myreminder.alarm_manager.MyAlarm;

import java.util.Calendar;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    public static String userNum = "";

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        BottomNavigationView navigationView = findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(this);

        userNum =getSharedPreferences(Common.sharedPreferences, MODE_PRIVATE).getString("Number", "0");
        Log.e(MainActivity.class.getSimpleName(),userNum );

        
        
        loadFragment(new ExpenseFragment());
        
    }




    private boolean loadFragment(Fragment fragment){

        if(fragment != null){

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.framelayout, fragment)
                    .commit();


            return true;
        }
        return false;
    }
@Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
    Fragment fragment = null;

    switch (item.getItemId()){

        case R.id.navigation_record:
            fragment = new ExpenseFragment();
            break;

        case R.id.navigation_loan:
            fragment = new LoanFragment();
            break;

        case R.id.navigation_income:
            fragment = new IncomeFragment();
            break;
    }

    return loadFragment(fragment);


    }
}

