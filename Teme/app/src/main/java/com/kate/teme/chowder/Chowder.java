package com.kate.teme.chowder;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.kate.teme.Constants;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@SuppressLint("InlinedApi")
public class Chowder extends Dialog {
	SharedPreferences mTemeprefferences;
	public static long ONE_DAY = TimeUnit.MILLISECONDS
			.convert(1, TimeUnit.DAYS);
	public static long ONE_WEEK = TimeUnit.MILLISECONDS.convert(7,
			TimeUnit.DAYS);
	public static long TWO_WEEKS = TimeUnit.MILLISECONDS.convert(14,
			TimeUnit.DAYS);
	public static long A_MONTH = TimeUnit.MILLISECONDS.convert(30,
			TimeUnit.DAYS);
	public static long SIX_MONTHS = TimeUnit.MILLISECONDS.convert(180,
			TimeUnit.DAYS);
	public static long ONE_YEAR = TimeUnit.MILLISECONDS.convert(365,
			TimeUnit.DAYS);
	private static Activity activity;
	Firebase myFirebaseRef;
	private Button bDone;
	private TextView tvPhoneNumber, tvTitle;
	private EditText etTransactionCode;
	private String transactionCode, phoneNumber, nameOfFeature;
	private int amount;

	public Chowder(Activity activity, int amount, String phoneNumber,
				   String nameOfFeature) {
		super(activity);
		// TODO Auto-generated constructor stub
		Chowder.activity = activity;
		this.amount = amount;
		this.phoneNumber = phoneNumber;
		this.nameOfFeature = nameOfFeature;
	}

	public static void subscribeUser(Activity activity, String nameOfFeature,
									 long subscriptionPeriod) {
		// TODO Auto-generated method stub
		if (subscriptionPeriod != 0) {
			int alarmType = AlarmManager.ELAPSED_REALTIME;
			AlarmManager alarmManager = (AlarmManager) activity
					.getSystemService(Context.ALARM_SERVICE);

			Intent alarmIntent = new Intent(activity.getApplication(),
					Mung.class);
			Bundle b = new Bundle();
			b.putString("nameOfFeature", nameOfFeature);
			alarmIntent.putExtras(b);

			PendingIntent pIntent = PendingIntent.getBroadcast(
					activity.getApplicationContext(), 0, alarmIntent,
					PendingIntent.FLAG_UPDATE_CURRENT);
			alarmManager.setRepeating(alarmType, SystemClock.elapsedRealtime()
					+ subscriptionPeriod, subscriptionPeriod, pIntent);

		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Firebase.setAndroidContext(activity.getApplicationContext());
		myFirebaseRef = new Firebase("https://teme.firebaseio.com/");

		requestWindowFeature((int) Window.FEATURE_NO_TITLE);

		int layoutId = getResourceIdByName(activity.getPackageName(), "layout",
				"chowder");
		setContentView(layoutId);
		getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

		setUp();
	}

	private void setUp() {
		// TODO Auto-generated method stub
		int tvPhoneNumberId = getResourceIdByName(activity.getPackageName(),
				"id", "tvPhoneNumber");
		int tvTitleId = getResourceIdByName(activity.getPackageName(), "id",
				"tvTitle");
		int etTransactionCodeId = getResourceIdByName(
				activity.getPackageName(), "id", "etTransactionCode");
		int bDoneId = getResourceIdByName(activity.getPackageName(), "id",
				"bDone");

		tvPhoneNumber = (TextView) findViewById(tvPhoneNumberId);
		tvPhoneNumber.setText(phoneNumber);
		tvPhoneNumber.setOnClickListener(new View.OnClickListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				int currentapiVersion = android.os.Build.VERSION.SDK_INT;
				if (currentapiVersion >= android.os.Build.VERSION_CODES.HONEYCOMB) {
					android.content.ClipboardManager clipboard = (android.content.ClipboardManager) activity
							.getSystemService(Context.CLIPBOARD_SERVICE);
					ClipData clip = ClipData.newPlainText("phoneNumber",
							phoneNumber);
					clipboard.setPrimaryClip(clip);
				} else {
					android.text.ClipboardManager clipboard = (android.text.ClipboardManager) activity
							.getSystemService(Context.CLIPBOARD_SERVICE);
					clipboard.setText(phoneNumber);
				}
				Toast.makeText(activity.getApplicationContext(),
						"Phone number copied to clipboard", Toast.LENGTH_SHORT)
						.show();
			}
		});

		tvTitle = (TextView) findViewById(tvTitleId);
		tvTitle.setText("Hi, please pay Ksh."
				+ amount
				+ " to the number above via M-Pesa as "
				+ nameOfFeature
				+" and enter the transaction code below:");

		etTransactionCode = (EditText) findViewById(etTransactionCodeId);
		etTransactionCode.setHint("Transaction code...");

		bDone = (Button) findViewById(bDoneId);
		bDone.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				transactionCode = etTransactionCode.getText().toString().trim();
				if (transactionCode.length() > 7) {
					sendPaymentDetails(transactionCode, amount, phoneNumber,
							nameOfFeature);
					dismiss();
				} else {
					etTransactionCode
							.setError("Please enter a valid transaction code");
				}
			}
		});
	}

	private void sendPaymentDetails(String transactionCode, int amount,
									String phoneNumber, String nameOfFeature) {
		// TODO Auto-generated method stub
		try {
			Map<String,Object> post1 = new HashMap<String,Object>();
			post1.put("transaction code",transactionCode);
			post1.put("amount",amount);
			post1.put("phone Number",phoneNumber);
			post1.put("Name of feature",nameOfFeature);
			myFirebaseRef.child("Payments").push().setValue(post1);
			mTemeprefferences=getOwnerActivity().getSharedPreferences(Constants.TEME_PREFERENCES, Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = mTemeprefferences.edit();
			editor.putString(Constants.TEME_CURRENT_PAYMENT_KEY,myFirebaseRef.child("Payments").push().getKey());
			editor.commit();

// SmsManager smsManager = SmsManager.getDefault();
//			smsManager.sendTextMessage(phoneNumber, null, "Chowder::"
//					+ transactionCode + "::" + amount + "::" + phoneNumber
//					+ "::" + nameOfFeature, null, null);

			Toast.makeText(
					activity.getApplicationContext(),
					"Please wait, you will recieve a verification notificaton shortly",
					Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Chowder is sick: " + e.toString());
			Toast.makeText(
					activity.getApplicationContext(),
					"Something went wrong sending the verification, please check your airtime and try again: "
							+ e.toString(), Toast.LENGTH_LONG).show();
		}

	}

	@SuppressWarnings("rawtypes")
	private int getResourceIdByName(String packageName, String className,
									String name) {
		Class r = null;
		int id = 0;
		try {
			r = Class.forName(packageName + ".R");
			Class[] classes = r.getClasses();
			Class desireClass = null;

			for (int i = 0; i < classes.length; i++) {
				if (classes[i].getName().split("\\$")[1].equals(className)) {
					desireClass = classes[i];
					break;
				}
			}

			if (desireClass != null) {
				id = desireClass.getField(name).getInt(desireClass);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
		return id;
	}
}
