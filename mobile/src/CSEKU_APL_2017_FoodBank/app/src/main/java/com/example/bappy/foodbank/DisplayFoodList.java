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
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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
import java.util.Locale;

public class DisplayFoodList extends AppCompatActivity {

    JSONObject jsonObject;
    JSONArray jsonArray;
    FoodAdapter foodAdapter;
    ListView listView;

    ArrayList<Food> addfood,isFound;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Boolean save_login;
    String name,resname,pass,type;
    EditText search;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog=new ProgressDialog(this);
        //progressDialog.setTitle("Attention");
        progressDialog.setCancelable(false);

        setContentView(R.layout.display_foodlist_layout);
        search=(EditText)findViewById(R.id.search_food);
        search.setVisibility(View.GONE);

        sharedPreferences=getSharedPreferences(getString(R.string.PREF_FILE), 0);
        editor=sharedPreferences.edit();
        save_login=sharedPreferences.getBoolean(getString(R.string.SAVE_LOGIN),false);

        type=sharedPreferences.getString(getString(R.string.TYPE),"None");
        name=sharedPreferences.getString(getString(R.string.NAME),"None");
        pass=sharedPreferences.getString(getString(R.string.PASSWORD),"None");
        resname=sharedPreferences.getString(getString(R.string.RESTAURANT_NAME),"None");

            addfood = new ArrayList<>();
            new BackgroundTask().execute();
            listView = (ListView) findViewById(R.id.lisview);
            //initialize the foodadapter
            foodAdapter = new FoodAdapter(this, R.layout.food_layout, addfood);
            //seeting adapter in the listview
            listView.setAdapter(foodAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        if(save_login)
            menuInflater.inflate(R.menu.display_food_search,menu);
        else
            menuInflater.inflate(R.menu.food_list_no_sign_in,menu);
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
                            startActivity(new Intent(DisplayFoodList.this, staff_login_resistor.class));
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
                            startActivity(new Intent(DisplayFoodList.this, staff_login_resistor.class));
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
                            startActivity(new Intent(DisplayFoodList.this, ShowProfile.class));
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
                            startActivity(new Intent(DisplayFoodList.this, CreateNewRestaurant.class));
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
                            Intent intent = new Intent(DisplayFoodList.this, EditChangeProfile.class);
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
                            new BackgroundTask2().execute("Delete", name, name, resname, type, pass, pass);
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
            case R.id.search:
                if(!isNetworkAvilabe())
                    nointernet();
                else {
                    search.setVisibility(View.VISIBLE);
                    search.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            String newText = search.getText().toString().toLowerCase(Locale.getDefault());
                            if (newText != null && !newText.isEmpty()) {

                                isFound = new ArrayList<Food>();
                                for (Food item : addfood) {
                                    Log.d(item.toString(), newText);
                                    if (item.getName().toLowerCase().contains(newText.toString().toLowerCase())) {
                                        isFound.add(item);
                                    }
                                }
                                foodAdapter = new FoodAdapter(DisplayFoodList.this, R.layout.food_layout, isFound);
                                //seeting adapter in the listview
                                listView.setAdapter(foodAdapter);
                            } else if (newText == null || newText.isEmpty()) {
                                foodAdapter = new FoodAdapter(DisplayFoodList.this, R.layout.food_layout, addfood);
                                //seeting adapter in the listview
                                listView.setAdapter(foodAdapter);
                            }
                        }
                    });
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

    class BackgroundTask2 extends AsyncTask<String,Void,Boolean> {
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
            if(result) {
                editor.clear();
                editor.commit();
                Intent intent = new Intent(DisplayFoodList.this, staff_login_resistor.class);
                progressDialog.cancel();
                startActivity(intent);
                finish();
            }
            else
                Toast.makeText(DisplayFoodList.this, "Failed", Toast.LENGTH_SHORT).show();
        }
    }


    public class FoodAdapter extends ArrayAdapter {

        ArrayList<Food> list=new ArrayList();
        ArrayList<Food> list2=new ArrayList();
        Context ct;
        String st;

        public FoodAdapter(@NonNull Context context, @LayoutRes int resource,ArrayList<Food> string) {
            super(context, resource);
            ct=context;
            list=string;
            list2=string;
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
        public View getView(int position, @Nullable final View convertView, @NonNull ViewGroup parent) {

            View food;
            food=convertView;
            FoodHolder foodHolder;
            if(food==null)
            {
                //creating view for listview again and again
                LayoutInflater layoutInflater=(LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                food=layoutInflater.inflate(R.layout.food_layout,parent,false);
                foodHolder=new FoodHolder();

                //initialize the id for holder textview
                foodHolder.name=(TextView)food.findViewById(R.id.t_name);
                foodHolder.type=(TextView)food.findViewById(R.id.t_type);
                food.setTag(foodHolder);
            }
            else
            {
                foodHolder=(FoodHolder)food.getTag();
            }

            //making a instance of Food object
            final Food food1=(Food)this.getItem(position);
            foodHolder.name.setText(food1.getName());
            foodHolder.type.setText(food1.getType());

            //selecting the activity which will do after clicking
            food.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        st = food1.getName();
                        gointent(st);
                }
            });

            return food;
        }

        //making a holder for keeping instant data
        class FoodHolder
        {
            TextView name,type;
        }
        public void gointent(String st){
            if(!isNetworkAvilabe())
                nointernet();
            else {
                Intent intent = new Intent(ct, DisplayFoodRestaurentList.class);
                //sending data to another intent
                intent.putExtra("food_name", st);
                ct.startActivity(intent);
                finish();
            }
        }
    }

    class BackgroundTask extends AsyncTask<Void,Void,Boolean>
    {

        String json_url;
        String JSON_STRING;

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Loading.Please Wait....");
            progressDialog.show();
            //making a link to php file for reading data from database
            json_url="http://"+getString(R.string.ip_address)+"/FoodBank/ReadFood.php";
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
                String jsonFood= stringBuilder.toString().trim();

                //set json string json object
                jsonObject=new JSONObject(jsonFood);
                //initialize array from json object
                jsonArray=jsonObject.getJSONArray("Server_response");

                int count=0;
                String name,type;
                while(count<jsonArray.length())
                {
                    //get json object from count length
                    JSONObject jo=jsonArray.getJSONObject(count);
                    name=jo.getString("name");
                    type=jo.getString("type");
                    //making it as a java object
                    Food food=new Food(name,type);
                    //adding to the adapter
                    addfood.add(food);
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
                foodAdapter.notifyDataSetChanged();
            }
            else
                Toast.makeText(DisplayFoodList.this, "Failed", Toast.LENGTH_SHORT).show();
        }
    }


    //creating java class for setting all JSON individual data and getting it;
    public class Food {
        private String name,type;

        public Food(String name, String type) {
            this.setName(name);
            this.setType(type);
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
    }
    private boolean isNetworkAvilabe()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    public void nointernet() {
        //Creating an Alertdialog
        AlertDialog.Builder CheckBuild = new AlertDialog.Builder(DisplayFoodList.this);
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
        Intent intent=new Intent(DisplayFoodList.this,HomeActivity.class);
        startActivity(intent);
        finish();
    }
}
