package com.ros.workandhome.core.entities.ActiveTime;

import java.io.Serializable;
import java.util.Date;

import com.ros.workandhome.auxiliary.WHTimeUtils;

public class WHActiveTime implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -8776890926009770020L;
	private String startTime;
    private String endTime;
    
    public WHActiveTime(String startTime, String endTime) {
    	this.startTime = startTime;
    	this.endTime = endTime;
    }
    public String getStartTime() {
    	return this.startTime;
    }
    
    public void setStartTime(String startTime) {
    	this.startTime = startTime;
    }
    
    public String getEndTime() {
    	return this.endTime;
    }
    
    public void setEndTime(String endTime) {
    	this.endTime = endTime;
    }
    
    public String toString() {
    	return startTime + "-" + endTime;
    }
    
    public boolean inside(WHActiveTime time) {
    	boolean result = false;
    	WHTimeUtils timeUtils = WHTimeUtils.sharedUtils();
    	
    	Date date = timeUtils.parseDate(this.getStartTime());
    	Date dateCompareStart = timeUtils.parseDate(time.getStartTime());
    	Date dateCompareEnd = timeUtils.parseDate(time.getEndTime());
    	
    	if ( date.after(dateCompareStart) && date.before(dateCompareEnd)) {
    		result = true;
        }
    	
    	return result;
    }
    
    
    
}
