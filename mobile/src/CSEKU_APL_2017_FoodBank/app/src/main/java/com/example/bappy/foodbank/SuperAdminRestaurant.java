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

public class SuperAdminRestaurant extends AppCompatActivity {

    String name,type;
    JSONObject jsonObject;
    JSONArray jsonArray;
    RestaurentAdapter restaurentAdapter;
    ListView listView;
    TextView txt;

    ArrayList<Restaurent> addsuperRestaurant;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_admin_restaurant);

        sharedPreferences=getSharedPreferences(getString(R.string.PREF_FILE), 0);
        editor=sharedPreferences.edit();

        name=getIntent().getExtras().getString("username");
        type=getIntent().getExtras().getString("role");

        txt=(TextView)findViewById(R.id.admintext);
        txt.setText(name+"\n("+type+")");

        addsuperRestaurant=new ArrayList<>();
        new BackgroundTask4().execute();

        listView=(ListView)findViewById(R.id.lisview);
        restaurentAdapter=new RestaurentAdapter(this,R.layout.restaurent_layout,addsuperRestaurant);
        listView.setAdapter(restaurentAdapter);
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
                editor.clear();
                editor.commit();
                startActivity(new Intent(SuperAdminRestaurant.this, staff_login_resistor.class));
                finish();
                return true;
            case R.id.my_profile:
                startActivity(new Intent(this, ShowProfile.class));
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
                restaurentview = layoutInflater.inflate(R.layout.restaurent_layout, parent, false);
                restaurentHolder = new RestaurentHolder();
                restaurentHolder.name = (TextView) restaurentview.findViewById(R.id.tname);
                restaurentHolder.town = (TextView) restaurentview.findViewById(R.id.ttown);
                restaurentHolder.street = (TextView) restaurentview.findViewById(R.id.tstreet);
                restaurentHolder.phone = (TextView) restaurentview.findViewById(R.id.tphone);
                restaurentHolder.type = (TextView) restaurentview.findViewById(R.id.ttype);
                restaurentview.setTag(restaurentHolder);
            } else {
                restaurentHolder = (RestaurentHolder) restaurentview.getTag();
            }

            final Restaurent restaurent1 = (Restaurent) this.getItem(position);
            restaurentHolder.name.setText(restaurent1.getName());
            restaurentHolder.town.setText(restaurent1.getTown());
            restaurentHolder.street.setText(restaurent1.getStreet());
            restaurentHolder.phone.setText(restaurent1.getPhone());
            restaurentHolder.type.setText(restaurent1.getType());
            restaurentview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), restaurent1.getName(), Toast.LENGTH_SHORT).show();
                    st = restaurent1.getName();
                    gointent(st);
                }
            });
            return restaurentview;
        }

        class RestaurentHolder {
            TextView name, town, street, phone, type;
        }

        public void gointent(String result) {
            Intent intent=new Intent(SuperAdminRestaurant.this,SuperAdminRestaurantStaff.class);
            intent.putExtra("username", name);
            intent.putExtra("restaurent_name",result);
            startActivity(intent);
            finish();
        }
    }

    class BackgroundTask4 extends AsyncTask<Void,Void,Boolean>
    {

        String json_url;
        String JSON_STRING;

        @Override
        protected void onPreExecute() {
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
                String json_resturent_string= stringBuilder.toString().trim();

                jsonObject=new JSONObject(json_resturent_string);
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
                    Restaurent restaurent=new Restaurent(name,town,street,phone,type);
                    addsuperRestaurant.add(restaurent);
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
                Toast.makeText(SuperAdminRestaurant.this, "can't connect to the database", Toast.LENGTH_SHORT).show();
            restaurentAdapter.notifyDataSetChanged();
        }
    }
        public void pendingrestaurant(View view){
            new BackgroundTask().execute();
        }

    class BackgroundTask extends AsyncTask<Void,Void,Boolean>
    {

        String json_url;
        String JSON_STRING;
        String string;

        @Override
        protected void onPreExecute() {
            //making a link to php file for reading data from database
            json_url="http://"+getString(R.string.ip_address)+"/FoodBank/ReadRestaurantOnly.php";
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
                string= stringBuilder.toString().trim();
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
            Intent intent = new Intent(SuperAdminRestaurant.this, SuperAdminPendingRestaurent.class);
            intent.putExtra("username", name);
            intent.putExtra("role", "SuperAdmin");
            intent.putExtra("restaurant",string);
            startActivity(intent);
            finish();
        }
    }
    //creating activity for back pressing from phone
    public void onBackPressed() {
        //creating a alert dialog(for exit)
        final AlertDialog.Builder exitbuilder = new AlertDialog.Builder(SuperAdminRestaurant.this);
        //setting the alertdialog title
        exitbuilder.setTitle("Attention");
        //setting the body message
        exitbuilder.setMessage("Do You Want To Exit?");
        //setting the icon
        exitbuilder.setIcon(R.drawable.exit);
        //set state for cancelling state
        exitbuilder.setCancelable(true);

        //setting activity for positive state button
        exitbuilder.setPositiveButton("YES, Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        //setting activity for negative state button
        exitbuilder.setNegativeButton("NO, i don't", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        //alertdialog create
        AlertDialog mydialog=exitbuilder.create();
        //for working the alertdialog state
        mydialog.show();
    }
}
