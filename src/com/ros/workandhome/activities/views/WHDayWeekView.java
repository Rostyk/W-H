package com.ros.workandhome.activities.views;

import java.util.ArrayList;

import com.ros.workandhome.R;
import com.ros.workandhome.activities.sectionsettings.SectionSettingsActivity;
import com.ros.workandhome.core.entities.WHDay.WHDay;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;

public class WHDayWeekView extends FrameLayout{
	 public DayOfWeekChange containerView; 
	 private Button currentSelecteButton;
	 private boolean buttonStates[] = new boolean[7];
	 private WHDay[] days = {new WHDay("Mon"), new WHDay("Tue"), new WHDay("Wed"), new WHDay("Thu"), new WHDay("Fri"), new WHDay("Sat"), new WHDay("Sun") };
	 private DayViewMode mode;
	 private int [] buttonsIDs = {R.id.mondayButton, R.id.tuesdayButton, R.id.wednesdayButton, R.id.thursdayButton, R.id.fridayButton, R.id.saturdayButton, R.id.sundayButton};
	 public void setMode(DayViewMode mode) {
		 this.mode = mode;
	 }
	 
	 public WHDayWeekView(Context context, AttributeSet attrs, int defStyle) {
         super(context, attrs, defStyle);
         initView();
     }

     public WHDayWeekView(Context context, AttributeSet attrs) {
         super(context, attrs);
         initView();
     }

     public WHDayWeekView(Context context) {
             super(context);
             initView();
             
     }
     
    @SuppressLint("UseValueOf")
	private void initView() {
    	    View view = inflate(getContext(), R.layout.section_settings_day_view, null);
            addView(view);
            
            for(int i=0; i<buttonsIDs.length; i++) {
            	int buttonID = buttonsIDs[i];
            	final WHDayWeekView dayView = this;
            	final Button dayButton = (Button) findViewById(buttonID);
            	dayButton.setOnClickListener(new View.OnClickListener()
    		    {
    		      @SuppressLint("UseValueOf")
				public void onClick(View v)
    		      {
    		    	  if(dayView.mode == DayViewMode.DAY_VIEW_SINGLESELECT) {
    		    		  if(currentSelecteButton != null) {
    		    			  currentSelecteButton.setBackgroundResource(R.drawable.day_button);
    		    		  }
    		    		  dayButton.setBackgroundResource(R.drawable.day_button_border);
    		    		  currentSelecteButton = dayButton;
    		    		  buttonStates[new Integer((String)currentSelecteButton.getTag()).intValue()] = false;
    		    		  buttonStates[new Integer((String)dayButton.getTag()).intValue()] = true;
    		    		  
    		    		  if(dayView.containerView != null) {
    		    			  WHDay day = days[new Integer((String)dayButton.getTag()).intValue()];
    		    			  containerView.dayOfWeekChanged(day);
    		    		  }
              	      }
    		    	  if(dayView.mode == DayViewMode.DAY_VIEW_MULTISELECT) {
    		    		  if(buttonStates[new Integer((String)dayButton.getTag()).intValue()]) {
    		    			  dayButton.setBackgroundResource(R.drawable.day_button);
    		    		      buttonStates[new Integer((String)dayButton.getTag()).intValue()] = false;
    		    		  }
    		    	      else{
    		    		      dayButton.setBackgroundResource(R.drawable.day_button_border);
    		    		      buttonStates[new Integer((String)dayButton.getTag()).intValue()] = true;
    		    	      }
    		    	 
    		          }
    		      }
    		    
    		    });
            }
    }
            
   public void setCurrentDay(WHDay day) {
            	
            	int buttonIndex = 0;
            	int i = 0;
            	for(WHDay _day : days) {
            		if(_day.equals(day)) {
            			buttonIndex = i;
            			break;
            		}
            		i++;
            	}
            	int buttonID = buttonsIDs[buttonIndex];
            	Button dayButton = (Button)findViewById(buttonID);
            	if(mode == DayViewMode.DAY_VIEW_SINGLESELECT) {
		    		  if(currentSelecteButton != null) {
		    			  currentSelecteButton.setBackgroundResource(R.drawable.day_button);
		    		  }
		    		  dayButton.setBackgroundResource(R.drawable.day_button_border);
		    		  currentSelecteButton = dayButton;
		    		  buttonStates[new Integer((String)currentSelecteButton.getTag()).intValue()] = false;
		    		  buttonStates[new Integer((String)dayButton.getTag()).intValue()] = true;
        	      }
     }
               
    
     public ArrayList<WHDay> getSelectedDays() {
    	 ArrayList<WHDay> selectedDays = new ArrayList<WHDay>();
   		    for(int i=0; i<buttonStates.length; i++) {
   		        if(buttonStates[i]) {
   		    	    selectedDays.add(days[i]);
   		        }
            }
		return selectedDays;
   }
     
}
