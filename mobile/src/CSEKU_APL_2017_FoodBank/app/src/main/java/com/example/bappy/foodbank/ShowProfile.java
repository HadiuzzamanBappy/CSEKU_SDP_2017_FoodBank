package com.example.bappy.foodbank;

import android.content.Intent;
import android.content.SharedPreferences;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_profile_layout);
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
                editor.clear();
                editor.commit();
                startActivity(new Intent(this, staff_login_resistor.class));
                finish();
                return true;
            case R.id.my_profile:
                Toast.makeText(this, "Your Are Already Seeing Your Profile Dear "+name, Toast.LENGTH_SHORT).show();
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
}
