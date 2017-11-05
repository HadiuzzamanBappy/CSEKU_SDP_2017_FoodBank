package com.example.bappy.foodbank;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class CreateNewRestaurant extends AppCompatActivity {

    String name,street,town,type,phone,password,passwordagain;

    EditText tname,tstreet,ttown,ttype,tphone,tpassword,tpasswordagain;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_new_restaurant_layout);
        progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);

        sharedPreferences=getSharedPreferences(getString(R.string.PREF_FILE), 0);
        editor=sharedPreferences.edit();

        tname=(EditText) findViewById(R.id.username);
        tstreet=(EditText) findViewById(R.id.street);
        ttown=(EditText) findViewById(R.id.town);
        ttype=(EditText) findViewById(R.id.type);
        tphone=(EditText) findViewById(R.id.phone);
        tpassword=(EditText) findViewById(R.id.password);
        tpasswordagain=(EditText) findViewById(R.id.passwordagain);
    }

    public void onrestaurentresistor(View view){
        if(!isNetworkAvilabe())
            nointernet();
        else {

            name = tname.getText().toString();
            street = tstreet.getText().toString();
            town = ttown.getText().toString();
            type = ttype.getText().toString();
            phone = tphone.getText().toString();
            password = tpassword.getText().toString();
            passwordagain = tpasswordagain.getText().toString();

            if (name.equals("") || street.equals("") || town.equals("") || type.equals("") || phone.equals("") || password.equals("")) {
                Toast.makeText(this, "Please Fill All The Field", Toast.LENGTH_SHORT).show();
            } else {
                if (password.equals(passwordagain)) {
                    AlertDialog.Builder alert=new AlertDialog.Builder(this);
                    alert.setCancelable(true);
                    alert.setTitle("Attention");
                    alert.setMessage("Are You sure???\nYou Will Not Get Any Chance to Edit");
                    alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            progressDialog.setMessage("Please Wait For a While....");
                            progressDialog.show();
                            Runnable progressrunnable2 = new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(CreateNewRestaurant.this, CreateNewRestaurantStaff.class);
                                    //sending data to another intent
                                    intent.putExtra("name", name);
                                    intent.putExtra("street", street);
                                    intent.putExtra("town", town);
                                    intent.putExtra("type", type);
                                    intent.putExtra("phone", phone);
                                    intent.putExtra("password", password);
                                    progressDialog.cancel();
                                    startActivity(intent);
                                    finish();
                                }
                            };
                            Handler handler2 = new Handler();
                            handler2.postDelayed(progressrunnable2, 3500);
                        }
                    });
                    alert.setNegativeButton("No,Wait", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog alertDialog=alert.create();
                    alertDialog.show();
                } else
                    Toast.makeText(this, "password didn't match", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private boolean isNetworkAvilabe()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    public void nointernet() {
        //Creating an Alertdialog
        AlertDialog.Builder CheckBuild = new AlertDialog.Builder(CreateNewRestaurant.this);
        CheckBuild.setIcon(R.drawable.no);
        CheckBuild.setTitle("Error!");
        CheckBuild.setMessage("Check Your Internet Connection");

        //Builder Retry Button

        CheckBuild.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id) {
                //Restart The Activity
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }

        });
        CheckBuild.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                //Exit The Activity
                finish();
            }

        });
        AlertDialog alertDialog = CheckBuild.create();
        alertDialog.show();
    }
}
