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
import java.util.List;

public class SuperAdminPendingRestaurent extends AppCompatActivity {

    String name,type,restaurent,selected;
    JSONObject jsonObject,jsonObject2;
    JSONArray jsonArray,jsonArray2;
    RestaurentAdapter restaurentAdapter;
    ListView listView;
    TextView txt;
    ArrayList<String> ass=new ArrayList<String>();
    ArrayList<Restaurent> adPendingRestaurant;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_admin_pending_restaurent);
        progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);

        sharedPreferences=getSharedPreferences(getString(R.string.PREF_FILE), 0);
        editor=sharedPreferences.edit();

        name=getIntent().getExtras().getString("username");
        type=getIntent().getExtras().getString("role");
        restaurent=getIntent().getExtras().getString("restaurant");

        txt=(TextView)findViewById(R.id.admintext);
        txt.setText(name+"\n("+type+")");

        ass.add("None");

        adPendingRestaurant=new ArrayList<>();

        progressDialog.setMessage("Loading.Please Wait....");
        progressDialog.show();
        new BackgroundTask4().execute();

        listView=(ListView)findViewById(R.id.lisview);
        restaurentAdapter=new RestaurentAdapter(this,R.layout.pending_restaurant_layout,adPendingRestaurant);
        listView.setAdapter(restaurentAdapter);

        try {
            jsonObject2=new JSONObject(restaurent);
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
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu_item,menu);
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
                            startActivity(new Intent(SuperAdminPendingRestaurent.this, staff_login_resistor.class));
                            finish();
                        }
                    };

                    Handler handler = new Handler();
                    handler.postDelayed(progressrunnable,3500);
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
                            startActivity(new Intent(SuperAdminPendingRestaurent.this, ShowProfile.class));
                        }
                    };
                    Handler handler3 = new Handler();
                    handler3.postDelayed(progressrunnable3, 3500);
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
                            Intent intent = new Intent(SuperAdminPendingRestaurent.this, EditChangeProfile.class);
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

    private boolean isNetworkAvilabe()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    public void nointernet() {
        //Creating an Alertdialog
        AlertDialog.Builder CheckBuild = new AlertDialog.Builder(SuperAdminPendingRestaurent.this);
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

    public class Restaurent {
        private String name,town,street,phone,type,admin;

        public Restaurent(String name, String town, String street, String phone, String type, String admin) {
            this.name = name;
            this.town = town;
            this.street = street;
            this.phone = phone;
            this.type = type;
            this.admin = admin;
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

        public String getAdmin() {
            return admin;
        }

        public void setAdmin(String admin) {
            this.admin = admin;
        }
    }
    public class RestaurentAdapter extends ArrayAdapter {

        ArrayList<Restaurent> list = new ArrayList();
        Context ct;
        String st;

        public RestaurentAdapter(@NonNull Context context, @LayoutRes int resource,ArrayList<Restaurent> string) {
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

            View restaurentview;
            restaurentview = convertView;
            RestaurentHolder restaurentHolder;
            if (restaurentview == null) {
                LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                restaurentview = layoutInflater.inflate(R.layout.pending_restaurant_layout, parent, false);
                restaurentHolder = new RestaurentHolder();
                restaurentHolder.name = (TextView) restaurentview.findViewById(R.id.tname);
                restaurentHolder.town = (TextView) restaurentview.findViewById(R.id.ttown);
                restaurentHolder.street = (TextView) restaurentview.findViewById(R.id.tstreet);
                restaurentHolder.phone = (TextView) restaurentview.findViewById(R.id.tphone);
                restaurentHolder.type = (TextView) restaurentview.findViewById(R.id.ttype);
                restaurentHolder.admin = (TextView) restaurentview.findViewById(R.id.tadmin);
                restaurentview.setTag(restaurentHolder);
            } else {
                restaurentHolder = (RestaurentHolder) restaurentview.getTag();
            }

            final Restaurent restaurent1 = (Restaurent) this.getItem(position);
            restaurentHolder.name.setText(restaurent1.getName());
            restaurentHolder.town.setText(restaurent1.getTown());
            restaurentHolder.street.setText("Place: "+restaurent1.getStreet()+" , ");
            restaurentHolder.phone.setText("Contact Us: "+restaurent1.getPhone());
            restaurentHolder.type.setText("( "+restaurent1.getType()+")");
            restaurentHolder.admin.setText("Admin: "+restaurent1.getAdmin());
            restaurentview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), restaurent1.getName(), Toast.LENGTH_SHORT).show();
                    st = restaurent1.getName();
                    Intent intent=new Intent(SuperAdminPendingRestaurent.this,SuperAdminRestaurantStaffWithoutPending.class);
                    intent.putExtra("restaurent_name",st);
                    intent.putExtra("username", name);
                    intent.putExtra("role", "SuperAdmin");
                    intent.putExtra("allres", restaurent);
                    startActivity(intent);
                    finish();
                }
            });

            Button approve=(Button)restaurentview.findViewById(R.id.approve);
            approve.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    approveitstaff(v,restaurent1.getName());
                }
            });
            Button delete=(Button)restaurentview.findViewById(R.id.delete);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteit(v,restaurent1.getName());
                }
            });

            Button merge=(Button)restaurentview.findViewById(R.id.merge);
            merge.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    View vt=v;
                    AlertDialog.Builder resbuilder = new AlertDialog.Builder(SuperAdminPendingRestaurent.this);
                    View mview=getLayoutInflater().inflate(R.layout.spinner_layout_superadmin_pending,null);
                    resbuilder.setTitle("Select your Merging Restaurent ");
                    final Spinner rspinner=(Spinner)mview.findViewById(R.id.spinnerres);
                    ArrayAdapter arrayAdapter=new ArrayAdapter(SuperAdminPendingRestaurent.this,android.R.layout.simple_spinner_item,ass);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    rspinner.setAdapter(arrayAdapter);
                    resbuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            selected=rspinner.getSelectedItem().toString();
                            if(selected.equals("None"))
                            {
                                Toast.makeText(SuperAdminPendingRestaurent.this, "You Have Selected None", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                progressDialog.setMessage("Merging.Please Wait....");
                                progressDialog.show();
                                //Toast.makeText(SuperAdminPendingRestaurent.this, "You Have Selected "+selected, Toast.LENGTH_SHORT).show();
                                new BackgroundTask2().execute("Merge",restaurent1.getName(),selected);
                            }
                        }
                    });
                    resbuilder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                           dialog.dismiss();
                        }
                    });
                    resbuilder.setView(mview);
                    AlertDialog dialog=resbuilder.create();
                    dialog.show();
                }
            });

            return restaurentview;
        }

        class RestaurentHolder {
            TextView name, town, street, phone, type,admin;
        }
    }
    class BackgroundTask4 extends AsyncTask<Void,Void,Boolean>
    {

        String json_url;
        String JSON_STRING;
        String restaurent;

        @Override
        protected void onPreExecute() {
            //making a link to php file for reading data from database
            json_url="http://"+getString(R.string.ip_address)+"/FoodBank/pendingrestaurant.php";
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
                String postdata = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode("Pending", "UTF-8");
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
                String json_resturent_string= stringBuilder.toString().trim();

                jsonObject=new JSONObject(json_resturent_string);
                jsonArray=jsonObject.getJSONArray("Server_response");

                int count=0;
                String name,type,town,street,phone,admin;
                while(count<jsonArray.length())
                {
                    JSONObject jo=jsonArray.getJSONObject(count);
                    name=jo.getString("name");
                    town=jo.getString("town");
                    street=jo.getString("street");
                    phone=jo.getString("phone");
                    type=jo.getString("type");
                    admin=jo.getString("admin");
                    Restaurent restaurent=new Restaurent(name,town,street,phone,type,admin);
                    adPendingRestaurant.add(restaurent);
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
            restaurentAdapter.notifyDataSetChanged();
        }
    }

    public void approveitstaff(View view, final String st){
        AlertDialog.Builder paidbuilder = new AlertDialog.Builder(SuperAdminPendingRestaurent.this);
        //setting the alertdialog title
        paidbuilder.setTitle("Attention");
        //setting the body message
        paidbuilder.setMessage("Do You Want To Approve it?");
        //set state for cancelling state
        paidbuilder.setCancelable(true);

        //setting activity for positive state button
        paidbuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                progressDialog.setMessage("Approving.Please Wait....");
                progressDialog.show();
                new BackgroundTask2().execute("Add",st,st);
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
    public void deleteit(View view, final String st){
        AlertDialog.Builder paidbuilder = new AlertDialog.Builder(SuperAdminPendingRestaurent.this);
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
                progressDialog.setMessage("Deleting.Please Wait....");
                progressDialog.show();
                new BackgroundTask2().execute("Delete",st,st);
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

    class BackgroundTask2 extends AsyncTask<String,Void,Boolean> {
        String json_url;
        String JSON_STRING;

        @Override
        protected void onPreExecute() {
            json_url = "http://" + getString(R.string.ip_address) + "/FoodBank/ApprovedRestaurant.php";
        }

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                String type = params[0];
                String res_name = params[1];
                String res_name2=params[2];
                URL url = new URL(json_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputstream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedwritter = new BufferedWriter(new OutputStreamWriter(outputstream, "UTF-8"));
                String postdata = URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode(type, "UTF-8")
                        + "&" + URLEncoder.encode("resname", "UTF-8") + "=" + URLEncoder.encode(res_name, "UTF-8")
                        + "&" + URLEncoder.encode("resname2", "UTF-8") + "=" + URLEncoder.encode(res_name2, "UTF-8");
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
                String string= stringBuilder.toString().trim();
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
            progressDialog.cancel();
            Intent intent=getIntent();
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SuperAdminPendingRestaurent.this, SuperAdminRestaurant.class);
        intent.putExtra("username", name);
        intent.putExtra("role", "SuperAdmin");
        startActivity(intent);
        finish();
    }
}
