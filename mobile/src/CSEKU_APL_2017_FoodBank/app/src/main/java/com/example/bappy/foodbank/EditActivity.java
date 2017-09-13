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

public class EditActivity extends AppCompatActivity {

    EditText name,price;
    Spinner spinnertype;
    String getname,gettype,getprice,getresname;
    String newname,newprice,newtype;
    String alltype[]={"Spicy","General","Soft Drinks","Meals","Desert","Drinks"};

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_layout);

        sharedPreferences=getSharedPreferences(getString(R.string.PREF_FILE), 0);
        editor=sharedPreferences.edit();

        getresname=getIntent().getExtras().getString("resname");
        getname=getIntent().getExtras().getString("name");
        gettype=getIntent().getExtras().getString("type");
        getprice=getIntent().getExtras().getString("price");
        name=(EditText) findViewById(R.id.foodname);
        price=(EditText) findViewById(R.id.foodprice);

        name.setText(getname);
        price.setText(getprice);

        spinnertype=(Spinner)findViewById(R.id.spinnertype);
        ArrayAdapter arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_spinner_item,alltype);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnertype.setAdapter(arrayAdapter);

        int index = 0;

        for (int i=0;i<spinnertype.getCount();i++) {
            if (spinnertype.getItemAtPosition(i).toString().equalsIgnoreCase(gettype)) {
                index = i;
                break;
            }
        }
            spinnertype.setSelection(index);

        spinnertype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView mytext=(TextView)view;
                newtype=(String)mytext.getText();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
        Intent intent=new Intent(EditActivity.this,DecorateRestaurant.class);
        intent.putExtra("resname",getresname);
        startActivity(intent);
        finish();
    }
    public void editfood(View view){
        newname=name.getText().toString();
        newprice=price.getText().toString();
        if(newname.equals(getname) && newprice.equals(getprice) && newtype.equals(gettype))
            Toast.makeText(this, "You didn't change anything...", Toast.LENGTH_LONG).show();
        else {
            AlertDialog.Builder paidbuilder = new AlertDialog.Builder(EditActivity.this);
            //setting the alertdialog title
            paidbuilder.setTitle("Attention");
            //setting the body message
            paidbuilder.setMessage("Do You Want To Edit it?");
            //set state for cancelling state
            paidbuilder.setCancelable(true);

            //setting activity for positive state button
            paidbuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new BackgroundTask2().execute("Edit");
                }
            });
            //setting activity for negative state button
            paidbuilder.setNegativeButton("NO, Later", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            //alertdialog create
            AlertDialog mydialog = paidbuilder.create();
            //for working the alertdialog state
            mydialog.show();
        }
    }
    class BackgroundTask2 extends AsyncTask<String,Void,Boolean> {
        AlertDialog.Builder alert;
        String json_url;
        String JSON_STRING;

        @Override
        protected void onPreExecute() {
            alert = new AlertDialog.Builder(EditActivity.this);
            alert.setTitle("Update Status");
            json_url = "http://" + getString(R.string.ip_address) + "/FoodBank/FoodEditDelete.php";
        }

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                String type = params[0];
                URL url = new URL(json_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputstream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedwritter = new BufferedWriter(new OutputStreamWriter(outputstream, "UTF-8"));
                String postdata = URLEncoder.encode("pertype", "UTF-8") + "=" + URLEncoder.encode(type, "UTF-8")
                        + "&" + URLEncoder.encode("resname", "UTF-8") + "=" + URLEncoder.encode(getresname, "UTF-8")
                        + "&" + URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(newname, "UTF-8")
                        + "&" + URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode(newtype, "UTF-8")
                        + "&" + URLEncoder.encode("price", "UTF-8") + "=" + URLEncoder.encode(newprice, "UTF-8")
                        + "&" + URLEncoder.encode("prevname", "UTF-8") + "=" + URLEncoder.encode(getname, "UTF-8");
                bufferedwritter.write(postdata);
                bufferedwritter.flush();
                bufferedwritter.close();
                outputstream.close();
                return true;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
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
            if(result.equals("false"))
                Toast.makeText(EditActivity.this, "can't connect to the database", Toast.LENGTH_SHORT).show();
            else {
                Intent intent = new Intent(EditActivity.this, DecorateRestaurant.class);
                intent.putExtra("resname", getresname);
                startActivity(intent);
                finish();
            }
        }
    }

}
