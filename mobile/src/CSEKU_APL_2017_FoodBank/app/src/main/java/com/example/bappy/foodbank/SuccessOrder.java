package com.example.bappy.foodbank;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class SuccessOrder extends AppCompatActivity {

    TextView tclient,trestaurent,tfood,tphone,tdate,tquan,taddrs,tdelitype,tprice;
    String client,restaurent,food,phone,date,quan,addrs,delitype,price,resfood;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Boolean save_login;

    String name,resname,pass,type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.successorder_layout);
        sharedPreferences = getSharedPreferences(getString(R.string.PREF_FILE), 0);
        editor = sharedPreferences.edit();
        save_login = sharedPreferences.getBoolean(getString(R.string.SAVE_LOGIN), false);

        type = sharedPreferences.getString(getString(R.string.TYPE), "None");
        name = sharedPreferences.getString(getString(R.string.NAME), "None");
        pass = sharedPreferences.getString(getString(R.string.PASSWORD), "None");
        resname = sharedPreferences.getString(getString(R.string.RESTAURANT_NAME), "None");

        client = getIntent().getExtras().getString("client");
        restaurent = getIntent().getExtras().getString("restaurant");
        food = getIntent().getExtras().getString("foodname");
        phone = getIntent().getExtras().getString("phone");
        date = getIntent().getExtras().getString("date");
        quan = getIntent().getExtras().getString("quan");
        addrs = getIntent().getExtras().getString("addrs");
        delitype = getIntent().getExtras().getString("delitype");
        price = getIntent().getExtras().getString("price");
        resfood = getIntent().getExtras().getString("resfood");
        tclient = (TextView) findViewById(R.id.name);
        trestaurent = (TextView) findViewById(R.id.restaurent);
        tfood = (TextView) findViewById(R.id.foodname);
        tphone = (TextView) findViewById(R.id.phone);
        tdate = (TextView) findViewById(R.id.deliverydate);
        tquan = (TextView) findViewById(R.id.quantity);
        taddrs = (TextView) findViewById(R.id.address);
        tdelitype = (TextView) findViewById(R.id.deliverytype);
        tprice = (TextView) findViewById(R.id.price);
        tclient.setText("Name : " + client);
        trestaurent.setText("Restaurant : " + restaurent);
        tfood.setText("Food Name : " + food);
        tphone.setText("Phone Number : " + phone);
        tdate.setText("Delivery Date : " + date);
        tquan.setText("Quantity : " + quan);
        taddrs.setText("Address : " + addrs);
        tdelitype.setText("Home Delivery :" + delitype);
        tprice.setText("Price : " + price);
    }
    public void homepage(View view){
        if(resfood.equals("1")) {
            Intent intent = new Intent(this, DisplayFoodRestaurentList.class);
            intent.putExtra("food_name", food);
            startActivity(intent);
            finish();
        }
        else
        {
            Intent intent = new Intent(this, DisplayRestaurentFoodList.class);
            intent.putExtra("restaurent_name", restaurent);
            startActivity(intent);
            finish();
        }
    }
    //creating activity for back pressing from phone
    public void onBackPressed() {
        if(resfood.equals("1")) {
            Intent intent = new Intent(this, DisplayFoodRestaurentList.class);
            intent.putExtra("food_name", food);
            startActivity(intent);
            finish();
        }
        else
        {
            Intent intent = new Intent(this, DisplayRestaurentFoodList.class);
            intent.putExtra("restaurent_name", restaurent);
            startActivity(intent);
            finish();
        }
    }
}
