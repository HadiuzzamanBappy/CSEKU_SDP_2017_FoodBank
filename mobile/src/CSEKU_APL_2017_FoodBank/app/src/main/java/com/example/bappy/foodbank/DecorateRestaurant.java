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
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

public class DecorateRestaurant extends AppCompatActivity {

    String resname,json_food_string,name,role;

    String pressname,presstype,pressprice;

    JSONObject jsonObject;
    JSONArray jsonArray;
    RestaurentFoodAdapter restaurentfoodAdapter;
    ListView listView;
    ArrayList<RestaurentFood> addrestaurantfood;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.decorate_restaurant_layout);
        progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);

        sharedPreferences=getSharedPreferences(getString(R.string.PREF_FILE), 0);
        editor=sharedPreferences.edit();

        name=getIntent().getExtras().getString("username");
        resname=getIntent().getExtras().getString("resname");
        role=getIntent().getExtras().getString("role");

        addrestaurantfood=new ArrayList<>();

        progressDialog.setMessage("Loading.Please Wait....");
        progressDialog.show();
        new DecorateRestaurant.BackgroundTask3().execute(resname);

        listView = (ListView) findViewById(R.id.foodview);
        restaurentfoodAdapter = new RestaurentFoodAdapter(this, R.layout.restaurent_food_layout,addrestaurantfood);
        listView.setAdapter(restaurentfoodAdapter);

        registerForContextMenu(listView);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){

        switch(item.getItemId()) {
            case R.id.id_edit:
                if(!isNetworkAvilabe())
                    nointernet();
                else {
                    progressDialog.setMessage("Please Wait....");
                    progressDialog.show();
                    Runnable progressrunnable = new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.cancel();
                            Intent intent = new Intent(DecorateRestaurant.this, EditActivity.class);
                            intent.putExtra("username", name);
                            intent.putExtra("resname", resname);
                            intent.putExtra("role", role);
                            intent.putExtra("name", pressname);
                            intent.putExtra("type", presstype);
                            intent.putExtra("price", pressprice);
                            startActivity(intent);
                            finish();
                        }
                    };

                    Handler handler = new Handler();
                    handler.postDelayed(progressrunnable, 3500);
                }
                return true;
            case R.id.id_delete:
                AlertDialog.Builder paidbuilder = new AlertDialog.Builder(DecorateRestaurant.this);
                //setting the alertdialog title
                paidbuilder.setTitle("Attention");
                //setting the body message
                paidbuilder.setMessage("Do You Want To Delete it?");
                //set state for cancelling state
                paidbuilder.setCancelable(true);

                //setting activity for positive state button
                paidbuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(!isNetworkAvilabe())
                            nointernet();
                        else {
                            progressDialog.setMessage("Deleting.Please Wait....");
                            progressDialog.show();
                            new BackgroundTask2().execute("Delete");
                        }
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
                return true;
            default:
                return DecorateRestaurant.super.onContextItemSelected(item);
        }
    }

    class BackgroundTask2 extends AsyncTask<String,Void,String> {
        AlertDialog.Builder alert;
        String json_url;
        String JSON_STRING;

        @Override
        protected void onPreExecute() {
            alert = new AlertDialog.Builder(DecorateRestaurant.this);
            alert.setTitle("Delete Status");
            json_url = "http://" + getString(R.string.ip_address) + "/FoodBank/FoodEditDelete.php";
        }

        @Override
        protected String doInBackground(String... params) {
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
                        + "&" + URLEncoder.encode("resname", "UTF-8") + "=" + URLEncoder.encode(resname, "UTF-8")
                        + "&" + URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(pressname, "UTF-8")
                        + "&" + URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode(presstype, "UTF-8")
                        + "&" + URLEncoder.encode("price", "UTF-8") + "=" + URLEncoder.encode(pressprice, "UTF-8")
                        + "&" + URLEncoder.encode("prevname", "UTF-8") + "=" + URLEncoder.encode(pressname, "UTF-8");
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
                return stringBuilder.toString().trim();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            Intent intent = getIntent();
            progressDialog.cancel();
            startActivity(intent);
            finish();
        }
    }

    public void addfood(View view){
        if(!isNetworkAvilabe())
            nointernet();
        else {
            Intent intent = new Intent(DecorateRestaurant.this, AddFood.class);
            intent.putExtra("username", name);
            intent.putExtra("resname", resname);
            intent.putExtra("role", role);
            startActivity(intent);
            finish();
        }
    }


    public class RestaurentFoodAdapter extends ArrayAdapter {
        Context ct;
        ArrayList<RestaurentFood> list=new ArrayList<>();

        public RestaurentFoodAdapter(@NonNull Context context, @LayoutRes int resource,ArrayList<RestaurentFood> string) {
            super(context, resource);
            ct=context;
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

            View restaurentfoodview;
            restaurentfoodview=convertView;
            RestaurentfoodHolder restaurentfoodHolder;
            if(restaurentfoodview==null)
            {
                LayoutInflater layoutInflater=(LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                restaurentfoodview=layoutInflater.inflate(R.layout.restaurent_food_layout,parent,false);
                restaurentfoodHolder=new RestaurentfoodHolder();
                restaurentfoodHolder.name=(TextView)restaurentfoodview.findViewById(R.id.t_name);
                restaurentfoodHolder.type=(TextView)restaurentfoodview.findViewById(R.id.t_type);
                restaurentfoodHolder.price=(TextView)restaurentfoodview.findViewById(R.id.t_price);
                restaurentfoodview.setTag(restaurentfoodHolder);
            }
            else
            {
                restaurentfoodHolder=(RestaurentfoodHolder) restaurentfoodview.getTag();
            }

            final RestaurentFood restaurentfood=(RestaurentFood) this.getItem(position);
            restaurentfoodHolder.name.setText(restaurentfood.getName());
            restaurentfoodHolder.type.setText(restaurentfood.getType());
            restaurentfoodHolder.price.setText(restaurentfood.getPrice());
            restaurentfoodview.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                @Override
                public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                    pressname=restaurentfood.getName();
                    presstype=restaurentfood.getType();
                    pressprice=restaurentfood.getPrice();
                    MenuInflater menuInflater=getMenuInflater();
                    menuInflater.inflate(R.menu.context_menu,menu);
                }
            });

            return restaurentfoodview;
        }

        class RestaurentfoodHolder
        {
            TextView name,type,price;
        }
    }
    public class RestaurentFood {

        private String name,type,price;

        public RestaurentFood(String name, String type, String price) {
            this.name = name;
            this.type = type;
            this.price = price;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }
    }

    class BackgroundTask3 extends AsyncTask<String,Void,Boolean>
    {

        String json_url;
        String JSON_STRING;
        String name;

        @Override
        protected void onPreExecute() {
            json_url="http://"+getString(R.string.ip_address)+"/FoodBank/RestaurentFood.php";
        }

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                String type = params[0];
                name=type;
                URL url=new URL(json_url);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputstream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedwritter = new BufferedWriter(new OutputStreamWriter(outputstream, "UTF-8"));
                String postdata = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(type, "UTF-8");
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
                json_food_string= stringBuilder.toString().trim();
                jsonObject = new JSONObject(json_food_string);
                jsonArray = jsonObject.getJSONArray("Server_response");

                int count = 0;
                String name, type2, price;
                while (count < jsonArray.length()) {
                    JSONObject jo = jsonArray.getJSONObject(count);
                    name = jo.getString("name");
                    type2 = jo.getString("type");
                    price = jo.getString("foodprice");
                    RestaurentFood restaurentFood = new RestaurentFood(name, type2, price);
                    addrestaurantfood.add(restaurentFood);
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
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result) {
                progressDialog.cancel();
                restaurentfoodAdapter.notifyDataSetChanged();
            }
            else
                Toast.makeText(DecorateRestaurant.this, "Failed", Toast.LENGTH_SHORT).show();
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
        AlertDialog.Builder CheckBuild = new AlertDialog.Builder(DecorateRestaurant.this);
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
                    editor.putBoolean(getString(R.string.SKIP),false);
                    progressDialog.setMessage("Logging Out.Please Wait....");
                    progressDialog.show();
                    Runnable progressrunnable = new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.cancel();
                            startActivity(new Intent(DecorateRestaurant.this, staff_login_resistor.class));
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
                            startActivity(new Intent(DecorateRestaurant.this, ShowProfile.class));
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
                            startActivity(new Intent(DecorateRestaurant.this, CreateNewRestaurant.class));
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
                            Intent intent = new Intent(DecorateRestaurant.this, EditChangeProfile.class);
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
        Intent intent=new Intent(DecorateRestaurant.this,Adminstaff.class);
        intent.putExtra("username", name);
        intent.putExtra("resname", resname);
        intent.putExtra("role", role);
        startActivity(intent);
        finish();
    }
}