package com.example.lenovo.myreminder.Fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.example.lenovo.myreminder.IncomeRecord;
import com.example.lenovo.myreminder.Login;
import com.example.lenovo.myreminder.R;
import com.example.lenovo.myreminder.Records;
import com.example.lenovo.myreminder.Services.LoanService;
import com.example.lenovo.myreminder.Services.ReminderService;
import com.example.lenovo.myreminder.dbHelper.DbHelper;
import com.example.lenovo.myreminder.model.IncomeModel;
import com.example.lenovo.myreminder.model.RecordModel;
import com.example.lenovo.myreminder.model.UserModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class IncomeFragment extends Fragment {
    EditText source, amount;
    Button insert, show,daily;
    TextView textView;
    DbHelper dbHelper;
    ArrayList<IncomeModel> arrayList = new ArrayList<>();
    Toolbar toolbar;
    ImageView logout;
    public static String userNumber = "";
    public static String number,date ;

    SharedPreferences.Editor preferences;

    Integer walletAmount,incomeAmount;

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-M-dd");
    SimpleDateFormat monthFormat = new SimpleDateFormat("yyyy-M");
    SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
    SimpleDateFormat simpleDate = new SimpleDateFormat("dd");
    SimpleDateFormat simplemonth = new SimpleDateFormat("MMM");


    public IncomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view= inflater.inflate(R.layout.fragment_income, null);

        dbHelper = new DbHelper(getActivity());
        preferences = getActivity().getSharedPreferences(Common.sharedPreferences, MODE_PRIVATE).edit();

        userNumber = getActivity().getSharedPreferences(Common.sharedPreferences, MODE_PRIVATE).getString("Number", "0");

        UserModel helperUserWallet= dbHelper.getUserWallet(userNumber);
        final Integer userWallet =Integer.valueOf(helperUserWallet.getWallet());

        Log.e("UserNumber", userNumber);
        source = view.findViewById(R.id.income_source);
        amount =view.findViewById(R.id.income_amount);
        insert = view.findViewById(R.id.insertincome);
        show = view.findViewById(R.id.income_records);
        daily= view.findViewById(R.id.daily);
        daily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),DailyActivity.class);
                startActivity(intent);
            }
        });

        toolbar= view.findViewById(R.id.toolbar3);

        logout= view.findViewById(R.id.logoutincome);
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
                                Intent intent1= new Intent(getActivity(),ReminderService.class);
                                getActivity().stopService(intent1);

                                Intent intent2= new Intent(getActivity(),LoanService.class);
                                getActivity().stopService(intent2);
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

        textView=view.findViewById(R.id.toolbar_title);
        Typeface typeface= Typeface.createFromAsset(getActivity().getAssets(),"fonts/PatchworkStitchlingsColor.ttf");
        textView.setTypeface(typeface);

        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String incomesource = source.getText().toString();
                String incomeamount = amount.getText().toString();

                incomeAmount=Integer.valueOf(incomeamount);
                walletAmount= userWallet+incomeAmount;

                if (!TextUtils.isEmpty(incomesource) && !TextUtils.isEmpty(incomeamount)) {

                    date = simpleDateFormat.format(Calendar.getInstance().getTime());

                    Log.e("dates", date);

                    long id=0;

                           id = dbHelper.insertIncomeData(incomesource, incomeamount,
                            simpleDateFormat.format(Calendar.getInstance().getTime()),
                            monthFormat.format(Calendar.getInstance().getTime()),
                            yearFormat.format(Calendar.getInstance().getTime()),
                            userNumber,
                            simpleDate.format(Calendar.getInstance().getTime()),
                            simplemonth.format(Calendar.getInstance().getTime())
                           );

//                    IncomeModel model = dbHelper.getallincomrec(id);
//                    arrayList.add(model);

                    dbHelper.updateWallet(walletAmount.toString());
                    source.setText("");
                    amount.setText("");

                    }
                else {
                    Toast.makeText(getActivity(), "something missing", Toast.LENGTH_SHORT).show();
                }
            }

        });

        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), IncomeRecord.class);
                intent.putExtra("current date",date);
                startActivity(intent);

                }
        });



        return view;
    }

}
