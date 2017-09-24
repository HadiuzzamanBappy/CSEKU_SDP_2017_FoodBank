package com.example.bappy.foodbank;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

public class SuperAdminRestaurantStaffWithoutPending extends AppCompatActivity {

    String resname,name,type,allres;
    JSONObject jsonObject;
    JSONArray jsonArray;
    RestaurentAdapter restaurentAdapter;
    ListView listView;
    TextView txt;

    ArrayList<Staff> addpendingStaff;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.super_admin_restaurant_staff_without_pending);
        sharedPreferences=getSharedPreferences(getString(R.string.PREF_FILE), 0);
        editor=sharedPreferences.edit();

        resname=getIntent().getExtras().getString("restaurent_name");
        name=getIntent().getExtras().getString("username");
        type=getIntent().getExtras().getString("role");
        allres=getIntent().getExtras().getString("allres");

        txt=(TextView)findViewById(R.id.admintext);
        txt.setText(resname);

        addpendingStaff=new ArrayList<>();
        new BackgroundTask3().execute(resname);

        listView=(ListView)findViewById(R.id.lisview);
        restaurentAdapter=new RestaurentAdapter(this,R.layout.restaurant_staff_list,addpendingStaff);
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
                startActivity(new Intent(this, staff_login_resistor.class));
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

    public class Staff{
        String name,role,active;

        public Staff(String name, String role, String active) {
            this.name = name;
            this.role = role;
            this.active = active;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getActive() {
            return active;
        }

        public void setActive(String active) {
            this.active = active;
        }
    }
    public class RestaurentAdapter extends ArrayAdapter {

        ArrayList<Staff> list = new ArrayList();
        Context ct;
        String st;

        public RestaurentAdapter(@NonNull Context context, @LayoutRes int resource,ArrayList<Staff> string) {
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
                restaurentview = layoutInflater.inflate(R.layout.restaurant_staff_list, parent, false);
                restaurentHolder = new RestaurentHolder();
                restaurentHolder.name = (TextView) restaurentview.findViewById(R.id.name);
                restaurentHolder.type = (TextView) restaurentview.findViewById(R.id.type);
                restaurentHolder.active = (TextView) restaurentview.findViewById(R.id.active);
                restaurentview.setTag(restaurentHolder);
            } else {
                restaurentHolder = (RestaurentHolder) restaurentview.getTag();
            }

            final Staff restaurent1 = (Staff) this.getItem(position);
            restaurentHolder.name.setText(restaurent1.getName());
            restaurentHolder.type.setText(restaurent1.getRole());
            restaurentHolder.active.setText(restaurent1.getActive());
            restaurentview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(),restaurent1.getName(), Toast.LENGTH_SHORT).show();
                    st = restaurent1.getName();
                }
            });
            return restaurentview;
        }

        class RestaurentHolder {
            TextView name,type,active;
        }
    }
    class BackgroundTask3 extends AsyncTask<String,Void,Boolean>
    {

        String json_url;
        String JSON_STRING;

        @Override
        protected void onPreExecute() {
            json_url="http://"+getString(R.string.ip_address)+"/FoodBank/superadminreadstaff.php";
        }

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                String type2 = params[0];
                URL url=new URL(json_url);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputstream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedwritter = new BufferedWriter(new OutputStreamWriter(outputstream, "UTF-8"));
                String postdata = URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode("NA", "UTF-8")
                        + "&" + URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(type2, "UTF-8");
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
                String json_staff_string= stringBuilder.toString().trim();
                jsonObject=new JSONObject(json_staff_string);
                jsonArray=jsonObject.getJSONArray("Server_response");

                int count=0;
                String name,type,active;
                while(count<jsonArray.length())
                {
                    JSONObject jo=jsonArray.getJSONObject(count);
                    name=jo.getString("name");
                    type=jo.getString("type");
                    active=jo.getString("activerole");
                    Staff restaurent=new Staff(name,type,active);
                    addpendingStaff.add(restaurent);
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
                Toast.makeText(SuperAdminRestaurantStaffWithoutPending.this, "can't connect to the database", Toast.LENGTH_SHORT).show();
            restaurentAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SuperAdminRestaurantStaffWithoutPending.this, SuperAdminPendingRestaurent.class);
        intent.putExtra("username", name);
        intent.putExtra("role", "SuperAdmin");
        intent.putExtra("restaurant",allres);
        startActivity(intent);
        finish();
    }
}
