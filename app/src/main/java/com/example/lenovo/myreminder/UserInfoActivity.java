package com.example.lenovo.myreminder;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lenovo.myreminder.dbHelper.DbHelper;
import com.example.lenovo.myreminder.model.UserModel;

public class UserInfoActivity extends AppCompatActivity {

    ImageView wallet,limit;
    String currentbalance,currentlimit;
    DbHelper db;
    TextView mywallet,expenselimit;
    Button home;
    private static String userNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        db= new DbHelper(this);
        userNum =getSharedPreferences(Common.sharedPreferences, MODE_PRIVATE).getString("Number", "0");

        UserModel user= db.getUserWallet(userNum);
        String userWallet= user.getWallet();
        String userLimit= user.getLimit();


        wallet= findViewById(R.id.user_wallet);


        limit= findViewById(R.id.user_walletlimit);
        mywallet=findViewById(R.id.mywallet);
        mywallet.setText(userWallet);
        expenselimit= findViewById(R.id.myexpenselimit);
        expenselimit.setText(userLimit);
        home= findViewById(R.id.homes);





        wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
                View v = inflater.inflate(R.layout.wallet_alertdialog, null);

                final EditText mybalance ;
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(UserInfoActivity.this);
                alertDialogBuilder.setMessage("Your Balance :");
                alertDialogBuilder.setView(v);

                mybalance= v.findViewById(R.id.walletamount);

                alertDialogBuilder.setPositiveButton("okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        currentbalance= mybalance.getText().toString();
                        mywallet.setText(currentbalance);

                        db.updateWallet(currentbalance);

                    }
                });

                alertDialogBuilder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                alertDialogBuilder.setView(v);
                AlertDialog dialog = alertDialogBuilder.create();
                dialog.show();




            }
        });

        limit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
                View v = inflater.inflate(R.layout.expenselimit_alertdialog, null);

                final EditText balance ;
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(UserInfoActivity.this);
                alertDialogBuilder.setMessage("Your Limit :");

                alertDialogBuilder.setView(v);

                balance= v.findViewById(R.id.limitamount);

                alertDialogBuilder.setPositiveButton("okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        currentlimit= balance.getText().toString();
                        expenselimit.setText(currentlimit);
                        db.updateLimit(currentlimit);


                    }
                });

                alertDialogBuilder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                alertDialogBuilder.setView(v);
                AlertDialog alertDialog= alertDialogBuilder.create();
                alertDialog.show();

            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserInfoActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
