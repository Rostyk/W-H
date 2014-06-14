package com.ros.workandhome.core.entities.WHDay;

import java.io.Serializable;


public class WHDay implements Serializable{
    String dayName;
    
    public WHDay(String dayName) {
    	this.dayName = dayName;
    }
    
    public String toString() {
    	return this.dayName;
    }
    
    public int hashCode() {
    	int code = this.dayName.hashCode();
    	return code;
    }
    
    @Override public boolean equals(Object other) {
        if (!(other instanceof WHDay)) {
            return false;
        }
        WHDay p = (WHDay) other;
        boolean result = this.hashCode() == p.hashCode();
        return result;
    }
}
