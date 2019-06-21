package com.example.lenovo.myreminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.example.lenovo.myreminder.Services.LoanService;
import com.example.lenovo.myreminder.Services.ReminderService;
import com.example.lenovo.myreminder.alarm_manager.DailyAlarm;
import com.example.lenovo.myreminder.dbHelper.DbHelper;
import com.example.lenovo.myreminder.model.UserModel;

import java.util.ArrayList;
import java.util.Calendar;

public class Signin extends AppCompatActivity {

    Button signin;
    EditText signname,signemail,signpassword,signnumber;
    static String email,password,name,number;
    SharedPreferences.Editor preferences;
    DbHelper openhelper;
    SQLiteDatabase db;
    ArrayList<UserModel> userModelArrayList= new ArrayList<>();

    AwesomeValidation awesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        awesomeValidation= new AwesomeValidation(ValidationStyle.BASIC);

        preferences=getSharedPreferences(Common.sharedPreferences,MODE_PRIVATE).edit();
        openhelper=new DbHelper(this);
        signname=findViewById(R.id.name);
        signemail=findViewById(R.id.myemail);
        signpassword=findViewById(R.id.mypswrd);
        signnumber= findViewById(R.id.number);
        signin=findViewById(R.id.buttonsignin);

        String regexPassword = "(?=.*[a-z])(?=.*[\\d]).{6,}";

        awesomeValidation.addValidation(Signin.this,R.id.name,"[a-zA-Z\\s]+",R.string.name);
        awesomeValidation.addValidation(Signin.this,R.id.myemail,android.util.Patterns.EMAIL_ADDRESS,R.string.email);
        awesomeValidation.addValidation(Signin.this,R.id.number,RegexTemplate.TELEPHONE,R.string.phnumber);
        awesomeValidation.addValidation(Signin.this,R.id.mypswrd,regexPassword,R.string.password);


        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email=signemail.getText().toString();
                password=signpassword.getText().toString();
                name=signname.getText().toString();
                number=signnumber.getText().toString();

                preferences.putString("Name",name);
                preferences.putString("Number",number);
                preferences.putString("Password",password);
                preferences.apply();

                if (awesomeValidation.validate()){

                long id =openhelper.insertUserData(email,name,password,number);


                    if(id!=-1){

                        Toast.makeText(Signin.this, "Inserted", Toast.LENGTH_SHORT).show();
                    }
                    Intent sign = new Intent(Signin.this,UserInfoActivity.class);
                    startActivity(sign);

//                    Intent intent1= new Intent(Signin.this,ReminderService.class);
//
//                    startService(intent1);

                    Intent intent2= new Intent(Signin.this,LoanService.class);
                    intent2.setAction("com.testApp.service.MY_SERVICE");

                    startService(intent2);

                    finish();

                }
                else {
                    Toast.makeText(Signin.this, "Error", Toast.LENGTH_SHORT).show();

                }
            }
        });
      Calendar calendar = Calendar.getInstance();

        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 19);
        calendar.set(Calendar.MINUTE, 39);
        calendar.set(Calendar.SECOND, 0);

        Intent intent= new Intent(Signin.this,DailyAlarm.class);

        PendingIntent pendingIntent=PendingIntent.getBroadcast(this,1234,intent,0);

        AlarmManager alarmManager= (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY, pendingIntent);




    }
}
