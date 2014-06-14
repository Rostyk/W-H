package com.ros.workandhome.activities.gps;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ros.workandhome.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MapFragment extends SupportMapFragment {
	
    MapView m;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, 
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState); 
		// inflat and return the layout
		View v = inflater.inflate(R.layout.map_fragment, container, false);
		
		

		return v;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		m.onResume();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		m.onPause();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		m.onDestroy();
	}
	
	@Override
	public void onLowMemory() {
		super.onLowMemory();
		m.onLowMemory();
	}
}