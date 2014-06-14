package com.ros.workandhome.activities.calls;

import java.util.ArrayList;

import com.ros.workandhome.R;
import com.ros.workandhome.SharedApplication;
import com.ros.workandhome.activities.sectionsettings.SectionSettingsIncomingCallsAdapter;
import com.ros.workandhome.activities.sectionsettings.SectionSettingsTimeScheduleListAdapter;
import com.ros.workandhome.core.entities.ActiveTime.WHActiveTime;
import com.ros.workandhome.core.entities.WHContact.WHContact;
import com.ros.workandhome.core.entities.section.WHSection;
import com.ros.workandhome.core.model.SectionsManager;
import com.ros.workandhome.db.WHContactsProxy;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.ListView;

public class ManageCallsActivity extends FragmentActivity {
	 private ListView contactsListView;
	 private SectionSettingsIncomingCallsAdapter adapter;
	 public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.section_settings_call_settings);
	        
	        initList();
	 }
	 
	 public void initList () {
		    contactsListView = (ListView) findViewById(R.id.manageIncomingCallsList);
		    contactsListView.setClickable(true);
		  
		    SectionsManager sectionsManager = SectionsManager.sharedManager();
	        WHSection section = sectionsManager.getCurrentSection();
	        
	        if(WHContactsProxy.sharedProxy().getAllContacts(section).size() == 0) {
	        	WHContact contact = new WHContact("Jude", "380634523467", true, true, "You jerk");
	        	WHContactsProxy.sharedProxy().addContact(contact);
	        }
	        ArrayList<WHContact> contactsList = WHContactsProxy.sharedProxy().getAllContacts(section);
	        // Create a customized ArrayAdapter
	        adapter = new SectionSettingsIncomingCallsAdapter(
	                SharedApplication.getContext(), R.layout.section_settings_incoming_call_list_item, contactsList);
	         
	       
	        // Set the ListView adapter
	        contactsListView.setAdapter(adapter);
	 }
	 protected void onDestroy() {
			super.onDestroy();
	 }
}
