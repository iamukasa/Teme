package com.kate.teme;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;

import java.io.IOException;
import java.util.List;

/**
 * Created by irving on 6/9/15.
 */
public class DestinationsDetailsFragment extends Fragment {
    SharedPreferences mTemeprefferences;
    private Geocoder gc;
    List<Address> locationList;
    Firebase myFirebaseRef;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mTemeprefferences=getActivity().getSharedPreferences(Constants.TEME_PREFERENCES, Context.MODE_PRIVATE);
        View fsnobbedtexts= inflater.inflate(R.layout.fragment_destination_details, container, false);
        final EditText destination=(EditText)fsnobbedtexts.findViewById(R.id.entDestination);
//        final Button nxt=(Button)fsnobbedtexts.findViewById(R.id.nextStep);

        Firebase.setAndroidContext(getActivity().getApplicationContext());

        myFirebaseRef = new Firebase("https://teme.firebaseio.com/");


        destination.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    SharedPreferences.Editor editor = mTemeprefferences.edit();
                    editor.putString(Constants.TEME_CURRENT_DESTINATION, destination.getText().toString());

                    editor.commit();
                    getPAsssengerLocation();

                    myFirebaseRef.child("New Fare").child("New fare Destination").push().setValue(destination.getText().toString());


                    FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, new SelectDriverFragment())
                            .commit();


                    return true;
                }
                return false;
            }
        });
getPAsssengerLocation();
        return fsnobbedtexts;
    }

    private void getPAsssengerLocation() {

        Context ctx = getActivity().getApplicationContext();
        gc = new Geocoder(ctx);
        final LocationManager lm = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);



        if(lm.isProviderEnabled(getActivity().WIFI_SERVICE))

        {
            try {
                locationList = gc.getFromLocation(location.getLatitude(),location.getLongitude(), 1);
                Toast.makeText(getActivity().getApplicationContext(), "Latitude : " + location.getLongitude() +
                        " Longitude : " + location.getLongitude(), Toast.LENGTH_SHORT).show() ;
                sendDataOnline(locationList);
            } catch (IOException e) {
                e.printStackTrace();
            }


        }





        final LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {

                try {
//                    cord.setText( "Latitude : "+location.getLongitude()+" Longitude : "+location.getLongitude());
                    Toast.makeText(getActivity().getApplicationContext(), "Latitude : "+location.getLongitude()+" Longitude : "+location.getLongitude(), Toast.LENGTH_SHORT).show() ;
                    if(lm.isProviderEnabled(getActivity().WIFI_SERVICE))
                    {
                        locationList = gc.getFromLocation(location.getLatitude(),location.getLongitude(), 1);
                        sendDataOnline(locationList);

                    }

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }

            public void onProviderDisabled(String provider) {
                // TODO Auto-generated method stub

            }

            public void onProviderEnabled(String provider) {
                // TODO Auto-generated method stub

            }

            public void onStatusChanged(String provider, int status,
                                        Bundle extras) {
                // TODO Auto-generated method stub

            }
        };

        //Change to LocationManager.GPS_PROVIDER to access GPS cordinates, Recommend way is to use Crietria
        //The update frequency is set to 500 which will drain battery change it to higher value for increasing

        //update time
        Criteria criteria = new Criteria();
        criteria.setPowerRequirement(Criteria.POWER_LOW); // Chose your desired power consumption level.
        criteria.setAccuracy(Criteria.ACCURACY_FINE); // Choose your accuracy requirement.
        criteria.setSpeedRequired(true); // Chose if speed for first location fix is required.
        criteria.setAltitudeRequired(false); // Choose if you use altitude.
        criteria.setBearingRequired(false); // Choose if you use bearing.
        criteria.setCostAllowed(false); // Choose if this provider can waste money :-)
        lm.getBestProvider(criteria,true);

        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000,1, locationListener);

    }

    private void sendDataOnline(List<Address> location) {

        myFirebaseRef.child("New Fare").child("New fare location").push().setValue(location.get(0).toString());

    }


}
