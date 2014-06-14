package com.ros.workandhome.sectionmanagement;

import com.ros.workandhome.geofence.*;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.ros.workandhome.R;
import com.ros.workandhome.SharedApplication;
import com.ros.workandhome.activities.views.WHCircleView;
import com.ros.workandhome.auxiliary.WHTimeUtils;
import com.ros.workandhome.core.entities.GPSLocation.WHGPSLocation;
import com.ros.workandhome.core.entities.WHDay.WHDay;
import com.ros.workandhome.core.entities.section.WHSection;
import com.ros.workandhome.core.entities.wifispot.WHWifiSpot;
import com.ros.workandhome.db.WHProxy;
import com.ros.workandhome.geofence.GeofenceUtils.REMOVE_TYPE;
import com.ros.workandhome.geofence.GeofenceUtils.REQUEST_TYPE;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.Geofence;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class WHSectionManagingService extends Service{
	 private static Timer timer = new Timer(); 
	 
	 public WHSectionManagingService() {
	 
	 }
	 
	 @Override
	    public IBinder onBind(Intent arg0) {
	          return null;
	 }
	 
	 public int onStartCommand (Intent intent, int flags, int startId){
		    return START_STICKY;
	 }
	 
	 private final BroadcastReceiver receiver = new BroadcastReceiver() {
		   @Override
		   public void onReceive(Context context, Intent intent) {
			    if(intent.getAction().equalsIgnoreCase("com.ros.workandhome.intents.geofencetransition"))
			    {
			    	String [] geofenceIDs = intent.getStringArrayExtra("geofenceids");
			    	WHSection sectionAround = WHGeofencesManager.sharedManager().getSectionAround(geofenceIDs);
			    	if(sectionAround != null)
			    	   setActiveSection(sectionAround);
			    }

		   }
		};
	 
	 
	 private class FetchNetworksClass extends TimerTask
	 { 
	        public void run() 
	        {
	        	//Wifi spots
               WHNetworkManager manager = WHNetworkManager.sharedManager();
               WHSection sectionAround = manager.getSectionAround();
               if(sectionAround != null)
            	   setActiveSection(sectionAround); 
               
               //Time schedule
               sectionAround = WHTimeScheduleManager.sharedManager().getSectionAround();
               if(sectionAround != null)
            	   setActiveSection(sectionAround); 
	        }
	 }    
	 
	 @Override
	 public void onCreate() {
	          super.onCreate();
	          
	          //create geofences and register Braodcastreceiver to get transitions
	          WHGeofencesManager geofenceManager = WHGeofencesManager.sharedManager();
	          geofenceManager.addGeofences(); 
	          initBroadcastReceiver();
	          
	          timer.scheduleAtFixedRate(new FetchNetworksClass(), 0, 60000);
	 }
	 
	 public void initBroadcastReceiver() {
		 IntentFilter filter = new IntentFilter("actionname");
 	     filter.addAction("com.ros.workandhome.intents.geofencetransition");
 	     registerReceiver(receiver, filter);
	 }
	 
	 public void setActiveSection(WHSection section) {
		 Intent activeSectionIntent = new Intent();
		 activeSectionIntent.putExtra("section", section);
		 activeSectionIntent.setAction("com.ros.workandhome.intents.ACTIVE_SECTION_CHANGED");
		 sendBroadcast(activeSectionIntent);
	 }
	 
     @Override
     public void onDestroy() {
          super.onDestroy(); 
          WHGeofencesManager geofenceManager = WHGeofencesManager.sharedManager();
          geofenceManager.removeAllGeofences(); 
     }
}
