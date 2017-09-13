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
        sharedPreferences=getSharedPreferences(getString(R.string.PREF_FILE), 0);
        editor=sharedPreferences.edit();
        save_login=sharedPreferences.getBoolean(getString(R.string.SAVE_LOGIN),false);

        type=sharedPreferences.getString(getString(R.string.TYPE),"None");
        name=sharedPreferences.getString(getString(R.string.NAME),"None");
        pass=sharedPreferences.getString(getString(R.string.PASSWORD),"None");
        resname=sharedPreferences.getString(getString(R.string.RESTAURANT_NAME),"None");

        client=getIntent().getExtras().getString("client");
        restaurent=getIntent().getExtras().getString("restaurant");
        food=getIntent().getExtras().getString("foodname");
        phone=getIntent().getExtras().getString("phone");
        date=getIntent().getExtras().getString("date");
        quan=getIntent().getExtras().getString("quan");
        addrs=getIntent().getExtras().getString("addrs");
        delitype=getIntent().getExtras().getString("delitype");
        price=getIntent().getExtras().getString("price");
        resfood = getIntent().getExtras().getString("resfood");
        tclient=(TextView)findViewById(R.id.name);
        trestaurent=(TextView)findViewById(R.id.restaurent);
        tfood=(TextView)findViewById(R.id.foodname);
        tphone=(TextView)findViewById(R.id.phone);
        tdate=(TextView)findViewById(R.id.deliverydate);
        tquan=(TextView)findViewById(R.id.quantity);
        taddrs=(TextView)findViewById(R.id.address);
        tdelitype=(TextView)findViewById(R.id.deliverytype);
        tprice=(TextView)findViewById(R.id.price);
        tclient.setText("Name : "+client);
        trestaurent.setText("Restaurant : "+restaurent);
        tfood.setText("Food Name : "+food);
        tphone.setText("Phone Number : "+phone);
        tdate.setText("Delivery Date : "+date);
        tquan.setText("Quantity : "+quan);
        taddrs.setText("Address : "+addrs);
        tdelitype.setText("Home Delivery :"+delitype);
        tprice.setText("Price : "+price);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        if(save_login)
            menuInflater.inflate(R.menu.menu_item_super,menu);
        else
            menuInflater.inflate(R.menu.menu_main2,menu);
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
            case R.id.LogIn:
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
            case R.id.delete_profile:
                AlertDialog.Builder alert =new AlertDialog.Builder(this);
                alert.setTitle("Attention");
                alert.setMessage("Are You Sure??");
                alert.setCancelable(true);
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new BackgroundTask3().execute("Delete",name,name,resname,type,pass,pass);
                    }
                });
                alert.setNegativeButton("No,later", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog al=alert.create();
                al.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    class BackgroundTask3 extends AsyncTask<String,Void,Boolean> {
        String json_url;
        String JSON_STRING;
        String newpass, newname, op_type, resul, res_name, role;

        @Override
        protected void onPreExecute() {
            json_url = "http://" + getString(R.string.ip_address) + "/FoodBank/ProfileEditDelete.php";
        }

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                op_type = params[0];
                String oldname = params[1];
                newname = params[2];
                res_name = params[3];
                role = params[4];
                String oldpass = params[5];
                newpass = params[6];
                URL url = new URL(json_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputstream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedwritter = new BufferedWriter(new OutputStreamWriter(outputstream, "UTF-8"));
                String postdata = URLEncoder.encode("op_type", "UTF-8") + "=" + URLEncoder.encode(op_type, "UTF-8")
                        + "&" + URLEncoder.encode("oldname", "UTF-8") + "=" + URLEncoder.encode(oldname, "UTF-8")
                        + "&" + URLEncoder.encode("newname", "UTF-8") + "=" + URLEncoder.encode(newname, "UTF-8")
                        + "&" + URLEncoder.encode("res_name", "UTF-8") + "=" + URLEncoder.encode(res_name, "UTF-8")
                        + "&" + URLEncoder.encode("role", "UTF-8") + "=" + URLEncoder.encode(role, "UTF-8")
                        + "&" + URLEncoder.encode("oldpass", "UTF-8") + "=" + URLEncoder.encode(oldpass, "UTF-8")
                        + "&" + URLEncoder.encode("newpass", "UTF-8") + "=" + URLEncoder.encode(newpass, "UTF-8");
                bufferedwritter.write(postdata);
                bufferedwritter.flush();
                bufferedwritter.close();
                outputstream.close();
                InputStream inputstream = httpURLConnection.getInputStream();
                BufferedReader bufferdreader = new BufferedReader(new InputStreamReader(inputstream, "iso-8859-1"));
                resul = "";
                String line = "";
                while ((line = bufferdreader.readLine()) != null) {
                    resul += line;
                }
                bufferdreader.close();
                inputstream.close();
                httpURLConnection.disconnect();
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
            editor.clear();
            editor.commit();
            Intent intent = new Intent(SuccessOrder.this, staff_login_resistor.class);
            startActivity(intent);
            finish();
        }
    }
    public void homepage(View view){
        if(resfood.equals("1")) {
            Intent intent = new Intent(this, DisplayFoodRestaurentList.class);
            intent.putExtra("food_name", food);
            startActivity(intent);
        }
        else
        {
            Intent intent = new Intent(this, DisplayRestaurentFoodList.class);
            intent.putExtra("restaurent_name", restaurent);
            startActivity(intent);
        }
        finish();
        //finishing
    }
    //creating activity for back pressing from phone
    public void onBackPressed() {
        if(resfood.equals("1")) {
            Intent intent = new Intent(this, DisplayFoodRestaurentList.class);
            intent.putExtra("food_name", food);
            startActivity(intent);
        }
        else
        {
            Intent intent = new Intent(this, DisplayRestaurentFoodList.class);
            intent.putExtra("restaurent_name", restaurent);
            startActivity(intent);
        }
        finish();
    }
}
