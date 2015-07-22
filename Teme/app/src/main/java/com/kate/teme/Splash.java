package com.kate.teme;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


public class Splash extends ActionBarActivity {
    static final int CHOOSE_REG=0;

    SharedPreferences mTemeprefferences;
    final String admin ="admin";
    final String driver ="driver";
    final String passenger ="passenger";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();
        mTemeprefferences=getSharedPreferences(Constants.TEME_PREFERENCES, Context.MODE_PRIVATE);


        boolean b = new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {


                if (mTemeprefferences.contains(Constants.TEME_DEF_USER)) {
                    String s = mTemeprefferences.getString(Constants.TEME_DEF_USER, null);
                    String show="Logged in as :";

                    System.out.print(s);
                    if (s != null) {
                        if (s.contentEquals(admin)) {
                            Toast.makeText(getApplicationContext(),show+s, Toast.LENGTH_SHORT).show();

                            if((mTemeprefferences.getString(Constants.TEME_ADMIN_LOGGED_IN, null).contentEquals("LoggedIn"))
                                    ){
                                if (mTemeprefferences.contains(Constants.TEME_ADMIN_DETAILS_PHONE) &
                                        mTemeprefferences.contains(Constants.TEME_ADMIN_DETAILS_PAYBILL) &
                                        mTemeprefferences.contains(Constants.TEME_ADMIN_DETAILS_FARE_RATE)
                                        ) {

                                    Intent iu = new Intent(getApplicationContext(), AdminContent.class);
                                    startActivity(iu);
                                    finish();
                                    Toast.makeText(getApplicationContext(), show + admin, Toast.LENGTH_SHORT).show();


                                } else {
                                    Intent iu = new Intent(getApplicationContext(), EnterAdminDetails.class);
                                    startActivity(iu);
                                    finish();
                                    Toast.makeText(getApplicationContext(), show + admin, Toast.LENGTH_SHORT).show();

                                }

                            }else{
                                Intent iu = new Intent(getApplicationContext(), AdminLogIn.class);
                                startActivity(iu);
                                finish();

                            }







//
                        } else if (s.contentEquals(driver)) {
                            if (mTemeprefferences.contains(Constants.TEME_DRIVER_LOGGED_IN)){
                                if("logged in".equals(mTemeprefferences.getString(Constants.TEME_DRIVER_LOGGED_IN, null))){
                                    Intent i = new Intent(getApplicationContext(), DriverContent.class);
                                    startActivity(i);
                                    finish();
                                    Toast.makeText(getApplicationContext(),show+driver, Toast.LENGTH_SHORT).show();

                                }else{
                                    Intent i = new Intent(getApplicationContext(), DriverLogIn.class);
                                    startActivity(i);
                                    finish();
                                    Toast.makeText(getApplicationContext(),show+driver, Toast.LENGTH_SHORT).show();

                                }


                            }else{
                                Intent i = new Intent(getApplicationContext(), DriverLogIn.class);
                                startActivity(i);
                                finish();
                                Toast.makeText(getApplicationContext(),show+driver, Toast.LENGTH_SHORT).show();

                            }




                        } else if (s.contentEquals(passenger)) {


                            Intent ii = new Intent(getApplicationContext(), PassengerContent.class);
                            startActivity(ii);
                            finish();
                            Toast.makeText(getApplicationContext(),show+passenger, Toast.LENGTH_SHORT).show();

                        }
                    }

                } else {
                    Splash.this.showDialog(CHOOSE_REG);

                }

            }
        }, 2 * 1000);// wait for 3 seconds

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash, menu);

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
            Splash.this.showDialog(CHOOSE_REG);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    protected Dialog onCreateDialog(final int id){
        switch(id){
            case CHOOSE_REG:
                final LayoutInflater inflater3 =
                        (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View layoutswrong =
                        inflater3.inflate(R.layout.dialog_choosemode,
                                (ViewGroup) findViewById(R.id.chooseMode));
                final Button wrn=(Button)layoutswrong.findViewById(R.id.regAdmin);
                final Button wrn1=(Button)layoutswrong.findViewById(R.id.regDriver);
                final Button wrn2=(Button)layoutswrong.findViewById(R.id.regPassenger);
                wrn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        handleAdmin();
                    }
                });

                wrn1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        handleDriver();

                    }
                });
                wrn2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        handlePassenger();
                    }
                });


                final AlertDialog.Builder builder3 = new AlertDialog.Builder(this);
                builder3.setView(layoutswrong);
                builder3.setTitle("Use Teme as ?");

                builder3.setIcon(getResources().getDrawable(R.drawable.ic_launcher));
                final AlertDialog lDialog11 = builder3.create();
                return lDialog11;
        }
        return null;
    }

    private void handlePassenger() {
        Intent i= new Intent(getApplicationContext(),PassengerContent.class);
        startActivity(i);
        finish();
        SharedPreferences.Editor editor = mTemeprefferences.edit();
        editor.putString(Constants.TEME_DEF_USER,passenger );
        editor.commit();
    }

    private void handleDriver() {
       if (mTemeprefferences.contains(Constants.TEME_DRIVER_LOGGED_IN)){
           Intent i= new Intent(getApplicationContext(),DriverContent.class);
           startActivity(i);
           finish();
           SharedPreferences.Editor editor = mTemeprefferences.edit();
           editor.putString(Constants.TEME_DEF_USER,driver);
           editor.commit();

       }else{
           Intent i= new Intent(getApplicationContext(),DriverLogIn.class);
           startActivity(i);
           finish();
           SharedPreferences.Editor editor = mTemeprefferences.edit();
           editor.putString(Constants.TEME_DEF_USER,driver);
           editor.commit();

       }





    }

    private void handleAdmin() {

        if(mTemeprefferences.contains(Constants.TEME_ADMIN_DETAILS_PHONE) &
                mTemeprefferences.contains(Constants.TEME_ADMIN_DETAILS_PAYBILL)&
                mTemeprefferences.contains(Constants.TEME_ADMIN_DETAILS_FARE_RATE)
                 ){
            Intent i= new Intent(getApplicationContext(),AdminContent.class);
            startActivity(i);
            finish();
            SharedPreferences.Editor editor = mTemeprefferences.edit();
            editor.putString(Constants.TEME_DEF_USER,admin);
            editor.commit();



        }else{
            Intent i= new Intent(getApplicationContext(),EnterAdminDetails.class);
            startActivity(i);
            finish();
            SharedPreferences.Editor editor = mTemeprefferences.edit();
            editor.putString(Constants.TEME_DEF_USER,admin);
            editor.commit();

        }


    }
}
