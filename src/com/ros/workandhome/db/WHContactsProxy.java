package com.ros.workandhome.db;

import java.util.ArrayList;

import com.ros.workandhome.core.entities.WHContact.WHContact;
import com.ros.workandhome.core.entities.section.WHSection;
import com.ros.workandhome.db.dao.WHCallsSettingsDAO;
import com.ros.workandhome.db.dao.WHSectionDAO;

public class WHContactsProxy {
	private static WHContactsProxy instance;
	private WHCallsSettingsDAO callSettingsDAO;

    public static synchronized WHContactsProxy sharedProxy() {
    if (instance == null)
            instance = new WHContactsProxy();
        return instance;
    }
    
    public WHContactsProxy() {
    	if(callSettingsDAO == null) {
    		callSettingsDAO = new WHCallsSettingsDAO();
    	}
    }
    
    public ArrayList<WHContact> getAllContacts(WHSection section) {
    	return (ArrayList<WHContact>) callSettingsDAO.getAll(section);
    }
    
    public void addContact(WHContact contact) {
    	callSettingsDAO.saveContact(contact);
    }
    
    public void addContacts(ArrayList<WHContact> contacts) {
    	callSettingsDAO.saveContacts(contacts);
    }
    
    public void updateContact(WHContact contact) {
    	callSettingsDAO.updateContact(contact);
    }
    
    public void removeContact(WHContact contact) {
    	callSettingsDAO.removeContact(contact);
    }
    
}
