package com.example.lenovo.myreminder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.lenovo.myreminder.adapters.PendingAdapter;
import com.example.lenovo.myreminder.dbHelper.DbHelper;
import com.example.lenovo.myreminder.model.RecordModel;
import com.example.lenovo.myreminder.model.UserModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class DailyActivity extends AppCompatActivity {

    ArrayList<RecordModel> arrayList= new ArrayList<>();
    RecyclerView rView;
    PendingAdapter adapter;
    DbHelper db;
    TextView total,summary;
    Toolbar toolbar;

     static String userNum;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-M-dd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending);

        db = new DbHelper(this);
       String date = simpleDateFormat.format(Calendar.getInstance().getTime());

        userNum =getSharedPreferences(Common.sharedPreferences, MODE_PRIVATE).getString("Number", "0");

        arrayList.addAll(db.getdailyrecords(date));
        UserModel user= db.getUserWallet(userNum);
        String userLimit= user.getLimit();



        total = findViewById(R.id.totalsum);
        summary= findViewById(R.id.summary);
        rView = findViewById(R.id.rview2);
        rView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PendingAdapter(arrayList, this);
        rView.setAdapter(adapter);


        total.setText("Total : " + String.valueOf(db.dailysum(date))+" rps");

        double sum=db.dailysum(date);
        double d= Double.parseDouble(userLimit);

        if (sum> d){
            summary.setText(" WARNING : Today you spent more then your prescribe limit, " +
                    "\n try to save for few days.");
        }
   if (sum< d){
            summary.setText(" GREAT JOB : Today you spent less then your prescribe limit," +
                    " \n keep saving.");
        }
 if (sum== d){
            summary.setText(" KEEP IT UP : You are going acording to your budget,");
        }



        toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DailyActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });



    }
}
