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
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DisplayRestaurentListView extends AppCompatActivity {


    JSONObject jsonObject;
    JSONArray jsonArray;
    RestaurentAdapter restaurentAdapter;
    ListView listView;

    ArrayList<Restaurent> addrestaurent;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Boolean save_login;

    String name,resname,pass,type;
    ProgressDialog progressDialog;

    Boolean show_value=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_restaurentlistview_layout);
        progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);

        sharedPreferences=getSharedPreferences(getString(R.string.PREF_FILE), 0);
        editor=sharedPreferences.edit();
        save_login=sharedPreferences.getBoolean(getString(R.string.SAVE_LOGIN),false);

        type=sharedPreferences.getString(getString(R.string.TYPE),"None");
        name=sharedPreferences.getString(getString(R.string.NAME),"None");
        pass=sharedPreferences.getString(getString(R.string.PASSWORD),"None");
        resname=sharedPreferences.getString(getString(R.string.RESTAURANT_NAME),"None");

            addrestaurent = new ArrayList<>();

            new BackgroundTask2().execute();

            listView = (ListView) findViewById(R.id.lisview);
            restaurentAdapter = new RestaurentAdapter(this, R.layout.restaurent_layout, addrestaurent);
            listView.setAdapter(restaurentAdapter);
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

                            startActivity(new Intent(DisplayRestaurentListView.this, staff_login_resistor.class));
                            finish();
                        }
                    };

                    Handler handler = new Handler();
                    handler.postDelayed(progressrunnable, 6000);
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
                            startActivity(new Intent(DisplayRestaurentListView.this, staff_login_resistor.class));
                            finish();
                        }
                    };
                    Handler handler2 = new Handler();
                    handler2.postDelayed(progressrunnable2, 6000);
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
                            startActivity(new Intent(DisplayRestaurentListView.this, ShowProfile.class));
                        }
                    };
                    Handler handler3 = new Handler();
                    handler3.postDelayed(progressrunnable3, 6000);
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
                            startActivity(new Intent(DisplayRestaurentListView.this, CreateNewRestaurant.class));
                        }
                    };
                    Handler handler4 = new Handler();
                    handler4.postDelayed(progressrunnable4, 6000);
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
                            Intent intent = new Intent(DisplayRestaurentListView.this, EditChangeProfile.class);
                            intent.putExtra("op_type", "Edit");
                            startActivity(intent);
                        }
                    };
                    Handler handler5 = new Handler();
                    handler5.postDelayed(progressrunnable5, 6000);
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
            Intent intent = new Intent(DisplayRestaurentListView.this, staff_login_resistor.class);
                progressDialog.cancel();
            startActivity(intent);
            finish();
        }
            else
                    Toast.makeText(DisplayRestaurentListView.this, "Failed", Toast.LENGTH_SHORT).show();
        }
    }

    public class RestaurentAdapter extends ArrayAdapter {

        ArrayList<Restaurent> list=new ArrayList();
        Context ct;
        String st;

        public RestaurentAdapter(@NonNull Context context, @LayoutRes int resource,ArrayList<Restaurent> string) {
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

            View restaurentview;
            restaurentview=convertView;
            final RestaurentHolder restaurentHolder;
            if(restaurentview==null)
            {
                LayoutInflater layoutInflater=(LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                restaurentview=layoutInflater.inflate(R.layout.restaurent_layout,parent,false);
                restaurentHolder=new RestaurentHolder();
                restaurentHolder.name=(TextView)restaurentview.findViewById(R.id.tname);
                restaurentHolder.town=(TextView)restaurentview.findViewById(R.id.ttown);
                restaurentHolder.street=(TextView)restaurentview.findViewById(R.id.tstreet);
                restaurentHolder.phone=(TextView)restaurentview.findViewById(R.id.tphone);
                restaurentHolder.type=(TextView)restaurentview.findViewById(R.id.ttype);
                //restaurentHolder.button=(Button)restaurentview.findViewById(R.id.circleImageView);
                restaurentview.setTag(restaurentHolder);
            }
            else
            {
                restaurentHolder=(RestaurentHolder) restaurentview.getTag();
            }

            final Restaurent restaurent1=(Restaurent) this.getItem(position);
            restaurentHolder.name.setText(restaurent1.getName());
            restaurentHolder.town.setText(restaurent1.getTown());
            restaurentHolder.street.setText("Place: "+restaurent1.getStreet()+" , ");
            restaurentHolder.phone.setText("Contact Us: "+restaurent1.getPhone());
            restaurentHolder.type.setText("( "+restaurent1.getType()+")");

//            restaurentHolder.town.setVisibility(View.GONE);
//            restaurentHolder.street.setVisibility(View.GONE);
//            restaurentHolder.phone.setVisibility(View.GONE);

            restaurentview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    {
                        st = restaurent1.getName();
                        gointent(st);
                    }
                }
            });

//            restaurentHolder.button.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if(!show_value)
//                    {
//                        restaurentHolder.town.setVisibility(View.VISIBLE);
//                        restaurentHolder.street.setVisibility(View.VISIBLE);
//                        restaurentHolder.phone.setVisibility(View.VISIBLE);
//                        show_value=true;
//                    }
//                    else
//                    {
//                        restaurentHolder.town.setVisibility(View.GONE);
//                        restaurentHolder.street.setVisibility(View.GONE);
//                        restaurentHolder.phone.setVisibility(View.GONE);
//                        show_value=false;
//                    }
//                }
//            });
            return restaurentview;
        }

        class RestaurentHolder
        {
            TextView name,town,street,phone,type;
            Button button;
        }
        public void gointent(String result){
            if(!isNetworkAvilabe())
                nointernet();
            else {
                Intent intent = new Intent(ct, DisplayRestaurentFoodList.class);
                intent.putExtra("restaurent_name", result);
                ct.startActivity(intent);
                finish();
            }
        }
    }

    class BackgroundTask2 extends AsyncTask<Void,Void,Boolean>
    {

        String json_url;
        String JSON_STRING;

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Loading.Please Wait....");
            progressDialog.show();
            //making a link to php file for reading data from database
            json_url="http://"+getString(R.string.ip_address)+"/FoodBank/ReadingDataRestaurant.php";
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                //initialize the url
                URL url=new URL(json_url);
                //initialize the connection
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputstream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedwritter = new BufferedWriter(new OutputStreamWriter(outputstream, "UTF-8"));
                String postdata = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode("Staff", "UTF-8");
                bufferedwritter.write(postdata);
                bufferedwritter.flush();
                bufferedwritter.close();
                outputstream.close();
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
                String jsonRestaurant= stringBuilder.toString().trim();

                jsonObject=new JSONObject(jsonRestaurant);
                jsonArray=jsonObject.getJSONArray("Server_response");

                int count=0;
                String name,type,town,street,phone;
                while(count<jsonArray.length())
                {
                    JSONObject jo=jsonArray.getJSONObject(count);
                    name=jo.getString("name");
                    town=jo.getString("town");
                    street=jo.getString("street");
                    phone=jo.getString("phone");
                    type=jo.getString("type");
                    //restaurantT.add(name);
                    Restaurent restaurent=new Restaurent(name,town,street,phone,type);
                    addrestaurent.add(restaurent);
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
                restaurentAdapter.notifyDataSetChanged();
            }
            else
                Toast.makeText(DisplayRestaurentListView.this, "can't connect to the database", Toast.LENGTH_SHORT).show();

        }
    }

    public class Restaurent {
        private String name,town,street,phone,type;

        public Restaurent(String name, String town, String street, String phone, String type) {
            this.setName(name);
            this.setTown(town);
            this.setStreet(street);
            this.setPhone(phone);
            this.setType(type);
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTown() {
            return town;
        }

        public void setTown(String town) {
            this.town = town;
        }

        public String getStreet() {
            return street;
        }

        public void setStreet(String street) {
            this.street = street;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
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
        AlertDialog.Builder CheckBuild = new AlertDialog.Builder(DisplayRestaurentListView.this);
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
        Intent intent=new Intent(DisplayRestaurentListView.this,HomeActivity.class);
        startActivity(intent);
        finish();
    }
}
