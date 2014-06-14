package com.ros.workandhome.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.ros.workandhome.SharedApplication;
import com.ros.workandhome.db.WHDatabaseHelper;

public class WHSimpleSettingsDAO {
	private String sectionName;
	private String sectionNameInQuotes;
	private WHDatabaseHelper dbHelper;
	private SQLiteDatabase database;
	private String DATABASE_TABLE = "whSimpleSettings";
	
	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
		this.sectionNameInQuotes = "'" + sectionName + "'";
	}
	
	boolean stringToBoolean(String str) {
		if(str == null)
			return false;
		
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
	
	public WHSimpleSettingsDAO () {
		dbHelper = WHDatabaseHelper.getHelper(SharedApplication.getContext());
		database = dbHelper.getWritableDatabase();
	}
	
	
	public void saveGPSState(boolean state) {
		ContentValues initialValues = new ContentValues();
		initialValues.put("section_name", sectionName);

		initialValues.put("gps_state", booleanToString(state));

		int rowsAffected = database.update(DATABASE_TABLE, initialValues, "section_name=?", new String[] {sectionName});
		if(rowsAffected <= 0) {
			database.insert(DATABASE_TABLE, null,  initialValues);
		}
	}
	
	public boolean getGPSState() {
		Cursor cursor = database.rawQuery("select * from " + DATABASE_TABLE + " where section_name= " + sectionNameInQuotes, null);
		String gpsState = "off";
		if (cursor.moveToFirst()) {
            	//Parse gps_state
                gpsState = cursor.getString(cursor
                        .getColumnIndex("gps_state")); 
		}
		cursor.close();
		return stringToBoolean(gpsState);
	}
	
	public void saveWifiState(boolean state) {
		ContentValues initialValues = new ContentValues();
		initialValues.put("section_name", sectionName);

		initialValues.put("wifi_state", booleanToString(state));

		int rowsAffected = database.update(DATABASE_TABLE, initialValues, "section_name=?", new String[] {sectionName});
		if(rowsAffected <= 0) {
			database.insert(DATABASE_TABLE, null,  initialValues);
		}
	}
	
	public boolean getWifiState() {
		Cursor cursor = database.rawQuery("select * from " + DATABASE_TABLE + " where section_name= " + sectionNameInQuotes, null);
		String wifiState = "off";
		if (cursor.moveToFirst()) {
            	//Parse wifi_state
			    wifiState = cursor.getString(cursor
                        .getColumnIndex("wifi_state"));
		}
		cursor.close();
		return stringToBoolean(wifiState);
	}
	
	public void saveBluetoothState(boolean state) {
		ContentValues initialValues = new ContentValues();
		initialValues.put("section_name", sectionName);

		initialValues.put("blue_tooth_state", booleanToString(state));

		int rowsAffected = database.update(DATABASE_TABLE, initialValues, "section_name=?", new String[] {sectionName});
		if(rowsAffected <= 0) {
			database.insert(DATABASE_TABLE, null,  initialValues);
		}
	}
	
	public boolean getBluetoothState() {
		Cursor cursor = database.rawQuery("select * from " + DATABASE_TABLE + " where section_name= " + sectionNameInQuotes, null);
		String blueTooth = "off";
		if (cursor.moveToFirst()) {
            	//Parse blue_tooth_state
                blueTooth = cursor.getString(cursor
                        .getColumnIndex("blue_tooth_state"));
		}
		cursor.close();
		return stringToBoolean(blueTooth);
	}
	
	public void saveMobileDataState(boolean state) {
		ContentValues initialValues = new ContentValues();
		initialValues.put("section_name", sectionName);

		initialValues.put("mobile_data_state", booleanToString(state));

		int rowsAffected = database.update(DATABASE_TABLE, initialValues, "section_name=?", new String[] {sectionName});
		if(rowsAffected <= 0) {
			database.insert(DATABASE_TABLE, null,  initialValues);
		}
	}
	
	public boolean getMobileDataState() {
		Cursor cursor = database.rawQuery("select * from " + DATABASE_TABLE + " where section_name= " + sectionNameInQuotes, null);
		String mobileDataState = "off";
		if (cursor.moveToFirst()) {
            	//Parse mobile data
                mobileDataState = cursor.getString(cursor
                        .getColumnIndex("mobile_data_state"));
		}
		cursor.close();
		return stringToBoolean(mobileDataState);
	}
	
	
	public void saveVibrationState(boolean state) {
		ContentValues initialValues = new ContentValues();
		initialValues.put("section_name", sectionName);

		initialValues.put("vibration_state", booleanToString(state));

		int rowsAffected = database.update(DATABASE_TABLE, initialValues, "section_name=?", new String[] {sectionName});
		if(rowsAffected <= 0) {
			database.insert(DATABASE_TABLE, null,  initialValues);
		}
	}
	
	public boolean getVibrationState() {
		Cursor cursor = database.rawQuery("select * from " + DATABASE_TABLE + " where section_name= " + sectionNameInQuotes, null);
		String vibrationState = "off";
		if (cursor.moveToFirst()) {
            	//Parse vibration state
			    vibrationState = cursor.getString(cursor
                        .getColumnIndex("vibration_state"));
		}
		cursor.close();
		return stringToBoolean(vibrationState);
	}
	
	
	
	public void saveSoundVolume(int volume) {
		ContentValues initialValues = new ContentValues();
		initialValues.put("section_name", sectionName);

		initialValues.put("sound",  Integer.valueOf(volume).toString());

		int rowsAffected = database.update(DATABASE_TABLE, initialValues, "section_name=?", new String[] {sectionName});
		if(rowsAffected <= 0) {
			database.insert(DATABASE_TABLE, null,  initialValues);
		}
	}
	
	public int getSoundVolume() {
		int soundVolume = 0;
		Cursor cursor = database.rawQuery("select * from " + DATABASE_TABLE  + " where section_name = "  + sectionNameInQuotes, null);
		if (cursor.moveToFirst()) {	
            	//Parse sound volume
                soundVolume = cursor.getInt(cursor
                        .getColumnIndex("sound"));
                
		}
		cursor.close();
		return soundVolume;
	}
	
	
	public void saveBrightness(int brightness) {
		ContentValues initialValues = new ContentValues();
		initialValues.put("section_name", sectionName);

		initialValues.put("brightness", new Integer(brightness).toString());

		int rowsAffected = database.update(DATABASE_TABLE, initialValues, "name=?", new String[] {sectionName});
		if(rowsAffected <= 0) {
			database.insert(DATABASE_TABLE, null,  initialValues);
		}
	}
	
	public int getBrightnes() {
		int brightness = 0;
		Cursor cursor = database.rawQuery("select * from " + DATABASE_TABLE + " where section_name= " + sectionName, null);
		if (cursor.moveToFirst()) {
            	//Parse sound volume
                brightness = cursor.getInt(cursor
                        .getColumnIndex("brightness"));
                return brightness;
		}
		cursor.close();
		return brightness;
	}
}
