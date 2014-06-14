package com.ros.workandhome.core.entities.wifispot;

import java.io.Serializable;

import com.ros.workandhome.core.entities.WHDay.WHDay;

public class WHWifiSpot implements Serializable{
   /**
	 * 
	 */
   private static final long serialVersionUID = 3463291949846616436L;
   
   private String ssID;
   private String description;
   
   public WHWifiSpot(String ssID, String description) {
	   this.ssID = ssID;
	   this.description = description;
   }
   
   public WHWifiSpot() {
	 
   }
   
   public String getSsID () {
	   return this.ssID;
   }
   
   public void setSsID(String ssID) {
	   this.ssID = ssID;
   }
   
   public String getDescription () {
	   return this.description;
   }
   
   public void setDescription(String description) {
	   this.description = description;
   }
   
   public String toString() {
	   return ssID;
   }
   
   public int hashCode() {
   	  int code = this.ssID.hashCode();
   	  return code;
   }
   
   @Override public boolean equals(Object other) {
       if (!(other instanceof WHWifiSpot)) {
           return false;
       }
       WHWifiSpot s = (WHWifiSpot) other;
       boolean result = this.getSsID().equalsIgnoreCase(s.getSsID());
       return result;
   }
   
}
