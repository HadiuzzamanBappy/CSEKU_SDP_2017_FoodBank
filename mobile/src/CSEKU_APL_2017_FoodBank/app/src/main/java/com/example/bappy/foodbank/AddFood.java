package com.example.bappy.foodbank;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
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

    String resname,foodtype="None";
    EditText foodname,foodprice;

    Spinner spinnertype;
    String[] type={"None","Spicy","General","Soft Drinks","Meals","Desert","Drinks"};

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);
        if(!isNetworkAvilabe())
            nointernet();
        else {
            sharedPreferences=getSharedPreferences(getString(R.string.PREF_FILE), 0);
            editor=sharedPreferences.edit();

            resname = getIntent().getExtras().getString("resname");
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
                editor.clear();
                editor.commit();
                startActivity(new Intent(this, staff_login_resistor.class));
                finish();
                return true;
            case R.id.my_profile:
                startActivity(new Intent(this, ShowProfile.class));
                return true;
            case R.id.new_restaurant:
                startActivity(new Intent(this, CreateNewRestaurant.class));
                return true;
            case R.id.edit_profile:
                Intent intent=new Intent(this, EditChangeProfile.class);
                intent.putExtra("op_type","Edit");
                startActivity(intent);
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
            intent.putExtra("resname", resname);
            finish();
            startActivity(intent);
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
            if(resul.equals("false"))
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
                            intent.putExtra("resname", resname);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
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
