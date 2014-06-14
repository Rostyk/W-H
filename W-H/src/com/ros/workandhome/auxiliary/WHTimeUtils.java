package com.ros.workandhome.auxiliary;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.ros.workandhome.core.entities.WHDay.WHDay;

public class WHTimeUtils {
	public static final String inputFormat = "kk:mm";
	private SimpleDateFormat inputParser;
	private static WHTimeUtils timeUtils;
    public static synchronized WHTimeUtils sharedUtils() {
   
    	if (timeUtils == null)
    		timeUtils = new WHTimeUtils();
        return timeUtils;
    }
    
    private WHTimeUtils() {
    	inputParser = new SimpleDateFormat(inputFormat, Locale.UK);
    }
    
   public WHDay getCurrentDay() {
	   SimpleDateFormat sdf = new SimpleDateFormat("EEEE", Locale.getDefault());
	   Date d = new Date();
	   String dayOfTheWeek = sdf.format(d);
	   
	   return new WHDay(dayOfTheWeek.substring(0, 3));
   }
   
   public String getCurrentHourAndMinute() {
	   Calendar c = Calendar.getInstance(); 
	   int hour = c.get(Calendar.HOUR_OF_DAY);
	   int minute = c.get(Calendar.MINUTE);
	   
	   return hour + ":" + minute;
   }
   
   public Date parseDate(String date) {
		    try {
		        return inputParser.parse(date);
		    } catch (java.text.ParseException e) {
		    	e.printStackTrace();
		        return new Date(0);
		    }
   }
}
