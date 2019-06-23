package com.example.lenovo.myreminder.Fragments;


import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.myreminder.Common;
import com.example.lenovo.myreminder.DailyActivity;
import com.example.lenovo.myreminder.Login;
import com.example.lenovo.myreminder.R;
import com.example.lenovo.myreminder.Records;
import com.example.lenovo.myreminder.Services.LoanService;
import com.example.lenovo.myreminder.Services.ReminderService;
import com.example.lenovo.myreminder.UserInfoActivity;
import com.example.lenovo.myreminder.dbHelper.DbHelper;
import com.example.lenovo.myreminder.model.RecordModel;
import com.example.lenovo.myreminder.model.UserModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;
import static com.example.lenovo.myreminder.MainActivity.userNum;


/**
 * A simple {@link Fragment} subclass.
 */
public class ExpenseFragment extends Fragment {

    EditText name, price,address;
    Button insert, show;
    TextView textView;
    DbHelper dbHelper;
    ArrayList<RecordModel> arrayList = new ArrayList<>();
    Toolbar toolbar;
    ImageView logout,user;
    public static String userNumber = "";
    public static String date ;

    Integer walletAmount,itemprice;
    SharedPreferences.Editor preferences;

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-M-dd");
    SimpleDateFormat monthFormat = new SimpleDateFormat("yyyy-M");
    SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
    SimpleDateFormat simpleDate = new SimpleDateFormat("dd");
    SimpleDateFormat simplemonth = new SimpleDateFormat("MMM");


    public ExpenseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_expense, null);

        dbHelper = new DbHelper(getActivity());
        preferences = getActivity().getSharedPreferences(Common.sharedPreferences, MODE_PRIVATE).edit();

        userNumber = getActivity().getSharedPreferences(Common.sharedPreferences, MODE_PRIVATE).getString("Number", "0");

        UserModel helperUserWallet= dbHelper.getUserWallet(userNumber);
        final Integer userWallet =Integer.valueOf(helperUserWallet.getWallet());
        final Integer userLimit= Integer.valueOf(helperUserWallet.getLimit());

        name = view.findViewById(R.id.inputname);
        price =view.findViewById(R.id.inputprice);
        address = view.findViewById(R.id.inputAddresss);
        insert = view.findViewById(R.id.insert);
        show = view.findViewById(R.id.button1);
        user= view.findViewById(R.id.userinfo);
        textView=view.findViewById(R.id.toolbar_title2);
        Typeface typeface= Typeface.createFromAsset(getActivity().getAssets(),"fonts/PatchworkStitchlingsColor.ttf");
        textView.setTypeface(typeface);

        toolbar= view.findViewById(R.id.toolbar2);

        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent userinfo= new Intent(getActivity(),UserInfoActivity.class);
                startActivity(userinfo);

            }
        });

        logout= view.findViewById(R.id.logoutexpnse);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Are you sure you want to logout?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent=new Intent(getActivity() ,Login.class);
                                preferences.clear();
                                startActivity(intent);
//                                Intent intent1= new Intent(getActivity(),ReminderService.class);
//                                getActivity().stopService(intent1);
//
//                                Intent intent2= new Intent(getActivity(),LoanService.class);
//                                getActivity().stopService(intent2);

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();


            }
        });

        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String namee = name.getText().toString();
                String pricee = price.getText().toString();
                String address1 = address.getText().toString();

                itemprice=Integer.valueOf(pricee);
                walletAmount= userWallet-itemprice;
                if (!TextUtils.isEmpty(namee) && !TextUtils.isEmpty(pricee)) {

                    date = simpleDateFormat.format(Calendar.getInstance().getTime());


                    Log.e("dates", date);

                    long id = dbHelper.insertRecordData(namee, pricee,
                            simpleDateFormat.format(Calendar.getInstance().getTime()),
                            monthFormat.format(Calendar.getInstance().getTime()),
                            yearFormat.format(Calendar.getInstance().getTime()),
                            simpleDate.format(Calendar.getInstance().getTime()),
                            simplemonth.format(Calendar.getInstance().getTime()),
                            address1);

                    dbHelper.updateWallet(walletAmount.toString());

                    int totalSum = (int) dbHelper.dailysum(date);

                    if (totalSum > userLimit){

                        Log.e("amount",String.valueOf(totalSum));
                        showNotification();
                    }

//                    RecordModel model = dbHelper.getalldatarec(id);
//                    arrayList.add(model);
                    name.setText("");
                    price.setText("");
                    address.setText("");
                    Toast.makeText(getActivity(), "Added", Toast.LENGTH_SHORT).show();


                }
                else {
                    Toast.makeText(getActivity(), "something missing", Toast.LENGTH_SHORT).show();
                }
            }

        });

        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Records.class);
                intent.putExtra("current date",date);
                startActivity(intent);

            }
        });




        return view;
    }

    private void showNotification() {
        Log.e("successfullu","SUccessful");

        NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(
                Context.NOTIFICATION_SERVICE);


        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        String CHANNEL_ID = "my_channel_01";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,"XYZ", importance
            );
            notificationManager.createNotificationChannel(channel);

        }


        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext())
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("My Reminder")
                .setContentText("Warning !!! You have spent more then your Daily Limit.")
                .setSound(alarmSound)
                .setAutoCancel(true)
                .setVibrate(new long[]{1000, 1000, 1000, 1000});



        notificationManager.notify(0000, builder.build());


    }

}
