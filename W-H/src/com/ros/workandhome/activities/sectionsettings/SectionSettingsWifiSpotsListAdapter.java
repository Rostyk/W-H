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
import com.ros.workandhome.core.entities.wifispot.WHWifiSpot;

public class SectionSettingsWifiSpotsListAdapter extends ArrayAdapter<WHWifiSpot>{
	    private TextView ssidTextView;
	    private List<WHWifiSpot> wifiSpots = new ArrayList<WHWifiSpot>();
	 
	    public SectionSettingsWifiSpotsListAdapter(Context context, int textViewResourceId,
	            List<WHWifiSpot> objects) {
	        super(context, textViewResourceId, objects);
	        this.wifiSpots = objects;
	    }
	 
	    public int getCount() {
	    	if(this.wifiSpots == null)
	    		return 0;
	        return this.wifiSpots.size();
	    }
	 
	    public WHWifiSpot getItem(int index) {
	        return this.wifiSpots.get(index);
	    }
	 
	    public View getView(int position, View convertView, ViewGroup parent) {
	        View row = convertView;
	        if (row == null) {
	            // ROW INFLATION
	            LayoutInflater inflater = (LayoutInflater) this.getContext()
	                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            row = inflater.inflate(R.layout.section_settings_wifi_spots_list_item, parent, false);
	        }
	 
	        // Get item
	        WHWifiSpot wifiSpot = getItem(position);
	          
	        ssidTextView = (TextView) row.findViewById(R.id.section_settings_list_wifi_spot_ssid);
	        ssidTextView.setText(wifiSpot.getSsID());
	        return row;
	    }
}
