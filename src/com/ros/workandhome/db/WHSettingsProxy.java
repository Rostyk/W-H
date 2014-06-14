package com.ros.workandhome.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;

import com.ros.workandhome.core.entities.section.WHSection;
import com.ros.workandhome.db.dao.WHSectionDAO;
import com.ros.workandhome.db.dao.WHSimpleSettingsDAO;

public class WHSettingsProxy {
	private String sectionName;
	private static WHSettingsProxy instance;
	private WHSimpleSettingsDAO simpleSettingsDAO;

    public static synchronized WHSettingsProxy sharedProxy() {
    if (instance == null)
            instance = new WHSettingsProxy();
        return instance;
    }
    
    public void setSectionName(String sectionName) {
       this.sectionName = sectionName;
       simpleSettingsDAO.setSectionName(sectionName);
    }
    
    public String getSectionName() {
    	return sectionName;
    }
    
    public WHSettingsProxy() {
    	if(simpleSettingsDAO == null) {
    		simpleSettingsDAO = new WHSimpleSettingsDAO();
    	}
    }
    
    public void saveGPSState(boolean state) {
    	simpleSettingsDAO.saveGPSState(state);
	}
	
	public boolean getGPSState() {
		return simpleSettingsDAO.getGPSState();
	}
	
	
	public void saveWifiState(boolean state) {
		simpleSettingsDAO.saveWifiState(state);
	}
	
	public boolean getWifiState() {
		return simpleSettingsDAO.getWifiState();
	}
	
	
	public void saveBluetoothState(boolean state) {
		simpleSettingsDAO.saveBluetoothState(state);
	}
	
	public boolean getBluetoothState() {
		return simpleSettingsDAO.getBluetoothState();
	}
	
	
	public void saveMobileDataState(boolean state) {
		simpleSettingsDAO.saveMobileDataState(state);
	}
	
	public boolean getMobileDataState() {
		return simpleSettingsDAO.getMobileDataState();
	}
	
	
	public void saveVibrationState(boolean state) {
		simpleSettingsDAO.saveVibrationState(state);
	}
	
	public boolean getVibrationState() {
		return simpleSettingsDAO.getVibrationState();
	}
	
	
	
	public void saveSoundVolume(int volume) {
		simpleSettingsDAO.saveSoundVolume(volume);
	}
	
	public int getSoundVolume() {
		return simpleSettingsDAO.getSoundVolume();
	}
	
	
	public void saveBrightness(int brightness) {
		simpleSettingsDAO.saveBrightness(brightness);
	}
	
	public int getBrightnes() {
		return simpleSettingsDAO.getBrightnes();
	}
}
