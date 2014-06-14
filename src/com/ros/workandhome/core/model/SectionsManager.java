package com.ros.workandhome.core.model;

import com.ros.workandhome.core.entities.section.WHSection;

public class SectionsManager {

    private static SectionsManager instance;
    private WHSection currentSection;
    
    public static SectionsManager sharedManager() {
    	if(instance == null)
    		instance = new SectionsManager();
    	return instance;
    }
    
    public void setCurrentSection(WHSection section) {
    	currentSection = section;
    }
    
    public WHSection getCurrentSection() {
    	return currentSection;
    }
    
}