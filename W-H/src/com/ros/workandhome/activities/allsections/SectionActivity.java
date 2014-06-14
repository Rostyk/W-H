package com.ros.workandhome.activities.allsections;


import com.ros.workandhome.R;
import com.ros.workandhome.activities.sectionsettings.SectionSettingsActivity;
import com.ros.workandhome.core.entities.GPSLocation.WHGPSLocation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;


public class SectionActivity extends FragmentActivity {
	private Activity currentActivity = this;
	 public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.section_activity);
	        addButtonEventHandlers();
	    }
	 
	 public void addButtonEventHandlers() {
		 ImageButton gpsLocationButton = (ImageButton) findViewById(R.id.gpsLocationsButton);
		    gpsLocationButton.setOnClickListener(new View.OnClickListener()
		    {
		      public void onClick(View v)
		      {
		    	  Intent i = new Intent(currentActivity, SectionSettingsActivity.class);
		    	  i.putExtra("tabNumber", 0);
		    	  startActivity(i);
		      }
		    });
		    
		    ImageButton wifiSpotsButton = (ImageButton) findViewById(R.id.wifiSpotsButton);
		    wifiSpotsButton.setOnClickListener(new View.OnClickListener()
		    {
		      public void onClick(View v)
		      {
		    	  Intent i = new Intent(currentActivity, SectionSettingsActivity.class);
		    	  i.putExtra("tabNumber", 1);
		    	  startActivity(i);
		      }
		    });
		    
		    ImageButton timeSchedulesButton = (ImageButton) findViewById(R.id.timeSchedulesButton);
		    timeSchedulesButton.setOnClickListener(new View.OnClickListener()
		    {
		      public void onClick(View v)
		      {
		    	  Intent i = new Intent(currentActivity, SectionSettingsActivity.class);
		    	  i.putExtra("tabNumber", 2);
		    	  startActivity(i);
		      }
		    });
	 }
	 
	
}
