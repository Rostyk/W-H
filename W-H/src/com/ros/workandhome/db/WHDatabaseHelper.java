package com.ros.workandhome.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class WHDatabaseHelper extends SQLiteOpenHelper
{
    private static WHDatabaseHelper instance;

    public static synchronized WHDatabaseHelper getHelper(Context context) {
     if (instance == null)
            instance = new WHDatabaseHelper(context);
        return instance;
    }
    public WHDatabaseHelper(Context context) {
    	super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
    private static final String DATABASE_NAME = "WHDB";
    private static final int DATABASE_VERSION = 2;
    // Database creation sql statement
    private static final String DATABASE_CREATE1 = "CREATE TABLE IF NOT EXISTS whSections (name VARCHAR, description VARCHAR, gps_locations VARCHAR, wifi_spots VARCHAR, active_times VARCHAR, detect_by_wifi VARCHAR, detect_by_gps VARCHAR, detect_by_time VARCHAR)";
    private static final String DATABASE_CREATE2 = "CREATE TABLE IF NOT EXISTS whSimpleSettings (section_name VARCHAR, gps_state VARCHAR, wifi_state VARCHAR, blue_tooth_state, mobile_data_state VARCHAR, vibration_state VARCHAR, brightness INTEGER, sound INTEGER)";
    private static final String DATABASE_CREATE3 = "CREATE TABLE IF NOT EXISTS whContacts(section_name VARCHAR, name VARCHAR, cell_phone VARCHAR, reject_call VARCHAR, reply_call_with_sms VARCHAR, sms_text VARCHAR)";
    

    // Method is called during creation of the database
    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE1);
        database.execSQL(DATABASE_CREATE2);
        database.execSQL(DATABASE_CREATE3);
    }

    // Method is called during an upgrade of the database,
    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion,
            int newVersion) {
        Log.w(WHDatabaseHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS whSections");
        database.execSQL("DROP TABLE IF EXISTS whSimpleSettings");
        onCreate(database);
    }   
//Other stuff... 
}
