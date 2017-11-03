package com.example.bappy.foodbank;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

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
import java.util.ArrayList;

public class CreateNewRestaurantStaff extends AppCompatActivity {

    EditText username,password,repassword;
    Button resistor;

    String tname,tstreet,ttown,ttype,tphone,tpassword;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_new_restaurant_staff_layout);
        progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);

        sharedPreferences=getSharedPreferences(getString(R.string.PREF_FILE), 0);
        editor=sharedPreferences.edit();

        tname=getIntent().getExtras().getString("name");
        tstreet=getIntent().getExtras().getString("street");
        ttown=getIntent().getExtras().getString("town");
        ttype=getIntent().getExtras().getString("type");
        tphone=getIntent().getExtras().getString("phone");
        tpassword=getIntent().getExtras().getString("password");

        resistor=(Button) findViewById(R.id.resistor);
        username=(EditText) findViewById(R.id.username);
        password=(EditText) findViewById(R.id.password);
        repassword=(EditText) findViewById(R.id.repassword);
    }
    public void onResister(View view){
        if(!isNetworkAvilabe())
            nointernet();
        else {
            String user = username.getText().toString();
            String pass = password.getText().toString();
            String repass = repassword.getText().toString();

            if (user.equals("") || pass.equals("") || repass.equals("")) {
                Toast.makeText(this, "Please Fill All The Field", Toast.LENGTH_SHORT).show();
            } else {
                if (pass.equals(repass)) {
                    progressDialog.setMessage("Please Wait For Registration....");
                    progressDialog.show();
                    new ResistorBackground().execute(tname, tstreet, ttown, ttype, tphone, tpassword, user, pass);
                }
                else
                    Toast.makeText(this, "Password Didn't Match", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public class ResistorBackground extends AsyncTask<String,Void,Boolean> {

        AlertDialog.Builder alert;
        String st;

        @Override
        protected Boolean doInBackground(String... params) {
            String loginurl = "http://"+getString(R.string.ip_address)+"/FoodBank/CreateRestaurant.php";
            try {
                String resname = params[0];
                String resstreet = params[1];
                String restown = params[2];
                String restype = params[3];
                String resphone = params[4];
                String respass = params[5];
                String username = params[6];
                String userpass = params[7];
                    URL url = new URL(loginurl);
                    HttpURLConnection httpurlconnection = (HttpURLConnection) url.openConnection();
                    httpurlconnection.setRequestMethod("POST");
                    httpurlconnection.setDoOutput(true);
                    httpurlconnection.setDoInput(true);
                    OutputStream outputstream = httpurlconnection.getOutputStream();
                    BufferedWriter bufferedwritter = new BufferedWriter(new OutputStreamWriter(outputstream, "UTF-8"));
                    String postdata = URLEncoder.encode("resname", "UTF-8") + "=" + URLEncoder.encode(resname, "UTF-8")
                            + "&" + URLEncoder.encode("resstreet", "UTF-8") + "=" + URLEncoder.encode(resstreet, "UTF-8")
                            + "&" + URLEncoder.encode("restown", "UTF-8") + "=" + URLEncoder.encode(restown, "UTF-8")
                            + "&" + URLEncoder.encode("restype", "UTF-8") + "=" + URLEncoder.encode(restype, "UTF-8")
                            + "&" + URLEncoder.encode("resphone", "UTF-8") + "=" + URLEncoder.encode(resphone, "UTF-8")
                            + "&" + URLEncoder.encode("respass", "UTF-8") + "=" + URLEncoder.encode(respass, "UTF-8")
                            + "&" + URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8")
                            + "&" + URLEncoder.encode("userpass", "UTF-8") + "=" + URLEncoder.encode(userpass, "UTF-8");
                    bufferedwritter.write(postdata);
                    bufferedwritter.flush();
                    bufferedwritter.close();
                    outputstream.close();
                    InputStream inputstream = httpurlconnection.getInputStream();
                    BufferedReader bufferdreader = new BufferedReader(new InputStreamReader(inputstream, "iso-8859-1"));
                    String result = "";
                    String line = "";
                    while ((line = bufferdreader.readLine()) != null) {
                        result += line;
                    }
                    bufferdreader.close();
                    inputstream.close();
                    httpurlconnection.disconnect();
                    st= result;
                return true;
            } catch(MalformedURLException e){
                e.printStackTrace();
            } catch(IOException e){
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPreExecute() {
            alert = new AlertDialog.Builder(CreateNewRestaurantStaff.this);
            alert.setTitle("Resistor Status");
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(!result)
                Toast.makeText(CreateNewRestaurantStaff.this, "can't connect to the database", Toast.LENGTH_SHORT).show();
            else {
                alert.setMessage(st);
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                progressDialog.cancel();
                AlertDialog mydialog = alert.create();
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
        AlertDialog.Builder CheckBuild = new AlertDialog.Builder(CreateNewRestaurantStaff.this);
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

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this,CreateNewRestaurant.class));
        finish();
    }
}
