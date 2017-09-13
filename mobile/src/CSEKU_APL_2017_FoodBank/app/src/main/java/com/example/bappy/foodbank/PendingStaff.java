package com.example.bappy.foodbank;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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
import java.util.List;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pending_staff_layout);

        sharedPreferences=getSharedPreferences(getString(R.string.PREF_FILE), 0);
        editor=sharedPreferences.edit();

        addpending=new ArrayList<>();

        name=getIntent().getExtras().getString("name");
        res_name=getIntent().getExtras().getString("res_name");
        role=getIntent().getExtras().getString("role");
        adminstaff=getIntent().getExtras().getString("adminstaff");
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
                editor.clear();
                editor.commit();
                startActivity(new Intent(PendingStaff.this, staff_login_resistor.class));
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public class Pending {
        String name;

        public Pending(String name) {
            this.name = name;
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
            pendingHolder.type.setText(role);
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
                    approveitstaff(v,pend1.getName());
                }
            });
            Button delete=(Button)pend.findViewById(R.id.delete);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    delete(v,pend1.getName());
                }
            });

            return pend;
        }

        class PendingHolder {
            TextView name,type;
        }
    }
    public void approveitstaff(View view, final String st){
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
                new BackgroundTask2().execute("Approve",st);
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
    public void delete(View view, final String st){
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
                Toast.makeText(PendingStaff.this, res_name, Toast.LENGTH_SHORT).show();
                new BackgroundTask2().execute("Delete",st);
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
                String name;
                while (count < jsonArray.length()) {
                    JSONObject jo = jsonArray.getJSONObject(count);
                    name = jo.getString("name");
                    Pending pending = new Pending(name);
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
            if(result.equals("false"))
                Toast.makeText(PendingStaff.this, "can't connect to the database", Toast.LENGTH_SHORT).show();
            pendingAdapter.notifyDataSetChanged();
        }
    }

    class BackgroundTask2 extends AsyncTask<String,Void,String> {
        String json_url;
        String JSON_STRING;

        @Override
        protected void onPreExecute() {
            json_url = "http://" + getString(R.string.ip_address) + "/FoodBank/Approved.php";
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String type = params[0];
                String username = params[1];
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
                        + "&" + URLEncoder.encode("role", "UTF-8") + "=" + URLEncoder.encode(role, "UTF-8");
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
            intent.putExtra("role", role);
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