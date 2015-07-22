package com.kate.teme;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewConfiguration;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.lang.reflect.Field;
import java.util.Map;


public class PassengerContent extends ActionBarActivity {

    SharedPreferences mTemeprefferences;
    Firebase myFirebaseRef;

    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);

        myFirebaseRef = new Firebase("https://teme.firebaseio.com/");
        setContentView(R.layout.activity_passenger_content);
        mTemeprefferences=getSharedPreferences(Constants.TEME_PREFERENCES, Context.MODE_PRIVATE);
      fragmentManager= getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, new DestinationsDetailsFragment())
                .commit();

        getOverflowMenu();
        loadadmindata();



        setToolBar();

    }

    private void loadadmindata() {
        myFirebaseRef.child("AdminList").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String s) {
                Map<String, Object> newPost = (Map<String, Object>) snapshot.getValue();
                if(newPost!=null){
                    SharedPreferences.Editor editor = mTemeprefferences.edit();
                    editor.putString(Constants.TEME_ADMIN_DETAILS_PAYBILL,
                            newPost.get("Admin paybill").toString());
                    editor.commit();

                    SharedPreferences.Editor editor2 = mTemeprefferences.edit();
                    editor.putString(Constants.TEME_ADMIN_DETAILS_PHONE,
                            newPost.get("Admin phone no ").toString());
                    editor.commit();

                    SharedPreferences.Editor editor3 = mTemeprefferences.edit();
                    editor.putString(Constants.TEME_ADMIN_DETAILS_FARE_RATE,
                            newPost.get("Admin fare rate").toString());
                    editor.commit();

                }

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


    private void setToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarPassenger);
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
        getMenuInflater().inflate(R.menu.menu_passenger_content, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.call_admin) {
if(mTemeprefferences.getString(Constants.TEME_ADMIN_DETAILS_PHONE, null) !=null){
    String phnNo=mTemeprefferences.getString(Constants.TEME_ADMIN_DETAILS_PHONE, null);
    Intent callIntent = new Intent(Intent.ACTION_CALL);
    callIntent.setData(Uri.parse("tel:" +phnNo));
    startActivity(callIntent);

}

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

    public void onStop(){
        super.onStop();

        SharedPreferences.Editor editor2 = mTemeprefferences.edit();
        editor2.putString(Constants.TEME_DEF_USER,"passenger");
        editor2.commit();

    }

    public void onDestroy(){
        super.onDestroy();
        SharedPreferences.Editor editor = mTemeprefferences.edit();
        editor.putString(Constants.TEME_DEF_USER,"passenger");
        editor.commit();
    }

}
