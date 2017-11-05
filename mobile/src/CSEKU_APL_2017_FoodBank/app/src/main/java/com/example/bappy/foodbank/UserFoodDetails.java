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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
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

import static android.R.attr.id;
import static com.example.bappy.foodbank.R.id.orderlist;
import static com.example.bappy.foodbank.R.id.showall;

public class UserFoodDetails extends AppCompatActivity {

    ListView userorderlist;
    UserOrderAdapter userOrderAdapter;
    ArrayList<UserOrder> adduserorder;

    int show_all_value = 0;

    JSONObject jsonObject;
    JSONArray jsonArray;
    ArrayList<FoodOrderListClass> addfoodorder;

    FoodOrderListAdapter foodOrderListAdapter;
    AlertDialog.Builder orderbuilder;
    AlertDialog mydialog;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Boolean save_login;
    String name,resname,pass,type;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_food_details_layout);

        progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);

        sharedPreferences=getSharedPreferences(getString(R.string.PREF_FILE), 0);
        editor=sharedPreferences.edit();
        save_login=sharedPreferences.getBoolean(getString(R.string.SAVE_LOGIN),false);

        type=sharedPreferences.getString(getString(R.string.TYPE),"None");
        name=sharedPreferences.getString(getString(R.string.NAME),"None");
        pass=sharedPreferences.getString(getString(R.string.PASSWORD),"None");
        resname=sharedPreferences.getString(getString(R.string.RESTAURANT_NAME),"None");

        adduserorder=new ArrayList<UserOrder>();
        userorderlist=(ListView)findViewById(R.id.userfood);

        progressDialog.setMessage("Loading.Please Wait....");
        progressDialog.show();
        new BackgroundTaskUserOrder().execute(name);

        addfoodorder=new ArrayList<FoodOrderListClass>();
        userOrderAdapter=new UserOrderAdapter(this,R.layout.user_order_cart_layout,adduserorder);
        userorderlist.setAdapter(userOrderAdapter);
    }

    public class BackgroundTaskUserOrder extends AsyncTask<String,Void,Boolean>
    {

        String json_url;
        String JSON_STRING;
        String OrderList;

        @Override
        protected void onPreExecute() {
            json_url="http://"+getString(R.string.ip_address)+"/FoodBank/UserFoodOrderEditShow.php";
        }

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                String clientname=params[0];
                URL url=new URL(json_url);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputstream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedwritter = new BufferedWriter(new OutputStreamWriter(outputstream, "UTF-8"));
                String postdata = URLEncoder.encode("clientname", "UTF-8") + "=" + URLEncoder.encode(clientname, "UTF-8");
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
                OrderList = stringBuilder.toString().trim();

                jsonObject = new JSONObject(OrderList);
                jsonArray = jsonObject.getJSONArray("Server_response");

                int count = 0;
                String number,clientid,client,orderdate,ispaid,phonenumber,deliverydate,isdelivery,price,orderplace,condition;
                while (count < jsonArray.length()) {
                    JSONObject jo = jsonArray.getJSONObject(count);
                    number=jo.getString("number");
                    clientid = jo.getString("clientid");
                    client = jo.getString("clientname");
                    orderdate=jo.getString("orderdate");
                    ispaid = jo.getString("ispaid");
                    phonenumber = jo.getString("phonenumber");
                    deliverydate=jo.getString("deliverydate");
                    isdelivery = jo.getString("isdelivery");
                    price = jo.getString("price");
                    orderplace=jo.getString("orderplace");
                    condition=jo.getString("condition");
                    UserOrder userOrder=new UserOrder(number,clientid,client,orderdate,ispaid,phonenumber,deliverydate,isdelivery,price,orderplace,condition);
                    adduserorder.add(userOrder);
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
           // Toast.makeText(UserFoodDetails.this, OrderList, Toast.LENGTH_SHORT).show();
            userOrderAdapter.notifyDataSetChanged();
            progressDialog.cancel();
        }
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
                if(!isNetworkAvilabe())
                    nointernet();
                else {
                    editor.clear();
                    editor.commit();
                    editor.putBoolean(getString(R.string.SKIP),false);
                    progressDialog.setMessage("Logging Out.Please Wait....");
                    progressDialog.show();
                    Runnable progressrunnable = new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.cancel();
                            startActivity(new Intent(UserFoodDetails.this, staff_login_resistor.class));
                            finish();
                        }
                    };

                    Handler handler = new Handler();
                    handler.postDelayed(progressrunnable, 3500);
                }
                return true;
            case R.id.LogIn:
                if(!isNetworkAvilabe())
                    nointernet();
                else {
                    progressDialog.setMessage("Loading.Please Wait....");
                    progressDialog.show();
                    Runnable progressrunnable2 = new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.cancel();
                            startActivity(new Intent(UserFoodDetails.this, staff_login_resistor.class));
                            finish();
                        }
                    };
                    Handler handler2 = new Handler();
                    handler2.postDelayed(progressrunnable2, 3500);
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
                            startActivity(new Intent(UserFoodDetails.this, ShowProfile.class));
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
                            startActivity(new Intent(UserFoodDetails.this, CreateNewRestaurant.class));
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
                            Intent intent = new Intent(UserFoodDetails.this, EditChangeProfile.class);
                            intent.putExtra("op_type", "Edit");
                            startActivity(intent);
                        }
                    };
                    Handler handler5 = new Handler();
                    handler5.postDelayed(progressrunnable5, 3500);
                }
                return true;
            case R.id.delete_profile:
                if(!isNetworkAvilabe())
                    nointernet();
                else {
                    AlertDialog.Builder alert = new AlertDialog.Builder(this);
                    alert.setTitle("Attention");
                    alert.setMessage("Are You Sure??");
                    alert.setCancelable(true);
                    alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new BackgroundTask3().execute("Delete", name, name, resname, type, pass, pass);
                        }
                    });
                    alert.setNegativeButton("No,later", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog al = alert.create();
                    al.show();
                }
                return true;
            case R.id.myorder:
                Toast.makeText(this, "You are Already in it", Toast.LENGTH_SHORT).show();
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

    class BackgroundTask3 extends AsyncTask<String,Void,Boolean> {
        String json_url;
        String JSON_STRING;
        String newpass, newname, op_type, resul, res_name, role;

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Performing.Please Wait....");
            progressDialog.show();
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
            if(result){
                editor.clear();
                editor.commit();
                Intent intent = new Intent(UserFoodDetails.this, staff_login_resistor.class);
                progressDialog.cancel();
                startActivity(intent);
                finish();
            }
            else
                Toast.makeText(UserFoodDetails.this, "Failed", Toast.LENGTH_SHORT).show();
        }
    }

    public class UserOrderAdapter extends ArrayAdapter{

        ArrayList<UserOrder> list;

        public UserOrderAdapter(@NonNull Context context, @LayoutRes int resource,ArrayList<UserOrder> lis) {
            super(context, resource);
            list=lis;
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
            View userorderView;
            userorderView=convertView;
            final UserOrdereHolder userOrdereHolder;
            if(userorderView==null)
            {
                LayoutInflater inflater=(LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                userorderView=inflater.inflate(R.layout.user_order_cart_layout,parent,false);
                userOrdereHolder=new UserOrdereHolder();
                userOrdereHolder.number=(TextView)userorderView.findViewById(R.id.number);
                userOrdereHolder.orderdate=(TextView)userorderView.findViewById(R.id.sorderdate);
                userOrdereHolder.ispaid=(TextView)userorderView.findViewById(R.id.spaid);
                userOrdereHolder.phonenumber=(TextView)userorderView.findViewById(R.id.snumber);
                userOrdereHolder.deliverydate=(TextView)userorderView.findViewById(R.id.sdeliverydate);
                userOrdereHolder.isdelivery=(TextView)userorderView.findViewById(R.id.sdeliverytype);
                userOrdereHolder.price=(TextView)userorderView.findViewById(R.id.sprice);
                userOrdereHolder.orderplace=(TextView)userorderView.findViewById(R.id.sorderplace);
                userOrdereHolder.condition=(TextView)userorderView.findViewById(R.id.condition);
                userorderView.setTag(userOrdereHolder);
            }
            else {
                userOrdereHolder = (UserOrdereHolder) userorderView.getTag();
            }
            final UserOrder userOrder = (UserOrder) this.getItem(position);
            userOrdereHolder.number.setText(userOrder.getNumber());
            userOrdereHolder.orderdate.setText("Order Date: "+userOrder.getOrderdate());
            userOrdereHolder.ispaid.setText(userOrder.getIspaid());
            userOrdereHolder.phonenumber.setText("Phone: "+userOrder.getPhonenumber());
            userOrdereHolder.deliverydate.setText("Delivery Date: "+userOrder.getDeliverydate());
            userOrdereHolder.isdelivery.setText(userOrder.getIsdelivery());
            userOrdereHolder.price.setText("Price: "+userOrder.getPrice());
            userOrdereHolder.orderplace.setText("From :"+userOrder.getOrderplace());
            userOrdereHolder.condition.setText(userOrder.getCondition());

            userOrdereHolder.orderdate.setVisibility(View.GONE);
            userOrdereHolder.ispaid.setVisibility(View.GONE);
            userOrdereHolder.phonenumber.setVisibility(View.GONE);
            userOrdereHolder.isdelivery.setVisibility(View.GONE);
            userOrdereHolder.price.setVisibility(View.GONE);
            userOrdereHolder.orderplace.setVisibility(View.GONE);

            final Button show_all=(Button)userorderView.findViewById(showall);
            show_all.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(show_all_value==0) {
                        userOrdereHolder.orderdate.setVisibility(View.VISIBLE);
                        userOrdereHolder.ispaid.setVisibility(View.VISIBLE);
                        userOrdereHolder.phonenumber.setVisibility(View.VISIBLE);
                        userOrdereHolder.isdelivery.setVisibility(View.VISIBLE);
                        userOrdereHolder.price.setVisibility(View.VISIBLE);
                        userOrdereHolder.orderplace.setVisibility(View.VISIBLE);
                        show_all.setText("Hide All");
                        show_all_value =1;
                    }
                    else
                    {
                        userOrdereHolder.orderdate.setVisibility(View.GONE);
                        userOrdereHolder.ispaid.setVisibility(View.GONE);
                        userOrdereHolder.phonenumber.setVisibility(View.GONE);
                        userOrdereHolder.isdelivery.setVisibility(View.GONE);
                        userOrdereHolder.price.setVisibility(View.GONE);
                        userOrdereHolder.orderplace.setVisibility(View.GONE);
                        show_all.setText("Show All");
                        show_all_value =0;
                    }
                }
            });

            Button foodlist=(Button)userorderView.findViewById(R.id.foodlist);
            foodlist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    progressDialog.setMessage("Loading.Please Wait....");
                    progressDialog.show();
                    String id=userOrder.getClientid();
                    new FoodOrderList().execute(id);
                }
            });
            return userorderView;
        }

        class UserOrdereHolder
        {
            TextView number,orderdate,ispaid,phonenumber,deliverydate,isdelivery,price,orderplace,condition;
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
            progressDialog.cancel();
            if(result) {
                ListView listViewOrder = new ListView(UserFoodDetails.this);
                foodOrderListAdapter = new FoodOrderListAdapter(UserFoodDetails.this, R.layout.food_order_list_layout, addfoodorder);
                listViewOrder.setAdapter(foodOrderListAdapter);
                //Toast.makeText(this, "cart ok", Toast.LENGTH_SHORT).show();
                orderbuilder = new AlertDialog.Builder(UserFoodDetails.this);
                orderbuilder.setCancelable(true);
                orderbuilder.setTitle("Order List");
                if (addfoodorder.isEmpty())
                    orderbuilder.setMessage("it can't read any item");
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
                Toast.makeText(UserFoodDetails.this, "Failed", Toast.LENGTH_SHORT).show();

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

    public class UserOrder{
        String number,clientid,clientname,orderdate,ispaid,phonenumber,deliverydate,isdelivery,price,orderplace,condition;

        public UserOrder(String number, String clientid, String clientname, String orderdate, String ispaid, String phonenumber, String deliverydate, String isdelivery, String price, String orderplace, String condition) {
            this.number = number;
            this.clientid = clientid;
            this.clientname = clientname;
            this.orderdate = orderdate;
            this.ispaid = ispaid;
            this.phonenumber = phonenumber;
            this.deliverydate = deliverydate;
            this.isdelivery = isdelivery;
            this.price = price;
            this.orderplace = orderplace;
            this.condition = condition;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
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

        public String getPhonenumber() {
            return phonenumber;
        }

        public void setPhonenumber(String phonenumber) {
            this.phonenumber = phonenumber;
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

        public String getCondition() {
            return condition;
        }

        public void setCondition(String condition) {
            this.condition = condition;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private boolean isNetworkAvilabe()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    public void nointernet() {
        //Creating an Alertdialog
        AlertDialog.Builder CheckBuild = new AlertDialog.Builder(UserFoodDetails.this);
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
