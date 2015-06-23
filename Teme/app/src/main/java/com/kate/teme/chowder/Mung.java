package com.kate.teme.chowder;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class Mung extends BroadcastReceiver {

	public void onReceive(Context context, Intent intent) {

		Bundle b = intent.getExtras();
		String nameOfFeature = b.getString("nameOfFeature");

		SharedPreferences prefs = context.getSharedPreferences(
				context.getPackageName(), Context.MODE_PRIVATE);
		prefs.edit().putBoolean(nameOfFeature, false);

		new AlertDialog.Builder(context)
				.setTitle(nameOfFeature + " has expired")
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setMessage(
						"You'll need to renew your subscription to keep using it")
				.setPositiveButton("Okay",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								// TODO Auto-generated method stub
								arg0.dismiss();
							}
						});
	}
}