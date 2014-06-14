package com.ros.workandhome.activities.sectionsettings;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ros.workandhome.R;
import com.ros.workandhome.core.entities.ActiveTime.WHActiveTime;
public class SectionSettingsTimeScheduleListAdapter extends ArrayAdapter<WHActiveTime>{
	    private TextView timeScheduleTextView;
	    private List<WHActiveTime> timeSchedules = new ArrayList<WHActiveTime>();
	 
	    public SectionSettingsTimeScheduleListAdapter(Context context, int textViewResourceId,
	            List<WHActiveTime> objects) {
	        super(context, textViewResourceId, objects);
	        this.timeSchedules = objects;
	    }
	 
	    public int getCount() {
	    	if(this.timeSchedules == null)
	    		return 0;
	    	
	        return this.timeSchedules.size();
	    }
	 
	    public WHActiveTime getItem(int index) {
	        return this.timeSchedules.get(index);
	    }
	 
	    public View getView(int position, View convertView, ViewGroup parent) {
	        View row = convertView;
	        if (row == null) {
	            // ROW INFLATION
	            LayoutInflater inflater = (LayoutInflater) this.getContext()
	                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            row = inflater.inflate(R.layout.section_settings_time_list_item, parent, false);
	        }
	 
	        // Get item
	        WHActiveTime timeSchedule = getItem(position);
	          
	        timeScheduleTextView = (TextView) row.findViewById(R.id.section_settings_list_time_schedule);
	        timeScheduleTextView.setText(timeSchedule.toString());
	     
	        return row;
	    }
}
