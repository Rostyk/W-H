package com.ros.workandhome.activities.wifi;

import java.util.ArrayList;

import com.ros.workandhome.R;
import com.ros.workandhome.SharedApplication;
import com.ros.workandhome.activities.gps.PickGPSLocationActivity;
import com.ros.workandhome.activities.sectionsettings.SectionSettingsGPSLocationsListAdapter;
import com.ros.workandhome.activities.sectionsettings.SectionSettingsTimeScheduleListAdapter;
import com.ros.workandhome.activities.sectionsettings.SectionSettingsWifiSpotsListAdapter;
import com.ros.workandhome.activities.views.DayOfWeekChange;
import com.ros.workandhome.activities.views.DayViewMode;
import com.ros.workandhome.activities.views.WHDayWeekView;
import com.ros.workandhome.core.entities.ActiveTime.WHActiveTime;
import com.ros.workandhome.core.entities.GPSLocation.WHGPSLocation;
import com.ros.workandhome.core.entities.WHDay.WHDay;
import com.ros.workandhome.core.entities.section.WHSection;
import com.ros.workandhome.core.entities.wifispot.WHWifiSpot;
import com.ros.workandhome.core.model.SectionsManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class SectionSettingsWifiSpotsFragment extends Fragment implements DayOfWeekChange{
	private boolean isOn;
	private WHDayWeekView dayWeekView;
	private WHDay currentDay;
	private Context currentActivity = SharedApplication.getContext();
    private ListView wifiSpotsListView;
    private SectionSettingsWifiSpotsListAdapter adapter;
	@Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container,
	  Bundle savedInstanceState) {
	    View view = inflater.inflate(R.layout.section_settings_wifi_spots_fragment,
	        container, false);
	    
	    initEventHandlers(view);
	    initListView(view);
	    
	    return view;
	}
	
	public void initListView(View view) {
		dayWeekView = (WHDayWeekView) view.findViewById(R.id.weekDaysView);
		dayWeekView.setMode(DayViewMode.DAY_VIEW_SINGLESELECT);
		dayWeekView.containerView = this;
		
        // Get reference to ListView holder
        wifiSpotsListView = (ListView) view.findViewById(R.id.wifiSpotsList);
        wifiSpotsListView.setClickable(true);
        setDay(new WHDay("Mon"));
        dayWeekView.setCurrentDay(new WHDay("Mon")); 

        /*
        wifiSpotsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        
          @Override
          public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
            //WHSection section = (WHSection)sectionsListView.getItemAtPosition(position);
            //Intent intent = new Intent(currentActivity, SectionActivity.class);
	    	//startActivity(intent);
          }
        });
        */
	}
	
	public void initEventHandlers(View view) {
		Button addWifiSpotButton = (Button) view.findViewById(R.id.addWifiSpotButton);
		addWifiSpotButton.setOnClickListener(new View.OnClickListener()
	    {
	      public void onClick(View v)
	      {
	    	  Intent i = new Intent(currentActivity, PickWifiSpotActivity.class);
	    	  getActivity().startActivityForResult(i, 2);
	      }
	    });
		
		final WHSection section = SectionsManager.sharedManager().getCurrentSection();
	    isOn = section.getDetectByWifi();
	    
		final ToggleButton toggleButton = (ToggleButton) view.findViewById(R.id.toggle);
		toggleButton.setChecked(isOn);
		toggleButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			         isOn = buttonView.isChecked();
			         section.setDetectByWifi(isOn);
			    }
		});
	}
	
	public void updateList() {
		setDay(currentDay);
		wifiSpotsListView.invalidate();
	}
	
	public void dayOfWeekChanged(WHDay day) {
	    setDay(day);	
	}
	public void setDay(WHDay day) {
		currentDay = day;
		SectionsManager sectionsManager = SectionsManager.sharedManager();
        WHSection section = sectionsManager.getCurrentSection();
        
		ArrayList<WHWifiSpot> wifiSpotsList  = section.getWifiSpots().get(day);
        // Create a customized ArrayAdapter
        adapter = new SectionSettingsWifiSpotsListAdapter(
                SharedApplication.getContext(), R.layout.section_settings_wifi_spots_list_item, wifiSpotsList);
         
        // Set the ListView adapter
        wifiSpotsListView.setAdapter(adapter);
	}
}
