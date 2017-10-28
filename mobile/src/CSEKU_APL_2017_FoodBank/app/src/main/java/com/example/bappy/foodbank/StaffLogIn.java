package com.example.bappy.foodbank;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bappy.foodbank.R;

import org.json.JSONArray;
import org.json.JSONException;
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

public class StaffLogIn extends AppCompatActivity{

    String name,rolestaff,res;
    String resname;
    Button login;
    EditText username,password;
    Spinner spinnerrole,spinnerres;
    JSONObject jsonObject2;
    JSONArray jsonArray2;

    LinearLayout linearLayout;

    ArrayList<String> ass=new ArrayList<String>();

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    String[] role={"User","Admin","Staff","Chef"};
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_log_in);
        progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);

        sharedPreferences=getSharedPreferences(getString(R.string.PREF_FILE), 0);
        editor=sharedPreferences.edit();

        progressDialog.setMessage("Loading.Please Wait....");
        progressDialog.show();
        new BackgroundTask2().execute();

        login=(Button) findViewById(R.id.login);
        username=(EditText) findViewById(R.id.username);
        password=(EditText) findViewById(R.id.restaurentpassword);
        spinnerrole=(Spinner)findViewById(R.id.spinnerrole);
        linearLayout=(LinearLayout)findViewById(R.id.linearres);
        linearLayout.setVisibility(View.GONE);

        ArrayAdapter arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_spinner_item,role);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerrole.setAdapter(arrayAdapter);

        spinnerrole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView mytext=(TextView)view;
                // Toast.makeText(getBaseContext(), "You Selected "+mytext.getText(), Toast.LENGTH_SHORT).show();
                rolestaff=(String)mytext.getText();
                if(rolestaff.equals("Staff") || rolestaff.equals("Admin") || rolestaff.equals("Chef"))
                    linearLayout.setVisibility(View.VISIBLE);
                else
                    linearLayout.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ass.add("None");
        spinnerres=(Spinner)findViewById(R.id.spinnerres);

        ArrayAdapter arrayAdapter2=new ArrayAdapter(this,android.R.layout.simple_spinner_item,ass);
        arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerres.setAdapter(arrayAdapter2);

        spinnerres.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView mytext=(TextView)view;
                // Toast.makeText(getBaseContext(), "You Selected "+mytext.getText(), Toast.LENGTH_SHORT).show();
                res=(String)mytext.getText();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    class BackgroundTask2 extends AsyncTask<Void,Void,Boolean>
    {

        String json_url;
        String JSON_STRING;

        @Override
        protected void onPreExecute() {
            //making a link to php file for reading data from database
            json_url="http://"+getString(R.string.ip_address)+"/FoodBank/ReadRestaurantOnly.php";
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                //initialize the url
                URL url=new URL(json_url);
                //initialize the connection
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                //getting input to the program from database
                InputStream inputStream=httpURLConnection.getInputStream();
                //reading data from database through php file
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
                //initialize a string-builder
                StringBuilder stringBuilder=new StringBuilder();
                while((JSON_STRING=bufferedReader.readLine())!=null){
                    //adding string to the string builder
                    stringBuilder.append(JSON_STRING+"\n");
                }
                //closing all connection
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                //returning the string as a result
                String string= stringBuilder.toString().trim();
                jsonObject2=new JSONObject(string);
                jsonArray2=jsonObject2.getJSONArray("Server_response");

                int coun=0;
                String clientnam;
                while(coun<jsonArray2.length())
                {
                    JSONObject jo=jsonArray2.getJSONObject(coun);
                    clientnam=jo.getString("name");
                    ass.add(clientnam);
                    coun++;
                }
                return true;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            progressDialog.cancel();
            if(!result)
                Toast.makeText(StaffLogIn.this, "can't connect to the database\ncan't load the restaurant name", Toast.LENGTH_SHORT).show();
        }
    }

    public void onLogin(View view){
        String user=username.getText().toString();
        String restaurent=res;
        String pass=password.getText().toString();
        String role=rolestaff;
        if(user.equals("") || pass.equals(""))
            Toast.makeText(this, "Please Fill All The Field", Toast.LENGTH_SHORT).show();
        else {
            if (role.equals("Admin") || role.equals("Staff") || role.equals("Chef"))
                if (restaurent.equals("None"))
                    if (role.equals("Admin"))
                        Toast.makeText(this, "Please check your Restaurant Mr. Admin", Toast.LENGTH_SHORT).show();
                    else if(role.equals("Staff"))
                        Toast.makeText(this, "Please check your Restaurant Mr. Staff", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(this, "Please check your Restaurant Mr. Chef", Toast.LENGTH_SHORT).show();
                else {
                    String type = "login";
                    new LoginBackground().execute(type, user, restaurent, role, pass);
                }
            else {
                String type = "login";
                new LoginBackground().execute(type, user, "None", role, pass);
            }
        }
    }
    public class LoginBackground extends AsyncTask<String,Void,String> {

        AlertDialog alert;
        String password;

        @Override
        protected String doInBackground(String... params) {
            String type = params[0];
            String loginurl = "http://"+getString(R.string.ip_address)+"/FoodBank/stafflogin.php";
            if (type.equals("login")) {
                try {
                    String username = params[1];
                    name = username;
                    String restaurent = params[2];
                    resname = restaurent;
                    String role=params[3];
                    password = params[4];

                    URL url = new URL(loginurl);
                    HttpURLConnection httpurlconnection = (HttpURLConnection) url.openConnection();
                    httpurlconnection.setRequestMethod("POST");
                    httpurlconnection.setDoOutput(true);
                    httpurlconnection.setDoInput(true);
                    OutputStream outputstream = httpurlconnection.getOutputStream();
                    BufferedWriter bufferedwritter = new BufferedWriter(new OutputStreamWriter(outputstream, "UTF-8"));
                    String postdata = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8") + "&"
                            + URLEncoder.encode("restaurent", "UTF-8") + "=" + URLEncoder.encode(restaurent, "UTF-8") + "&" +
                            URLEncoder.encode("role", "UTF-8") + "=" + URLEncoder.encode(role, "UTF-8") + "&" +
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
        protected void onPreExecute() {
            progressDialog.setMessage("Logging In.Please Wait....");
            progressDialog.show();
            alert = new AlertDialog.Builder(StaffLogIn.this).create();
            alert.setTitle("Login Status");
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.cancel();
            if (result.equals("true")) {
                if(rolestaff.equals("Admin")) {
                    Intent intent = new Intent(StaffLogIn.this, Adminstaff.class);
                    editor.putBoolean(getString(R.string.SAVE_LOGIN), true);
                    editor.putString(getString(R.string.NAME), name);
                    editor.putString(getString(R.string.PASSWORD), password);
                    editor.putString(getString(R.string.RESTAURANT_NAME), resname);
                    editor.putString(getString(R.string.TYPE), rolestaff);
                    editor.commit();
                    intent.putExtra("username", name);
                    intent.putExtra("resname", resname);
                    intent.putExtra("role", rolestaff);
                    startActivity(intent);
                    finish();
                }
                else if(rolestaff.equals("Staff"))
                {
                    Intent intent = new Intent(StaffLogIn.this, OnlyStaff.class);
                    editor.putBoolean(getString(R.string.SAVE_LOGIN), true);
                    editor.putString(getString(R.string.NAME), name);
                    editor.putString(getString(R.string.PASSWORD), password);
                    editor.putString(getString(R.string.RESTAURANT_NAME), resname);
                    editor.putString(getString(R.string.TYPE), rolestaff);
                    editor.commit();
                    intent.putExtra("username", name);
                    intent.putExtra("resname", resname);
                    intent.putExtra("role", rolestaff);
                    startActivity(intent);
                    finish();
                }
                else if(rolestaff.equals("Chef"))
                {
                    Intent intent = new Intent(StaffLogIn.this, ChefWork.class);
                    editor.putBoolean(getString(R.string.SAVE_LOGIN), true);
                    editor.putString(getString(R.string.NAME), name);
                    editor.putString(getString(R.string.PASSWORD), password);
                    editor.putString(getString(R.string.RESTAURANT_NAME), resname);
                    editor.putString(getString(R.string.TYPE), rolestaff);
                    editor.commit();
                    intent.putExtra("username", name);
                    intent.putExtra("resname", resname);
                    intent.putExtra("role", rolestaff);
                    startActivity(intent);
                    finish();
                }
                else {
                    Intent intent = new Intent(StaffLogIn.this, HomeActivity.class);
                    editor.putBoolean(getString(R.string.SAVE_LOGIN), true);
                    editor.putString(getString(R.string.NAME), name);
                    editor.putString(getString(R.string.PASSWORD), password);
                    editor.putString(getString(R.string.RESTAURANT_NAME), "None");
                    editor.putString(getString(R.string.TYPE), rolestaff);
                    editor.commit();
                    startActivity(intent);
                    finish();
                }
            } else {
                alert.setMessage("Wrong Information... Please Input correct USERNAME(exact) RESTAURENT_NAME And PASSWORD");
                alert.show();
                }
            }

            @Override
            protected void onProgressUpdate (Void...values){
                super.onProgressUpdate(values);
            }
        }

    public void onLoginsuperadmin(View view){
        Intent intent=new Intent(StaffLogIn.this,SuperAdminLogin.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(StaffLogIn.this,staff_login_resistor.class);
        startActivity(intent);
        finish();
    }
}
