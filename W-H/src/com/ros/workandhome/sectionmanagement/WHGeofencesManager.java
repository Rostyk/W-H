package com.ros.workandhome.sectionmanagement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.Geofence;
import com.ros.workandhome.R;
import com.ros.workandhome.SharedApplication;
import com.ros.workandhome.core.entities.GPSLocation.WHGPSLocation;
import com.ros.workandhome.core.entities.WHDay.WHDay;
import com.ros.workandhome.core.entities.section.WHSection;
import com.ros.workandhome.db.WHProxy;
import com.ros.workandhome.geofence.GeofenceRemover;
import com.ros.workandhome.geofence.GeofenceRequester;
import com.ros.workandhome.geofence.GeofenceUtils;
import com.ros.workandhome.geofence.SimpleGeofence;
import com.ros.workandhome.geofence.SimpleGeofenceStore;
import com.ros.workandhome.geofence.GeofenceUtils.REMOVE_TYPE;
import com.ros.workandhome.geofence.GeofenceUtils.REQUEST_TYPE;


public class WHGeofencesManager {
	private static WHGeofencesManager instance;

    public static synchronized WHGeofencesManager sharedManager() {
   
    	if (instance == null)
            instance = new WHGeofencesManager();
        return instance;
    }
    
    
    
    public FragmentActivity activtyForErrorResolution;
    
	/*
     * Use to set an expiration time for a geofence. After this amount
     * of time Location Services will stop tracking the geofence.
     * Remember to unregister a geofence when you're finished with it.
     * Otherwise, your app will use up battery. To continue monitoring
     * a geofence indefinitely, set the expiration time to
     * Geofence#NEVER_EXPIRE.
     */
    private static final long GEOFENCE_EXPIRATION_IN_HOURS = 12;
    private static final long GEOFENCE_EXPIRATION_IN_MILLISECONDS =
            GEOFENCE_EXPIRATION_IN_HOURS * DateUtils.HOUR_IN_MILLIS;

    // Store the current request
    private REQUEST_TYPE mRequestType;

    // Store the current type of removal
    private REMOVE_TYPE mRemoveType;

    // Persistent storage for geofences
    private SimpleGeofenceStore mPrefs;

    // Store a list of geofences to add
    List<Geofence> mCurrentGeofences;

    // Add geofences handler
    private GeofenceRequester mGeofenceRequester;
    // Remove geofences handler
    private GeofenceRemover mGeofenceRemover;
    
    /*
     * An instance of an inner class that receives broadcasts from listeners and from the
     * IntentService that receives geofence transition events
     */
    private GeofenceSampleReceiver mBroadcastReceiver;

    // An intent filter for the broadcast receiver
    private IntentFilter mIntentFilter;

    // Store the list of geofences to remove
    private List<String> mGeofenceIdsToRemove;

   
	
    
    public void addGeofences() {
    
        // Create a new broadcast receiver to receive updates from the listeners and service
        mBroadcastReceiver = new GeofenceSampleReceiver();
        // Create an intent filter for the broadcast receiver
        mIntentFilter = new IntentFilter();
        // Action for broadcast Intents that report successful addition of geofences
        mIntentFilter.addAction(GeofenceUtils.ACTION_GEOFENCES_ADDED);
        // Action for broadcast Intents that report successful removal of geofences
        mIntentFilter.addAction(GeofenceUtils.ACTION_GEOFENCES_REMOVED);
        // Action for broadcast Intents containing various types of geofencing errors
        mIntentFilter.addAction(GeofenceUtils.ACTION_GEOFENCE_ERROR);
        // All Location Services sample apps use this category
        mIntentFilter.addCategory(GeofenceUtils.CATEGORY_LOCATION_SERVICES);
        // Instantiate a new geofence storage area
        mPrefs = new SimpleGeofenceStore(activtyForErrorResolution);
        // Instantiate the current List of geofences
        mCurrentGeofences = new ArrayList<Geofence>();
        // Instantiate a Geofence requester
        mGeofenceRequester = new GeofenceRequester(activtyForErrorResolution);
        // Instantiate a Geofence remover
        mGeofenceRemover = new GeofenceRemover(activtyForErrorResolution);
        registerGeofences();
        
    }
    
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        // Choose what to do based on the request code
        switch (requestCode) {

            // If the request code matches the code sent in onConnectionFailed
            case GeofenceUtils.CONNECTION_FAILURE_RESOLUTION_REQUEST :
                switch (resultCode) {
                    // If Google Play services resolved the problem
                    case Activity.RESULT_OK:
                        // If the request was to add geofences
                        if (GeofenceUtils.REQUEST_TYPE.ADD == mRequestType) {

                            // Toggle the request flag and send a new request
                            mGeofenceRequester.setInProgressFlag(false);
                            // Restart the process of adding the current geofences
                            mGeofenceRequester.addGeofences(mCurrentGeofences);

                        // If the request was to remove geofences
                        } else if (GeofenceUtils.REQUEST_TYPE.REMOVE == mRequestType ){

                            // Toggle the removal flag and send a new removal request
                            mGeofenceRemover.setInProgressFlag(false);

                            // If the removal was by Intent
                            if (GeofenceUtils.REMOVE_TYPE.INTENT == mRemoveType) {

                                // Restart the removal of all geofences for the PendingIntent
                                mGeofenceRemover.removeGeofencesByIntent(
                                    mGeofenceRequester.getRequestPendingIntent());

                            // If the removal was by a List of geofence IDs
                            } else {

                                // Restart the removal of the geofence list
                                mGeofenceRemover.removeGeofencesById(mGeofenceIdsToRemove);
                            }
                        }
                    break;

                    // If any other result was returned by Google Play services
                    default:
                        // Report that Google Play services was unable to resolve the problem.
                        Log.d(GeofenceUtils.APPTAG, SharedApplication.getContext().getString(R.string.no_resolution));
                }

            // If any other request code was received
            default:
               // Report that this Activity received an unknown requestCode
               Log.d(GeofenceUtils.APPTAG,
                       SharedApplication.getContext().getString(R.string.unknown_activity_request_code, requestCode));

               break;
        }
    }

    
    protected void onResume() {
        // Register the broadcast receiver to receive status updates
        LocalBroadcastManager.getInstance(SharedApplication.getContext()).registerReceiver(mBroadcastReceiver, mIntentFilter);
    }
        
    
    private boolean servicesConnected() {
        int resultCode =
                GooglePlayServicesUtil.isGooglePlayServicesAvailable(SharedApplication.getContext());
        if (ConnectionResult.SUCCESS == resultCode) {
            Log.d(GeofenceUtils.APPTAG, SharedApplication.getContext().getString(R.string.play_services_available));
            return true;
       } else {
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(resultCode, activtyForErrorResolution, 0);
            if (dialog != null) {
                ErrorDialogFragment errorFragment = new ErrorDialogFragment();
                errorFragment.setDialog(dialog);
                errorFragment.show(activtyForErrorResolution.getSupportFragmentManager(), GeofenceUtils.APPTAG);
            }
            return false;
        }
    }

    public void removeAllGeofences() {
        mRemoveType = GeofenceUtils.REMOVE_TYPE.INTENT;
        if (!servicesConnected()) {

            return;
        }
        // Try to make a removal request
        try {
        mGeofenceRemover.removeGeofencesByIntent(mGeofenceRequester.getRequestPendingIntent());

        } catch (UnsupportedOperationException e) {
            // Notify user that previous request hasn't finished.
            Toast.makeText(SharedApplication.getContext(), R.string.remove_geofences_already_requested_error,
                        Toast.LENGTH_LONG).show();
        }
    }
   
    public void removeGeofenceByID(String geofenceID) {
        mGeofenceIdsToRemove = Collections.singletonList(geofenceID);
        mRemoveType = GeofenceUtils.REMOVE_TYPE.LIST;
        if (!servicesConnected()) {

            return;
        }
        try {
            mGeofenceRemover.removeGeofencesById(mGeofenceIdsToRemove);

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (UnsupportedOperationException e) {
          
        }
    }
 
    public void registerGeofences() {
        mRequestType = GeofenceUtils.REQUEST_TYPE.ADD;
        if (!servicesConnected()) {

            return;
        }
        ArrayList<WHSection> sections = WHProxy.sharedProxy().getAllSections();
        for(WHSection section: sections) {
        	ArrayList<WHGPSLocation> arrayOfLocations = section.getGPSLocations().get(new WHDay("Mon"));
            int lcoationNumber = 0;
            if(arrayOfLocations != null)
            for(WHGPSLocation location : arrayOfLocations) {
            	if (!checkLocation(location)) {
                    continue;
                }
            	lcoationNumber++;
            	String geofenceID = section.getName() + lcoationNumber; 
            	SimpleGeofence mUIGeofence = new SimpleGeofence(
                	geofenceID,
                    // Get latitude, longitude, and radius from the UI
                    Double.valueOf(location.getLatitude()),
                    Double.valueOf(location.getLongtitude()),
                    Float.valueOf(location.getRadius()),
                    // Set the expiration time
                    GEOFENCE_EXPIRATION_IN_MILLISECONDS,
                    // Only detect entry transitions
                    Geofence.GEOFENCE_TRANSITION_ENTER);

                // Store this flat version in SharedPreferences
                mPrefs.setGeofence(geofenceID, mUIGeofence);

                mCurrentGeofences.add(mUIGeofence.toGeofence());  
            }
        }
        
        // Start the request. Fail if there's already a request in progress
        try {
            // Try to add geofences
        	if(mCurrentGeofences.size() > 0)
               mGeofenceRequester.addGeofences(mCurrentGeofences);
        } catch (UnsupportedOperationException e) {
            // Notify user that previous request hasn't finished.
            Toast.makeText(SharedApplication.getContext(), R.string.add_geofences_already_requested_error,
                        Toast.LENGTH_LONG).show();
        }
        
    }
   
    private boolean checkLocation(WHGPSLocation location) {
        boolean inputOK = true;
     
        if (TextUtils.isEmpty(location.getLatitude())) {
            Toast.makeText(SharedApplication.getContext(), R.string.geofence_input_error_missing, Toast.LENGTH_LONG).show();
            inputOK = false;
        }
        if (TextUtils.isEmpty(location.getLongtitude())) {
            Toast.makeText(SharedApplication.getContext(), R.string.geofence_input_error_missing, Toast.LENGTH_LONG).show();
            inputOK = false;
        }
        
        if (inputOK) {
            double lat1 = Double.valueOf(location.getLatitude());
            double lng1 = Double.valueOf(location.getLongtitude());
            float rd1 = Float.valueOf(location.getRadius());
            if (lat1 > GeofenceUtils.MAX_LATITUDE || lat1 < GeofenceUtils.MIN_LATITUDE) {
                Toast.makeText(
                		SharedApplication.getContext(),
                        R.string.geofence_input_error_latitude_invalid,
                        Toast.LENGTH_LONG).show();
                inputOK = false;
            } 
            if ((lng1 > GeofenceUtils.MAX_LONGITUDE) || (lng1 < GeofenceUtils.MIN_LONGITUDE)) {
                Toast.makeText(
                		SharedApplication.getContext(),
                        R.string.geofence_input_error_longitude_invalid,
                        Toast.LENGTH_LONG).show();
                inputOK = false;
            } 
            if (rd1 < GeofenceUtils.MIN_RADIUS) {
                Toast.makeText(
                		SharedApplication.getContext(),
                        R.string.geofence_input_error_radius_invalid,
                        Toast.LENGTH_LONG).show();

                // Set the validity to "invalid" (false)
                inputOK = false;
            } 
        }
        return inputOK;
    }

    /**
     * Define a Broadcast receiver that receives updates from connection listeners and
     * the geofence transition service.
     */
    public class GeofenceSampleReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            // Check the action code and determine what to do
            String action = intent.getAction();

            // Intent contains information about errors in adding or removing geofences
            if (TextUtils.equals(action, GeofenceUtils.ACTION_GEOFENCE_ERROR)) {

                handleGeofenceError(context, intent);

            // Intent contains information about successful addition or removal of geofences
            } else if (
                    TextUtils.equals(action, GeofenceUtils.ACTION_GEOFENCES_ADDED)
                    ||
                    TextUtils.equals(action, GeofenceUtils.ACTION_GEOFENCES_REMOVED)) {

                handleGeofenceStatus(context, intent);

            // Intent contains information about a geofence transition
            } else if (TextUtils.equals(action, GeofenceUtils.ACTION_GEOFENCE_TRANSITION)) {

                handleGeofenceTransition(context, intent);

            // The Intent contained an invalid action
            } else {
                Log.e(GeofenceUtils.APPTAG, SharedApplication.getContext().getString(R.string.invalid_action_detail, action));
                Toast.makeText(context, R.string.invalid_action, Toast.LENGTH_LONG).show();
            }
        }

        /**
         * If you want to display a UI message about adding or removing geofences, put it here.
         *
         * @param context A Context for this component
         * @param intent The received broadcast Intent
         */
        private void handleGeofenceStatus(Context context, Intent intent) {

        }

        /**
         * Report geofence transitions to the UI
         *
         * @param context A Context for this component
         * @param intent The Intent containing the transition
         */
        private void handleGeofenceTransition(Context context, Intent intent) {
            /*
             * If you want to change the UI when a transition occurs, put the code
             * here. The current design of the app uses a notification to inform the
             * user that a transition has occurred.
             */
        }

        /**
         * Report addition or removal errors to the UI, using a Toast
         *
         * @param intent A broadcast Intent sent by ReceiveTransitionsIntentService
         */
        private void handleGeofenceError(Context context, Intent intent) {
            String msg = intent.getStringExtra(GeofenceUtils.EXTRA_GEOFENCE_STATUS);
            Log.e(GeofenceUtils.APPTAG, msg);
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
        }
    }
    /**
     * Define a DialogFragment to display the error dialog generated in
     * showErrorDialog.
     */
    public static class ErrorDialogFragment extends DialogFragment {

        // Global field to contain the error dialog
        private Dialog mDialog;

        /**
         * Default constructor. Sets the dialog field to null
         */
        public ErrorDialogFragment() {
            super();
            mDialog = null;
        }

        /**
         * Set the dialog to display
         *
         * @param dialog An error dialog
         */
        public void setDialog(Dialog dialog) {
            mDialog = dialog;
        }

        /*
         * This method must return a Dialog to the DialogFragment.
         */
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return mDialog;
        }
    }
    
    public WHSection getSectionAround(String[] geofenceIDs) {
    	ArrayList<WHSection> sections = WHProxy.sharedProxy().getAllSections();
    	for(String geofenceID : geofenceIDs) {
    		for(WHSection section: sections) {
    			if(geofenceID.contains(section.getName()))
    			return section;
    		}
    	}
    	
    	return null;
    }

}
