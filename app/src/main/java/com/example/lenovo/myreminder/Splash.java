package com.example.lenovo.myreminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.lenovo.myreminder.Services.LoanService;
import com.example.lenovo.myreminder.Services.ReminderService;
import com.example.lenovo.myreminder.alarm_manager.DailyAlarm;

import java.util.Calendar;

public class Splash extends AppCompatActivity {


    protected void startApp() {



    SharedPreferences preferences = getSharedPreferences("SharedPreferences", MODE_PRIVATE);

        String number = preferences.getString("Number", null);
        String password = preferences.getString("Password", null);


        if (number != null && password != null) {
            Intent intent = new Intent(Splash.this, MainActivity.class);
            startActivity(intent);
            finish();



        } else {
            Intent intent = new Intent(Splash.this, Login.class);
            startActivity(intent);
            finish();

        }




    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Thread mythread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(2000);
                    startApp();
                } catch (InterruptedException e) {

                    Log.e("Exception", e.getMessage());

                }
            }
        };
        mythread.start();


    }

}
