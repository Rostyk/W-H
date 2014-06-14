package com.ros.workandhome.activities.allsections;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import com.ros.workandhome.R;
import com.ros.workandhome.SharedApplication;
import com.ros.workandhome.activities.calls.ManageCallsActivity;
import com.ros.workandhome.activities.sectionsettings.SectionSettingsActivity;
import com.ros.workandhome.activities.views.WHCircleView;
import com.ros.workandhome.activities.views.WHDayWeekView;
import com.ros.workandhome.core.entities.section.WHSection;
import com.ros.workandhome.core.model.SectionsManager;
import com.ros.workandhome.db.WHProxy;
import com.ros.workandhome.db.WHSettingsProxy;
import com.ros.workandhome.sectionmanagement.WHGeofencesManager;
import com.ros.workandhome.sectionmanagement.WHSectionManagingService;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

@SuppressLint("DefaultLocale")
public class AllSectionsFragment extends Fragment {
	private int audioValues[] = {0, 30, 60, 100};
	private int audioVolumePoint;
	private View currentView;
	private View views[];
	private int visibleSectionPosition = 0;
	private WHSection currentVisibleSection;
	private RadioButton[] radiobuttons;
	private HorizontalPager mPager;
	private RadioGroup mRadioGroup;
	private String suffix = "_main_section_view_";
	private ListView sectionsListView;
	private boolean optionsBarVisible = false;
	private List<WHSection> sectionsList = new ArrayList<WHSection>();
	private WHSection previousActiveSection;
	
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
	
	private final BroadcastReceiver receiver = new BroadcastReceiver() {
		   @Override
		   public void onReceive(Context context, Intent intent) {
			    if(intent.getAction().equalsIgnoreCase("com.ros.workandhome.intents.ACTIVE_SECTION_CHANGED"))
			    {
			    	if(previousActiveSection != null) {
			    		unhighlightSection(previousActiveSection);
			    	}
			    	
			    	WHSection activeSection = (WHSection) intent.getSerializableExtra("section");
			    	highlightSection(activeSection);
			        
			        previousActiveSection = activeSection;
			    }
			    if(intent.getAction().equalsIgnoreCase("com.ros.workandhome.intents.SECTION_SETTING_CHANGED"))
			    {
			    	WHSection section = (WHSection)sectionsList.get(visibleSectionPosition);
			    	String resourceName = /*section.getName().toLowerCase()*/"work" + suffix;
			    	
			    	Button barGPSButton = (Button) currentView.findViewById(R.id.barGPSButton);
			    	int resID = SharedApplication.getContext().getResources().getIdentifier(resourceName + "gps_" + booleanToString(section.getDetectByGPS()), "drawable", SharedApplication.getContext().getPackageName());
			    	barGPSButton.setBackgroundResource(resID);
			    	
			    	Button barWifiButton = (Button) currentView.findViewById(R.id.barWifiButton);
			    	resID = SharedApplication.getContext().getResources().getIdentifier(resourceName + "wifi_" + booleanToString(section.getDetectByWifi()), "drawable", SharedApplication.getContext().getPackageName());
			    	barWifiButton.setBackgroundResource(resID);
			    	
			    	Button barTimeButton = (Button) currentView.findViewById(R.id.barTimeButton);
			        resID = SharedApplication.getContext().getResources().getIdentifier(resourceName + "time_" + booleanToString(section.getDetectByTime()), "drawable", SharedApplication.getContext().getPackageName());
			        barTimeButton.setBackgroundResource(resID);
			    }

		   }
		};
	@Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container,
	      Bundle savedInstanceState) {
	    View view = inflater.inflate(R.layout.allsections_list_fragment,
	        container, false);
	    

	    //initStartServiceButton(view);
	    
	    initPager(view); 
	    initBroadcastReceiver();
	    startService();
	    return view;
	}
	
	public void initStartServiceButton(View view) {
		/*
		Button startServiceButton = (Button) view.findViewById(R.id.startServiceButton);
        startServiceButton.setOnClickListener(new View.OnClickListener()
	    {
	      public void onClick(View v)
	      {
	    	  startService();
	      }
	    });*/   
	}
	
	public void startService() {
		 WHGeofencesManager geofenceManager = WHGeofencesManager.sharedManager();
         geofenceManager.activtyForErrorResolution = getActivity();   	
	 
		 getActivity().startService(new Intent(getActivity(), WHSectionManagingService.class));
    }
	
	/*
	 * initialization of sections pager
	 */
    @SuppressLint("DefaultLocale")
	public void initPager(View view) {
    	// Set the View layer
        WHProxy proxy = WHProxy.sharedProxy();
        sectionsList = proxy.getAllSections(); 
        views = new View[sectionsList.size()];
        Collections.sort(sectionsList, new Comparator<WHSection>() {

            public int compare(WHSection o1, WHSection o2) {
                return o1.getName().compareToIgnoreCase(o2.getName());
            }
        });
        currentVisibleSection = sectionsList.get(0);
        mPager = (HorizontalPager) view.findViewById(R.id.horizontal_pager);
        mPager.setOnScreenSwitchListener(onScreenSwitchListener);
        mRadioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);
        mRadioGroup.setOnCheckedChangeListener(onCheckedChangedListener);
        
        radiobuttons = new RadioButton[sectionsList.size()];
        for(int i=0; i<sectionsList.size(); i++){
        	radiobuttons[i]  = new RadioButton(SharedApplication.getContext());
        	if(i == 0)
        		radiobuttons[i].setChecked(true);
        	radiobuttons[i].setId(i);
            mRadioGroup.addView(radiobuttons[i]); //the RadioButtons are added to the radioGroup instead of the layout
        }
        
        int i = 0;
        for (WHSection section : sectionsList) {
	        View sectionView =  LayoutInflater.from(SharedApplication.getContext()).inflate(R.layout.section_main_view, null);
	        WHCircleView circleView = (WHCircleView)sectionView.findViewById(R.id.section_circle_view);
	        int resID = SharedApplication.getContext().getResources().getIdentifier(section.getName().toLowerCase(Locale.getDefault()) + "_section_icon", "drawable", SharedApplication.getContext().getPackageName());
	        circleView.updateSecondsPassed(0);
	        circleView.updateSecondsTotal(60);
	        circleView.setBackgroundResource(resID);
	        initButtonHandlers(sectionView, section);
	        views[i] = sectionView;
	        mPager.addView(sectionView);
	        i++;
        }
    }
    
    /*
     * Button handlers (option bar buttons, function buttons)
     */
    public void initButtonHandlers(View view, WHSection section) {
    	final View optionsView = (View) view.findViewById(R.id.optionsBar);
    	optionsView.setVisibility(View.INVISIBLE);
    	
    	final String resourceName = /*section.getName().toLowerCase()*/"work" + suffix;
    	WHSettingsProxy.sharedProxy().setSectionName(section.getName());
    	//Options bar button
    	Button showBarButton = (Button)view.findViewById(R.id.showBarButton);
    	showBarButton.setOnClickListener(new View.OnClickListener()
	    {
  	      public void onClick(View v)
  	      {
  	    	  if(optionsBarVisible)
  	    	     optionsView.setVisibility(View.INVISIBLE);
  	    	  else 
  	    		 optionsView.setVisibility(View.VISIBLE);
  	    	  
  	    	optionsBarVisible = !optionsBarVisible;
  	      } 
  	    });
    	
    	
    	//Options gps
    	Button barGPSButton = (Button) view.findViewById(R.id.barGPSButton);
    	int resID = SharedApplication.getContext().getResources().getIdentifier(resourceName + "gps_" + booleanToString(section.getDetectByGPS()), "drawable", SharedApplication.getContext().getPackageName());
    	barGPSButton.setBackgroundResource(resID);
    	
    	barGPSButton.setOnClickListener(new View.OnClickListener()
	    {
    		 public void onClick(View v)
     	      {
    		    WHSection section = (WHSection)sectionsList.get(visibleSectionPosition);
                SectionsManager sectionsManager = SectionsManager.sharedManager();
                sectionsManager.setCurrentSection(section);
            
	    	      Intent i = new Intent(getActivity(), SectionSettingsActivity.class);
		    	  i.putExtra("tabNumber", 0);
		    	  startActivity(i);
     	    }
    	});
      	
    		 

    	//Wifi options button
    	
    	Button barWifiButton = (Button) view.findViewById(R.id.barWifiButton);
    	resID = SharedApplication.getContext().getResources().getIdentifier(resourceName + "wifi_" + booleanToString(section.getDetectByWifi()), "drawable", SharedApplication.getContext().getPackageName());
    	barWifiButton.setBackgroundResource(resID);
    	barWifiButton.setOnClickListener(new View.OnClickListener()
	    {
   		 public void onClick(View v)
    	      {
   		        WHSection section = (WHSection)sectionsList.get(visibleSectionPosition);
                SectionsManager sectionsManager = SectionsManager.sharedManager();
                sectionsManager.setCurrentSection(section);
           
	    	      Intent i = new Intent(getActivity(), SectionSettingsActivity.class);
		    	  i.putExtra("tabNumber", 1);
		    	  startActivity(i);
    	    }
   	   });
       
    	// time options button
       Button barTimeButton = (Button) view.findViewById(R.id.barTimeButton);
       resID = SharedApplication.getContext().getResources().getIdentifier(resourceName + "time_" + booleanToString(section.getDetectByTime()), "drawable", SharedApplication.getContext().getPackageName());
       barTimeButton.setBackgroundResource(resID);
       barTimeButton.setOnClickListener(new View.OnClickListener()
	   {
     		 public void onClick(View v)
      	     {
     		      WHSection section = (WHSection)sectionsList.get(visibleSectionPosition);
                  SectionsManager sectionsManager = SectionsManager.sharedManager();
                  sectionsManager.setCurrentSection(section);
             
  	    	      Intent i = new Intent(getActivity(), SectionSettingsActivity.class);
  		    	  i.putExtra("tabNumber", 2);
  		    	  startActivity(i);
      	    }
     	});
    	
    	/*
    	manageCallsButton
    	smsAutoResponderButton
    	socialButton
    	mailButton
    	brightnessButton
    	batteryAlertButton
    	*/
       
       
        final WHSettingsProxy settingsProxy = WHSettingsProxy.sharedProxy();
        
        //Manage calls button
        final Button manageCallsButton = (Button) view.findViewById(R.id.manageCallsButton);
        manageCallsButton.setOnClickListener(new View.OnClickListener()
 	    {
      		 public void onClick(View v)
       	     {
      			 WHSection section = (WHSection)sectionsList.get(visibleSectionPosition);
                 SectionsManager sectionsManager = SectionsManager.sharedManager();
                 sectionsManager.setCurrentSection(section);
             
 	    	     Intent i = new Intent(getActivity(), ManageCallsActivity.class);
 		    	 startActivity(i); 
       	     }
      	});
        int currentVolumeFromPersistence = settingsProxy.getSoundVolume();
        for(int i=0; i<4; i++) {
        	if(i == currentVolumeFromPersistence) {
        		audioVolumePoint = i;
        		break;
        	}
        }
        
        // sound function button
        final Button audioProfileButton = (Button) view.findViewById(R.id.audioProfileButton);
        resID = SharedApplication.getContext().getResources().getIdentifier(resourceName + "audio_" + audioValues[audioVolumePoint], "drawable", SharedApplication.getContext().getPackageName());
        audioProfileButton.setBackgroundResource(resID);
        audioProfileButton.setOnClickListener(new View.OnClickListener()
 	    {
      		 public void onClick(View v)
       	     {
      			audioVolumePoint = audioVolumePoint + 1;
      			if(audioVolumePoint > 3)
      				audioVolumePoint = 0;
      			
  			    int resID = SharedApplication.getContext().getResources().getIdentifier(resourceName + "audio_" + audioValues[audioVolumePoint], "drawable", SharedApplication.getContext().getPackageName());
  			    audioProfileButton.setBackgroundResource(resID);
      		    settingsProxy.saveSoundVolume(audioVolumePoint);     
       	    }
      	});
        
        // vibration function button
        final Button vibrationButton = (Button) view.findViewById(R.id.vibrationButton);
        resID = SharedApplication.getContext().getResources().getIdentifier(resourceName + "vibration_" + booleanToString(settingsProxy.getVibrationState()), "drawable", SharedApplication.getContext().getPackageName());
        vibrationButton.setBackgroundResource(resID);
        vibrationButton.setOnClickListener(new View.OnClickListener()
 	    {
      		 public void onClick(View v)
       	     {
      			boolean newState = !settingsProxy.getVibrationState();
  			    int resID = SharedApplication.getContext().getResources().getIdentifier(resourceName + "vibration_" + booleanToString(newState), "drawable", SharedApplication.getContext().getPackageName());
  			    vibrationButton.setBackgroundResource(resID);
      		    settingsProxy.saveVibrationState(newState);     
       	    }
      	});
       
        // wifi function button
        final Button wifiButton = (Button) view.findViewById(R.id.wifiButton);
        resID = SharedApplication.getContext().getResources().getIdentifier(resourceName + "wifi_" + booleanToString(settingsProxy.getWifiState()), "drawable", SharedApplication.getContext().getPackageName());
        wifiButton.setBackgroundResource(resID);
        wifiButton.setOnClickListener(new View.OnClickListener()
 	    {
      		 public void onClick(View v)
       	     {
      			boolean newState = !settingsProxy.getWifiState();
  			    int resID = SharedApplication.getContext().getResources().getIdentifier(resourceName + "wifi_" + booleanToString(newState), "drawable", SharedApplication.getContext().getPackageName());
  			    wifiButton.setBackgroundResource(resID);
      		    settingsProxy.saveWifiState(newState);     
       	    }
      	});
        
        //bluetooth function button
        final Button bluetoothButton = (Button) view.findViewById(R.id.bluetoothButton);
        resID = SharedApplication.getContext().getResources().getIdentifier(resourceName + "bluetooth_" + booleanToString(settingsProxy.getBluetoothState()), "drawable", SharedApplication.getContext().getPackageName());
        bluetoothButton.setBackgroundResource(resID);
        bluetoothButton.setOnClickListener(new View.OnClickListener()
 	    {
      		 public void onClick(View v)
       	     {
      			  boolean newState = !settingsProxy.getBluetoothState();
    			  int resID = SharedApplication.getContext().getResources().getIdentifier(resourceName + "bluetooth_" + booleanToString(newState), "drawable", SharedApplication.getContext().getPackageName());
    			  bluetoothButton.setBackgroundResource(resID);
      		      settingsProxy.saveBluetoothState(newState);     
       	    }
      	});
        
        //gps function button
        final Button gpsButton = (Button) view.findViewById(R.id.gpsButton);
        resID = SharedApplication.getContext().getResources().getIdentifier(resourceName + "gps_" + booleanToString(settingsProxy.getGPSState()), "drawable", SharedApplication.getContext().getPackageName());
        gpsButton.setBackgroundResource(resID);
        gpsButton.setOnClickListener(new View.OnClickListener()
 	    {
      		 public void onClick(View v)
       	     {
      			  boolean newState = !settingsProxy.getGPSState();
      			  int resID = SharedApplication.getContext().getResources().getIdentifier(resourceName + "gps_" + booleanToString(newState), "drawable", SharedApplication.getContext().getPackageName());
      	          gpsButton.setBackgroundResource(resID);
      			  settingsProxy.saveGPSState(newState);  
      		      
       	    }
      	});
        
        //mobile data function button
        final Button mobileDataButton = (Button) view.findViewById(R.id.mobileDataButton);
        resID = SharedApplication.getContext().getResources().getIdentifier(resourceName + "mobile_data_" + booleanToString(settingsProxy.getMobileDataState()), "drawable", SharedApplication.getContext().getPackageName());
        mobileDataButton.setBackgroundResource(resID);
        mobileDataButton.setOnClickListener(new View.OnClickListener()
 	    {
      		 public void onClick(View v)
       	     {
      			  boolean newState = !settingsProxy.getMobileDataState();
      			  int resID = SharedApplication.getContext().getResources().getIdentifier(resourceName + "mobile_data_" + booleanToString(newState), "drawable", SharedApplication.getContext().getPackageName());
      			  mobileDataButton.setBackgroundResource(resID);
      			  settingsProxy.saveMobileDataState(newState);      
       	    }
      	});
    }
    
    /*
     * Broadcast receiver for tracking section transitions, etc
     */
    
    public void initBroadcastReceiver() {
    	 IntentFilter filter = new IntentFilter("com.ros.workandhome.intents.ACTIVE_SECTION_CHANGED");
    	 filter.addAction("com.ros.workandhome.intents.ACTIVE_SECTION_CHANGED");
    	 filter.addAction("com.ros.workandhome.intents.SECTION_SETTING_CHANGED");
    	 getActivity().registerReceiver(receiver, filter);
    }
    
    public void highlightSection(WHSection activeSection) {
    	int position = 0;
    	for(WHSection section: sectionsList) {
    		if(section.getName().equalsIgnoreCase(activeSection.getName())) {
    			break;
    		}
    		position++;
    	}
    	View v = views[position];
    	
    	WHCircleView circleView = (WHCircleView) v.findViewById(R.id.section_circle_view);
    	circleView.updateSecondsPassed(59);
        circleView.updateSecondsTotal(60);
        circleView.invalidate();
        sectionsListView.invalidate();
    }
    
    public void unhighlightSection(WHSection activeSection) {
    	int position = 1;
    	for(WHSection section: sectionsList) {
    		if(section.getName().equalsIgnoreCase(activeSection.getName())) {
    			break;
    		}
    		position++;
    	}
    	View v = views[position];
    	
    	WHCircleView circleView = (WHCircleView) v.findViewById(R.id.section_circle_view);
    	circleView.updateSecondsPassed(0);
        circleView.updateSecondsTotal(60);
        circleView.invalidate();
        sectionsListView.invalidate();
    }
    
    
    
    /* pager scroll and radiubuttons */
    
    private final HorizontalPager.OnScreenSwitchListener onScreenSwitchListener =
        new HorizontalPager.OnScreenSwitchListener() {
            @Override
            public void onScreenSwitched(final int screen) {
                // Check the appropriate button when the user swipes screens.
                 mRadioGroup.check(screen);
                 visibleSectionPosition = screen;
                 currentVisibleSection = sectionsList.get(visibleSectionPosition);
                 WHSettingsProxy.sharedProxy().setSectionName(currentVisibleSection.getName());
                 currentView = views[screen];
            }
        };
        

     private final RadioGroup.OnCheckedChangeListener onCheckedChangedListener =
        new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final RadioGroup group, final int checkedId) {
                // Slide to the appropriate screen when the user checks a button.
                visibleSectionPosition = checkedId;
                currentVisibleSection = sectionsList.get(visibleSectionPosition);
                mPager.setCurrentScreen(checkedId, true);
                WHSettingsProxy.sharedProxy().setSectionName(currentVisibleSection.getName());        
                currentView = views[checkedId];
            }
        };
}
