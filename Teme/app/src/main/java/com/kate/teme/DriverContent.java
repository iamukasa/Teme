package com.kate.teme;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewConfiguration;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;


public class DriverContent extends ActionBarActivity {
//    private static final String GEO_FIRE_REF = "https://teme.firebaseio.com/New_Fare/New_fare_location";
//    private GeoFire geoFire;
    CheckBox chkAvailable;
    SharedPreferences mTemeprefferences;

    private NotificationManager mNotificationManager;
    private int notificationID = 100;
    private int numMessages = 0;
    Firebase myFirebaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_content);
        chkAvailable=(CheckBox)findViewById(R.id.chkAvailable);
        mTemeprefferences=getSharedPreferences(Constants.TEME_PREFERENCES, Context.MODE_PRIVATE);

        Firebase.setAndroidContext(getApplicationContext());
        // setup GeoFire
//        this.geoFire = new GeoFire(new Firebase(GEO_FIRE_REF));

        myFirebaseRef = new Firebase("https://teme.firebaseio.com/");





        chkAvailable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Intent i = getIntent();
                    String the_thedriver=i.getExtras().getString("driver");
                    String the_thecar=i.getExtras().getString("car");
                    String useful=i.getExtras().getString("our");

                    Map<String, String> post1 = new HashMap<String, String>();
                    post1.put("drivername",the_thedriver);
                    post1.put("carname",the_thecar);
                    post1.put("available","Yes");

                    Map<String, Object> drivers = new HashMap<String, Object>();
                    drivers.put(useful,post1);
                    myFirebaseRef.child("DriversList").updateChildren(drivers);


checknewFare();

                }
                else if (!isChecked) {
                    Intent i = getIntent();
                    String the_thedriver=i.getExtras().getString("driver");
                    String the_thecar=i.getExtras().getString("car");
                    String useful=i.getExtras().getString("our");

                    Map<String, String> post1 = new HashMap<String, String>();
                    post1.put("drivername",the_thedriver);
                    post1.put("carname",the_thecar);
                    post1.put("available","No");

                    Map<String, Object> drivers = new HashMap<String, Object>();
                    drivers.put(useful,post1);
                    myFirebaseRef.child("DriversList").updateChildren(drivers);


                   }



            }
        });
        setToolBar();


    }

    private void checknewFare() {
        Intent i = getIntent();
        String useful=i.getExtras().getString("our");
        Query queryRef = myFirebaseRef.child("DriversList").orderByKey().equalTo(useful);
        queryRef.addChildEventListener(new ChildEventListener() {


            @Override
            public void onChildAdded(DataSnapshot snapshot, String s) {
                Map<String, Object> newPost = (Map<String, Object>) snapshot.getValue();
                if (newPost != null) {
                    if( newPost.get("fare_destination") !=null & newPost.get("fare_location")  !=null){
                        String currlocation =newPost.get("fare_location").toString();
                        String currdestination =newPost.get("fare_destination").toString();


                            System.out.println(currlocation);
                            System.out.println(currdestination);


                 /* Invoking the default notification service */
                            NotificationCompat.Builder mBuilder =
                                    new NotificationCompat.Builder(getApplicationContext());

                            mBuilder.setContentTitle("New Fare");
                            mBuilder.setContentText("Go pick someone up");
                            mBuilder.setTicker("Go pick up a new fare from" + currlocation + " and drop him at " + currdestination);
                            mBuilder.setSmallIcon(R.mipmap.ic_launcher);
                            mBuilder.setSound(Uri.parse("android.resource://"
                                    + getPackageName() + "/" + R.raw.notification_loaded));

      /* Increase notification number every time a new notification arrives */
                            mBuilder.setNumber(++numMessages);

      /* Creates an explicit intent for an Activity in your app */
                            Intent resultIntent = new Intent(DriverContent.this, NotificationView.class);
                            resultIntent.putExtra("Destination", currdestination);
                            resultIntent.putExtra("CurrentLocation", currlocation);

                            TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
                            stackBuilder.addParentStack(NotificationView.class);

      /* Adds the Intent that starts the Activity to the top of the stack */
                            stackBuilder.addNextIntent(resultIntent);
                            PendingIntent resultPendingIntent =
                                    stackBuilder.getPendingIntent(
                                            0,
                                            PendingIntent.FLAG_UPDATE_CURRENT
                                    );

                            mBuilder.setContentIntent(resultPendingIntent);

                            mNotificationManager =
                                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

      /* notificationID allows you to update the notification later on. */
                            mNotificationManager.notify(notificationID, mBuilder.build());
                        }


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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarDriver);
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

        }   }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_driver_content, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.log_out) {
            Intent r=new Intent(getApplicationContext(),DriverLogIn.class);
            startActivity(r);
            finish();
            SharedPreferences.Editor editor = mTemeprefferences.edit();
            editor.putString(Constants.TEME_DRIVER_LOGGED_IN,"logged out");
            editor.commit();
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
    @Override
    protected void onDestroy() {
        SharedPreferences.Editor editor = mTemeprefferences.edit();
        editor.putString(Constants.TEME_DEF_USER,"driver");
        editor.commit();
        super.onDestroy();
    }
}
