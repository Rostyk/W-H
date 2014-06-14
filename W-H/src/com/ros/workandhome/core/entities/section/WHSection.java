package com.ros.workandhome.core.entities.section;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

import com.ros.workandhome.core.entities.ActiveTime.WHActiveTime;
import com.ros.workandhome.core.entities.GPSLocation.WHGPSLocation;
import com.ros.workandhome.core.entities.WHDay.WHDay;
import com.ros.workandhome.core.entities.wifispot.WHWifiSpot;

public class WHSection implements Serializable {
       /**
	 * 
	 */
	private static final long serialVersionUID = 1481063259569467499L;
	
	String name;
	boolean detectByWifi;
	boolean detectByGPS;
	boolean detectByTime;
	
	    public boolean getDetectByWifi() {
		   return detectByWifi;
	    }
	
	    public void setDetectByWifi(boolean detectByWifi) {
		    this.detectByWifi = detectByWifi;
	    }
	    
	    public boolean getDetectByGPS() {
			   return detectByGPS;
		}
		
		public void setDetectByGPS(boolean detectByGPS) {
			    this.detectByGPS = detectByGPS;
		}
		
		public boolean getDetectByTime() {
			   return detectByTime;
		}
		
		public void setDetectByTime(boolean detectByTime) {
			    this.detectByTime = detectByTime;
		}
	
	
	
	
	
       String description;
       
       public WHSection(String name) {
    	   this.name = name;
       }
       public WHSection(String name, String description) {
    	   this.name = name;
    	   this.description = description;
       }
       
       public void setName(String name) {
    	   this.name = name;
       }
       
       public String getName() {
    	   return this.name;
       }
       
       public void setDescription(String description) {
    	   this.description = description;
       }
       
       public String getDescription() {
    	   return this.description;
       }
       
       public void setGPSLocationsForDay(ArrayList<WHGPSLocation> gpsLocations, WHDay day) {
    	   this.gpsLocationsMap.put(day, gpsLocations);
       }
       
       public void setWifiSpotsForDay(ArrayList<WHWifiSpot> wifiSpots, WHDay day) {
    	   this.wiSpotsMap.put(day, wifiSpots);
       }
       
       public void setActiveTimesForDay(ArrayList<WHActiveTime> activeTimes, WHDay day) {
    	   this.activeTimesMap.put(day, activeTimes);
       }
       
       public void setGPSLocations(Map<WHDay, ArrayList<WHGPSLocation>> gpsLocationsMap) {
    	   this.gpsLocationsMap = gpsLocationsMap;
       }
       
       public void addGPSLocationForDay(WHGPSLocation location, WHDay day) {
    	  ArrayList<WHGPSLocation> arrayOfLocation =  this.gpsLocationsMap.get(day);
    	  if(arrayOfLocation == null) {
    		  arrayOfLocation = new ArrayList<WHGPSLocation>();
    		  this.gpsLocationsMap.put(day, arrayOfLocation);
    	  }
    	  arrayOfLocation.add(location);
       }
       
       public void setWifiSpots(Map<WHDay, ArrayList<WHWifiSpot>> wiSpotsMap) {
    	   this.wiSpotsMap = wiSpotsMap;
       }
       public void addWifiSpotForDay(WHWifiSpot wifiSpot, WHDay day) {
     	  ArrayList<WHWifiSpot> arrayOfWifiSpots =  this.wiSpotsMap.get(day);
     	  if(arrayOfWifiSpots == null) {
     		  arrayOfWifiSpots = new ArrayList<WHWifiSpot>();
     		  this.wiSpotsMap.put(day, arrayOfWifiSpots);
     	  }
     	 arrayOfWifiSpots.add(wifiSpot);
        }
       
       public void setActiveTimes(Map<WHDay, ArrayList<WHActiveTime>> activeTimesMap) {
    	   this.activeTimesMap = activeTimesMap;
       }
       
       public void addActiveTimeForDay(WHActiveTime timeSchedule, WHDay day) {
     	  ArrayList<WHActiveTime> arrayOfTimeSchedules =  this.activeTimesMap.get(day);
     	  if(arrayOfTimeSchedules == null) {
     		  arrayOfTimeSchedules = new ArrayList<WHActiveTime>();
     		  this.activeTimesMap.put(day, arrayOfTimeSchedules);
     	  }
     	 arrayOfTimeSchedules.add(timeSchedule);
       }
       
       public Map<WHDay, ArrayList<WHGPSLocation>> getGPSLocations() {
    	   return gpsLocationsMap;
       }
       
       public Map<WHDay, ArrayList<WHWifiSpot>> getWifiSpots() {
    	   return wiSpotsMap;
       }
       
       public Map<WHDay, ArrayList<WHActiveTime>> getActiveTimes() {
    	   return activeTimesMap;
       }
       
       private Map<WHDay, ArrayList<WHGPSLocation>> gpsLocationsMap;
       private Map<WHDay, ArrayList<WHWifiSpot>> wiSpotsMap;
       private Map<WHDay, ArrayList<WHActiveTime>> activeTimesMap;
       
}
