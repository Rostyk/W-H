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
import com.ros.workandhome.core.entities.GPSLocation.WHGPSLocation;


public class SectionSettingsGPSLocationsListAdapter extends ArrayAdapter<WHGPSLocation>{
	    private TextView gpsLocationLatitude;
	    private TextView gpsLocationLongtitude;
	    private List<WHGPSLocation> locations = new ArrayList<WHGPSLocation>();
	 
	    public SectionSettingsGPSLocationsListAdapter(Context context, int textViewResourceId,
	            List<WHGPSLocation> objects) {
	        super(context, textViewResourceId, objects);
	        this.locations = objects;
	    }
	 
	    public int getCount() {
	    	if(this.locations == null)
	    		return 0;
	        return this.locations.size();
	    }
	 
	    public WHGPSLocation getItem(int index) {
	        return this.locations.get(index);
	    }
	 
	    public View getView(int position, View convertView, ViewGroup parent) {
	        View row = convertView;
	        if (row == null) {
	            // ROW INFLATION
	            LayoutInflater inflater = (LayoutInflater) this.getContext()
	                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            row = inflater.inflate(R.layout.section_settings_gps_location_list_item, parent, false);
	        }
	 
	        // Get item
	        WHGPSLocation location = getItem(position);
	          
	        gpsLocationLatitude = (TextView) row.findViewById(R.id.section_settings_list_latitude);
	        String latStr = location.getLatitude() + "";
	        if(latStr.length() > 10) {
	        	latStr = latStr.substring(0, 6);
	        }
	        String longStr = location.getLongtitude() + "";
	        if(longStr.length() > 10) {
	        	longStr = longStr.substring(0, 6);
	        }
	        gpsLocationLatitude.setText("Latitude: " + latStr);
	        gpsLocationLongtitude = (TextView) row.findViewById(R.id.section_settings_list_longtitude);
	        gpsLocationLongtitude.setText("Longtitude: " + longStr);
	        
	        
	        return row;
	    }
}
