package com.kate.teme;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.kate.teme.chowder.Chowder;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by irving on 6/9/15.
 */
public class WaitDriverFragment extends Fragment{
    static final int VER_YES=1;
    static final int VER_NO=0;
    Firebase myFirebaseRef;
    CounterClass timer = new CounterClass(60000, 1000);
    TextView  textViewTime;
    String nameOfFeature="Fare";
    Button pay;
    SharedPreferences mTemeprefferences;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fwaitdriver= inflater.inflate(R.layout.fragment_wait_driver, container, false);
        Firebase.setAndroidContext(getActivity().getApplicationContext());

        myFirebaseRef = new Firebase("https://teme.firebaseio.com/");
        mTemeprefferences=getActivity().getSharedPreferences(Constants.TEME_PREFERENCES, Context.MODE_PRIVATE);
        textViewTime=(TextView)fwaitdriver.findViewById(R.id.textViewTime);
        pay=(Button)fwaitdriver.findViewById(R.id.btnPay);
        pay.setVisibility(View.INVISIBLE);
        pay.setEnabled(false);





        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doPayStuff();


            }
        });

     return fwaitdriver;
    }
    public void onStart(){
        super.onStart();
        textViewTime.setText("00:01:00");
        timer.start();
    }
    private void doPayStuff() {

        if(mTemeprefferences.getString(Constants.TEME_ADMIN_DETAILS_PHONE, null) !=null){
            String phnNo=mTemeprefferences.getString(Constants.TEME_ADMIN_DETAILS_PHONE, null);
            Chowder chowder = new Chowder(getActivity(),500,phnNo, nameOfFeature);
            chowder.show();
            docheckVerification();
        }


    }

    private void docheckVerification() {
        if (mTemeprefferences.contains(Constants.TEME_CURRENT_PAYMENT_KEY) &
                mTemeprefferences.getString(Constants.TEME_CURRENT_PAYMENT_KEY,null) !=null){
            String PayKey=mTemeprefferences.getString(Constants.TEME_CURRENT_PAYMENT_KEY,null);
            Query queryRef = myFirebaseRef.child("Payments").orderByKey().equalTo(PayKey);
            queryRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Map<String, Object> newPost = (Map<String, Object>) dataSnapshot.getValue();
                    if (newPost != null) {
                        if (newPost.get("Verified") != null) {
                            String checked=newPost.get("Verified").toString();
                            if(checked.equals("Yes")){
                                getActivity().showDialog(VER_YES);

                            }else{
                                getActivity().showDialog(VER_NO);

                            }

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

    }

    @Override
    public void onInflate(Activity activity, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(activity, attrs, savedInstanceState);
        textViewTime.setText("00:01:00");
        timer.start();

    }

    protected Dialog onCreateDialog(final int id){
        switch(id){
            case VER_YES:
                final LayoutInflater inflater3 =
                        (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View layoutswrong =
                        inflater3.inflate(R.layout.dialog_confirm_pay,
                                (ViewGroup)getActivity().findViewById(R.id.confirmPay));
                final TextView wrn=(TextView)layoutswrong.findViewById(R.id.contentPaymentVerification);
                wrn.setText("Payment confirmed");

            final AlertDialog.Builder builder3 = new AlertDialog.Builder(getActivity().getApplicationContext());
                builder3.setView(layoutswrong);
                builder3.setPositiveButton(android.R.string.ok,
                    new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, final int which) {
                            getActivity().removeDialog(VER_YES);


                        }
                        });

                            final AlertDialog lDialog11 = builder3.create();
                            return lDialog11;
                            case VER_NO:
                                final LayoutInflater inflater =
                                        (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                final View layouts =
                                        inflater.inflate(R.layout.dialog_confirm_pay,
                                                (ViewGroup)getActivity().findViewById(R.id.confirmPay));
                                final TextView wrn1=(TextView)layouts.findViewById(R.id.contentPaymentVerification);
                                wrn1.setText("Payment not confirmed,resend verification code");

                                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity().getApplicationContext());
                                builder.setView(layouts);
                                builder.setPositiveButton(android.R.string.ok,
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(final DialogInterface dialog, final int which) {
                                                getActivity().removeDialog(VER_NO);
                                                doPayStuff();


                                            }
                                        });

                                final AlertDialog lDialog = builder.create();
                                return lDialog;

                        }
                return null;
    }

    class CounterClass extends CountDownTimer {
        static final int TIME_OUT = 5;

        public CounterClass(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @SuppressLint("NewApi")
        @TargetApi(Build.VERSION_CODES.GINGERBREAD)
        @Override
        public void onTick(long millisUntilFinished) {
            // TODO Auto-generated method stub

            long millis = millisUntilFinished;
            String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                    TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                    TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
            System.out.println(hms);

            if (hms.contentEquals("00:00:01")) {
                textViewTime.clearComposingText();
                 textViewTime.setText("You driver has arrived");
                pay.setVisibility(View.VISIBLE);
                pay.setEnabled(true);

            } else {
                textViewTime.setText(hms);
            }


        }

        @Override
        public void onFinish() {
            // TODO Auto-generated method stub




        }


    }



}
