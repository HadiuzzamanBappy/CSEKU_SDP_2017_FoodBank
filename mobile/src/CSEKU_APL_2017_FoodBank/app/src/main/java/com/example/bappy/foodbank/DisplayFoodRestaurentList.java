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
import java.util.List;

public class DisplayFoodRestaurentList extends AppCompatActivity {

    String food;
    TextView foodrestaurent;
    JSONObject jsonObject;
    JSONArray jsonArray;
    ListView listView;
    FoodRestaurentAdapter foodRestaurentAdapter;

    ArrayList<Foodrestaurent> addFoodRestaurant;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Boolean save_login;
    String name,resname,pass,type;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);

        setContentView(R.layout.display_food_restaurentlist_layout);
        sharedPreferences=getSharedPreferences(getString(R.string.PREF_FILE), 0);
        editor=sharedPreferences.edit();
        save_login=sharedPreferences.getBoolean(getString(R.string.SAVE_LOGIN),false);
        type=sharedPreferences.getString(getString(R.string.TYPE),"None");
        name=sharedPreferences.getString(getString(R.string.NAME),"None");
        pass=sharedPreferences.getString(getString(R.string.PASSWORD),"None");
        resname=sharedPreferences.getString(getString(R.string.RESTAURANT_NAME),"None");

        food=getIntent().getExtras().getString("food_name");
        addFoodRestaurant=new ArrayList<>();
        progressDialog.setMessage("Loading.Please Wait....");
        progressDialog.show();
        new BackgroundTask().execute(food);

        foodrestaurent=(TextView)findViewById(R.id.txtview);
        foodrestaurent.setText(food);
        listView = (ListView) findViewById(R.id.listrestaurentview);
        foodRestaurentAdapter = new FoodRestaurentAdapter(this, R.layout.food_restaurent_layout,food,addFoodRestaurant);
        listView.setAdapter(foodRestaurentAdapter);
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
                            startActivity(new Intent(DisplayFoodRestaurentList.this, staff_login_resistor.class));
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
                            startActivity(new Intent(DisplayFoodRestaurentList.this, staff_login_resistor.class));
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
                            startActivity(new Intent(DisplayFoodRestaurentList.this, ShowProfile.class));
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
                            startActivity(new Intent(DisplayFoodRestaurentList.this, CreateNewRestaurant.class));
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
                            Intent intent = new Intent(DisplayFoodRestaurentList.this, EditChangeProfile.class);
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
                if(!isNetworkAvilabe())
                    nointernet();
                else {
                    startActivity(new Intent(this, UserFoodDetails.class));
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
                Intent intent = new Intent(DisplayFoodRestaurentList.this, staff_login_resistor.class);
                progressDialog.cancel();
                startActivity(intent);
                finish();
            }
            else
                Toast.makeText(DisplayFoodRestaurentList.this, "Failed", Toast.LENGTH_SHORT).show();
        }
    }


    public class FoodRestaurentAdapter extends ArrayAdapter {

        ArrayList<Foodrestaurent> list=new ArrayList();
        Context ct;
        String ffood,frestarant,fprice;

        public FoodRestaurentAdapter(@NonNull Context context, @LayoutRes int resource, String food,ArrayList<Foodrestaurent> string) {
            super(context, resource);
            ct=context;
            this.ffood=food;
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

            View foodrestaurentview;
            foodrestaurentview=convertView;
            FoodRestaurentHolder restaurentfoodHolder;
            if(foodrestaurentview==null)
            {
                LayoutInflater layoutInflater=(LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                foodrestaurentview=layoutInflater.inflate(R.layout.food_restaurent_layout,parent,false);
                restaurentfoodHolder=new FoodRestaurentHolder();
                restaurentfoodHolder.name=(TextView)foodrestaurentview.findViewById(R.id.t_name);
                restaurentfoodHolder.price=(TextView)foodrestaurentview.findViewById(R.id.t_price);
                foodrestaurentview.setTag(restaurentfoodHolder);
            }
            else
            {
                restaurentfoodHolder=(FoodRestaurentHolder) foodrestaurentview.getTag();
            }

            final Foodrestaurent foodrestaurent=(Foodrestaurent) this.getItem(position);
            restaurentfoodHolder.name.setText(foodrestaurent.getName());
            restaurentfoodHolder.price.setText(foodrestaurent.getPrice());
            foodrestaurentview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(save_login) {
                    frestarant=foodrestaurent.getName();
                    fprice=foodrestaurent.getPrice();
                    AlertDialog.Builder paidbuilder = new AlertDialog.Builder(ct);
                    //setting the alertdialog title
                    paidbuilder.setTitle("Attention");
                    //setting the body message
                    paidbuilder.setMessage("Do You Want To Order It?");
                    //set state for cancelling state
                    paidbuilder.setCancelable(true);

                    //setting activity for positive state button
                    paidbuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(!isNetworkAvilabe())
                                nointernet();
                            else {
                                    Intent intent = new Intent(ct, Order.class);
                                    intent.putExtra("restaurant", frestarant);
                                    intent.putExtra("food", ffood);
                                    intent.putExtra("price", fprice);
                                    intent.putExtra("resfood", "1");
                                    ct.startActivity(intent);
                                    finish();
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
                }
                    else
                        Toast.makeText(ct, "OPPS Sorry,U didn't SIGN IN as User", Toast.LENGTH_SHORT).show();
                }
            });
            return foodrestaurentview;
        }

        class FoodRestaurentHolder
        {
            TextView name,price;
        }
    }

    public class Foodrestaurent {
        private String name,price;

        public Foodrestaurent(String name, String price) {
            this.name = name;
            this.price = price;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }
    }
    class BackgroundTask extends AsyncTask<String,Void,Boolean>
    {

        String json_url;
        String JSON_STRING;
        String name;

        @Override
        protected void onPreExecute() {
            //making a link to php file for reading data from database
            json_url="http://"+getString(R.string.ip_address)+"/FoodBank/FoodRestaurent.php";
        }

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                //taking string value as a parameter
                String name2 = params[0];
                //initialize the url
                URL url=new URL(json_url);
                //initialize the connection
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                //setting up a request method by which we can sget data into php file
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                //sending data and request to php file
                OutputStream outputstream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedwritter = new BufferedWriter(new OutputStreamWriter(outputstream, "UTF-8"));
                String postdata = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name2, "UTF-8");
                //writing data to the url php connection
                bufferedwritter.write(postdata);
                bufferedwritter.flush();
                bufferedwritter.close();
                outputstream.close();
                InputStream inputStream=httpURLConnection.getInputStream();
                //getting data fro database through php file
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder=new StringBuilder();
                while((JSON_STRING=bufferedReader.readLine())!=null){
                    stringBuilder.append(JSON_STRING+"\n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                String restaurent= stringBuilder.toString().trim();
                jsonObject = new JSONObject(restaurent);
                jsonArray = jsonObject.getJSONArray("Server_response");

                int count = 0;
                String name,price;
                while (count < jsonArray.length()) {
                    JSONObject jo = jsonArray.getJSONObject(count);
                    name = jo.getString("name");
                    price = jo.getString("foodprice");
                    Foodrestaurent foodrestaurent=new Foodrestaurent(name,price);
                    addFoodRestaurant.add(foodrestaurent);
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
            if(result.equals("false"))
                Toast.makeText(DisplayFoodRestaurentList.this, "can't connect to the database", Toast.LENGTH_SHORT).show();
            foodRestaurentAdapter.notifyDataSetChanged();
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
        AlertDialog.Builder CheckBuild = new AlertDialog.Builder(DisplayFoodRestaurentList.this);
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
    //creating activity for back pressing from phone
    public void onBackPressed() {
        Intent intent=new Intent(DisplayFoodRestaurentList.this,DisplayFoodList.class);
        startActivity(intent);
        finish();
    }
}
