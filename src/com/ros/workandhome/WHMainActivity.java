package com.ros.workandhome;
import com.ros.workandhome.activities.allsections.AllSectionsFragment;
import com.ros.workandhome.activities.raports.RaportsFragment;
import com.ros.workandhome.core.entities.section.WHSection;
import com.ros.workandhome.db.WHProxy;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.widget.TabHost.TabSpec;

public class WHMainActivity extends FragmentActivity {
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        WHProxy proxy = WHProxy.sharedProxy();
        if(proxy.getAllSections().size() == 0) {
        	testDB();
        }
        
        setContentView(R.layout.main);
        
        FragmentTabHost mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
        TabSpec allSectionsSpec = mTabHost.newTabSpec("Tab1Tag").setIndicator("Sections", SharedApplication.getContext().getResources().getDrawable(R.drawable.tab_main));
        mTabHost.addTab(allSectionsSpec, AllSectionsFragment.class, null);
        
        TabSpec raportsSpec = mTabHost.newTabSpec("Tab2Tag").setIndicator("Raports", SharedApplication.getContext().getResources().getDrawable(R.drawable.tab_raports));
        mTabHost.addTab(raportsSpec, RaportsFragment.class, null);
             
    }
    
    
    public void testDB() {
    	
       WHProxy proxy = WHProxy.sharedProxy();
       WHSection section1 = new WHSection("Home", "You're home.");
       WHSection section2 = new WHSection("Work", "You're working.");
       WHSection section3 = new WHSection("Sleep", "You're sleeping.");
       
       proxy.addSection(section1);
       proxy.addSection(section2);
       proxy.addSection(section3);
    }         
}