package com.example.bappy.foodbank;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

public class Staff_Resister extends AppCompatActivity {

    EditText username,restaurantpassword,password,repassword;
    Button resistor;

    JSONObject jsonObject2;
    JSONArray jsonArray2;

    ArrayList<String> ass=new ArrayList<String>();
    String res,rolestaff;

    String[] role={"User","Staff","Admin","Chef"};

    Spinner spinnerrole,spinnerres;
    LinearLayout linearLayout;

    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.staff__resister_layout);
        progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);

        progressDialog.setMessage("Loading.Please Wait....");
        progressDialog.show();
        new BackgroundTask().execute("http://"+getString(R.string.ip_address)+"/FoodBank/ReadRestaurantOnly.php");

        resistor=(Button) findViewById(R.id.resistor);
        username=(EditText) findViewById(R.id.username);
        restaurantpassword=(EditText) findViewById(R.id.restaurentpassword);
        password=(EditText) findViewById(R.id.password);
        repassword=(EditText) findViewById(R.id.repassword);
        linearLayout=(LinearLayout)findViewById(R.id.linearres);
        linearLayout.setVisibility(View.GONE);
        restaurantpassword.setVisibility(View.GONE);

        ass.add("None");

        spinnerres=(Spinner)findViewById(R.id.spinnerres);

        ArrayAdapter arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_spinner_item,ass);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerres.setAdapter(arrayAdapter);

        spinnerres.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView mytext=(TextView)view;
                res=(String)mytext.getText();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerrole=(Spinner)findViewById(R.id.spinnerrole);

        ArrayAdapter arrayAdapter2=new ArrayAdapter(this,android.R.layout.simple_spinner_item,role);
        arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerrole.setAdapter(arrayAdapter2);

        spinnerrole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView mytext=(TextView)view;
                rolestaff=(String)mytext.getText();
                if(rolestaff.equals("Staff") || rolestaff.equals("Admin") || rolestaff.equals("Chef")) {
                    linearLayout.setVisibility(View.VISIBLE);
                    restaurantpassword.setVisibility(View.VISIBLE);
                }
                else {
                    linearLayout.setVisibility(View.GONE);
                    restaurantpassword.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void onResister(View view){
        String user=username.getText().toString();
        String restaurantpass=restaurantpassword.getText().toString();
        String pass=password.getText().toString();
        String repass=repassword.getText().toString();

        if(rolestaff.equals("Admin") || rolestaff.equals("Staff") || rolestaff.equals("Chef")) {
            if (user.equals("") || restaurantpass.equals("") || pass.equals("") || repass.equals("") || res.equals("None")) {
                Toast.makeText(this, "Please Fill All The Field", Toast.LENGTH_SHORT).show();
            } else {
                if (pass.equals(repass)) {
                    progressDialog.setMessage("Registration processing.\nPlease Wait....");
                    progressDialog.show();
                    new ResistorBackground().execute(user, res, restaurantpass, pass, rolestaff);
                } else
                    Toast.makeText(this, "Password Didn't Match", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            if(user.equals("") || pass.equals("") || repass.equals("")){
                Toast.makeText(this, "Please Fill All The Field", Toast.LENGTH_SHORT).show();
            }
            else {
                if(pass.equals(repass)) {
                    progressDialog.setMessage("Registration processing.\nPlease Wait....");
                    progressDialog.show();
                    new ResistorBackground().execute(user,"No","No",pass,rolestaff);
                }
                else
                    Toast.makeText(this, "Password Didn't Match", Toast.LENGTH_SHORT).show();
            }
        }
    }
    class BackgroundTask extends AsyncTask<String,Void,Boolean>
    {

        String json_url;
        String JSON_STRING;

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                json_url=params[0];
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
                String string=stringBuilder.toString().trim();
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
            if(result)
                progressDialog.cancel();
            else {
                Toast.makeText(Staff_Resister.this, "please try again later", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Staff_Resister.this,staff_login_resistor.class));
                finish();
            }
        }
    }

    public class ResistorBackground extends AsyncTask<String,Void,Boolean> {

        AlertDialog.Builder alert;
        String resu="";

        @Override
        protected Boolean doInBackground(String... params) {
            String loginurl = "http://"+getString(R.string.ip_address)+"/FoodBank/staffResister.php";
                try {
                    String username = params[0];
                    String restaurent = params[1];
                    String respass = params[2];
                    String pass = params[3];
                    String role = params[4];
                        URL url = new URL(loginurl);
                        HttpURLConnection httpurlconnection = (HttpURLConnection) url.openConnection();
                        httpurlconnection.setRequestMethod("POST");
                        httpurlconnection.setDoOutput(true);
                        httpurlconnection.setDoInput(true);
                        OutputStream outputstream = httpurlconnection.getOutputStream();
                        BufferedWriter bufferedwritter = new BufferedWriter(new OutputStreamWriter(outputstream, "UTF-8"));
                        String postdata = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8")
                                + "&" + URLEncoder.encode("restaurent", "UTF-8") + "=" + URLEncoder.encode(restaurent, "UTF-8")
                                + "&" + URLEncoder.encode("respass", "UTF-8") + "=" + URLEncoder.encode(respass, "UTF-8")
                                + "&" + URLEncoder.encode("pass", "UTF-8") + "=" + URLEncoder.encode(pass, "UTF-8")
                                + "&" + URLEncoder.encode("role", "UTF-8") + "=" + URLEncoder.encode(role, "UTF-8");
                        bufferedwritter.write(postdata);
                        bufferedwritter.flush();
                        bufferedwritter.close();
                        outputstream.close();
                        InputStream inputstream = httpurlconnection.getInputStream();
                        BufferedReader bufferdreader = new BufferedReader(new InputStreamReader(inputstream, "iso-8859-1"));
                        String line = "";
                        while ((line = bufferdreader.readLine()) != null) {
                            resu += line;
                        }
                        bufferdreader.close();
                        inputstream.close();
                        httpurlconnection.disconnect();
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
            alert = new AlertDialog.Builder(Staff_Resister.this);
            alert.setTitle("Resistor Status");
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(!result)
                Toast.makeText(Staff_Resister.this, "can't connect to the database", Toast.LENGTH_SHORT).show();
            else {
                alert.setMessage(resu);
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent=new Intent(Staff_Resister.this,staff_login_resistor.class);
                        progressDialog.cancel();
                        startActivity(intent);
                        finish();
                    }
                });
                AlertDialog mydialog = alert.create();
                mydialog.show();
            }
        }

        @Override
        protected void onProgressUpdate (Void...values){
            super.onProgressUpdate(values);
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Staff_Resister.this,staff_login_resistor.class));
        finish();
    }
}
