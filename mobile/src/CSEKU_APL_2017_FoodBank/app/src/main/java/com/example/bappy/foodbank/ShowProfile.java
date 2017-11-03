package com.example.bappy.foodbank;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ShowProfile extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    String name,resname,pass,type;
    TextView tname,tresname,tpass,ttype;
    LinearLayout linearLayout;
    SharedPreferences.Editor editor;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_profile_layout);
        progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);

        sharedPreferences=getSharedPreferences(getString(R.string.PREF_FILE),0);
        editor=sharedPreferences.edit();

        tname=(TextView)findViewById(R.id.name_id);
        tpass=(TextView)findViewById(R.id.pass_id);
        tresname=(TextView)findViewById(R.id.restaurant_id);
        ttype=(TextView)findViewById(R.id.type_id);
        linearLayout= (LinearLayout) findViewById(R.id.showrestaurant);

        type=sharedPreferences.getString(getString(R.string.TYPE),"None");
        name=sharedPreferences.getString(getString(R.string.NAME),"None");
        pass=sharedPreferences.getString(getString(R.string.PASSWORD),"None");
        resname=sharedPreferences.getString(getString(R.string.RESTAURANT_NAME),"None");
        if(type.equals("User") || type.equals("Superadmin"))
        {
            linearLayout.setVisibility(View.GONE);
            tname.setText(name);
            tpass.setText(pass);
            ttype.setText(type);
        }
        else
        {
            tresname.setText(resname);
            tname.setText(name);
            tpass.setText(pass);
            ttype.setText(type);
        }
    }
    public void okgo(View view)
    {
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        if(type.equals("Admin") || type.equals("Staff"))
        menuInflater.inflate(R.menu.menu_staff_admin,menu);
        else if(type.equals("Superadmin"))
        menuInflater.inflate(R.menu.menu_item,menu);
        else
        menuInflater.inflate(R.menu.menu_item_super,menu);
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
                            startActivity(new Intent(ShowProfile.this, staff_login_resistor.class));
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
                            startActivity(new Intent(ShowProfile.this, staff_login_resistor.class));
                            finish();
                        }
                    };
                    Handler handler2 = new Handler();
                    handler2.postDelayed(progressrunnable2, 6000);
                }
                return true;
            case R.id.my_profile:
                Toast.makeText(this, "Your Are Already in Your Profile Dear "+name, Toast.LENGTH_SHORT).show();
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
                            startActivity(new Intent(ShowProfile.this, CreateNewRestaurant.class));
                            finish();
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
                            Intent intent = new Intent(ShowProfile.this, EditChangeProfile.class);
                            intent.putExtra("op_type", "Edit");
                            startActivity(intent);
                            finish();
                        }
                    };
                    Handler handler5 = new Handler();
                    handler5.postDelayed(progressrunnable5, 6000);
                }
                return true;
            case R.id.myorder:
                if(!isNetworkAvilabe())
                    nointernet();
                else {
                    startActivity(new Intent(this, UserFoodDetails.class));
                    finish();
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
        AlertDialog.Builder CheckBuild = new AlertDialog.Builder(ShowProfile.this);
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
}
