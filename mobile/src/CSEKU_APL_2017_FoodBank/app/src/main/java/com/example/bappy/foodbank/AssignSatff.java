package com.example.bappy.foodbank;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
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
import java.util.List;

import static com.example.bappy.foodbank.R.id.assignstaff;
import static com.example.bappy.foodbank.R.id.foodname;
import static com.example.bappy.foodbank.R.id.price;
import static com.example.bappy.foodbank.R.id.showall;

public class AssignSatff extends AppCompatActivity {

    String nameofst;
    TextView txt;
    String datet,resname,assignstaff;
    String name,role;
    JSONObject jsonObject,jsonObject2;
    JSONArray jsonArray,jsonArray2;
    ListView listView;
    StaffFoodAdapter staffFoodAdapter;
    FoodOrderListAdapter foodOrderListAdapter;

    ArrayList<StaffFoodAssign> addstafffood;

    ArrayList<FoodOrderListClass> addfoodorder;

    ArrayList<String> ass=new ArrayList<String>();
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    int show_all_value = 0;

    AlertDialog.Builder orderbuilder;
    AlertDialog mydialog;

    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assign_staff_layout);
        progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);

        sharedPreferences=getSharedPreferences(getString(R.string.PREF_FILE), 0);
        editor=sharedPreferences.edit();

        addstafffood=new ArrayList<StaffFoodAssign>();

        addfoodorder=new ArrayList<FoodOrderListClass>();

        name=getIntent().getExtras().getString("username");
        role=getIntent().getExtras().getString("role");
        datet=getIntent().getExtras().getString("datet");
        assignstaff=getIntent().getExtras().getString("assignst");
        resname=getIntent().getExtras().getString("resname");

        txt=(TextView)findViewById(R.id.txtviw);
        ass.add("None");
        txt.setText(name+"\n"+role+"\n"+datet);

        progressDialog.setMessage("Loading.Please Wait....");
        progressDialog.show();
        new BackgroundtaskOrderlist2().execute(resname);

        listView=(ListView)findViewById(R.id.foodstafflist);
        staffFoodAdapter=new StaffFoodAdapter(this,R.layout.assign_staff_list,addstafffood);
        listView.setAdapter(staffFoodAdapter);

        try {
            jsonObject2 = new JSONObject(assignstaff);
            jsonArray2 = jsonObject2.getJSONArray("Server_response");

            int coun = 0;
            String clientnam;
            while (coun < jsonArray2.length()) {
                JSONObject jo = jsonArray2.getJSONObject(coun);
                clientnam = jo.getString("name");
                ass.add(clientnam);
                coun++;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public class StaffFoodAdapter extends ArrayAdapter {
        ArrayList<StaffFoodAssign> list = new ArrayList();
        Context ct;

        public StaffFoodAdapter(@NonNull Context context, @LayoutRes int resource,ArrayList<StaffFoodAssign> string) {
            super(context, resource);
            ct = context;
            list=string;
        }

        @Override
        public void add(@Nullable Object object) {
            super.add(object);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Nullable
        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            nameofst="None";
            View stafffoodview;
            stafffoodview = convertView;
            final StaffFoodHolder staffFoodHolder;
            if (stafffoodview == null) {
                LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                stafffoodview = layoutInflater.inflate(R.layout.assign_staff_list, parent, false);
                staffFoodHolder = new StaffFoodHolder();
                staffFoodHolder.clientname = (TextView) stafffoodview.findViewById(R.id.sname);
                staffFoodHolder.orderdate = (TextView) stafffoodview.findViewById(R.id.sorderdate);
                staffFoodHolder.ispaid = (TextView) stafffoodview.findViewById(R.id.spaid);
                staffFoodHolder.phone = (TextView) stafffoodview.findViewById(R.id.snumber);
                staffFoodHolder.deliverydate = (TextView) stafffoodview.findViewById(R.id.sdeliverydate);
                staffFoodHolder.isdelivery = (TextView) stafffoodview.findViewById(R.id.sdeliverytype);
                staffFoodHolder.price = (TextView) stafffoodview.findViewById(R.id.sprice);
                staffFoodHolder.orderplace = (TextView) stafffoodview.findViewById(R.id.sorderplace);
                staffFoodHolder.spin=(Spinner)stafffoodview.findViewById(R.id.spinner3);
                staffFoodHolder.chefname = (TextView) stafffoodview.findViewById(R.id.chef);
                stafffoodview.setTag(staffFoodHolder);
            } else {
                staffFoodHolder = (StaffFoodHolder) stafffoodview.getTag();
            }

            final StaffFoodAssign staffFood = (StaffFoodAssign) this.getItem(position);
            staffFoodHolder.clientname.setText("Name: "+staffFood.getClientname());
            staffFoodHolder.orderdate.setText("Order Date: "+staffFood.getOrderdate());
            staffFoodHolder.ispaid.setText("Paid Status: "+staffFood.getIspaid());
            staffFoodHolder.phone.setText("Phone No: "+staffFood.getPhone());
            staffFoodHolder.deliverydate.setText("Delivaery  Date: "+staffFood.getDeliverydate());
            staffFoodHolder.isdelivery.setText("Delivery Status: "+staffFood.getIsdelivery());
            staffFoodHolder.price.setText("Price: "+staffFood.getPrice());
            staffFoodHolder.orderplace.setText("Place: "+staffFood.getOrderplace());
            staffFoodHolder.chefname.setText("Chef: "+staffFood.getChefname());

            staffFoodHolder.orderdate.setVisibility(View.GONE);
            staffFoodHolder.ispaid.setVisibility(View.GONE);
            staffFoodHolder.phone.setVisibility(View.GONE);
            staffFoodHolder.deliverydate.setVisibility(View.GONE);
            staffFoodHolder.isdelivery.setVisibility(View.GONE);
            staffFoodHolder.price.setVisibility(View.GONE);
            staffFoodHolder.orderplace.setVisibility(View.GONE);
            staffFoodHolder.chefname.setVisibility(View.GONE);
            ArrayAdapter arrayAdapter=new ArrayAdapter(AssignSatff.this,android.R.layout.simple_spinner_item,ass);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            staffFoodHolder.spin.setAdapter(arrayAdapter);

            staffFoodHolder.spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    TextView mytext=(TextView)view;
                    //Toast.makeText(getBaseContext(), "You Selected "+mytext.getText(), Toast.LENGTH_SHORT).show();
                    nameofst=(String)mytext.getText();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            stafffoodview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), "Clicked on " + staffFood.getClientname(), Toast.LENGTH_SHORT).show();
                }
            });

            Button butassign=(Button)stafffoodview.findViewById(R.id.Assign);
            butassign.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {if (nameofst.equals("None")) {
                        Toast.makeText(AssignSatff.this, "You select none", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        goassign(v,nameofst,staffFood.getClientname(),staffFood.getPrice(),staffFood.getPhone(),staffFood.getOrderplace());
                    }
                }
            });
            final Button show_all=(Button)stafffoodview.findViewById(showall);
            show_all.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(show_all_value==0) {
                        staffFoodHolder.orderdate.setVisibility(View.VISIBLE);
                        staffFoodHolder.ispaid.setVisibility(View.VISIBLE);
                        staffFoodHolder.phone.setVisibility(View.VISIBLE);
                        staffFoodHolder.deliverydate.setVisibility(View.VISIBLE);
                        staffFoodHolder.isdelivery.setVisibility(View.VISIBLE);
                        staffFoodHolder.price.setVisibility(View.VISIBLE);
                        staffFoodHolder.orderplace.setVisibility(View.VISIBLE);
                        staffFoodHolder.chefname.setVisibility(View.VISIBLE);
                        show_all.setText("Hide All");
                        show_all_value =1;
                    }
                    else
                    {
                        staffFoodHolder.orderdate.setVisibility(View.GONE);
                        staffFoodHolder.ispaid.setVisibility(View.GONE);
                        staffFoodHolder.phone.setVisibility(View.GONE);
                        staffFoodHolder.deliverydate.setVisibility(View.GONE);
                        staffFoodHolder.isdelivery.setVisibility(View.GONE);
                        staffFoodHolder.price.setVisibility(View.GONE);
                        staffFoodHolder.orderplace.setVisibility(View.GONE);
                        staffFoodHolder.chefname.setVisibility(View.GONE);
                        show_all.setText("Show All");
                        show_all_value =0;
                    }
                }
            });

            Button foodlist=(Button)stafffoodview.findViewById(R.id.foodlist);
            foodlist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    progressDialog.setMessage("Loading.Please Wait....");
                    progressDialog.show();
                    String id=staffFood.getClientid();
                    new FoodOrderList().execute(id);
                }
            });

            return stafffoodview;
        }

        class StaffFoodHolder {
            TextView clientname, orderdate, ispaid, phone, deliverydate, isdelivery, price, orderplace,chefname;
            Spinner spin;
        }
    }

    public class FoodOrderList extends AsyncTask<String,Void,Boolean>
    {

        String json_url;
        String JSON_STRING;

        @Override
        protected void onPreExecute() {
            json_url="http://"+getString(R.string.ip_address)+"/FoodBank/FoodOrderList.php";
        }

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                String clientid=params[0];
                URL url=new URL(json_url);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputstream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedwritter = new BufferedWriter(new OutputStreamWriter(outputstream, "UTF-8"));
                String postdata = URLEncoder.encode("clientid", "UTF-8") + "=" + URLEncoder.encode(clientid, "UTF-8");
                bufferedwritter.write(postdata);
                bufferedwritter.flush();
                bufferedwritter.close();
                outputstream.close();
                InputStream inputStream=httpURLConnection.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder=new StringBuilder();
                while((JSON_STRING=bufferedReader.readLine())!=null){
                    stringBuilder.append(JSON_STRING+"\n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                String OrderList = stringBuilder.toString().trim();

                jsonObject = new JSONObject(OrderList);
                jsonArray = jsonObject.getJSONArray("Server_response");

                int count = 0;
                String foodname,quantity,price;
                while (count < jsonArray.length()) {
                    JSONObject jo = jsonArray.getJSONObject(count);
                    foodname=jo.getString("foodname");
                    quantity = jo.getString("quantity");
                    price = jo.getString("price");
                    FoodOrderListClass foodOrderListClass=new FoodOrderListClass(foodname,quantity,price);
                    addfoodorder.add(foodOrderListClass);
                    count++;
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
            if(result) {
                progressDialog.cancel();
                ListView listViewOrder = new ListView(AssignSatff.this);
                foodOrderListAdapter = new FoodOrderListAdapter(AssignSatff.this, R.layout.food_order_list_layout, addfoodorder);
                listViewOrder.setAdapter(foodOrderListAdapter);
                //Toast.makeText(this, "cart ok", Toast.LENGTH_SHORT).show();
                orderbuilder = new AlertDialog.Builder(AssignSatff.this);
                orderbuilder.setCancelable(true);
                orderbuilder.setTitle("Order List");
                if (addfoodorder.isEmpty())
                    orderbuilder.setMessage("it can't read any read any item");
                else
                    orderbuilder.setView(listViewOrder);

                orderbuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        addfoodorder.clear();
                        dialog.cancel();
                    }
                });
                //alertdialog create
                mydialog = orderbuilder.create();
                //for working the alertdialog state
                mydialog.show();
            }
            else
                Toast.makeText(AssignSatff.this, "Failed", Toast.LENGTH_SHORT).show();

        }
    }

    public class FoodOrderListClass{
        String foodname,quantity,price;

        public FoodOrderListClass(String foodname, String quantity, String price) {
            this.foodname = foodname;
            this.quantity = quantity;
            this.price = price;
        }

        public String getFoodname() {
            return foodname;
        }

        public void setFoodname(String foodname) {
            this.foodname = foodname;
        }

        public String getQuantity() {
            return quantity;
        }

        public void setQuantity(String quantity) {
            this.quantity = quantity;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }
    }

    public class FoodOrderListAdapter extends ArrayAdapter {
        ArrayList<FoodOrderListClass> list = new ArrayList();
        Context ct;

        public FoodOrderListAdapter(@NonNull Context context, @LayoutRes int resource,ArrayList<FoodOrderListClass> string) {
            super(context, resource);
            ct = context;
            list=string;
        }

        @Override
        public void add(@Nullable Object object) {
            super.add(object);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Nullable
        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            View foodorderlistview;
            foodorderlistview = convertView;
            final FoodOrderHolder foodOrderHolder;
            if (foodorderlistview == null) {
                LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                foodorderlistview = layoutInflater.inflate(R.layout.food_order_list_layout, parent, false);
                foodOrderHolder = new FoodOrderHolder();
                foodOrderHolder.foodname = (TextView) foodorderlistview.findViewById(R.id.foodnameorder);
                foodOrderHolder.quantity = (TextView) foodorderlistview.findViewById(R.id.quantityorder);
                foodOrderHolder.price = (TextView) foodorderlistview.findViewById(R.id.priceorder);
                foodorderlistview.setTag(foodOrderHolder);
            } else {
                foodOrderHolder = (FoodOrderHolder) foodorderlistview.getTag();
            }

            final FoodOrderListClass foodOrderListClass = (FoodOrderListClass) this.getItem(position);
            foodOrderHolder.foodname.setText("Food Name: "+foodOrderListClass.getFoodname());
            foodOrderHolder.quantity.setText("Quantity: "+foodOrderListClass.getQuantity());
            foodOrderHolder.price.setText("Price: "+foodOrderListClass.getPrice());

            return foodorderlistview;
        }

        class FoodOrderHolder {
            TextView foodname,quantity,price;
        }
    }

    public class StaffFoodAssign {

        String clientid,clientname,orderdate,ispaid,phone,deliverydate,isdelivery,price,orderplace,chefname;

        public StaffFoodAssign(String clientid, String clientname, String orderdate, String ispaid, String phone, String deliverydate, String isdelivery, String price, String orderplace, String chefname) {
            this.clientid = clientid;
            this.clientname = clientname;
            this.orderdate = orderdate;
            this.ispaid = ispaid;
            this.phone = phone;
            this.deliverydate = deliverydate;
            this.isdelivery = isdelivery;
            this.price = price;
            this.orderplace = orderplace;
            this.chefname = chefname;
        }

        public String getChefname() {
            return chefname;
        }

        public void setChefname(String chefname) {
            this.chefname = chefname;
        }

        public String getClientid() {
            return clientid;
        }

        public void setClientid(String clientid) {
            this.clientid = clientid;
        }

        public String getClientname() {
            return clientname;
        }

        public void setClientname(String clientname) {
            this.clientname = clientname;
        }

        public String getOrderdate() {
            return orderdate;
        }

        public void setOrderdate(String orderdate) {
            this.orderdate = orderdate;
        }

        public String getIspaid() {
            return ispaid;
        }

        public void setIspaid(String ispaid) {
            this.ispaid = ispaid;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getDeliverydate() {
            return deliverydate;
        }

        public void setDeliverydate(String deliverydate) {
            this.deliverydate = deliverydate;
        }

        public String getIsdelivery() {
            return isdelivery;
        }

        public void setIsdelivery(String isdelivery) {
            this.isdelivery = isdelivery;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getOrderplace() {
            return orderplace;
        }

        public void setOrderplace(String orderplace) {
            this.orderplace = orderplace;
        }
    }

    public class BackgroundtaskOrderlist2 extends AsyncTask<String,Void,Boolean>
    {

        String json_url;
        String JSON_STRING;

        @Override
        protected void onPreExecute() {
            json_url="http://"+getString(R.string.ip_address)+"/FoodBank/NonStaffOrder.php";
        }

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                String res=params[0];
                URL url=new URL(json_url);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputstream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedwritter = new BufferedWriter(new OutputStreamWriter(outputstream, "UTF-8"));
                String postdata = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(res, "UTF-8")+ "&" +
                                URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode("staff", "UTF-8");
                bufferedwritter.write(postdata);
                bufferedwritter.flush();
                bufferedwritter.close();
                outputstream.close();
                InputStream inputStream=httpURLConnection.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder=new StringBuilder();
                while((JSON_STRING=bufferedReader.readLine())!=null){
                    stringBuilder.append(JSON_STRING+"\n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                    String foodorder = stringBuilder.toString().trim();

                    jsonObject = new JSONObject(foodorder);
                    jsonArray = jsonObject.getJSONArray("Server_response");

                    int count = 0;
                    String clienid,clientname, orderdate, ispaid, phone, deliverydate, isdelivery, price, orderfrom,chefname;
                    while (count < jsonArray.length()) {
                        JSONObject jo = jsonArray.getJSONObject(count);
                        clienid=jo.getString("clientid");
                        clientname = jo.getString("clientname");
                        orderdate = jo.getString("orderdate");
                        ispaid = jo.getString("ispaid");
                        phone = jo.getString("phonenumber");
                        deliverydate = jo.getString("deliverydate");
                        isdelivery = jo.getString("isdelivery");
                        price = jo.getString("price");
                        orderfrom = jo.getString("orderplace");
                        chefname = jo.getString("name");
                        StaffFoodAssign staffFood = new StaffFoodAssign(clienid,clientname,orderdate, ispaid, phone, deliverydate, isdelivery, price, orderfrom,chefname);
                        addstafffood.add(staffFood);
                        count++;
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
            {
                progressDialog.cancel();
                staffFoodAdapter.notifyDataSetChanged();
            }
            else
                Toast.makeText(AssignSatff.this, "can't connect to the database", Toast.LENGTH_SHORT).show();
        }
    }

    public void goassign(View v,String staff,String username,String price,String phone,String address){
        if(!isNetworkAvilabe())
            nointernet();
        else {
            progressDialog.setMessage("Assigning.Please Wait....");
            progressDialog.show();
            new BackgroundTask2().execute(username, price, phone, address, staff);
        }
    }

    class BackgroundTask2 extends AsyncTask<String,Void,Boolean>
    {
        AlertDialog.Builder alert;
        String json_url;
        String JSON_STRING,st;

        @Override
        protected void onPreExecute() {
            alert = new AlertDialog.Builder(AssignSatff.this);
            alert.setTitle("Assign Staff Status");
            json_url="http://"+getString(R.string.ip_address)+"/FoodBank/AssignStaffChef.php";
        }

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                String username = params[0];
                String price = params[1];
                String phone=params[2];
                String address=params[3];
                String staff=params[4];
                URL url = new URL(json_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputstream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedwritter = new BufferedWriter(new OutputStreamWriter(outputstream, "UTF-8"));
                String postdata = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8")
                        + "&" + URLEncoder.encode("price", "UTF-8") + "=" + URLEncoder.encode(price, "UTF-8")
                        + "&" + URLEncoder.encode("phone", "UTF-8") + "=" + URLEncoder.encode(phone, "UTF-8")
                        + "&" + URLEncoder.encode("address", "UTF-8") + "=" + URLEncoder.encode(address, "UTF-8")
                        + "&" + URLEncoder.encode("staff", "UTF-8") + "=" + URLEncoder.encode(staff, "UTF-8")
                        + "&" + URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode("staff", "UTF-8");
                bufferedwritter.write(postdata);
                bufferedwritter.flush();
                bufferedwritter.close();
                outputstream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                while ((JSON_STRING = bufferedReader.readLine()) != null) {
                    stringBuilder.append(JSON_STRING + "\n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                st= stringBuilder.toString().trim();
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
                Toast.makeText(AssignSatff.this, "can't connect to the database", Toast.LENGTH_SHORT).show();
            else {
                alert.setMessage(st);
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = getIntent();
                        progressDialog.cancel();
                        startActivity(intent);
                        finish();
                    }
                });
                AlertDialog mydialog = alert.create();
                mydialog.show();
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
        AlertDialog.Builder CheckBuild = new AlertDialog.Builder(AssignSatff.this);
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
                            startActivity(new Intent(AssignSatff.this, staff_login_resistor.class));
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
                            startActivity(new Intent(AssignSatff.this, ShowProfile.class));
                        }
                    };
                    Handler handler3 = new Handler();
                    handler3.postDelayed(progressrunnable3, 3500);
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
                            startActivity(new Intent(AssignSatff.this, CreateNewRestaurant.class));
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
                            Intent intent = new Intent(AssignSatff.this, EditChangeProfile.class);
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
    @Override
    public void onBackPressed() {
        Intent intent=new Intent(AssignSatff.this,Adminstaff.class);
        intent.putExtra("username", name);
        intent.putExtra("resname", resname);
        intent.putExtra("role", role);
        startActivity(intent);
        finish();
    }

}

