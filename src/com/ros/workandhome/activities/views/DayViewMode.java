package com.ros.workandhome.activities.views;

public enum DayViewMode {
	DAY_VIEW_SINGLESELECT(0), DAY_VIEW_MULTISELECT(1);
	
	 private int dayViewMode;
	 
	 private DayViewMode(int mode) {
		 dayViewMode = mode;
	 }
	 
	 public int getDayViewMode() {
		   return dayViewMode;
	}
}
