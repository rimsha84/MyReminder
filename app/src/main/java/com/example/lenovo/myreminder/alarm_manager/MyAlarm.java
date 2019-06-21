package com.example.lenovo.myreminder.alarm_manager;

import android.app.AlarmManager;
import android.content.Context;

public class MyAlarm {


    private static AlarmManager myAlarm;

    public static AlarmManager getInstance(Context context) {

        if (myAlarm == null) {
            myAlarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        }
        return myAlarm;
    }
}
