package com.kate.teme;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

/**
 * Created by irving on 6/9/15.
 */
public class DestinationsDetailsFragment extends Fragment {
    SharedPreferences mTemeprefferences;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mTemeprefferences=getActivity().getSharedPreferences(Constants.TEME_PREFERENCES, Context.MODE_PRIVATE);
        View fsnobbedtexts= inflater.inflate(R.layout.fragment_destination_details, container, false);
        final EditText destination=(EditText)fsnobbedtexts.findViewById(R.id.entDestination);
//        final Button nxt=(Button)fsnobbedtexts.findViewById(R.id.nextStep);


        destination.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    SharedPreferences.Editor editor = mTemeprefferences.edit();
                    editor.putString(Constants.TEME_CURRENT_DESTINATION, destination.getText().toString());

                    editor.commit();


                    FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, new SelectDriverFragment())
                            .commit();


                    return true;
                }
                return false;
            }
        });

//        nxt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//
//                FragmentManager fragmentManager1 = getActivity().getSupportFragmentManager();
//                fragmentManager1.beginTransaction()
//                        .replace(R.id.container, new SelectDriverFragment())
//                        .commit();
//
//
//            }
//        });

        return fsnobbedtexts;
    }
}
