package com.kate.teme;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by irving on 6/9/15.
 */
public class DriverAdapter extends ArrayAdapter<Driver> {

    private ArrayList<Driver> objects;
    Context context;

    public DriverAdapter(Context context, int textViewResourceId,
                           ArrayList<Driver> objects) {
        super(context, textViewResourceId, textViewResourceId, objects);
        this.objects = objects;
        this.context = context;
    }

    @SuppressLint("InflateParams")
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.list_item, null);
        }

        Driver i = objects.get(position);
        if (i != null) {

            TextView tvName = (TextView) v.findViewById(R.id.memName);
            TextView tvphoneNo = (TextView) v.findViewById(R.id.memNo);

            if (tvName != null) {
                tvName.setText(i.getDriverName());
            }

            if (tvphoneNo != null) {
                tvphoneNo.setText(i.getCarDriven());
            }


        }
        return v;
    }
}
