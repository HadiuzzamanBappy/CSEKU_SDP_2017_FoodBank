package com.example.bappy.foodbank;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.EditText;
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

public class SuperAdminLogin extends AppCompatActivity {

    String name;
    EditText username,password;
    String user,pass;
    SharedPreferences loginPreferences;
    SharedPreferences.Editor loginPrefsEditor;

    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.super_admin_login_layout);
        progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setLogo(R.drawable.ic_android_white_24dp);

        loginPreferences =getSharedPreferences(getString(R.string.PREF_FILE), 0);
        loginPrefsEditor = loginPreferences.edit();

        username=(EditText) findViewById(R.id.username);
        password=(EditText) findViewById(R.id.restaurentpassword);
    }

    public void onLogin(View view){
        user=username.getText().toString();
        pass=password.getText().toString();
        if(user.equals("") || pass.equals(""))
            Toast.makeText(this, "Please Fill All The Field", Toast.LENGTH_SHORT).show();
        else {
            progressDialog.setMessage("Logging In.Please Wait....");
            progressDialog.show();
            new LoginBackground().execute("login", user, pass);
        }
    }

    public class LoginBackground extends AsyncTask<String,Void,String> {

        AlertDialog alert;
        String password;

        @Override
        protected void onPreExecute() {
            alert = new AlertDialog.Builder(SuperAdminLogin.this).create();
            alert.setTitle("Login Status");
        }

        @Override
        protected void onProgressUpdate (Void...values){
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {
            String type = params[0];
            String loginurl = "http://"+getString(R.string.ip_address)+"/FoodBank/superadminlogin.php";
            if (type.equals("login")) {
                try {
                    String username = params[1];
                    name = username;
                    String password = params[2];

                    URL url = new URL(loginurl);
                    HttpURLConnection httpurlconnection = (HttpURLConnection) url.openConnection();
                    httpurlconnection.setRequestMethod("POST");
                    httpurlconnection.setDoOutput(true);
                    httpurlconnection.setDoInput(true);
                    OutputStream outputstream = httpurlconnection.getOutputStream();
                    BufferedWriter bufferedwritter = new BufferedWriter(new OutputStreamWriter(outputstream, "UTF-8"));
                    String postdata = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8") + "&"+
                            URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
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
                    return result;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.cancel();
            if (result.equals("true")) {
                loginPrefsEditor.putBoolean(getString(R.string.SAVE_LOGIN), true);
                loginPrefsEditor.putString(getString(R.string.NAME), name);
                loginPrefsEditor.putString(getString(R.string.PASSWORD), pass);
                loginPrefsEditor.putString(getString(R.string.TYPE), "Superadmin");
                loginPrefsEditor.commit();
                Intent intent = new Intent(SuperAdminLogin.this, SuperAdminRestaurant.class);
                intent.putExtra("username", name);
                intent.putExtra("role", "SuperAdmin");
                startActivity(intent);
                finish();
            } else {
                alert.setMessage("Wrong Information... Please Input correct USERNAME(exact) And PASSWORD");
                alert.show();
            }
        }
    }


    @Override
    public void onBackPressed() {
        Intent intent=new Intent(SuperAdminLogin.this,StaffLogIn.class);
        startActivity(intent);
        finish();
    }
}