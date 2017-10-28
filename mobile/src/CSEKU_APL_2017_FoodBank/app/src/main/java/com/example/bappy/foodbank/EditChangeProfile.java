package com.example.bappy.foodbank;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
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

import static android.R.id.edit;

public class EditChangeProfile extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    String name,resname,pass,type,op_type;
    TextView tresname,ttype;
    LinearLayout linearLayout;
    EditText tname,tpass,tnewpass,tnewrepass;

    SharedPreferences.Editor editor;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_change_profile_layout);
        progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);

        sharedPreferences=getSharedPreferences(getString(R.string.PREF_FILE),0);
        editor=sharedPreferences.edit();

        op_type=getIntent().getExtras().getString("op_type");

        tname=(EditText) findViewById(R.id.name_id);
        tpass=(EditText) findViewById(R.id.old_password);
        tnewpass=(EditText) findViewById(R.id.new_password);
        tnewrepass=(EditText) findViewById(R.id.new_password2);
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
            ttype.setText(type);
        }
        else
        {
            tresname.setText(resname);
            tname.setText(name);
            ttype.setText(type);
        }
    }
    public void okchange(View view)
    {
        final String tname2=tname.getText().toString();
        String pass2=tpass.getText().toString();
        final String newpass2=tnewpass.getText().toString();
        String newrepass2=tnewrepass.getText().toString();
        if(pass2.equals("") || newpass2.equals("") || newrepass2.equals(""))
            Toast.makeText(this, "Please fill All the Field", Toast.LENGTH_SHORT).show();
        else {
            if (pass2.equals(pass)) {
                if (newpass2.equals(newrepass2)) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(EditChangeProfile.this);
                    alert.setTitle("Attention");
                    alert.setMessage("Are You sure?");
                    alert.setCancelable(true);
                    alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            progressDialog.setMessage("Editing.Please Wait....");
                            progressDialog.show();
                            if (type.equals("User") || type.equals("SuperAdmin"))
                                new BackgroundTask2().execute(op_type, name, tname2, "None", type, pass, newpass2);
                            else
                                new BackgroundTask2().execute(op_type, name, tname2, resname, type, pass, newpass2);
                        }
                    });
                    alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            finish();
                        }
                    });
                    //alertdialog create
                    AlertDialog mydialog = alert.create();
                    //for working the alertdialog state
                    mydialog.show();
                } else
                    Toast.makeText(this, "Your New Retyped Password Didn't Match", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(this, "You Input Incorect Master Password", Toast.LENGTH_SHORT).show();
        }
    }
    class BackgroundTask2 extends AsyncTask<String,Void,Boolean> {
        String json_url;
        String JSON_STRING;
        String newpass,newname,op_type,resul,res_name,role;

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
            if(result) {
                progressDialog.cancel();
                if (op_type.equals("Edit")) {
                    Toast.makeText(EditChangeProfile.this, resul, Toast.LENGTH_SHORT).show();
                    editor.clear();
                    editor.commit();
                    editor.putBoolean(getString(R.string.SAVE_LOGIN), true);
                    editor.putString(getString(R.string.NAME), newname);
                    editor.putString(getString(R.string.PASSWORD), newpass);
                    editor.putString(getString(R.string.RESTAURANT_NAME), res_name);
                    editor.putString(getString(R.string.TYPE), role);
                    editor.commit();
                    Intent intent = new Intent(EditChangeProfile.this, ShowProfile.class);
                    startActivity(intent);
                    finish();
                } else {
                    editor.clear();
                    editor.commit();
                    Intent intent = new Intent(EditChangeProfile.this, staff_login_resistor.class);
                    startActivity(intent);
                    finish();
                }
            }
            else
                Toast.makeText(EditChangeProfile.this, "Failed", Toast.LENGTH_SHORT).show();
        }
    }
}
