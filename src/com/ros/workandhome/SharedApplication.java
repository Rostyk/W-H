package com.ros.workandhome;

import android.content.Context;

public class SharedApplication extends android.app.Application {

    private static SharedApplication instance;

    public SharedApplication() {
    	instance = this;
    }
    
    @Override
    public void onCreate() {
      super.onCreate();  
      instance = this;
    }

    public static Context getContext() {
    	return instance;
    }

}
