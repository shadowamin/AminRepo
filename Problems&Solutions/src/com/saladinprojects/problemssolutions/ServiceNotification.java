package com.saladinprojects.problemssolutions;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.saladinprojects.problemssolutions.db.DBHandler;
import com.saladinprojects.problemssolutions.models.Problem;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;
public class ServiceNotification extends Service {

	final static String ACTION = "NotifyServiceAction";
	final static String STOP_SERVICE = "";
	final static int RQS_STOP_SERVICE = 1;
	private Context cxt;
	DBHandler db;
	private ArrayList<Problem> Problems;
	NotifyServiceReceiver notifyServiceReceiver;
	private static final int MY_NOTIFICATION_ID = 1;
	private NotificationManager notificationManager;
	private Notification myNotification;
	String userId;

	@Override
	public void onCreate() {
		super.onCreate();
		   db = new DBHandler(this);
		   
		notifyServiceReceiver = new NotifyServiceReceiver();
		cxt=this;
	}

	@SuppressLint("SimpleDateFormat") @Override
	public int onStartCommand(Intent intent, int flags, int startId) {
	
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(ACTION);
		registerReceiver(notifyServiceReceiver, intentFilter);
		Problems=db.getProblemes();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		String thedate=dateFormat.format(date);
		
		int pcount=0;
		for(int i=0;i<Problems.size();i++)
			if(Problems.get(i).getTypeprob()==0 && !Problems.get(i).getDate().equals(""))
			if(thedate.equals(Problems.get(i).getDate()))
			pcount++;	
				
			if(pcount!=0)
		Notify(pcount);
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		this.unregisterReceiver(notifyServiceReceiver);
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}


	public class NotifyServiceReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			
			// TODO Auto-generated method stub
			int rqs = arg1.getIntExtra("RQS", 0);
			if (rqs == RQS_STOP_SERVICE) {
				stopSelf();
			}
		}
	}

	
	@SuppressWarnings("deprecation")
	public void Notify(int problems)
	{
		notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		myNotification = new Notification(R.drawable.warningnotif,
				getString(R.string.notifmsg), (System.currentTimeMillis()));
		Context context = getApplicationContext();
		String notificationTitle;
		String notificationText;
		Intent myIntent = new Intent();
		myIntent.setComponent(new ComponentName(context, MainActivity.class));
	myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		PendingIntent pendingIntent = PendingIntent.getActivity(
				getBaseContext(), 0, myIntent,0);
		myNotification.defaults = Notification.DEFAULT_LIGHTS ;
		myNotification.flags = Notification.FLAG_AUTO_CANCEL ;
		
		notificationTitle=problems+" "+getString(R.string.notifmsg);
		notificationText=problems+" "+getString(R.string.notifmsg);
			
			myNotification.setLatestEventInfo(context, notificationTitle,notificationText, pendingIntent);	
		notificationManager.notify(MY_NOTIFICATION_ID, myNotification);
		
	}
	



}
