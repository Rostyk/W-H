package com.ros.workandhome.sectionmanagement;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.ros.workandhome.SharedApplication;
import com.ros.workandhome.auxiliary.WHTimeUtils;
import com.ros.workandhome.core.entities.WHDay.WHDay;
import com.ros.workandhome.core.entities.section.WHSection;
import com.ros.workandhome.core.entities.wifispot.WHWifiSpot;
import com.ros.workandhome.db.WHProxy;

public class WHNetworkManager {
	private static WHNetworkManager instance;

    public static synchronized WHNetworkManager sharedManager() {
   
    	if (instance == null)
            instance = new WHNetworkManager();
        return instance;
    }
    
    
    public ArrayList<WHWifiSpot> getReachableWifiSpots() {
    	ArrayList<WHWifiSpot> reachableWifiSpots = new ArrayList<WHWifiSpot>();
    	
    	Context context = SharedApplication.getContext();
    	final WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
	    WifiInfo info = wifiManager.getConnectionInfo();
	    String textStatus = "";
	    textStatus += "\n\nWiFi Status: " + info.toString();
	    String BSSID = info.getBSSID();
	    String MAC = info.getMacAddress();

	    List<ScanResult> results = wifiManager.getScanResults();
	    if(results != null) {
	    	for (ScanResult result : results) {
	    		reachableWifiSpots.add(new WHWifiSpot(result.SSID, ""));
    	           
    	    }
	    }
	    
	    return reachableWifiSpots;
    }
    
    public WHSection getSectionAround() {
    	
        ArrayList<WHWifiSpot> reachableSpots = getReachableWifiSpots();
        
        WHProxy proxy = WHProxy.sharedProxy();
        ArrayList<WHSection> sections = proxy.getAllSections();
        
        WHDay today = WHTimeUtils.sharedUtils().getCurrentDay();
        WHSection sectionAround = null;
        
    	for(WHSection section : sections) {
     	   ArrayList<WHWifiSpot> spots = section.getWifiSpots().get(today);
     	   if(spots != null)
     	   for(WHWifiSpot reachableSpot : reachableSpots) {
         	   for(WHWifiSpot sectionSpot : spots) {
         		   if(sectionSpot.equals(reachableSpot)) {
         			   sectionAround = section;
         			   return sectionAround;
         		   }
         	   }
            }
        }
    	
    	return sectionAround;
    }
} 
