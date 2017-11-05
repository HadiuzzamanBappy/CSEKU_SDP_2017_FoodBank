package com.example.bappy.foodbank;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

public class AddFood extends AppCompatActivity {

    String resname,foodtype="None",name,role;
    EditText foodname,foodprice;

    Spinner spinnertype;
    String[] type={"None","Spicy","General","Soft Drinks","Meals","Desert","Drinks"};

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);
        progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);

        if(!isNetworkAvilabe())
            nointernet();
        else {
            sharedPreferences=getSharedPreferences(getString(R.string.PREF_FILE), 0);
            editor=sharedPreferences.edit();

            name=getIntent().getExtras().getString("username");
            resname=getIntent().getExtras().getString("resname");
            role=getIntent().getExtras().getString("role");

            foodname = (EditText) findViewById(R.id.foodname);
            foodprice = (EditText) findViewById(R.id.foodprice);

            spinnertype = (Spinner) findViewById(R.id.spinnertype);
            ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, type);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnertype.setAdapter(arrayAdapter);

            spinnertype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    TextView mytext = (TextView) view;
                    foodtype = (String) mytext.getText();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu_staff_admin,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Logout:
                if(!isNetworkAvilabe())
                    nointernet();
                else {
                    editor.clear();
                    editor.commit();
                    progressDialog.setMessage("Logging Out.Please Wait....");
                    progressDialog.show();
                    Runnable progressrunnable = new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.cancel();
                            editor.putBoolean(getString(R.string.SKIP),false);
                            startActivity(new Intent(AddFood.this, staff_login_resistor.class));
                            finish();
                        }
                    };

                    Handler handler = new Handler();
                    handler.postDelayed(progressrunnable, 3500);
                }
                return true;
            case R.id.my_profile:
                if(!isNetworkAvilabe())
                    nointernet();
                else {
                    progressDialog.setMessage("Loading.Please Wait....");
                    progressDialog.show();
                    Runnable progressrunnable3 = new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.cancel();
                            startActivity(new Intent(AddFood.this, ShowProfile.class));
                        }
                    };
                    Handler handler3 = new Handler();
                    handler3.postDelayed(progressrunnable3, 35000);
                }
                return true;
            case R.id.new_restaurant:
                if(!isNetworkAvilabe())
                    nointernet();
                else {
                    progressDialog.setMessage("Loading.Please Wait....");
                    progressDialog.show();
                    Runnable progressrunnable4 = new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.cancel();
                            startActivity(new Intent(AddFood.this, CreateNewRestaurant.class));
                        }
                    };
                    Handler handler4 = new Handler();
                    handler4.postDelayed(progressrunnable4, 3500);
                }
                return true;
            case R.id.edit_profile:
                if(!isNetworkAvilabe())
                    nointernet();
                else {
                    progressDialog.setMessage("Loading.Please Wait....");
                    progressDialog.show();
                    Runnable progressrunnable5 = new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.cancel();
                            Intent intent = new Intent(AddFood.this, EditChangeProfile.class);
                            intent.putExtra("op_type", "Edit");
                            startActivity(intent);
                        }
                    };
                    Handler handler5 = new Handler();
                    handler5.postDelayed(progressrunnable5, 3500);
                }
                return true;
            case R.id.about:
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle("About");
                alert.setMessage(getString(R.string.about));
                alert.setCancelable(true);
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog al = alert.create();
                al.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    //creating activity for back pressing from phone
    public void onBackPressed() {
        if(!isNetworkAvilabe())
            nointernet();
        else {
            Intent intent = new Intent(AddFood.this, DecorateRestaurant.class);
            intent.putExtra("username", name);
            intent.putExtra("resname", resname);
            intent.putExtra("role", role);
            startActivity(intent);
            finish();
        }
    }
    public void addfood(View view){
        if(!isNetworkAvilabe())
            nointernet();
        else {
            String restaurent = resname;
            String food = foodname.getText().toString();
            String price = foodprice.getText().toString();
            String type = foodtype;
            progressDialog.setMessage("Adding.Please Wait....");
            progressDialog.show();
            new FoodAddBackground().execute("Add", restaurent, food, price, type);
        }
    }

    public class FoodAddBackground extends AsyncTask<String,Void,Boolean> {

        AlertDialog.Builder alert;
        String result;

        @Override
        protected Boolean doInBackground(String... params) {
            String  type= params[0];
            String loginurl = "http://"+getString(R.string.ip_address)+"/FoodBank/FoodAdd.php";
            if (type.equals("Add")) {
                try {
                    String restaurent = params[1];
                    String food = params[2];
                    String price = params[3];
                    String foodtype = params[4];
                    if (food.equals("") || price.equals("") || foodtype.equals("None")) {
                        Toast.makeText(AddFood.this, "please fill all the field", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        URL url = new URL(loginurl);
                        HttpURLConnection httpurlconnection = (HttpURLConnection) url.openConnection();
                        httpurlconnection.setRequestMethod("POST");
                        httpurlconnection.setDoOutput(true);
                        httpurlconnection.setDoInput(true);
                        OutputStream outputstream = httpurlconnection.getOutputStream();
                        BufferedWriter bufferedwritter = new BufferedWriter(new OutputStreamWriter(outputstream, "UTF-8"));
                        String postdata = URLEncoder.encode("restaurent", "UTF-8") + "=" + URLEncoder.encode(restaurent, "UTF-8") + "&"
                                + URLEncoder.encode("food", "UTF-8") + "=" + URLEncoder.encode(food, "UTF-8") + "&"
                                + URLEncoder.encode("price", "UTF-8") + "=" + URLEncoder.encode(price, "UTF-8") + "&"
                                + URLEncoder.encode("foodtype", "UTF-8") + "=" + URLEncoder.encode(foodtype, "UTF-8");
                        bufferedwritter.write(postdata);
                        bufferedwritter.flush();
                        bufferedwritter.close();
                        outputstream.close();
                        InputStream inputstream = httpurlconnection.getInputStream();
                        BufferedReader bufferdreader = new BufferedReader(new InputStreamReader(inputstream, "iso-8859-1"));
                        result = "";
                        String line = "";
                        while ((line = bufferdreader.readLine()) != null) {
                            result += line;
                        }
                        bufferdreader.close();
                        inputstream.close();
                        httpurlconnection.disconnect();
                        return true;
                    }
                } catch(MalformedURLException e){
                    e.printStackTrace();
                } catch(IOException e){
                    e.printStackTrace();
                }
            }
            return false;
        }

        @Override
        protected void onPreExecute() {
            alert = new AlertDialog.Builder(AddFood.this);
            alert.setTitle("Addition Status");
        }

        @Override
        protected void onPostExecute(Boolean resul) {
            if(!resul)
                Toast.makeText(AddFood.this, "can't connect to the database", Toast.LENGTH_SHORT).show();
            else {
                alert.setMessage(result);
                //set state for cancelling state
                alert.setCancelable(true);

                //setting activity for positive state button
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(!isNetworkAvilabe())
                            nointernet();
                        else {
                            Intent intent = new Intent(AddFood.this, DecorateRestaurant.class);
                            intent.putExtra("username", name);
                            intent.putExtra("resname", resname);
                            intent.putExtra("role", role);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
                progressDialog.cancel();
                AlertDialog mydialog = alert.create();
                //for working the alertdialog state
                mydialog.show();
            }
        }

        @Override
        protected void onProgressUpdate (Void...values){
            super.onProgressUpdate(values);
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
        AlertDialog.Builder CheckBuild = new AlertDialog.Builder(AddFood.this);
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
