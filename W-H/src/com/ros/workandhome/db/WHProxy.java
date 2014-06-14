package com.ros.workandhome.db;

import java.util.ArrayList;

import com.ros.workandhome.db.dao.WHSectionDAO;
import com.ros.workandhome.core.entities.section.WHSection;
public class WHProxy {
	private static WHProxy instance;
	private WHSectionDAO sectionDAO;

    public static synchronized WHProxy sharedProxy() {
    if (instance == null)
            instance = new WHProxy();
        return instance;
    }
    
    public WHProxy() {
    	if(sectionDAO == null) {
    		sectionDAO = new WHSectionDAO();
    	}
    }
    
    public ArrayList<WHSection> getAllSections() {
    	return (ArrayList<WHSection>)sectionDAO.getAll();
    }
    
    public void addSection(WHSection section) {
    	sectionDAO.save(section);
    }
    
    public void updateSection(WHSection section) {
    	sectionDAO.update(section);
    }
    
    public void removeSection(WHSection section) {
    	sectionDAO.delete(section);
    }
    
 
}
