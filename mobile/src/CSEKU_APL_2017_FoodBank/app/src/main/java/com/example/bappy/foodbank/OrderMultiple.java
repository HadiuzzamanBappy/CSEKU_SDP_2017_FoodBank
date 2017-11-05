package com.example.bappy.foodbank;

import android.app.ProgressDialog;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

import static android.R.attr.y;

public class OrderMultiple extends AppCompatActivity {

    RestaurentFoodAdapter restaurentfoodAdapter;
    ListView listView;
    ArrayList<RestaurentFood> addRestaurantFood;

    String deliverytype="",dd,mm,yyyy,allprice,restaurent_name,clientorderno;
    TextView client,trestaurent;
    EditText phonenumber,address;
    Spinner spinnerday,spinnermonth,spinneryear;
    RadioGroup rg;
    RadioButton rb;

    int fullprice=0;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Boolean save_login;
    String name,resname,pass,type;

    String[] quantityArray;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_multiple_layout);
        progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);

        sharedPreferences=getSharedPreferences(getString(R.string.PREF_FILE), 0);
        editor=sharedPreferences.edit();
        save_login=sharedPreferences.getBoolean(getString(R.string.SAVE_LOGIN),false);
        type=sharedPreferences.getString(getString(R.string.TYPE),"None");
        name=sharedPreferences.getString(getString(R.string.NAME),"None");
        pass=sharedPreferences.getString(getString(R.string.PASSWORD),"None");
        resname=sharedPreferences.getString(getString(R.string.RESTAURANT_NAME),"None");

        trestaurent=(TextView)findViewById(R.id.restaurant);
        client=(TextView)findViewById(R.id.name);

        phonenumber=(EditText)findViewById(R.id.cphone);
        address=(EditText)findViewById(R.id.caddress);

        rg=(RadioGroup)findViewById(R.id.radiogroup);

        spinnerday=(Spinner)findViewById(R.id.spinnerday);
        spinnermonth=(Spinner)findViewById(R.id.spinnermonth);
        spinneryear=(Spinner)findViewById(R.id.spinneryear);

        Bundle bundle=getIntent().getExtras();
        restaurent_name = getIntent().getExtras().getString("restaurent_name");
        addRestaurantFood = (ArrayList<RestaurentFood>)bundle.getSerializable("addRestaurantFood");

        trestaurent.setText(restaurent_name);
        client.setText(name);

        listView = (ListView) findViewById(R.id.lisview);
        restaurentfoodAdapter = new RestaurentFoodAdapter(this, R.layout.order_multiple_layout,addRestaurantFood);
        listView.setAdapter(restaurentfoodAdapter);

        int size=addRestaurantFood.size();
        quantityArray=new String[size];

        ArrayAdapter arrayAdapter2=ArrayAdapter.createFromResource(this,R.array.day,android.R.layout.simple_spinner_item);
        arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerday.setAdapter(arrayAdapter2);
        spinnerday.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView mytext=(TextView)view;
                dd=(String)mytext.getText();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter arrayAdapter3=ArrayAdapter.createFromResource(this,R.array.month,android.R.layout.simple_spinner_item);
        arrayAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnermonth.setAdapter(arrayAdapter3);
        spinnermonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView mytext=(TextView)view;
                mm=(String)mytext.getText();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter arrayAdapter4=ArrayAdapter.createFromResource(this,R.array.year,android.R.layout.simple_spinner_item);
        arrayAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinneryear.setAdapter(arrayAdapter4);
        spinneryear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView mytext=(TextView)view;
                yyyy=(String)mytext.getText();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void rbclick(View v){
        int radiobuttonid=rg.getCheckedRadioButtonId();
        rb=(RadioButton)findViewById(radiobuttonid);
        if(rb.getText().equals("Yes")){
            deliverytype="YES";
        }
        else
            deliverytype="NO";
    }

    public void order(View view){
        int full=0;
        for(RestaurentFood res:addRestaurantFood)
        {
            String oprice=res.getPrice();
            String quan=res.getQuantity();

            int quantityy = Integer.parseInt(quan);
            int price = Integer.parseInt(oprice);
            full += (price * quantityy);
        }
        fullprice=full;
        if(phonenumber.getText().toString().equals("") || address.getText().toString().equals("") || yyyy.equals("YYYY") || mm.equals("MM") || dd.equals("DD"))
            Toast.makeText(this, "Please Fill All The Field", Toast.LENGTH_SHORT).show();
        else {
            String client = name;
            String phone = phonenumber.getText().toString();
            String d = dd;
            String m = mm;
            String y = yyyy;
            String addrs = address.getText().toString();
            String date = y + "-" + m + "-" + d;
            String type = "order";
            allprice = Integer.toString(fullprice);
           // Toast.makeText(this, client+" "+phone+" "+date+" "+addrs+" "+allprice, Toast.LENGTH_SHORT).show();

            progressDialog.setMessage("Please Wait....");
            progressDialog.show();

            new OrderBackground().execute(type,client,phone,date,addrs,deliverytype,allprice);
        }
    }
    public class OrderBackground extends AsyncTask<String,Void,Boolean> {

        String client,phone,date,allprice,addrs,delitype,result;

        @Override
        protected Boolean doInBackground(String... params) {
            String  type= params[0];
            String loginurl = "http://"+getString(R.string.ip_address)+"/FoodBank/OrderPerson.php";
            String loginurl2 = "http://"+getString(R.string.ip_address)+"/FoodBank/OrderPersonFood.php";
            if (type.equals("order")) {
                try {
                    client = params[1];
                    phone = params[2];
                    date = params[3];
                    addrs = params[4];
                    delitype = params[5];
                    allprice=params[6];

                        URL url = new URL(loginurl);
                        HttpURLConnection httpurlconnection = (HttpURLConnection) url.openConnection();
                        httpurlconnection.setRequestMethod("POST");
                        httpurlconnection.setDoOutput(true);
                        httpurlconnection.setDoInput(true);
                        OutputStream outputstream = httpurlconnection.getOutputStream();
                        BufferedWriter bufferedwritter = new BufferedWriter(new OutputStreamWriter(outputstream, "UTF-8"));
                        String postdata = URLEncoder.encode("clientname", "UTF-8") + "=" + URLEncoder.encode(client, "UTF-8") + "&"
                                + URLEncoder.encode("phonenumber", "UTF-8") + "=" + URLEncoder.encode(phone, "UTF-8") + "&"
                                + URLEncoder.encode("datetime", "UTF-8") + "=" + URLEncoder.encode(date, "UTF-8") + "&"
                                + URLEncoder.encode("address", "UTF-8") + "=" + URLEncoder.encode(addrs, "UTF-8") + "&"
                                + URLEncoder.encode("delivery", "UTF-8") + "=" + URLEncoder.encode(delitype, "UTF-8") + "&"
                                + URLEncoder.encode("restaurant", "UTF-8") + "=" + URLEncoder.encode(restaurent_name, "UTF-8") + "&"
                                + URLEncoder.encode("price", "UTF-8") + "=" + URLEncoder.encode(allprice, "UTF-8");
                        bufferedwritter.write(postdata);
                        bufferedwritter.flush();
                        bufferedwritter.close();
                        outputstream.close();
                        InputStream inputstream = httpurlconnection.getInputStream();
                        BufferedReader bufferdreader = new BufferedReader(new InputStreamReader(inputstream, "iso-8859-1"));
                        result = "";
                        String line = "";
                        while ((line = bufferdreader.readLine()) != null) {
                            result += line;
                        }
                        bufferdreader.close();
                        inputstream.close();
                        httpurlconnection.disconnect();
                        if(!result.equals("false"))
                        {
                            for(RestaurentFood res:addRestaurantFood) {
                                String name=res.getName();
                                String oprice = res.getPrice();
                                String quan = res.getQuantity();
                            URL url2 = new URL(loginurl2);
                            HttpURLConnection httpurlconnection2 = (HttpURLConnection) url2.openConnection();
                            httpurlconnection2.setRequestMethod("POST");
                            httpurlconnection2.setDoOutput(true);
                            httpurlconnection2.setDoInput(true);
                            OutputStream outputstream2 = httpurlconnection2.getOutputStream();
                            BufferedWriter bufferedwritter2 = new BufferedWriter(new OutputStreamWriter(outputstream2, "UTF-8"));
                            String postdata2 =URLEncoder.encode("restaurantname", "UTF-8") + "=" + URLEncoder.encode(restaurent_name, "UTF-8") + "&"
                                    +URLEncoder.encode("clientid", "UTF-8") + "=" + URLEncoder.encode(result, "UTF-8") + "&"
                                    + URLEncoder.encode("foodname", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8") + "&"
                                    + URLEncoder.encode("quantity", "UTF-8") + "=" + URLEncoder.encode(quan, "UTF-8");
                            bufferedwritter2.write(postdata2);
                            bufferedwritter2.flush();
                            bufferedwritter2.close();
                            outputstream2.close();
                            InputStream inputstream2 = httpurlconnection2.getInputStream();
                            BufferedReader bufferdreader2 = new BufferedReader(new InputStreamReader(inputstream2, "iso-8859-1"));
                            String result2 = "";
                            String line2 = "";
                            while ((line2 = bufferdreader2.readLine()) != null) {
                                result2 += line2;
                            }
                            bufferdreader2.close();
                            inputstream2.close();
                            httpurlconnection2.disconnect();
                            }
                        }
                        return true;
                } catch(MalformedURLException e){
                    e.printStackTrace();
                } catch(IOException e){
                    e.printStackTrace();
                }
            }
            return false;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(Boolean resul) {
           // Toast.makeText(OrderMultiple.this, result, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(OrderMultiple.this, SuccessOrderMultiple.class);
            intent.putExtra("client", name);
            intent.putExtra("restaurent_name", restaurent_name);
            intent.putExtra("phone", phonenumber.getText().toString());
            intent.putExtra("date", yyyy+"-"+mm+"-"+dd);
            intent.putExtra("addrs", address.getText().toString());
            intent.putExtra("delitype", deliverytype);
            intent.putExtra("allprice", allprice);
            Bundle bundle=new Bundle();
            bundle.putSerializable("addRestaurantFood",addRestaurantFood);
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
        }

        @Override
        protected void onProgressUpdate (Void...values){
            super.onProgressUpdate(values);
        }
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
                restaurentfoodview=layoutInflater.inflate(R.layout.multi_order_final,parent,false);
                restaurentfoodHolder=new RestaurentfoodHolder();
                restaurentfoodHolder.name=(TextView)restaurentfoodview.findViewById(R.id.name);
                restaurentfoodHolder.price=(TextView)restaurentfoodview.findViewById(R.id.price);
                restaurentfoodHolder.spinner=(Spinner)restaurentfoodview.findViewById(R.id.spinner);
                restaurentfoodview.setTag(restaurentfoodHolder);
            }
            else
            {
                restaurentfoodHolder=(RestaurentfoodHolder) restaurentfoodview.getTag();
            }

            final RestaurentFood restaurentfood=(RestaurentFood) this.getItem(pos);
            restaurentfoodHolder.name.setText(restaurentfood.getName());
            restaurentfoodHolder.price.setText(restaurentfood.getPrice());
            ArrayAdapter arrayAdapter=ArrayAdapter.createFromResource(OrderMultiple.this,R.array.quantity,android.R.layout.simple_spinner_item);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            restaurentfoodHolder.spinner.setAdapter(arrayAdapter);
            restaurentfoodHolder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    TextView mytext=(TextView)view;
                    String quantity=(String)mytext.getText();
                    restaurentfood.setQuantity(quantity);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            return restaurentfoodview;
        }

        class RestaurentfoodHolder
        {
            TextView name,price;
            Spinner spinner;
        }
    }
        //creating activity for back pressing from phone
        public void onBackPressed() {
            Intent intent = new Intent(OrderMultiple.this, DisplayRestaurentFoodList.class);
            intent.putExtra("restaurent_name", restaurent_name);
            startActivity(intent);
            finish();
        }
}
