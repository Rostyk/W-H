package com.ros.workandhome.core.entities.GPSLocation;

import java.io.Serializable;

public class WHGPSLocation implements Serializable {
    private String longtitude;
    private String latitude;
    private String altitude;
    private static final long serialVersionUID = 46543445;
    public WHGPSLocation(String longtitude, String latitude, String altitude) {
    	this.longtitude = longtitude;
    	this.latitude = latitude;
    	this.altitude = altitude;
    }
    public void setLongtitude (String longtitude) {
    	this.longtitude = longtitude;
    }
    
    public String getLongtitude() {
    	return longtitude;
    }
    
    public void setLatidtude (String latitude) {
    	this.latitude = latitude;
    }
    
    public String getLatitude() {
    	return this.latitude;
    }

    public void setAltitude (String altitude) {
	  this.altitude = altitude;
    }   
    
    public String getAltitude() {
    	return this.altitude;
    }
    
    public String toString() {
    	return this.longtitude + "," + this.latitude + "," + this.altitude;
    }
    
    public String getRadius() {
    	return "5000";
    }
    public static String coordinatesDelimiter = ",";
    
    
}
