package com.ros.workandhome.db.dao;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils.InsertHelper;
import android.database.sqlite.SQLiteDatabase;

import com.ros.workandhome.SharedApplication;
import com.ros.workandhome.core.entities.WHContact.WHContact;
import com.ros.workandhome.core.entities.section.WHSection;
import com.ros.workandhome.db.WHDatabaseHelper;

public class WHCallsSettingsDAO {
	private String sectionName;
	private String sectionNameInQuotes;
	private WHDatabaseHelper dbHelper;
	private SQLiteDatabase database;
	private String DATABASE_TABLE = "whContacts";
	
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
	
	public WHCallsSettingsDAO () {
		dbHelper = WHDatabaseHelper.getHelper(SharedApplication.getContext());
		database = dbHelper.getWritableDatabase();
	}
	
	public void saveContact (WHContact contact) {
		ContentValues initialValues = new ContentValues();
		initialValues.put("section_name", sectionName);
		initialValues.put("name", contact.getName());
		initialValues.put("cell_phone", contact.getCellPhone());
		initialValues.put("reject_call", booleanToString(contact.getRejectCall()));
		initialValues.put("reply_call_with_sms", booleanToString(contact.getReplyWithSMS()));
		initialValues.put("sms_text", contact.getCellPhone());
		
		int rowsAffected = database.update(DATABASE_TABLE, initialValues, "section_name=? AND name=?", new String[] {sectionName, contact.getName()});
		if(rowsAffected <= 0) {
			database.insert(DATABASE_TABLE, null,  initialValues);
		}
	}
	
	public void saveContacts(ArrayList<WHContact> contacts) {
		InsertHelper ih = new InsertHelper(database, "whContacts"); 
		// Get the numeric indexes for each of the columns that we're updating
		        final int sectionNameColumn = ih.getColumnIndex("section_name");
		        final int nameColumn = ih.getColumnIndex("name");
		        final int cellPhoneColumn = ih.getColumnIndex("cell_phone");
		        final int rejectCallColumn = ih.getColumnIndex("reject_call");
		        final int replyCallWithSmsColumn = ih.getColumnIndex("reply_call_with_sms");
		        final int smsTextColumn = ih.getColumnIndex("sms_text");
		        for(WHContact contact : contacts) {
		        	 // ... Create the data for this row (not shown) ...
		        	 
		            // Get the InsertHelper ready to insert a single row
		            ih.prepareForInsert();
		 
		            // Add the data for each column
		            ih.bind(sectionNameColumn, sectionName);
		            ih.bind(nameColumn, contact.getName());
		            ih.bind(cellPhoneColumn, contact.getCellPhone());
		            ih.bind(rejectCallColumn, booleanToString(contact.getRejectCall()));
		            ih.bind(replyCallWithSmsColumn, booleanToString(contact.getReplyWithSMS()));
		            ih.bind(smsTextColumn, contact.getCellPhone());
		            // Insert the row into the database.
		            ih.execute();
		        }
	}
	
	public void removeContact(WHContact contact) {
		database.delete(DATABASE_TABLE, "name=" + "'" + sectionName + "' AND name=" + "'" + contact.getName() + "'", null);
	    
	}
	
	public void updateContact(WHContact contact) {
		ContentValues initialValues = new ContentValues();
		initialValues.put("section_name", sectionName);
		initialValues.put("name", contact.getName());
		initialValues.put("cell_phone", contact.getCellPhone());
		initialValues.put("reject_call", booleanToString(contact.getRejectCall()));
		initialValues.put("reply_call_with_sms", booleanToString(contact.getReplyWithSMS()));
		initialValues.put("sms_text", contact.getCellPhone());
		
		database.update(DATABASE_TABLE, initialValues, "section_name=? AND name=?", new String[] {sectionName, contact.getName()});	
	}
	
	public Iterable<WHContact> getAll(WHSection section) {
		Cursor cursor = database.rawQuery("select * from " + DATABASE_TABLE + " where section_name = '" + section.getName() + "'", null);
		ArrayList<WHContact> allContacts = new ArrayList<WHContact>(); 
		if (cursor.moveToFirst()) {
		            while (cursor.isAfterLast() == false) {
		            	
		            	//Parse name
		                String name = cursor.getString(cursor
		                        .getColumnIndex("name"));
		                
		                String cellPhone = cursor.getString(cursor
		                        .getColumnIndex("cell_phone"));
		                
		                boolean rejectCall = stringToBoolean(cursor.getString(cursor
		                        .getColumnIndex("rect_call")));
		                
		                boolean replyCallWithSMS = stringToBoolean(cursor.getString(cursor
		                        .getColumnIndex("reply_call_with_sms")));
		                
		                String smsText = cellPhone = cursor.getString(cursor
		                        .getColumnIndex("sms_text"));

		                WHContact contact = new WHContact(name, cellPhone, rejectCall, replyCallWithSMS, smsText);
		            
		                allContacts.add(contact);
		                cursor.moveToNext();
		            }
		        }
		cursor.close();
		return allContacts;
    
     }
}
