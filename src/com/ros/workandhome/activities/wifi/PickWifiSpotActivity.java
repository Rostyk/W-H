package com.ros.workandhome.activities.wifi;

import java.util.ArrayList;
import java.util.List;

import com.ros.workandhome.R;
import com.ros.workandhome.SharedApplication;
import com.ros.workandhome.activities.sectionsettings.SectionSettingsPickWifiSpotListAdapter;
import com.ros.workandhome.activities.views.DayViewMode;
import com.ros.workandhome.activities.views.WHDayWeekView;
import com.ros.workandhome.core.entities.wifispot.WHWifiSpot;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.ToggleButton;

public class PickWifiSpotActivity extends Activity {
	private WHDayWeekView dayWeekView;
    private ListView storedWifiSpotsListView;
    private SectionSettingsPickWifiSpotListAdapter adapter;

	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.section_settings_pick_wifi_spot_activity);
	        
	        initEventHandlers();
	        initListView();
	 }
	 
	 public void initEventHandlers() {
			dayWeekView = (WHDayWeekView)findViewById(R.id.weekDaysView);
			dayWeekView.setMode(DayViewMode.DAY_VIEW_MULTISELECT);
			
			Button pickWifiSpotButton = (Button) findViewById(R.id.pickWifiSpotButton);
			pickWifiSpotButton.setOnClickListener(new View.OnClickListener()
		    {
		      public void onClick(View v)
		      {
		    	  Intent returnIntent = new Intent();
		    	  ArrayList<WHWifiSpot> selectedWifiSpots = getSelectedItems();
		    	  returnIntent.putExtra("wifispots", selectedWifiSpots);
		    	  
		    	  dayWeekView = (WHDayWeekView)findViewById(R.id.weekDaysView);
		    	  returnIntent.putExtra("days", dayWeekView.getSelectedDays());
		    	  setResult(RESULT_OK, returnIntent);     
		    	  finish();
		      }
		    });
			
		}
		
		public void updateList() {
			adapter.notifyDataSetChanged();
		}
		
	public void initListView() {
		// Get reference to ListView holder
		storedWifiSpotsListView = (ListView) findViewById(R.id.pickStoreWifiSpotList);
		storedWifiSpotsListView.setClickable(true);
		storedWifiSpotsListView.setItemsCanFocus(false);
		storedWifiSpotsListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        
		
		ArrayList<WHWifiSpot> storedWifiSpotsList = getStoredWifis();
        // Create a customized ArrayAdapter
        adapter = new SectionSettingsPickWifiSpotListAdapter(
                SharedApplication.getContext(), R.layout.section_settings_pick_wifi_spot_item, storedWifiSpotsList);
         
        // Set the ListView adapter
        storedWifiSpotsListView.setAdapter(adapter);
        updateList();
	}
	
	public ArrayList<WHWifiSpot> getStoredWifis() {
		Context context = SharedApplication.getContext();
		ArrayList<WHWifiSpot> arrayOfStoredWifis = new ArrayList<WHWifiSpot>();
    	String ssid = null;
    	    ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    	    NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
    	    final WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    	    final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
    	    if (connectionInfo != null) {
    	        //if (connectionInfo != null && !StringUtil.isBlank(connectionInfo.getSSID())) {
    	      ssid = connectionInfo.getSSID();
    	    }
    	    
    	    // List stored networks
    	    List<WifiConfiguration> configs = wifiManager.getConfiguredNetworks();
    	    for (WifiConfiguration config : configs) {
    	    	arrayOfStoredWifis.add(new WHWifiSpot(config.SSID.replace("\"", ""), ""));
    	    }
    	    return arrayOfStoredWifis;
    	    
    }
	
	
	public ArrayList<WHWifiSpot> getSelectedItems() {
		ArrayList<WHWifiSpot> selectedWifiSpots = new ArrayList<WHWifiSpot>();
    	for (int i = 0; i < storedWifiSpotsListView.getChildCount(); i++)
    	{
    	    View _v = storedWifiSpotsListView.getChildAt(i);
    	    CheckedTextView ssIDCheckedTextView = (CheckedTextView) _v.findViewById(R.id.section_settings_pick_wifi_spot_check_item);
    	    if(ssIDCheckedTextView.isChecked()) {
    	    	selectedWifiSpots.add(new WHWifiSpot(ssIDCheckedTextView.getText().toString(), ""));
    	    }
    	}
    	return selectedWifiSpots;
	}
		
}
