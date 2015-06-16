package com.kate.teme;

import android.content.Context;
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
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class AdminContent extends ActionBarActivity {
    SharedPreferences mTemeprefferences;

    ListView adminList;
    Button adminSave;
    EditText adminDriver,adminCar;
    ArrayList<Driver> driver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_content);
        mTemeprefferences=getSharedPreferences(Constants.TEME_PREFERENCES, Context.MODE_PRIVATE);
        adminDriver=(EditText)findViewById(R.id.adminEntDriver);
        adminCar=(EditText)findViewById(R.id.adminEntCar);
        adminSave=(Button)findViewById(R.id.btnAdminSaveDriver);
        adminList=(ListView)findViewById(R.id.listAdminDrivers);
        driver=new ArrayList<Driver>();

//        if(mTemeprefferences.contains(Constants.TEME_DRIVER_LIST)) {
//            String jsonAllMembers=mTemeprefferences.getString(Constants.TEME_DRIVER_LIST,null);
//
//            Gson gson = new Gson();
//            Type listType = new TypeToken<ArrayList<Driver>>() {}.getType();
//            driver= gson.fromJson(jsonAllMembers, listType);
//            DriverAdapter adapters = new
//                    DriverAdapter(getApplicationContext(),
//                    R.layout.list_item, driver);
//            adminList.setAdapter(adapters);
//
//        }

        adminSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String driverstr=adminDriver.getText().toString();
                String carstr=adminCar.getText().toString();
                driver.add(new Driver(driverstr,carstr));

                DriverAdapter adapters = new DriverAdapter(getApplicationContext(),
                        R.layout.list_item, driver);
                adminList.setAdapter(adapters);
                Gson gson = new Gson();
                String jsonMembers= gson.toJson(driver);
                SharedPreferences.Editor editor = mTemeprefferences.edit();
                editor.putString(Constants.TEME_DRIVER_LIST,jsonMembers );
                editor.commit();

                adminDriver.setText("");
                adminCar.setText("");
                Toast.makeText(getApplicationContext(),"Saved",Toast.LENGTH_SHORT).show();

            }
        });


        setToolBar();
    }
    private void setToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarAdmin);
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


    public void onStop(){
        super.onStop();
        Gson gson = new Gson();
        String jsonMembers= gson.toJson(driver);
        SharedPreferences.Editor editor = mTemeprefferences.edit();
        editor.putString(Constants.TEME_DRIVER_LIST,jsonMembers );
        editor.commit();


    }

    public void onDestroy(){
        super.onDestroy();
    }
    public void onStart(){
        super.onStart();


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_admin_content, menu);
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
