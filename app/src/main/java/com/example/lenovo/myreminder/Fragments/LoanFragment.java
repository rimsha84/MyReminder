package com.example.lenovo.myreminder.Fragments;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.myreminder.Common;
import com.example.lenovo.myreminder.IncomeRecord;
import com.example.lenovo.myreminder.LoanRecords;
import com.example.lenovo.myreminder.Login;
import com.example.lenovo.myreminder.R;
import com.example.lenovo.myreminder.Services.LoanService;
import com.example.lenovo.myreminder.Services.ReminderService;
import com.example.lenovo.myreminder.dbHelper.DbHelper;
import com.example.lenovo.myreminder.model.LoanModel;
import com.example.lenovo.myreminder.model.RecordModel;
import com.example.lenovo.myreminder.model.UserModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoanFragment extends Fragment {

    EditText person, amount,returndates,type;
    Button add, show;
    DbHelper dbHelper;
    ArrayList<LoanModel> arrayList = new ArrayList<>();
    Toolbar toolbar;
    ImageView logout;
    Integer walletAmount,incomeAmount,money;


    public static String rdate;
    String inputtype;
    SharedPreferences.Editor preferences;
    public static String userNumber="";
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-M-dd");

    SimpleDateFormat DateFormat = new SimpleDateFormat("dd");
    SimpleDateFormat monthFormat = new SimpleDateFormat("MMM");
    SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
    final Calendar myCalendar = Calendar.getInstance();
    String returndate1;

TextView textView;

    public LoanFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_loan, null);
        preferences = getActivity().getSharedPreferences(Common.sharedPreferences, MODE_PRIVATE).edit();
        dbHelper = new DbHelper(getActivity());
        userNumber = getActivity().getSharedPreferences(Common.sharedPreferences, MODE_PRIVATE).getString("Number", "0");

        final String num=getActivity().getSharedPreferences(Common.sharedPreferences, MODE_PRIVATE).getString("Number", "0");
        UserModel helperUserWallet= dbHelper.getUserWallet(userNumber);
        final Integer userWallet =Integer.valueOf(helperUserWallet.getWallet());

        Log.e("UserNumber", userNumber);
        returndates = v.findViewById(R.id.inputdate);
        person = v.findViewById(R.id.inputnameperson);
        amount = v.findViewById(R.id.inputamount);
        type = v.findViewById(R.id.inputtype);
        add = v.findViewById(R.id.insert2);
        show = v.findViewById(R.id.button3);

        textView=v.findViewById(R.id.toolbar_title3);
        Typeface typeface= Typeface.createFromAsset(getActivity().getAssets(),"fonts/PatchworkStitchlingsColor.ttf");
        textView.setTypeface(typeface);

        toolbar= v.findViewById(R.id.toolbar3);


        logout= v.findViewById(R.id.logoutloan);
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



        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(),LoanRecords.class);
                startActivity(intent);
            }
        });



        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                String monthVar = monthOfYear<10 ? "0" + (monthOfYear+1): (monthOfYear+1)+"";
                String datevar = dayOfMonth<10 ? "0"+ dayOfMonth: dayOfMonth+"";
                returndate1 = year +"-"+monthVar+"-"+datevar;
                returndates.setText(returndate1);

            }

        };

        returndates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();





            }
        });

        type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String[] choose = {"Borrow", "Lend"};

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Pick one");
                builder.setItems(choose, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // the user clicked on colors[which]
                        inputtype = choose[which];
                        type.setText(inputtype);

                    }
                });
                builder.show();


            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String personname = person.getText().toString();
                String priceamount = amount.getText().toString();

                rdate= returndate1;

                money=Integer.valueOf(priceamount);
                if (inputtype.equals("Borrow")){
                    walletAmount=userWallet+money;
                }
                 if (inputtype.equals("Lend")){
                    walletAmount=userWallet-money;
                }
                dbHelper.updateWallet(walletAmount.toString());


                Log.e("ReturnDate", "rdate"+rdate);

              //  String inputtype= type.getText().toString();

                if (!TextUtils.isEmpty(personname) && !TextUtils.isEmpty(priceamount) && !TextUtils.isEmpty(inputtype)) {

                    String date = simpleDateFormat.format(Calendar.getInstance().getTime());

                    Log.e("dates", date);

                    long id = dbHelper.insertLoanData(personname,priceamount, num,
                            DateFormat.format(Calendar.getInstance().getTime()),
                            monthFormat.format(Calendar.getInstance().getTime()),
                            yearFormat.format(Calendar.getInstance().getTime()),
                            returndate1,
                            inputtype,
                            simpleDateFormat.format(Calendar.getInstance().getTime()));


                    // LoanModel model = dbHelper.getalldataLoan(id);
                    //arrayList.add(model);
                    person.setText("");
                    amount.setText("");
                    type.setText("(lend / borrow)");
                    returndates.setText("Click here to select return date");
                    Toast.makeText(getActivity(), "Added", Toast.LENGTH_SHORT).show();


                }
                else {
                    Toast.makeText(getActivity(), "something missing", Toast.LENGTH_SHORT).show();
                }
            }

        });

        return v;
    }

}
