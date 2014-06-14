package com.ros.workandhome.activities.gps;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ros.workandhome.R;
import com.ros.workandhome.activities.views.DayViewMode;
import com.ros.workandhome.activities.views.WHDayWeekView;
import com.ros.workandhome.core.entities.GPSLocation.WHGPSLocation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ToggleButton;
public class PickGPSLocationActivity extends FragmentActivity {
	private WHGPSLocation slectedLocation;
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.section_settings_pick_gps_activity);
	    
	    initEventHandlers();
	    SupportMapFragment fragment =  (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
	    
	    // Getting a reference to the map
        final GoogleMap googleMap = fragment.getMap(); 
        
	    // googleMap.setOn
	    googleMap.setOnMapClickListener(new OnMapClickListener() {
           @Override
            public void onMapClick(LatLng latLng) {

        	    slectedLocation = new WHGPSLocation((latLng.longitude + ""), (latLng.latitude +""), "0.0");
                // Creating a marker
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title(latLng.latitude + " : " + latLng.longitude);
                googleMap.clear();
                googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                googleMap.addMarker(markerOptions);
            }
        });
	    
	}
	
	protected boolean isRouteDisplayed() {
	    return false;
	}
	
	public void onResume() {
		super.onResume();
		//m.onResume();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		//m.onPause();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		//m.onDestroy();
	}
	
	@Override
	public void onLowMemory() {
		super.onLowMemory();
		//m.onLowMemory();
	}
	
	public void initEventHandlers() {
		WHDayWeekView dayWeekView = (WHDayWeekView)findViewById(R.id.weekDaysView);
		dayWeekView.setMode(DayViewMode.DAY_VIEW_MULTISELECT);
		
		Button pickGPSLocationButton = (Button) findViewById(R.id.pickGPSLocationButton);
		pickGPSLocationButton.setOnClickListener(new View.OnClickListener()
	    {
	      public void onClick(View v)
	      {
	    	  Intent returnIntent = new Intent();
	    	  returnIntent.putExtra("location", slectedLocation);
	    	  WHDayWeekView dayWeekView = (WHDayWeekView)findViewById(R.id.weekDaysView);
	    	  returnIntent.putExtra("days", dayWeekView.getSelectedDays());
	    	  setResult(RESULT_OK,returnIntent);     
	    	  finish();
	      }
	    });
		
	}
}
