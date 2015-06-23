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
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.gson.Gson;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class AdminContent extends ActionBarActivity {
    SharedPreferences mTemeprefferences;

    ListView adminList;
    Button adminSave;
    EditText adminDriver,adminCar;
    ArrayList<Driver> driver;
    Firebase myFirebaseRef;


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


        Firebase.setAndroidContext(getApplicationContext());

        myFirebaseRef = new Firebase("https://teme.firebaseio.com/");




        fillFromOnline();

        adminSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String driverstr = adminDriver.getText().toString();
                String carstr = adminCar.getText().toString();

                driver.add(new Driver(driverstr, carstr));

                myFirebaseRef.child("DriversList").child("Driver name").push().setValue(driverstr);
                myFirebaseRef.child("DriversList").child("Drivers car").push().setValue(carstr);
                myFirebaseRef.child("DriversList").child("Drivers availability").push().setValue("No");

                DriverAdapter adapters = new DriverAdapter(getApplicationContext(),
                        R.layout.list_item, driver);
                adminList.setAdapter(adapters);

                Gson gson = new Gson();
                String jsonMembers = gson.toJson(driver);
                SharedPreferences.Editor editor = mTemeprefferences.edit();
                editor.putString(Constants.TEME_DRIVER_LIST, jsonMembers);
                editor.commit();

                adminDriver.setText("");
                adminCar.setText("");
                Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();

            }
        });


        setToolBar();

    }

    private void fillFromOnline() {
 myFirebaseRef.child("DriversList").addValueEventListener(new ValueEventListener() {
     @Override
     public void onDataChange(DataSnapshot snapshot) {



//         GenericTypeIndicator<List<String>> t = new GenericTypeIndicator<List<String>>() {
//         };
//         GenericTypeIndicator<List<String>> t2 = new GenericTypeIndicator<List<String>>() {};
//         List<String>  namelist =  snapshot.child("Driver name").getValue(t);
//         List<String>  carlist =  snapshot.child("Drivers car").getValue(t2);
//         if (namelist!=null & carlist!=null){
//
//
//         for(int wee=0;wee<namelist.size();wee++){
//             driver.add(new Driver(namelist.get(wee).toString(),carlist.get(wee).toString()));
//         }

         String name=  snapshot.child("Driver name").getValue().toString();
         String car= snapshot.child("Drivers car").getValue().toString();
         Toast.makeText(getApplicationContext(),name,Toast.LENGTH_SHORT).show();
         Toast.makeText(getApplicationContext(),car,Toast.LENGTH_SHORT).show();
          Toast.makeText(getApplicationContext(),snapshot.getKey(),Toast.LENGTH_SHORT).show();



         System.out.println(name);
         System.out.println(car);
         driver.add(new Driver(name, car));
         DriverAdapter adapters = new DriverAdapter(getApplicationContext(),
                 R.layout.list_item, driver);
         adminList.setAdapter(adapters);
         }

//     }

     @Override
     public void onCancelled(FirebaseError firebaseError) {
         Toast.makeText(getApplicationContext(),firebaseError.toString(),Toast.LENGTH_SHORT).show();

     }
 });
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
        SharedPreferences.Editor editor2 = mTemeprefferences.edit();
        editor2.putString(Constants.TEME_DEF_USER,"admin");
        editor2.commit();

    }

    public void onDestroy(){
        super.onDestroy();
        SharedPreferences.Editor editor = mTemeprefferences.edit();
        editor.putString(Constants.TEME_DEF_USER,"admin");
        editor.commit();
    }
    public void onStart(){
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
            Intent e=new Intent(getApplicationContext(),EnterAdminDetails.class);
            startActivity(e);
            finish();
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