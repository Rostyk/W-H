package com.ros.workandhome.sectionmanagement;

import java.util.ArrayList;

import com.ros.workandhome.auxiliary.WHTimeUtils;
import com.ros.workandhome.core.entities.ActiveTime.WHActiveTime;
import com.ros.workandhome.core.entities.WHDay.WHDay;
import com.ros.workandhome.core.entities.section.WHSection;
import com.ros.workandhome.core.entities.wifispot.WHWifiSpot;
import com.ros.workandhome.db.WHProxy;

public class WHTimeScheduleManager {
	
	private static WHTimeScheduleManager instance;

    public static synchronized WHTimeScheduleManager sharedManager() {
   
    	if (instance == null)
            instance = new WHTimeScheduleManager();
        return instance;
    }
    
    WHSection getSectionAround() {
    	 String currentTime = WHTimeUtils.sharedUtils().getCurrentHourAndMinute();
         WHActiveTime currentTimeSchedule = new WHActiveTime(currentTime, currentTime);
         
         WHProxy proxy = WHProxy.sharedProxy();
         ArrayList<WHSection> sections = proxy.getAllSections();
         
         WHDay today = WHTimeUtils.sharedUtils().getCurrentDay();
         WHSection sectionAround = null;
         
     	for(WHSection section : sections) {
      	   ArrayList<WHActiveTime> schedules = section.getActiveTimes().get(today);
      	   if(schedules != null)
      	   for(WHActiveTime schedule : schedules) {
          		   if(currentTimeSchedule.inside(schedule)) {
          			   sectionAround = section;
          			   return sectionAround;
          		   }
          	   }
         }
     	
     	return sectionAround;
    }
    
}
