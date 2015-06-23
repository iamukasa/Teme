package com.kate.teme;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
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

import com.kate.teme.chowder.Chowder;

import java.util.concurrent.TimeUnit;

/**
 * Created by irving on 6/9/15.
 */
public class WaitDriverFragment extends Fragment{
    CounterClass timer = new CounterClass(60000, 1000);
    TextView  textViewTime;
    String nameOfFeature="Fare";
    Button pay;
    SharedPreferences mTemeprefferences;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fwaitdriver= inflater.inflate(R.layout.fragment_wait_driver, container, false);
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

        Chowder chowder = new Chowder(getActivity(),500,"0717133826", nameOfFeature);
        chowder.show();
    }

    @Override
    public void onInflate(Activity activity, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(activity, attrs, savedInstanceState);
        textViewTime.setText("00:01:00");
        timer.start();

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
