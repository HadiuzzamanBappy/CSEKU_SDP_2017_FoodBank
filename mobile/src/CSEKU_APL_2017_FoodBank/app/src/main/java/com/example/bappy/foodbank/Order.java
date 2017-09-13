package com.example.bappy.foodbank;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.IntegerRes;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class Order extends Activity{
    String orestaurant,ofoodname,oprice,quantity,deliverytype="",dd,mm,yyyy,resfood;
    TextView trestaurent,tfoodname,tprice;
    RadioGroup rg;
    RadioButton rb;
    EditText clientname,phonenumber,address;
    int fullprice,quantityy;
    Spinner spinner,spinnerday,spinnermonth,spinneryear;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Boolean save_login;
    String name,resname,pass,type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order2);
        sharedPreferences=getSharedPreferences(getString(R.string.PREF_FILE), 0);
        editor=sharedPreferences.edit();
        save_login=sharedPreferences.getBoolean(getString(R.string.SAVE_LOGIN),false);
        type=sharedPreferences.getString(getString(R.string.TYPE),"None");
        name=sharedPreferences.getString(getString(R.string.NAME),"None");
        pass=sharedPreferences.getString(getString(R.string.PASSWORD),"None");
        resname=sharedPreferences.getString(getString(R.string.RESTAURANT_NAME),"None");

        orestaurant=getIntent().getExtras().getString("restaurant");
        ofoodname = getIntent().getExtras().getString("food");
        oprice = getIntent().getExtras().getString("price");
        resfood = getIntent().getExtras().getString("resfood");

        trestaurent=(TextView)findViewById(R.id.restaurant);
        tfoodname=(TextView)findViewById(R.id.foodname);
        tprice=(TextView)findViewById(R.id.price);
        trestaurent.setText(orestaurant);
        tfoodname.setText(ofoodname);
        tprice.setText(oprice);

        clientname=(EditText)findViewById(R.id.cname);
        phonenumber=(EditText)findViewById(R.id.cphone);
        address=(EditText)findViewById(R.id.caddress);

        rg=(RadioGroup)findViewById(R.id.radiogroup);
        spinner=(Spinner)findViewById(R.id.spinner);
        spinnerday=(Spinner)findViewById(R.id.spinnerday);
        spinnermonth=(Spinner)findViewById(R.id.spinnermonth);
        spinneryear=(Spinner)findViewById(R.id.spinneryear);

        ArrayAdapter arrayAdapter=ArrayAdapter.createFromResource(this,R.array.quantity,android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView mytext=(TextView)view;
                quantity=(String)mytext.getText();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter arrayAdapter2=ArrayAdapter.createFromResource(this,R.array.day,android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerday.setAdapter(arrayAdapter2);
        spinnerday.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView mytext=(TextView)view;
                dd=(String)mytext.getText();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter arrayAdapter3=ArrayAdapter.createFromResource(this,R.array.month,android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnermonth.setAdapter(arrayAdapter3);
        spinnermonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView mytext=(TextView)view;
                mm=(String)mytext.getText();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter arrayAdapter4=ArrayAdapter.createFromResource(this,R.array.year,android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinneryear.setAdapter(arrayAdapter4);
        spinneryear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView mytext=(TextView)view;
                yyyy=(String)mytext.getText();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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
            Intent intent = new Intent(Order.this, staff_login_resistor.class);
            startActivity(intent);
            finish();
        }
    }
    public void rbclick(View v){
        int radiobuttonid=rg.getCheckedRadioButtonId();
        rb=(RadioButton)findViewById(radiobuttonid);
        if(rb.getText().equals("Yes")){
            deliverytype="YES";
        }
        else
            deliverytype="NO";
    }

    public void order(View view){
        String client=clientname.getText().toString();
        String phone=phonenumber.getText().toString();
        String d=dd;
        String m=mm;
        String y=yyyy;
        String quan=quantity;
        String addrs=address.getText().toString();
        String date=y+"-"+m+"-"+d;
        String type ="order";
        if(client.equals("") || phone.equals("") || addrs.equals(""))
            Toast.makeText(this, "Please Fill All The Field", Toast.LENGTH_SHORT).show();
        else
            new OrderBackground().execute(type,client,phone,date,quan,addrs,deliverytype);
    }

    public class OrderBackground extends AsyncTask<String,Void,Boolean> {

        String client,phone,date,quan,addrs,delitype;

        @Override
        protected Boolean doInBackground(String... params) {
            String  type= params[0];
            String loginurl = "http://"+getString(R.string.ip_address)+"/FoodBank/OrderDetails.php";
            if (type.equals("order")) {
                try {
                    String clientname = params[1];
                    client = clientname;
                    String phonenumber = params[2];
                    phone = phonenumber;
                    String datetime = params[3];
                    date = datetime;
                    String quantity = params[4];
                    quan = quantity;
                    String address = params[5];
                    addrs=address;
                    String delivery = params[6];
                    delitype=delivery;
                    quantityy=Integer.parseInt(quan);
                    fullprice=Integer.parseInt(oprice);
                    fullprice=(fullprice*quantityy);
                    oprice= Integer.toString(fullprice);
                    if (clientname.equals("") || phonenumber.equals("") || datetime.equals("") || quantity.equals("") || address.equals("") ||delivery.equals("")) {
                        Toast.makeText(Order.this, "please fill all the field", Toast.LENGTH_SHORT).show();
                    }
                    else {
                    URL url = new URL(loginurl);
                    HttpURLConnection httpurlconnection = (HttpURLConnection) url.openConnection();
                    httpurlconnection.setRequestMethod("POST");
                    httpurlconnection.setDoOutput(true);
                    httpurlconnection.setDoInput(true);
                    OutputStream outputstream = httpurlconnection.getOutputStream();
                    BufferedWriter bufferedwritter = new BufferedWriter(new OutputStreamWriter(outputstream, "UTF-8"));
                    String postdata = URLEncoder.encode("clientname", "UTF-8") + "=" + URLEncoder.encode(clientname, "UTF-8") + "&"
                            + URLEncoder.encode("phonenumber", "UTF-8") + "=" + URLEncoder.encode(phonenumber, "UTF-8") + "&"
                            + URLEncoder.encode("datetime", "UTF-8") + "=" + URLEncoder.encode(datetime, "UTF-8") + "&"
                            + URLEncoder.encode("quantity", "UTF-8") + "=" + URLEncoder.encode(quantity, "UTF-8") + "&"
                            + URLEncoder.encode("address", "UTF-8") + "=" + URLEncoder.encode(address, "UTF-8") + "&"
                            + URLEncoder.encode("delivery", "UTF-8") + "=" + URLEncoder.encode(delivery, "UTF-8") + "&"
                            + URLEncoder.encode("restaurant", "UTF-8") + "=" + URLEncoder.encode(orestaurant, "UTF-8") + "&"
                            + URLEncoder.encode("price", "UTF-8") + "=" + URLEncoder.encode(oprice, "UTF-8") + "&"
                            + URLEncoder.encode("food", "UTF-8") + "=" + URLEncoder.encode(ofoodname, "UTF-8");
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
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                Intent intent=new Intent(Order.this,SuccessOrder.class);
                intent.putExtra("client",client);
                intent.putExtra("restaurant",orestaurant);
                intent.putExtra("foodname",ofoodname);
                intent.putExtra("phone",phone);
                intent.putExtra("date",date);
                intent.putExtra("quan",quan);
                intent.putExtra("addrs",addrs);
                intent.putExtra("delitype",delitype);
                intent.putExtra("price",oprice);
                intent.putExtra("resfood", resfood);
                startActivity(intent);
                finish();
            } else {
                if(result.equals("false"))
                    Toast.makeText(Order.this, "can't connect to the database", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onProgressUpdate (Void...values){
            super.onProgressUpdate(values);
        }
    }
    //creating activity for back pressing from phone
    public void onBackPressed() {
        if(resfood.equals("1")) {
            Intent intent = new Intent(Order.this, DisplayFoodRestaurentList.class);
            intent.putExtra("food_name", ofoodname);
            startActivity(intent);
        }
        else
        {
            Intent intent = new Intent(Order.this, DisplayRestaurentFoodList.class);
            intent.putExtra("restaurent_name", orestaurant);
            startActivity(intent);
        }
        finish();
    }
}
