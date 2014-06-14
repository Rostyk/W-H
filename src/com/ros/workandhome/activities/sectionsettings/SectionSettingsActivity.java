package com.ros.workandhome.activities.sectionsettings;

import java.lang.reflect.Array;
import java.util.ArrayList;

import com.ros.workandhome.R;
import com.ros.workandhome.SharedApplication;
import com.ros.workandhome.activities.gps.SectionSettingGPSLocationsFragmet;
import com.ros.workandhome.activities.raports.RaportsFragment;
import com.ros.workandhome.activities.time.SectionSettingsTimeSchedulesFragment;
import com.ros.workandhome.activities.wifi.SectionSettingsWifiSpotsFragment;
import com.ros.workandhome.core.entities.ActiveTime.WHActiveTime;
import com.ros.workandhome.core.entities.GPSLocation.WHGPSLocation;
import com.ros.workandhome.core.entities.WHDay.WHDay;
import com.ros.workandhome.core.entities.section.WHSection;
import com.ros.workandhome.core.entities.wifispot.WHWifiSpot;
import com.ros.workandhome.core.model.SectionsManager;
import com.ros.workandhome.db.WHProxy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.widget.TabHost.TabSpec;

public class SectionSettingsActivity extends FragmentActivity {
	 public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.section_settings);
	        
	        FragmentTabHost mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
	        mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
	        
	        TabSpec gpsLocationTabSpec = mTabHost.newTabSpec("gps_tag").setIndicator("Location", SharedApplication.getContext().getResources().getDrawable(R.drawable.work_main_section_view_gps_off));
	        mTabHost.addTab(gpsLocationTabSpec, SectionSettingGPSLocationsFragmet.class, null);
	        
	        TabSpec wifiTabSpec = mTabHost.newTabSpec("wifi_tag").setIndicator("Wifi", SharedApplication.getContext().getResources().getDrawable(R.drawable.work_main_section_view_wifi_off));
	        mTabHost.addTab(wifiTabSpec, SectionSettingsWifiSpotsFragment.class, null);
	       
	        TabSpec timeTabSpec = mTabHost.newTabSpec("time_tag").setIndicator("Time", SharedApplication.getContext().getResources().getDrawable(R.drawable.work_main_section_view_time_off));
	        mTabHost.addTab(timeTabSpec, SectionSettingsTimeSchedulesFragment.class, null);
	       
	        Intent i = getIntent();
	        int tabIndex = i.getIntExtra("tabNumber", 0);
	        mTabHost.setCurrentTab(tabIndex);
	 }
	 
	 protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		  if (requestCode == 1) {
		     if(resultCode == RESULT_OK){      
		            addGPSLocation(data);      
		     }
		     if (resultCode == RESULT_CANCELED) {    
		         //Write your code if there's no result
		     }
		  }
		  if (requestCode == 2) {
			     if(resultCode == RESULT_OK){      
			            addWifiSpot(data);      
			     }
			     if (resultCode == RESULT_CANCELED) {    
			         //Write your code if there's no result
			     }
		  }
		  if (requestCode == 3) {
			     if(resultCode == RESULT_OK){      
			            addTimeSchedule(data);      
			     }
			     if (resultCode == RESULT_CANCELED) {    
			         //Write your code if there's no result
			     }
		  }
		  
		}
	 
	 public void addGPSLocation(Intent data) {
		 WHGPSLocation location = (WHGPSLocation)data.getSerializableExtra("location");          
         SectionsManager sectionsManager = SectionsManager.sharedManager();
         
         //Add locations to certain day in section
         WHSection section = sectionsManager.getCurrentSection();
         
         @SuppressWarnings("unchecked")
		 ArrayList<WHDay> arrayOfDays = (ArrayList<WHDay>) data.getSerializableExtra("days");
         for(WHDay day : arrayOfDays) {
        	 section.addGPSLocationForDay(location, day);
         }
         
         SectionSettingGPSLocationsFragmet gpsFragment = (SectionSettingGPSLocationsFragmet)getSupportFragmentManager().findFragmentByTag("gps_tag"); 
         gpsFragment.updateList();
	 }
	 
	 public void addWifiSpot(Intent data) {
		 @SuppressWarnings("unchecked")
		 ArrayList<WHWifiSpot> wifiSpots = (ArrayList<WHWifiSpot>)data.getSerializableExtra("wifispots");          
         SectionsManager sectionsManager = SectionsManager.sharedManager();
         
         //Add locations to certain day in section
         WHSection section = sectionsManager.getCurrentSection();
         
         @SuppressWarnings("unchecked")
		 ArrayList<WHDay> arrayOfDays = (ArrayList<WHDay>) data.getSerializableExtra("days");
         for(WHDay day : arrayOfDays) {
        	 for(WHWifiSpot wifiSpot : wifiSpots)
        	    section.addWifiSpotForDay(wifiSpot, day);
         }
         
         SectionSettingsWifiSpotsFragment wifiFragment = (SectionSettingsWifiSpotsFragment)getSupportFragmentManager().findFragmentByTag("wifi_tag"); 
         wifiFragment.updateList();
	 }
	 
	 public void addTimeSchedule(Intent data) {
		 WHActiveTime timeSchedule = (WHActiveTime) data.getSerializableExtra("timeschedule");          
         SectionsManager sectionsManager = SectionsManager.sharedManager();
         
         //Add locations to certain day in section
         WHSection section = sectionsManager.getCurrentSection();
         
         @SuppressWarnings("unchecked")
		 ArrayList<WHDay> arrayOfDays = (ArrayList<WHDay>) data.getSerializableExtra("days");
         for(WHDay day : arrayOfDays) {
        	 section.addActiveTimeForDay(timeSchedule, day);
         }

         SectionSettingsTimeSchedulesFragment timeFragment = (SectionSettingsTimeSchedulesFragment)getSupportFragmentManager().findFragmentByTag("time_tag"); 
         timeFragment.updateList();
	 }
	 
	 @Override
	protected void onDestroy() {
		super.onDestroy();
		
		SectionsManager sectionsManager = SectionsManager.sharedManager();
		WHSection section = sectionsManager.getCurrentSection();
        
		WHProxy proxy = WHProxy.sharedProxy();
        proxy.updateSection(section);
        
        Intent settingChangedIntent = new Intent("com.ros.workandhome.intents.SECTION_SETTING_CHANGED");
        sendBroadcast(settingChangedIntent);
	}
}
