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

//        Intent intent2= new Intent(MainActivity.this,LoanService.class);
//        intent2.setAction("com.testApp.service.MY_SERVICE");
//
//        Intent intent1= new Intent(MainActivity.this,ReminderService.class);
//        intent1.setAction("com.testApp.service.MY_SERVICE");
//        startService(intent1);
//
//        startService(intent2);
//        Intent intent= new Intent(MainActivity.this,DailyAlarm.class);
//
//        PendingIntent pendingIntent=PendingIntent.getBroadcast(this,1234,intent,0);
//
//        Calendar calendar = Calendar.getInstance();
//
//        calendar.setTimeInMillis(System.currentTimeMillis());
//
//        // if it's after or equal 9 am schedule for next day
////        if (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) >= 9) {
////            Log.e("", "Alarm will schedule for next day!");
////            calendar.add(Calendar.DAY_OF_YEAR, 1); // add, not set!
////        }
////        else{
////            Log.e(TAG, "Alarm will schedule for today!");
////        }
//        calendar.set(Calendar.HOUR_OF_DAY, 12);
//        calendar.set(Calendar.MINUTE, 0);
//        calendar.set(Calendar.SECOND, 0);
//
////        AlarmManager alarmManager= (AlarmManager) getSystemService(ALARM_SERVICE);
////        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
////
//        Log.e("time",calendar.getTime().toString());
//        if (Build.VERSION.SDK_INT > 19 && Build.VERSION.SDK_INT <= 23) {
//            startAlert(calendar.getTimeInMillis(), "between");
//
//
//        } else if (Build.VERSION.SDK_INT <= 19) {
//            startAlert(calendar.getTimeInMillis(), "below");
//        } else {
//            startAlert(calendar.getTimeInMillis(), "above");
//        }
//
//
//
        
        
        loadFragment(new ExpenseFragment());
        
    }

//    @RequiresApi(api = Build.VERSION_CODES.M)
//    private void startAlert(long timeInMillis, String switcher) {
//
//        Long obj = timeInMillis / 1000L;
//        int requestCode = obj.intValue();
//
//        Intent intent = new Intent(MainActivity.this, DailyAlarm.class);
//
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(
//                this.getApplicationContext(), requestCode, intent, 0);
//
//        AlarmManager alarmManager = MyAlarm.getInstance(this);
//        if (switcher.equals("above")){
//            alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);
//        Log.e("success1","working");}
//
//        else if (switcher.equals("below")) {
//            alarmManager.set(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);
//            Log.e("success2","working");
//
//        }
//        else if (switcher.equals("between")) {
//            alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);
//            Log.e("success3", "working");
//        }
//        Toast.makeText(this, "Alarm is set", Toast.LENGTH_LONG).show();
//
//
//    }


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

