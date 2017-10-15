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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

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

import static com.example.bappy.foodbank.R.drawable.food;

public class SuccessOrderMultiple extends AppCompatActivity {

    RestaurentFoodAdapter restaurentfoodAdapter;
    ListView listView;
    ArrayList<RestaurentFood> addRestaurantFood;

    TextView tclient,trestaurent,tphone,tdate,taddrs,tdelitype,tprice;
    String client,restaurent_name,phone,date,addrs,delitype,price;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Boolean save_login;

    String name,resname,pass,type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.success_order_multiple_layout);

        sharedPreferences=getSharedPreferences(getString(R.string.PREF_FILE), 0);
        editor=sharedPreferences.edit();
        save_login=sharedPreferences.getBoolean(getString(R.string.SAVE_LOGIN),false);

        type=sharedPreferences.getString(getString(R.string.TYPE),"None");
        name=sharedPreferences.getString(getString(R.string.NAME),"None");
        pass=sharedPreferences.getString(getString(R.string.PASSWORD),"None");
        resname=sharedPreferences.getString(getString(R.string.RESTAURANT_NAME),"None");

        Bundle bundle=getIntent().getExtras();
        addRestaurantFood = (ArrayList<RestaurentFood>)bundle.getSerializable("addRestaurantFood");

        client=getIntent().getExtras().getString("client");
        restaurent_name = getIntent().getExtras().getString("restaurent_name");
        phone=getIntent().getExtras().getString("phone");
        date=getIntent().getExtras().getString("date");
        addrs=getIntent().getExtras().getString("addrs");
        delitype=getIntent().getExtras().getString("delitype");
        price=getIntent().getExtras().getString("allprice");
        tclient=(TextView)findViewById(R.id.name);
        trestaurent=(TextView)findViewById(R.id.restaurent);
        tphone=(TextView)findViewById(R.id.phone);
        tdate=(TextView)findViewById(R.id.deliverydate);
        taddrs=(TextView)findViewById(R.id.address);
        tdelitype=(TextView)findViewById(R.id.deliverytype);
        tprice=(TextView)findViewById(R.id.price);
        tclient.setText("Name : "+client);
        trestaurent.setText("Restaurant : "+restaurent_name);
        tphone.setText("Phone Number : "+phone);
        tdate.setText("Delivery Date : "+date);
        taddrs.setText("Address : "+addrs);
        tdelitype.setText("Home Delivery :"+delitype);
        tprice.setText("Price : "+price);

        listView = (ListView) findViewById(R.id.lisview);
        restaurentfoodAdapter = new RestaurentFoodAdapter(this, R.layout.success_order_multiple_layout,addRestaurantFood);
        listView.setAdapter(restaurentfoodAdapter);
    }

    public class RestaurentFoodAdapter extends ArrayAdapter {
        ArrayList<RestaurentFood> list=new ArrayList();
        Context ct;
        String ffood,fprice;

        public RestaurentFoodAdapter(@NonNull Context context, @LayoutRes int resource, ArrayList<RestaurentFood> string) {
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
        public View getView(final int pos, @Nullable View convertView, @NonNull ViewGroup parent) {

            View restaurentfoodview;
            restaurentfoodview=convertView;
            RestaurentfoodHolder restaurentfoodHolder;
            if(restaurentfoodview==null)
            {
                LayoutInflater layoutInflater=(LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                restaurentfoodview=layoutInflater.inflate(R.layout.success_order_multiple_layout_for_listview,parent,false);
                restaurentfoodHolder=new RestaurentfoodHolder();
                restaurentfoodHolder.name=(TextView)restaurentfoodview.findViewById(R.id.name);
                restaurentfoodHolder.price=(TextView)restaurentfoodview.findViewById(R.id.price);
                restaurentfoodHolder.quantity=(TextView)restaurentfoodview.findViewById(R.id.quantity);
                restaurentfoodview.setTag(restaurentfoodHolder);
            }
            else
            {
                restaurentfoodHolder=(RestaurentfoodHolder) restaurentfoodview.getTag();
            }

            final RestaurentFood restaurentfood=(RestaurentFood) this.getItem(pos);
            restaurentfoodHolder.name.setText(restaurentfood.getName());
            restaurentfoodHolder.price.setText(restaurentfood.getPrice());
            restaurentfoodHolder.quantity.setText(restaurentfood.getQuantity());
            return restaurentfoodview;
        }

        class RestaurentfoodHolder
        {
            TextView name,price,quantity;
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
                editor.clear();
                editor.commit();
                startActivity(new Intent(this, staff_login_resistor.class));
                finish();
                return true;
            case R.id.LogIn:
                startActivity(new Intent(this, staff_login_resistor.class));
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
            case R.id.delete_profile:
                AlertDialog.Builder alert =new AlertDialog.Builder(this);
                alert.setTitle("Attention");
                alert.setMessage("Are You Sure??");
                alert.setCancelable(true);
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new BackgroundTask3().execute("Delete",name,name,resname,type,pass,pass);
                    }
                });
                alert.setNegativeButton("No,later", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog al=alert.create();
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
            editor.clear();
            editor.commit();
            Intent intent = new Intent(SuccessOrderMultiple.this, staff_login_resistor.class);
            startActivity(intent);
            finish();
        }
    }
    public void homepage(View view){
        Intent intent = new Intent(this, DisplayRestaurentFoodList.class);
        intent.putExtra("restaurent_name", restaurent_name);
        startActivity(intent);
        finish();
    }
    public void onBackPressed(View view){
        Intent intent = new Intent(this, DisplayRestaurentFoodList.class);
        intent.putExtra("restaurent_name", restaurent_name);
        startActivity(intent);
        finish();
    }
}
