package com.ros.workandhome.core.entities.WHContact;

public class WHContact {
    private String name;
    private String cellPhone;
    
    private boolean rejectCall;
    private boolean replyWithSMS;
    private String smsText;
    
    public WHContact(String name, String cellPhone, boolean rejectCall, boolean replyWithSMS, String smsText) {
	   this.name = name;
	   this.cellPhone = cellPhone;
	   this.rejectCall = rejectCall;
	   this.replyWithSMS = replyWithSMS;
	   this.smsText = smsText;
    }
    public void setName (String name) {
    	this.name = name;
    }
    
    public String getName () {
    	return name;
    }
    
    public void setCellPhone(String cellPhone) {
    	this.cellPhone = cellPhone;
    }
    
    public String getCellPhone() {
    	return cellPhone;
    }
    
    public void setRejectCall(boolean rejectCall) {
    	this.rejectCall = rejectCall;
    }
    
    public boolean getRejectCall() {
    	return rejectCall;
    }
    
    public void setReplyWithSms(boolean replyWithSMS) {
    	this.replyWithSMS = replyWithSMS;
    }
    
    public boolean getReplyWithSMS() {
    	return replyWithSMS;
    }
    
    public void setSMSText (String smsText) {
    	this.smsText = smsText;
    }
    
    public String getSMSText() {
    	return smsText;
    }
    
}
