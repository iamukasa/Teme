package com.kate.teme;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Button;
import android.widget.EditText;

import java.lang.reflect.Field;


public class EnterAdminDetails extends ActionBarActivity {
    EditText phnNo,paybillNo,farRate;
    Button seveDEt;
    SharedPreferences mTemeprefferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTemeprefferences=getSharedPreferences(Constants.TEME_PREFERENCES, Context.MODE_PRIVATE);

        setContentView(R.layout.activity_enter_admin_details);
        phnNo=(EditText)findViewById(R.id.adminEntPhnNo);
        paybillNo=(EditText)findViewById(R.id.adminEntPayBill);
        farRate=(EditText)findViewById(R.id.adminEntRate);
        seveDEt=(Button)findViewById(R.id.saveAdminDetails);
        seveDEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String a=paybillNo.getText().toString();
                String b=phnNo.getText().toString();
                String c=farRate.getText().toString();
                SharedPreferences.Editor editor = mTemeprefferences.edit();
                editor.putString(Constants.TEME_ADMIN_DETAILS_PAYBILL,a );
                editor.commit();

                SharedPreferences.Editor editor2 = mTemeprefferences.edit();
                editor.putString(Constants.TEME_ADMIN_DETAILS_PHONE,b);
                editor.commit();

                SharedPreferences.Editor editor3 = mTemeprefferences.edit();
                editor.putString(Constants.TEME_ADMIN_DETAILS_FARE_RATE,c );
                editor.commit();

                paybillNo.setText("");
                phnNo.setText("");
                farRate.setText("");


                Intent iu= new Intent(getApplicationContext(),AdminContent.class);
                startActivity(iu);
                finish();
                setToolBar();
            }
        });

    }
    private void setToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarAdminDetails);
        if (toolbar != null) {

            setSupportActionBar(toolbar);
            setUpActionbar();
            getOverflowMenu();

            toolbar.setBackgroundColor(getResources().getColor(R.color.blue));
            toolbar.setTitleTextColor(getResources().getColor(R.color.white_pure));






        }

    }

    private void setUpActionbar() {
        if(getSupportActionBar()!=null){
            ActionBar bar = getSupportActionBar();
            bar.setTitle(getResources().getString(R.string.app_name));
            bar.setHomeButtonEnabled(false);
            bar.setDisplayShowHomeEnabled(false);
            bar.setDisplayHomeAsUpEnabled(false);
            bar.setDisplayShowTitleEnabled(true);



            bar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);

        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_enter_driver_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void getOverflowMenu() {

        try {
            ViewConfiguration config = ViewConfiguration.get(this);

            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");

            if(menuKeyField != null) {

                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
                menuKeyField.isSynthetic();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
