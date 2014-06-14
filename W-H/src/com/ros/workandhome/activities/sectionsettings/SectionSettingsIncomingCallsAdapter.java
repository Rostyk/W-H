package com.ros.workandhome.activities.sectionsettings;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.ros.workandhome.R;
import com.ros.workandhome.core.entities.GPSLocation.WHGPSLocation;
import com.ros.workandhome.core.entities.WHContact.WHContact;

public class SectionSettingsIncomingCallsAdapter extends ArrayAdapter<WHContact>{
	private TextView contactNameTextView;
    private TextView contactCellPhoneTextView;
    private CheckBox rejectCallCheckBox;
    private CheckBox replyCallWithSMSCheckBox;
    
    private List<WHContact> contacts = new ArrayList<WHContact>();
 
    public SectionSettingsIncomingCallsAdapter(Context context, int textViewResourceId,
            List<WHContact> objects) {
        super(context, textViewResourceId, objects);
        this.contacts = objects;
    }
 
    public int getCount() {
    	if(this.contacts == null)
    		return 0;
        return this.contacts.size();
    }
 
    public WHContact getItem(int index) {
        return this.contacts.get(index);
    }
    /*
     * 
     * 
     * 
     * 
     * section_settings_incomming_call_item_remove
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            // ROW INFLATION
            LayoutInflater inflater = (LayoutInflater) this.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.section_settings_incoming_call_list_item, parent, false);
        }
 
        // Get item
        WHContact contact = getItem(position);
          
        //Name
        contactNameTextView = (TextView) row.findViewById(R.id.section_settings_incomming_call_item_name);
        String name = contact.getName(); 
        contactNameTextView.setText(name);
        
        contactCellPhoneTextView = (TextView) row.findViewById(R.id.section_settings_incomming_call_item_cell_phone);
        String cellPhone = contact.getCellPhone();
        contactCellPhoneTextView.setText(cellPhone);
        
        rejectCallCheckBox = (CheckBox) row.findViewById(R.id.section_settings_incomming_call_item_reject_call);
        rejectCallCheckBox.setChecked(contact.getRejectCall());
        
        replyCallWithSMSCheckBox = (CheckBox) row.findViewById(R.id.section_settings_incomming_call_item_reply_wth_sms);
        replyCallWithSMSCheckBox.setChecked(contact.getReplyWithSMS());
        
        
        return row;
    }
}
