package com.saladinprojects.problemssolutions;

import java.util.Calendar;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

public class ServiceManager {
	AlarmManager alarm;
	boolean withcat;
	boolean notifs;
	FragmentActivity activity;
	PendingIntent pendingIntent;
	
	public ServiceManager( boolean withcat, boolean notifs,
			FragmentActivity activity) {
		super();
		this.withcat = withcat;
		this.notifs = notifs;
		this.activity = activity;
		Intent myIntent = new Intent(activity,
				ServiceNotification.class);
		pendingIntent = PendingIntent.getService(activity, 0,
				myIntent, 0);
		alarm= (AlarmManager) activity.getSystemService(activity.ALARM_SERVICE);
	}

	public void loadSavedPreferences() {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(activity);
		notifs = sharedPreferences.getBoolean("notifs", true);
		withcat = sharedPreferences.getBoolean("withcat", true);

		if (notifs) {
			if (!isServiceRunning())
				start_service(false);
		} else {
			if (isServiceRunning())
				stop_service(false);
		}
	}

	public void savePreferences(String key, boolean value) {

		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(activity);
		Editor editor = sharedPreferences.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	public void stop_service(boolean toast) {
		if (alarm != null &&  (isServiceRunning())) {
			alarm.cancel(pendingIntent);
			Intent intent = new Intent();
			intent.setAction(ServiceNotification.ACTION);
			intent.putExtra("RQS", ServiceNotification.RQS_STOP_SERVICE);
			activity.sendBroadcast(intent);
			
			if(toast)
				Toast.makeText(activity, activity.getString(R.string.notifoff),
						Toast.LENGTH_LONG).show();
		}

		
	}

	public void start_service(boolean toast) {
		if (alarm != null && (!isServiceRunning())) {
			Calendar cal = Calendar.getInstance();
			// cal.set(Calendar.HOUR_OF_DAY, 12);
			// cal.set(Calendar.MINUTE, 00);
			// cal.set(Calendar.SECOND, 0);
		
			alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
					getInterval(), pendingIntent);
			if(toast)
				Toast.makeText(activity, activity.getString(R.string.notifon),
						Toast.LENGTH_LONG).show();
			
		}
	
	}

	private boolean isServiceRunning() {
		ActivityManager manager = (ActivityManager) activity.getSystemService(activity.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager
				.getRunningServices(Integer.MAX_VALUE)) {
			if ("com.saladinprojects.problemssolutions.ServiceNotification"
					.equals(service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}

	private int getInterval() {
		int days = 1;
		int hours = 12;
		int minutes = 60;
		int seconds = 60;
		int milliseconds = 1000;
		int repeatMS = days * hours * minutes * seconds * milliseconds;
	//	int repeatMS =  seconds * milliseconds;
		return repeatMS;
	}

}
