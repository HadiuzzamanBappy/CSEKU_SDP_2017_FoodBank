package com.example.bappy.foodbank;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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

public class PendingStaff extends AppCompatActivity {
    String res_name,role,name,adminstaff;
    JSONObject jsonObject;
    JSONArray jsonArray;
    PendingAdapter pendingAdapter;
    ListView listView;
    TextView txt;

    ArrayList<Pending> addpending;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pending_staff_layout);
        progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);

        sharedPreferences=getSharedPreferences(getString(R.string.PREF_FILE), 0);
        editor=sharedPreferences.edit();

        addpending=new ArrayList<>();

        name=getIntent().getExtras().getString("name");
        res_name=getIntent().getExtras().getString("res_name");
        role=getIntent().getExtras().getString("role");
        adminstaff=getIntent().getExtras().getString("adminstaff");
        progressDialog.setMessage("Loading.Please Wait....");
        progressDialog.show();
        new BackgroundTask().execute();

        txt=(TextView)findViewById(R.id.txt);
        listView = (ListView) findViewById(R.id.lisview);
        pendingAdapter = new PendingAdapter(this, R.layout.pending_staff_list,addpending);
        listView.setAdapter(pendingAdapter);

        txt.setText(res_name);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        if(adminstaff.equals("1"))
        menuInflater.inflate(R.menu.menu_item,menu);
        else
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
                            startActivity(new Intent(PendingStaff.this, staff_login_resistor.class));
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
                            startActivity(new Intent(PendingStaff.this, ShowProfile.class));
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
                            startActivity(new Intent(PendingStaff.this, CreateNewRestaurant.class));
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
                            Intent intent = new Intent(PendingStaff.this, EditChangeProfile.class);
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
        AlertDialog.Builder CheckBuild = new AlertDialog.Builder(PendingStaff.this);
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

    public class Pending {
        String name,typerole;

        public Pending(String name, String typerole) {
            this.name = name;
            this.typerole = typerole;
        }

        public String getTyperole() {
            return typerole;
        }

        public void setTyperole(String typerole) {
            this.typerole = typerole;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public class PendingAdapter extends ArrayAdapter {

        ArrayList<Pending> list = new ArrayList();
        Context ct;
        String st;

        public PendingAdapter(@NonNull Context context, @LayoutRes int resource,ArrayList<Pending> string) {
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
        public View getView(int position, @Nullable final View convertView, @NonNull ViewGroup parent) {

            View pend;
            pend = convertView;
            PendingHolder pendingHolder;
            if (pend == null) {
                LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                pend = layoutInflater.inflate(R.layout.pending_staff_list, parent, false);
                pendingHolder = new PendingHolder();
                pendingHolder.name = (TextView) pend.findViewById(R.id.t_name);
                pendingHolder.type = (TextView) pend.findViewById(R.id.t_type);
                pend.setTag(pendingHolder);
            } else {
                pendingHolder = (PendingHolder) pend.getTag();
            }

            final Pending pend1 = (Pending) this.getItem(position);
            pendingHolder.name.setText(pend1.getName());
            if(pend1.getTyperole().equals("2"))
                pendingHolder.type.setText("Admin");
            else if(pend1.getTyperole().equals("3"))
                pendingHolder.type.setText("Staff");
            else if(pend1.getTyperole().equals("4"))
                pendingHolder.type.setText("Chef");
            pend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    st = pend1.getName();
                    Toast.makeText(getContext(), "Clicked on " + st, Toast.LENGTH_SHORT).show();
                }
            });
            Button approve=(Button)pend.findViewById(R.id.approve);
            approve.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String rolet;
                    if(pend1.getTyperole().equals("3"))
                        rolet="Staff";
                    else if(pend1.getTyperole().equals("4"))
                        rolet="Chef";
                    else
                        rolet="Admin";
                    approveitstaff(v,pend1.getName(),rolet);
                }
            });
            Button delete=(Button)pend.findViewById(R.id.delete);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String rolet;
                    if(pend1.getTyperole().equals("3"))
                        rolet="Staff";
                    else if(pend1.getTyperole().equals("4"))
                        rolet="Chef";
                    else
                        rolet="Admin";
                    delete(v,pend1.getName(),rolet);
                }
            });

            return pend;
        }

        class PendingHolder {
            TextView name,type;
        }
    }
    public void approveitstaff(View view, final String st, final String rolet){
        AlertDialog.Builder paidbuilder = new AlertDialog.Builder(PendingStaff.this);
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
                new BackgroundTask2().execute("Approve",st,rolet);
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
    public void delete(View view, final String st, final String rolet){
        AlertDialog.Builder paidbuilder = new AlertDialog.Builder(PendingStaff.this);
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
                new BackgroundTask2().execute("Delete",st,rolet);
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

    class BackgroundTask extends AsyncTask<Void,Void,Boolean>
    {

        String json_url;
        String JSON_STRING;
        String json_pending_staff;

        @Override
        protected void onPreExecute() {
            if(role.equals("Staff"))
            json_url="http://"+getString(R.string.ip_address)+"/FoodBank/pendingstaff.php";
            else
            json_url="http://"+getString(R.string.ip_address)+"/FoodBank/pendingadmin.php";
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                URL url=new URL(json_url);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputstream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedwritter = new BufferedWriter(new OutputStreamWriter(outputstream, "UTF-8"));
                String postdata = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(res_name, "UTF-8");
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
                json_pending_staff=stringBuilder.toString().trim();

                jsonObject = new JSONObject(json_pending_staff);
                jsonArray = jsonObject.getJSONArray("Server_response");

                int count = 0;
                String name,typerole;
                while (count < jsonArray.length()) {
                    JSONObject jo = jsonArray.getJSONObject(count);
                    name = jo.getString("name");
                    typerole = jo.getString("typerole");
                    Pending pending = new Pending(name,typerole);
                    addpending.add(pending);
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
                pendingAdapter.notifyDataSetChanged();
            }
            else
                Toast.makeText(PendingStaff.this, "can't connect to the database", Toast.LENGTH_SHORT).show();
        }
    }

    class BackgroundTask2 extends AsyncTask<String,Void,String> {
        String json_url;
        String JSON_STRING;

        @Override
        protected void onPreExecute() {
            json_url = "http://" + getString(R.string.ip_address) + "/FoodBank/ApprovedStaff.php";
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String type = params[0];
                String username = params[1];
                String rolet = params[2];
                URL url = new URL(json_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputstream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedwritter = new BufferedWriter(new OutputStreamWriter(outputstream, "UTF-8"));
                String postdata = URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode(type, "UTF-8")
                        + "&" + URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8")
                        + "&" + URLEncoder.encode("resname", "UTF-8") + "=" + URLEncoder.encode(res_name, "UTF-8")
                        + "&" + URLEncoder.encode("role", "UTF-8") + "=" + URLEncoder.encode(rolet, "UTF-8");
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
            Toast.makeText(PendingStaff.this, result, Toast.LENGTH_SHORT).show();
            Intent intent=getIntent();
            progressDialog.cancel();
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        if(adminstaff.equals("2")) {
            Intent intent = new Intent(PendingStaff.this, Adminstaff.class);
            intent.putExtra("username", name);
            intent.putExtra("resname", res_name);
            intent.putExtra("role", "Admin");
            startActivity(intent);
        }
        else
        {
            Intent intent = new Intent(PendingStaff.this, SuperAdminRestaurantStaff.class);
            intent.putExtra("username", name);
            intent.putExtra("restaurent_name", res_name);
            startActivity(intent);
        }
        finish();
    }
}