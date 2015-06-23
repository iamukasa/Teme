package com.kate.teme;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import java.util.ArrayList;


public class SelectDriverFragment extends Fragment {
    ListView driversList;
    ArrayList<Driver> driversArray;
    ArrayList<String> dDereva,dCar;
    SharedPreferences mTemeprefferences;
    Firebase myFirebaseRef;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        View fWhatsapp= inflater.inflate(R.layout.fragment_select_driver, container, false);
        mTemeprefferences=getActivity().getSharedPreferences(Constants.TEME_PREFERENCES, Context.MODE_PRIVATE);
        driversList=(ListView)fWhatsapp.findViewById(R.id.listDriverstwo);
        driversArray=new ArrayList<Driver>();
        dDereva=new ArrayList<String>();
        dCar=new ArrayList<String>();
        Firebase.setAndroidContext(getActivity().getApplicationContext());
        Firebase.getDefaultConfig().setPersistenceEnabled(true);
        myFirebaseRef = new Firebase("https://teme.firebaseio.com/");


        dDereva.add("Sam");
         dDereva.add("Tom");
         dDereva.add("Mike");
         dDereva.add("John");
         dDereva.add("Jane");
         dDereva.add("Elvis");
         dDereva.add("Chris");

        dCar.add("Ford Mustang");
        dCar.add("Toyota G touring");
        dCar.add("Subaru Lancer");
        dCar.add("Mitubishi");
        dCar.add("Toyota Mark x");
        dCar.add("Toyota proBox");
        dCar.add("Nissan Premio");

        Query queryRef = myFirebaseRef.child("DriversList").orderByChild("Drivers availability").equalTo("Yes");
        queryRef.keepSynced(true);
        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                System.out.println(snapshot.getKey());
              String name=  snapshot.child("DriversList").child("DriversList").child("Driver name").getKey();
              String car= snapshot.child("DriversList").child("DriversList").child("Driver name").getKey();
                driversArray.add(new Driver(name,car));

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
            // ....
        });


//        for(int t=0;t<dDereva.size();t++){
//            driversArray.add(new Driver(dDereva.get(t),dCar.get(t)));
//
//        }
        DriverAdapter adapters = new DriverAdapter(getActivity().getApplicationContext(),
                R.layout.list_item,driversArray);
        driversList.setAdapter(adapters);
        driversList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(getActivity().getApplicationContext(), "Your Driver is on his way"
                        , Toast.LENGTH_SHORT).show();


                FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new WaitDriverFragment())
                                .commit();


            }
        });






        return fWhatsapp;
    }


}