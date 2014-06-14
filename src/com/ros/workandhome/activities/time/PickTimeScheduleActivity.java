package com.ros.workandhome.activities.time;

import java.util.ArrayList;

import com.ros.workandhome.R;
import com.ros.workandhome.activities.views.DayViewMode;
import com.ros.workandhome.activities.views.WHDayWeekView;
import com.ros.workandhome.core.entities.ActiveTime.WHActiveTime;
import com.ros.workandhome.core.entities.wifispot.WHWifiSpot;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.ToggleButton;

public class PickTimeScheduleActivity extends Activity {
	private TimePicker startTimePicker;
	private TimePicker endTimePicker;
	private WHDayWeekView dayWeekView;
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.section_settings_pick_time_schedule_activity);
	    
	    initEventHandlers();
	}
	
	public void initEventHandlers() {
		dayWeekView = (WHDayWeekView)findViewById(R.id.weekDaysView);
		dayWeekView.setMode(DayViewMode.DAY_VIEW_MULTISELECT);
		
		startTimePicker = (TimePicker) findViewById(R.id.startTimePicker);
		endTimePicker = (TimePicker) findViewById(R.id.endTimePicker);
		

		Button pickTimeScheduleButton = (Button) findViewById(R.id.pickTimeScheduleButton);
		pickTimeScheduleButton.setOnClickListener(new View.OnClickListener()
	    {
	      public void onClick(View v)
	      {
	    	  Intent returnIntent = new Intent();
	    	  String startTime = startTimePicker.getCurrentHour().toString() + ":" +
			  startTimePicker.getCurrentMinute().toString();
	    	  String endTime = endTimePicker.getCurrentHour().toString() + ":" +
	    	  endTimePicker.getCurrentMinute().toString();
	    	  
	    	  WHActiveTime timeSchedule = new WHActiveTime(startTime, endTime);
	    	  returnIntent.putExtra("timeschedule", timeSchedule);
	    	 
	    	  dayWeekView = (WHDayWeekView)findViewById(R.id.weekDaysView);
	    	  returnIntent.putExtra("days", dayWeekView.getSelectedDays());
	    	  setResult(RESULT_OK, returnIntent);     
	    	  finish();
	      }
	    });	
	}
	    
	
}
