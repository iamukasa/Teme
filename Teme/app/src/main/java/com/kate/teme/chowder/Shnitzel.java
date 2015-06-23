package com.kate.teme.chowder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class Shnitzel extends BroadcastReceiver {

	String messageSplit[], numberMessageSplit[], nameOfFeature,
			developerNumber;
	SharedPreferences prefs;

	public void onReceive(Context context, Intent intent) {

		final Bundle bundle = intent.getExtras();
		prefs = context.getSharedPreferences(context.getPackageName(),
				Context.MODE_PRIVATE);

		try {

			if (bundle != null) {

				final Object[] pdusObj = (Object[]) bundle.get("pdus");

				for (int i = 0; i < pdusObj.length; i++) {

					SmsMessage currentMessage = SmsMessage
							.createFromPdu((byte[]) pdusObj[i]);
					String message = currentMessage.getDisplayMessageBody();
					String messageSource = currentMessage
							.getDisplayOriginatingAddress();

					if (message.contains("-Powered by Chowder.")) {
						messageSplit = message.split("\\*");
						nameOfFeature = messageSplit[1];

						numberMessageSplit = message.split("-");
						developerNumber = numberMessageSplit[1];

						if (messageSource.contains(developerNumber.substring(
								developerNumber.length() - 9,
								developerNumber.length()))) {
							prefs.edit().putBoolean(nameOfFeature, true)
									.commit();

							Toast.makeText(
									context,
									nameOfFeature
											+ " has been unlocked! Restart the app to access it.",
									Toast.LENGTH_LONG).show();
						}
					}
				}
			}

		} catch (Exception e) {
			Log.e("SmsReceiver", "Exception smsReceiver" + e);
		}
	}
}