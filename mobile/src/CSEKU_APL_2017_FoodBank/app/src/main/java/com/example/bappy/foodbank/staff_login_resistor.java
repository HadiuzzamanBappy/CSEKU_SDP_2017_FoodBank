package com.example.bappy.foodbank;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class staff_login_resistor extends AppCompatActivity {

    AlertDialog.Builder alert;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.staff_login_resistor_layout);
        sharedPreferences=getSharedPreferences(getString(R.string.PREF_FILE), 0);
        edit=sharedPreferences.edit();

        ActionBar actionBar=getSupportActionBar();
        actionBar.setLogo(R.drawable.food_app_icon);
        actionBar.setDisplayUseLogoEnabled(true);
    }

    public void registeryou(View view){
        //set the next Page when it will pressed(Intent)
        Intent intent=new Intent(staff_login_resistor.this,Staff_Resister.class);
        //initial the intent activity
        startActivity(intent);
        finish();
    }

    public void login(View view){
        //set the next Page when it will pressed(Intent)
        Intent intent=new Intent(staff_login_resistor.this,StaffLogIn.class);
        //initial the intent activity
        startActivity(intent);
        finish();
    }
    public void skipall(View view)
    {
        alert=new AlertDialog.Builder(staff_login_resistor.this);
        alert.setTitle("Attention");
        alert.setMessage("You are skipping all register or login page \n and you won't able to order any kinda item");//set state for cancelling state
        alert.setCancelable(true);

        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                    edit.putBoolean(getString(R.string.SKIP),true);
                    edit.commit();
                    Intent intent = new Intent(staff_login_resistor.this, HomeActivity.class);
                    startActivity(intent);
                finish();
            }
        });
        alert.setNegativeButton("No,Later", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        //alertdialog create
        AlertDialog mydialog=alert.create();
        //for working the alertdialog state
        mydialog.show();
    }
    //creating activity for back pressing from phone
    public void onBackPressed() {
        //creating a alert dialog(for exit)
        final AlertDialog.Builder exitbuilder = new AlertDialog.Builder(staff_login_resistor.this);
        //setting the alertdialog title
        exitbuilder.setTitle("Attention");
        //setting the body message
        exitbuilder.setMessage("Do You Want To Exit?");
        //setting the icon
        exitbuilder.setIcon(R.drawable.exit);
        //set state for cancelling state
        exitbuilder.setCancelable(true);

        //setting activity for positive state button
        exitbuilder.setPositiveButton("YES, Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        //setting activity for negative state button
        exitbuilder.setNegativeButton("NO, i don't", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        //alertdialog create
        AlertDialog mydialog=exitbuilder.create();
        //for working the alertdialog state
        mydialog.show();
    }
}
