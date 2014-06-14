package com.ros.workandhome.activities.gps;

import java.util.ArrayList;
import java.util.List;

import com.ros.workandhome.R;
import com.ros.workandhome.SharedApplication;
import com.ros.workandhome.activities.allsections.SectionActivity;
import com.ros.workandhome.activities.sectionsettings.SectionSettingsActivity;
import com.ros.workandhome.activities.sectionsettings.SectionSettingsGPSLocationsListAdapter;
import com.ros.workandhome.activities.views.DayOfWeekChange;
import com.ros.workandhome.activities.views.DayViewMode;
import com.ros.workandhome.activities.views.WHDayWeekView;
import com.ros.workandhome.core.entities.GPSLocation.WHGPSLocation;
import com.ros.workandhome.core.entities.WHDay.WHDay;
import com.ros.workandhome.core.entities.section.WHSection;
import com.ros.workandhome.core.model.SectionsManager;
import com.ros.workandhome.db.WHProxy;

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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class SectionSettingGPSLocationsFragmet extends Fragment implements DayOfWeekChange{
	private boolean isOn;
	private WHDay currentDay;
	private WHDayWeekView dayWeekView;
	private Context currentActivity = SharedApplication.getContext();
    private ListView locationsListView;
    private SectionSettingsGPSLocationsListAdapter adapter;
	@Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container,
	  Bundle savedInstanceState) {
	    View view = inflater.inflate(R.layout.section_settings_gps_locations_fragment,
	        container, false);
	    
	    initEventHandlers(view);
	    initListView(view);

	    return view;
	}
	
	public void initListView(View view) {
		
		// Get reference to ListView holder
        locationsListView = (ListView) view.findViewById(R.id.gpsLocationsList);
        locationsListView.setClickable(true);
		
        setDay(new WHDay("Mon"));
        dayWeekView.setCurrentDay(new WHDay("Mon"));
        /*
        locationsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        
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
		dayWeekView = (WHDayWeekView) view.findViewById(R.id.weekDaysView);
		dayWeekView.setMode(DayViewMode.DAY_VIEW_SINGLESELECT);
		dayWeekView.containerView = this;
		
		Button addGPSLocationButton = (Button) view.findViewById(R.id.addGpsLocationsButton);
		addGPSLocationButton.setOnClickListener(new View.OnClickListener()
	    {
	      public void onClick(View v)
	      {
	    	  Intent i = new Intent(currentActivity, PickGPSLocationActivity.class);
	    	  getActivity().startActivityForResult(i, 1);
	      }
	    });
		
		final WHSection section = SectionsManager.sharedManager().getCurrentSection();
	    isOn = section.getDetectByGPS();
	    
		final ToggleButton toggleButton = (ToggleButton) view.findViewById(R.id.toggle);
		toggleButton.setChecked(isOn);
		toggleButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			         isOn = buttonView.isChecked();     
			         section.setDetectByGPS(isOn);
			    }
		});
	}
	
	public void updateList() {
		setDay(currentDay);
	}
	
	public void dayOfWeekChanged(WHDay day) {
	    setDay(day);	
	}
	public void setDay(WHDay day) {
		currentDay = day;
		SectionsManager sectionsManager = SectionsManager.sharedManager();
        WHSection section = sectionsManager.getCurrentSection();
        
        ArrayList<WHGPSLocation> locationsList = section.getGPSLocations().get(day);
        // Create a customized ArrayAdapter
        adapter = new SectionSettingsGPSLocationsListAdapter(
                SharedApplication.getContext(), R.layout.section_settings_gps_location_list_item, locationsList);
         
        // Set the ListView adapter
        locationsListView.setAdapter(adapter);
	}
}
