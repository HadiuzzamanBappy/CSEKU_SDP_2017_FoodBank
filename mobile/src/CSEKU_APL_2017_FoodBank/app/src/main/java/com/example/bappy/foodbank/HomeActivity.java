package com.example.bappy.foodbank;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
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

import static com.example.bappy.foodbank.R.id.Logout;
import static com.example.bappy.foodbank.R.id.default_activity_button;
import static com.example.bappy.foodbank.R.id.my_profile;

public class HomeActivity extends AppCompatActivity {

    //Creating some Button which can reference layout Button to set Their Activity
    Button exit;
    TextView logger;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Boolean save_login;
    String name,resname,pass,type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        sharedPreferences=getSharedPreferences(getString(R.string.PREF_FILE), 0);
        editor=sharedPreferences.edit();
        save_login=sharedPreferences.getBoolean(getString(R.string.SAVE_LOGIN),false);
        type=sharedPreferences.getString(getString(R.string.TYPE),"None");
        name=sharedPreferences.getString(getString(R.string.NAME),"None");
        pass=sharedPreferences.getString(getString(R.string.PASSWORD),"None");
        resname=sharedPreferences.getString(getString(R.string.RESTAURANT_NAME),"None");

        logger =(TextView)findViewById(R.id.logger);
        if(type.equals("User"))
            logger.setText(name+"("+type+")");
        if(!isNetworkAvilabe())
            nointernet();
        else {
            //Initial the Buttons Id
            exit = (Button) findViewById(R.id.exit);

            exit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //creating a alert dialog(for exit)
                    AlertDialog.Builder exitbuilder = new AlertDialog.Builder(HomeActivity.this);
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
                    AlertDialog mydialog = exitbuilder.create();
                    //for working the alertdialog state
                    mydialog.show();
                }
            });
        }

    }

    //for searching food we call this onclick function
    public void parsejsonfood(View view){
        if(!isNetworkAvilabe())
            nointernet();
        else {
            Intent intent = new Intent(HomeActivity.this, DisplayFoodList.class);
            startActivity(intent);
            finish();
        }
    }

    public void parsejsonrestaurent(View view){
        if(!isNetworkAvilabe())
            nointernet();
        else {
            Intent intent = new Intent(HomeActivity.this, DisplayRestaurentListView.class);
            startActivity(intent);
            finish();
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
            AlertDialog.Builder CheckBuild = new AlertDialog.Builder(HomeActivity.this);
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
                startActivity(new Intent(HomeActivity.this, staff_login_resistor.class));
                finish();
                return true;
            case R.id.LogIn:
                startActivity(new Intent(HomeActivity.this, staff_login_resistor.class));
                finish();
                return true;
            case R.id.my_profile:
                startActivity(new Intent(HomeActivity.this, ShowProfile.class));
                return true;
            case R.id.new_restaurant:
                startActivity(new Intent(HomeActivity.this, CreateNewRestaurant.class));
                return true;
            case R.id.edit_profile:
                Intent intent=new Intent(HomeActivity.this, EditChangeProfile.class);
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
                        new BackgroundTask2().execute("Delete",name,name,resname,type,pass,pass);
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

    class BackgroundTask2 extends AsyncTask<String,Void,Boolean> {
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
            Intent intent = new Intent(HomeActivity.this, staff_login_resistor.class);
            startActivity(intent);
            finish();
        }
    }
        //creating activity for back pressing from phone
        public void onBackPressed() {
            //creating a alert dialog(for exit)
            final AlertDialog.Builder exitbuilder = new AlertDialog.Builder(HomeActivity.this);
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