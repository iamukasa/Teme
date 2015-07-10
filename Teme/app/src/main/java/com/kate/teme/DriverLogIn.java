package com.kate.teme;

import android.app.ProgressDialog;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;
import java.util.Map;


public class DriverLogIn extends ActionBarActivity {
    SharedPreferences mTemeprefferences;


    Button loginDriver;
    EditText loginDriverName,logInCar;
    ArrayList<Driver> driver;
    Firebase myFirebaseRef;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_log_in);
        mTemeprefferences=getSharedPreferences(Constants.TEME_PREFERENCES, Context.MODE_PRIVATE);
        Firebase.setAndroidContext(getApplicationContext());

        myFirebaseRef = new Firebase("https://teme.firebaseio.com/");
        fillFromOnline();
        setToolBar();

    }
    private void setToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarLoginDriver);
        if (toolbar != null) {

            setSupportActionBar(toolbar);
            setUpActionbar();

            toolbar.setBackgroundColor(getResources().getColor(R.color.blue));
            toolbar.setTitleTextColor(getResources().getColor(R.color.white_pure));
 }

    }

    private void setUpActionbar() {
        if(getSupportActionBar()!=null){
            ActionBar bar = getSupportActionBar();
            bar.setTitle(getResources().getString(R.string.title_activity_driver_log_in));
            bar.setHomeButtonEnabled(false);
            bar.setDisplayShowHomeEnabled(false);
            bar.setDisplayHomeAsUpEnabled(false);
            bar.setDisplayShowTitleEnabled(true);

        }   }

    private void fillFromOnline() {
        myFirebaseRef.child("DriversList").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(final DataSnapshot snapshot, String s) {

                Map<String, Object> newPost = (Map<String, Object>) snapshot.getValue();
                if (newPost != null) {
                    driver=new ArrayList<Driver>();
                    ProgressDialog pDialog = new ProgressDialog(DriverLogIn.this);
                    pDialog.setMessage("Fetching data");
                    pDialog.show();
            driver.add(new Driver(newPost.get("drivername").toString(), newPost.get("carname").toString()));
                  pDialog.hide();
      }
                loginDriverName = (EditText) findViewById(R.id.loginDriveName);

                logInCar = (EditText) findViewById(R.id.loginDriveCar);
                loginDriver = (Button) findViewById(R.id.buttonLogInDriver);
                loginDriver.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                 String useful=snapshot.getKey();
                        String driversName = loginDriverName.getText().toString();
                        String driversCar = logInCar.getText().toString();
                        authenticate(driversName, driversCar, driver,useful);

                    }
                });


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    private void authenticate(String driversName, String driversCar, ArrayList<Driver> driver, String useful) {
        for (int i=0;i<driver.size();i++){
            Toast.makeText(getApplicationContext(),driver.get(i).DriverName
                    ,Toast.LENGTH_LONG).show();
            Toast.makeText(getApplicationContext(),driver.get(i).CarDriven
                    ,Toast.LENGTH_LONG).show();

            if(driver.get(i).getDriverName().contentEquals(driversName)
                    &
                    driver.get(i).getCarDriven().equals(driversCar) ){

                    Intent r=new Intent(getApplicationContext(),DriverContent.class);
                    r.putExtra("driver",driversName);
                    r.putExtra("car",driversCar);
                    r.putExtra("our", useful);
                    startActivity(r);
                    finish();
                    SharedPreferences.Editor editor = mTemeprefferences.edit();
                    editor.putString(Constants.TEME_DRIVER_LOGGED_IN, "logged in");
                    editor.commit();

            }else{
                Toast.makeText(getApplicationContext(),"Wrong details cannot log in",Toast.LENGTH_LONG).show();

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_driver_log_in, menu);
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

    @Override
    protected void onDestroy() {
        SharedPreferences.Editor editor = mTemeprefferences.edit();
        editor.putString(Constants.TEME_DEF_USER,"driver");
        editor.commit();
        super.onDestroy();
    }
    protected void onStop(){
        super.onStop();
        SharedPreferences.Editor editor = mTemeprefferences.edit();
        editor.putString(Constants.TEME_DEF_USER,"driver");
        editor.commit();
    }
}
