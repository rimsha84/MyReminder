package com.example.lenovo.myreminder;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.lenovo.myreminder.dbHelper.DbHelper;

public class Broadcast extends BroadcastReceiver {
    String loanid;
    DbHelper dbHelper;
    @Override
    public void onReceive(Context context, Intent intent) {

        dbHelper= new DbHelper(context);
        int noti_id = intent.getIntExtra("notification_id",0);
        if (noti_id != 0)
        {
            NotificationManager manager=(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.cancel(noti_id);

        }else
            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();

                loanid= intent.getStringExtra("LoanId");
     dbHelper.deteteLoanRcord(loanid);
        Toast.makeText(context, "successful", Toast.LENGTH_SHORT).show();
    }
}
