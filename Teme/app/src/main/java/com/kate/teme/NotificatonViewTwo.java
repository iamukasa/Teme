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
import android.widget.TextView;

import com.firebase.client.Firebase;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class NotificatonViewTwo extends ActionBarActivity {
    SharedPreferences mTemeprefferences;
    Firebase myFirebaseRef;
    TextView paymentdetails;
    Button btnyes,btnno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificaton_view_two);
        mTemeprefferences=getSharedPreferences(Constants.TEME_PREFERENCES, Context.MODE_PRIVATE);

        Firebase.setAndroidContext(getApplicationContext());
        myFirebaseRef = new Firebase("https://teme.firebaseio.com/");
        setToolBar();
        paymentdetails=(TextView)findViewById(R.id.content);
        btnyes=(Button)findViewById(R.id.btnYes);
        btnno=(Button)findViewById(R.id.btnNo);

        Intent i=getIntent();
        final String key=i.getExtras().getString("snapkey");
       final String transaction=i.getExtras().getString("transaction");
        final String phone_number=i.getExtras().getString("phone number");
        final String amount=i.getExtras().getString("amount");
        paymentdetails.setText("Verify Payment " +
                transaction + "from " + phone_number + "for fare" + "/n totaling "+amount);
        btnyes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> post1 = new HashMap<String, String>();
                post1.put("transaction code",transaction);
                post1.put("amount",amount);
                post1.put("phone Number",phone_number);
                post1.put("Name of feature","Fare");
                post1.put("Verified","Yes");
                Map<String, Object> payments = new HashMap<String, Object>();
                payments.put(key, post1);
                myFirebaseRef.child("Payments").updateChildren(payments);

            }
        });
        btnno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> post1 = new HashMap<String, String>();
                post1.put("transaction code",transaction);
                post1.put("amount",amount);
                post1.put("phone Number",phone_number);
                post1.put("Name of feature","Fare");
                post1.put("Verified","No");
                Map<String, Object> payments = new HashMap<String, Object>();
                payments.put(key, post1);
                myFirebaseRef.child("Payments").updateChildren(payments);

            }
        });


    }

    private void setToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarNotificationTwo);
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
            bar.setHomeButtonEnabled(true);
            bar.setDisplayShowHomeEnabled(true);
            bar.setDisplayHomeAsUpEnabled(true);
            bar.setDisplayShowTitleEnabled(true);



            bar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);

        }


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_notificaton_view_two, menu);
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
