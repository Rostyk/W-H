package com.ros.workandhome.db.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.ros.workandhome.SharedApplication;
import com.ros.workandhome.core.entities.ActiveTime.WHActiveTime;
import com.ros.workandhome.core.entities.GPSLocation.WHGPSLocation;
import com.ros.workandhome.core.entities.WHDay.WHDay;
import com.ros.workandhome.core.entities.section.WHSection;
import com.ros.workandhome.core.entities.wifispot.WHWifiSpot;
import com.ros.workandhome.db.WHDatabaseHelper;

public class WHSectionDAO implements BaseDAO<WHSection> {
	
	private WHDatabaseHelper dbHelper;
	private SQLiteDatabase database;
	private String DATABASE_TABLE = "whSections";
	private String innerDelimiter = ";";
	private String outerDelimiter = "<dayend>";
	public WHSectionDAO () {
		dbHelper = WHDatabaseHelper.getHelper(SharedApplication.getContext());
		database = dbHelper.getWritableDatabase();
	}
	boolean stringToBoolean(String str) {
		if(str.equalsIgnoreCase("on")) {
			return true;
		}
		else {
			return false;
		}
		
	}
	
	String booleanToString(boolean value) {
		if(value) {
			return "on";
		}
		else {
			return "off";
		}
	}
	
	public WHSection getByID(Long ID) {
		return new WHSection("Empty");
	}
	public WHSection save(WHSection section) {
		 ContentValues cv = new ContentValues();
		 cv.put("name", section.getName());
		 cv.put("description", section.getDescription());
		 cv.put("detect_by_wifi",booleanToString(section.getDetectByWifi()));
		 cv.put("detect_by_gps",booleanToString(section.getDetectByGPS()));
		 cv.put("detect_by_time",booleanToString(section.getDetectByTime()));
		 
		 //Serialize gps locations 
		 Map<WHDay, ArrayList<WHGPSLocation>> gpsLocationsMap = section.getGPSLocations();
		 String gpsString = "";
		 if(gpsLocationsMap != null) {
			 for (Map.Entry<WHDay, ArrayList<WHGPSLocation>> entry : gpsLocationsMap.entrySet()) {
				    WHDay day = entry.getKey();
				    Log.v("WH", "GPS : " + day.toString());
				    gpsString += day.toString();
				    ArrayList<WHGPSLocation> gpsLocationsArray = entry.getValue();
				    for(WHGPSLocation gpsLocation: gpsLocationsArray) {
				    	gpsString += gpsLocation.toString();
				    	gpsString += innerDelimiter;
				    }
				    
				    gpsString += outerDelimiter;
				    Log.v("WH", "GPSLocations: " + gpsString);
				    
			} 
		 }
		 
		cv.put("gps_locations", gpsString);
		 
		
		
		//Serialize wifispots
		Map<WHDay, ArrayList<WHWifiSpot>> wifiSpotsMap = section.getWifiSpots();
		String wifiSpotsString = "";
		if(wifiSpotsMap != null) {
			for (Map.Entry<WHDay, ArrayList<WHWifiSpot>> entry : wifiSpotsMap.entrySet()) {
			    WHDay day = entry.getKey();
			    wifiSpotsString += day.toString();
			    ArrayList<WHWifiSpot> wifiSpotsArray = entry.getValue();
			    for(WHWifiSpot wifiSpot: wifiSpotsArray) {
			    	wifiSpotsString += wifiSpot.toString();
			    	wifiSpotsString += innerDelimiter;
			    }
			    
			    wifiSpotsString += outerDelimiter;
			    Log.v("WH", "WifiSpots: " + wifiSpotsString);
			    
		     }	
		}
		
		cv.put("wifi_spots", wifiSpotsString);
		
		
		//Serialize active times
	    Map<WHDay, ArrayList<WHActiveTime>> activeTimesMap = section.getActiveTimes();
		String activeTimeString = "";
		if(activeTimesMap != null) {
			for (Map.Entry<WHDay, ArrayList<WHActiveTime>> entry : activeTimesMap.entrySet()) {
			    WHDay day = entry.getKey();
			    activeTimeString += day.toString();
			    ArrayList<WHActiveTime> activeTimesArray = entry.getValue();
			    for(WHActiveTime activeTime: activeTimesArray) {
			    	activeTimeString += activeTime.toString();
			    	activeTimeString += innerDelimiter;
			    }
			    activeTimeString += outerDelimiter;
			    Log.v("WH", "ActiveTime: " + activeTimeString);
			    
	        }
	   }
		
	   cv.put("active_times", activeTimeString); 
	   database.insert(DATABASE_TABLE, null, cv);
	   
	   
	   
	   return section;
	}
	
	public WHSection update(WHSection section) {
		delete(section);
		save(section);
		return section;
	}
	
	public void delete(WHSection section) {
		database.delete(DATABASE_TABLE, "name=" + "'" + section.getName() + "'", null);
    }
    
	public Iterable<WHSection> getAll() {
		Cursor cursor = database.rawQuery("select * from " + DATABASE_TABLE,null);
		ArrayList<WHSection> allSections = new ArrayList<WHSection>(); 
		if (cursor.moveToFirst()) {
		            while (cursor.isAfterLast() == false) {
		            	
		            	//Parse name
		                String name = cursor.getString(cursor
		                        .getColumnIndex("name"));
		                
		                String description = cursor.getString(cursor
		                        .getColumnIndex("description"));
		                
		            	WHSection section = new WHSection(name, description);
		            	
		            	//Parse gps locations
		                String gpsLocationsString = cursor.getString(cursor
		                        .getColumnIndex("gps_locations"));
		                section.setGPSLocations(parseGpsLocations(gpsLocationsString));
		                
		                
		                //Parse wifi spots
		                String wifiSpotsString = cursor.getString(cursor
		                        .getColumnIndex("wifi_spots"));
		                section.setWifiSpots(parseWifiSpots(wifiSpotsString));
		                
		                //Parse active times
		                String activeTimesString = cursor.getString(cursor
		                        .getColumnIndex("active_times"));
		                section.setActiveTimes(parseActiveTimes(activeTimesString));
		                
		                
		                //Parse detect by wifi
		                boolean detectByWifi = stringToBoolean(cursor.getString(cursor
		                        .getColumnIndex("detect_by_wifi")));
		                section.setDetectByWifi(detectByWifi);
		                
		                //Parse detect by gps
		                boolean detectByGPS = stringToBoolean(cursor.getString(cursor
		                        .getColumnIndex("detect_by_gps")));
		                section.setDetectByGPS(detectByGPS);
		                
		                //Parse detect by time
		                boolean detectByTime = stringToBoolean(cursor.getString(cursor
		                        .getColumnIndex("detect_by_time")));
		                section.setDetectByTime(detectByTime);
		                
		               
		                
		                allSections.add(section);
		                cursor.moveToNext();
		            }
		        }
		cursor.close();
		return allSections;
    
     }
	private Map<WHDay, ArrayList<WHGPSLocation>> parseGpsLocations(String gpsLocationsString) {
		Map<WHDay, ArrayList<WHGPSLocation>> gpsLocationsMap = new HashMap<WHDay, ArrayList<WHGPSLocation>>();
		String[] chunks = gpsLocationsString.split(outerDelimiter);
		
		for(int i=0; i<chunks.length; i++) {
			String item = chunks[i];
			if(item == "")
				continue;
			String dayStr = item.substring(0,3);
			WHDay day = new WHDay(dayStr);
			
			String gpsCoordicantes = item.substring(3, item.length());
			String[] gpsItems = gpsCoordicantes.split(innerDelimiter);
			ArrayList<WHGPSLocation> arrayOfGpsLocations = new ArrayList<WHGPSLocation>();
			for(int j=0; j<gpsItems.length; j++) {
				String [] gpsCoordinates = gpsItems[j].split(WHGPSLocation.coordinatesDelimiter);
				WHGPSLocation location = new WHGPSLocation(gpsCoordinates[0], gpsCoordinates[1], gpsCoordinates[2]);
				arrayOfGpsLocations.add(location);
			}
			Log.v("WH", "Day: " + dayStr + ".");
			gpsLocationsMap.put(day, arrayOfGpsLocations);
			
		}
		return gpsLocationsMap;
	}
	private Map<WHDay, ArrayList<WHWifiSpot>> parseWifiSpots(String wifiSpotsString) {
		Map<WHDay, ArrayList<WHWifiSpot>> wifiSpotsMap = new HashMap<WHDay, ArrayList<WHWifiSpot>>();
		String[] chunks = wifiSpotsString.split(outerDelimiter);
		for(int i=0; i<chunks.length; i++) {
			String item = chunks[i];
			if(item == "")
				continue;
			String dayStr = item.substring(0,3);
			WHDay day = new WHDay(dayStr);
			
			String wifiSpots = item.substring(3, item.length());
			String[] wifiSpotsSSIDs = wifiSpots.split(innerDelimiter);
			ArrayList<WHWifiSpot> arrayOfWifiSpots = new ArrayList<WHWifiSpot>();
			for(int j=0; j<wifiSpotsSSIDs.length; j++) {
				WHWifiSpot wifiSpot = new WHWifiSpot(wifiSpotsSSIDs[j], "");
				arrayOfWifiSpots.add(wifiSpot);
			}
			wifiSpotsMap.put(day, arrayOfWifiSpots);
			
		}
		return wifiSpotsMap;
	}
	
	private Map<WHDay, ArrayList<WHActiveTime>> parseActiveTimes(String activeTimesString) {
		Map<WHDay, ArrayList<WHActiveTime>> activeTimesMap = new HashMap<WHDay, ArrayList<WHActiveTime>>();
		String[] chunks = activeTimesString.split(outerDelimiter);
		for(int i=0; i<chunks.length; i++) {
			String item = chunks[i];
			if(item == "")
				continue;
			String dayStr = item.substring(0,3);
			WHDay day = new WHDay(dayStr);
			
			String activeTimes = item.substring(3, item.length());
			String[] activeTimesStrings = activeTimes.split(innerDelimiter);
			ArrayList<WHActiveTime> arrayOfActiveTimes = new ArrayList<WHActiveTime>();
			for(int j=0; j<activeTimesStrings.length; j++) {
				String[] hours = activeTimesStrings[j].split("-");
				WHActiveTime activeTime = new WHActiveTime(hours[0], hours[1]);
				arrayOfActiveTimes.add(activeTime);
			}
			activeTimesMap.put(day, arrayOfActiveTimes);
			
		}
		return activeTimesMap;
	}

}