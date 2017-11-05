package com.example.bappy.foodbank;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import java.util.Calendar;

public class Adminstaff extends AppCompatActivity {

    String name,resname,role;
    String datetime;
    TextView txt;

    private TextView startDateDisplay;
    private Button startPickDate;
    private Calendar startDate;
    static final int DATE_DIALOG_ID = 0;

    private TextView activeDateDisplay;
    private Calendar activeDate;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adminstaff_layout);
        progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);

        sharedPreferences=getSharedPreferences(getString(R.string.PREF_FILE), 0);
        editor=sharedPreferences.edit();

        name=getIntent().getExtras().getString("username");
        resname=getIntent().getExtras().getString("resname");
        role=getIntent().getExtras().getString("role");
        txt=(TextView)findViewById(R.id.name);
        txt.setText(name+" ("+role+")");

        /*  capture our View elements for the start date function   */
        startDateDisplay = (TextView) findViewById(R.id.date);
        startPickDate = (Button) findViewById(R.id.getdate);

        startDate = Calendar.getInstance();

        /* add a click listener to the button   */
        startPickDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDateDialog(startDateDisplay, startDate);
            }
        });
        updateDisplay(startDateDisplay, startDate);
    }



    private void updateDisplay(TextView dateDisplay, Calendar date) {
        datetime=date.get(Calendar.YEAR)+"-"+(date.get(Calendar.MONTH)+1)+"-"+date.get(Calendar.DAY_OF_MONTH);
        dateDisplay.setText(datetime);
    }

    public void showDateDialog(TextView dateDisplay, Calendar date) {
        activeDateDisplay = dateDisplay;
        activeDate = date;
        showDialog(DATE_DIALOG_ID);
    }

    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override

        public void onDateSet(android.widget.DatePicker view, int year,int monthOfYear, int dayOfMonth)
        {
            activeDate.set(Calendar.YEAR, year);
            activeDate.set(Calendar.MONTH, monthOfYear);
            activeDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDisplay(activeDateDisplay, activeDate);
            unregisterDateDisplay();
        }
    };

    private void unregisterDateDisplay() {
        activeDateDisplay = null;
        activeDate = null;
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this, dateSetListener, activeDate.get(Calendar.YEAR), activeDate.get(Calendar.MONTH), activeDate.get(Calendar.DAY_OF_MONTH));
        }
        return null;
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        super.onPrepareDialog(id, dialog);
        switch (id) {
            case DATE_DIALOG_ID:
                ((DatePickerDialog) dialog).updateDate(activeDate.get(Calendar.YEAR), activeDate.get(Calendar.MONTH), activeDate.get(Calendar.DAY_OF_MONTH));
                break;
        }
    }
    public void pending(View view){
        if(!isNetworkAvilabe())
            nointernet();
        else {
            Intent intent = new Intent(Adminstaff.this, PendingStaff.class);
            intent.putExtra("name",name);
            intent.putExtra("res_name", resname);
            intent.putExtra("role", "Staff");
            intent.putExtra("adminstaff", "2");
            startActivity(intent);
            finish();
        }
    }

    public void orderlist(View view){
        if(!isNetworkAvilabe())
            nointernet();
        else {
            new BackgroundtaskOrderlist().execute("O", name, resname, role, datetime);
        }
    }

    public void deliverylist(View view){
        if(!isNetworkAvilabe())
            nointernet();
        else {
            new BackgroundtaskOrderlist().execute("D", name, resname, role, datetime);
        }
    }
    public void allorder(View view){
        if(!isNetworkAvilabe())
            nointernet();
        else {
            new BackgroundtaskOrderlist().execute("A", name, resname, role, datetime);
        }
    }

    public class BackgroundtaskOrderlist extends AsyncTask<String,Void,Boolean>
    {

        String json_url;
        String JSON_STRING;
        String name2,role,datet,type,res,resu;

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Loading.Please Wait....");
            progressDialog.show();
            json_url="http://"+getString(R.string.ip_address)+"/FoodBank/dateorder.php";
        }

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                type=params[0];
                name2 = params[1];
                res=params[2];
                role=params[3];
                datet=params[4];
                URL url=new URL(json_url);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputstream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedwritter = new BufferedWriter(new OutputStreamWriter(outputstream, "UTF-8"));
                String postdata = URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode(type, "UTF-8") + "&" +
                        URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(name2, "UTF-8")+ "&" +
                        URLEncoder.encode("resname", "UTF-8") + "=" + URLEncoder.encode(res, "UTF-8") + "&" +
                        URLEncoder.encode("role", "UTF-8") + "=" + URLEncoder.encode(role, "UTF-8") + "&" +
                        URLEncoder.encode("date", "UTF-8") + "=" + URLEncoder.encode(datet, "UTF-8");
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
                resu= stringBuilder.toString().trim();
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
            if(!result)
                Toast.makeText(Adminstaff.this, "can't connect to the database", Toast.LENGTH_SHORT).show();
            else {
                Intent intent = new Intent(Adminstaff.this, StaffFoodOrder.class);
                intent.putExtra("type", type);
                intent.putExtra("username", name2);
                intent.putExtra("res", res);
                intent.putExtra("role", role);
                intent.putExtra("datet", datet);
                intent.putExtra("order_details", resu);
                intent.putExtra("staff_admin", "1");
                progressDialog.cancel();
                startActivity(intent);
                finish();
            }
        }
    }
    public void assignstaff(View view){
        if(!isNetworkAvilabe())
            nointernet();
        else {
            new Backgroundtaskassign().execute(resname,"staff");
        }
    }

    public void assignchef(View view){
        if(!isNetworkAvilabe())
            nointernet();
        else {
            new Backgroundtaskassign().execute(resname,"chef");
        }
    }


    public class Backgroundtaskassign extends AsyncTask<String,Void,String>
    {
        String json_url;
        String JSON_STRING;
        String res,type;

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Loading.Please Wait....");
            progressDialog.show();
            json_url="http://"+getString(R.string.ip_address)+"/FoodBank/ReadStaff.php";
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                res=params[0];
                type=params[1];
                URL url=new URL(json_url);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputstream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedwritter = new BufferedWriter(new OutputStreamWriter(outputstream, "UTF-8"));
                String postdata = URLEncoder.encode("res", "UTF-8") + "=" + URLEncoder.encode(res, "UTF-8")+ "&" +
                                            URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode(type, "UTF-8");
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
            progressDialog.cancel();
            if(type.equals("staff")) {
                Intent intent = new Intent(Adminstaff.this, AssignSatff.class);
                intent.putExtra("username", name);
                intent.putExtra("resname", resname);
                intent.putExtra("role", role);
                intent.putExtra("datet", datetime);
                intent.putExtra("assignst", result);
                startActivity(intent);
            }
            else if(type.equals("chef"))
            {
                Intent intent = new Intent(Adminstaff.this, AssignChef.class);
                intent.putExtra("username", name);
                intent.putExtra("resname", resname);
                intent.putExtra("role", role);
                intent.putExtra("datet", datetime);
                intent.putExtra("assignchef", result);
                startActivity(intent);
                //Toast.makeText(Adminstaff.this, type, Toast.LENGTH_SHORT).show();
            }
            finish();
        }
    }

    public void getfullprice(View view){
        if(!isNetworkAvilabe())
            nointernet();
        else {
            new BackgroundtaskOrderlist3().execute(name, resname, role, datetime);
        }
    }

    public class BackgroundtaskOrderlist3 extends AsyncTask<String,Void,Boolean>
    {

        String json_url;
        String JSON_STRING;
        String name2,role,datet,res,resu;

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Loading.Please Wait....");
            progressDialog.show();
            json_url="http://"+getString(R.string.ip_address)+"/FoodBank/FullPaid.php";
        }

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                name2 = params[0];
                res=params[1];
                role=params[2];
                datet=params[3];
                URL url=new URL(json_url);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputstream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedwritter = new BufferedWriter(new OutputStreamWriter(outputstream, "UTF-8"));
                String postdata = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(res, "UTF-8");
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
                resu= stringBuilder.toString().trim();
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
            if(result.equals("false"))
                Toast.makeText(Adminstaff.this, "can't connect to the database", Toast.LENGTH_SHORT).show();
            else {
                Intent intent = new Intent(Adminstaff.this, FullPaid.class);
                intent.putExtra("username", name2);
                intent.putExtra("role", role);
                intent.putExtra("datet", datet);
                intent.putExtra("res", res);
                intent.putExtra("order_details", resu);
                progressDialog.cancel();
                startActivity(intent);
                finish();
            }
        }
    }
    public void decoraterestaurant(View view){
        {
            if(!isNetworkAvilabe())
                nointernet();
            else {
                Intent intent = new Intent(Adminstaff.this, DecorateRestaurant.class);
                intent.putExtra("username", name);
                intent.putExtra("resname", resname);
                intent.putExtra("role", role);
                startActivity(intent);
                finish();
            }
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
        AlertDialog.Builder CheckBuild = new AlertDialog.Builder(Adminstaff.this);
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
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
                            editor.putBoolean(getString(R.string.SKIP),false);
                            startActivity(new Intent(Adminstaff.this, staff_login_resistor.class));
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
                            startActivity(new Intent(Adminstaff.this, ShowProfile.class));
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
                            startActivity(new Intent(Adminstaff.this, CreateNewRestaurant.class));
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
                            Intent intent = new Intent(Adminstaff.this, EditChangeProfile.class);
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

    //creating activity for back pressing from phone
    public void onBackPressed() {
        //creating a alert dialog(for exit)
        final AlertDialog.Builder exitbuilder = new AlertDialog.Builder(Adminstaff.this);
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
