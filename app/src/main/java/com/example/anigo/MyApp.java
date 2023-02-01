package com.example.anigo;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

public class MyApp extends Application {
    private static Context context;
    public void onCreate() {
        super.onCreate();
        context = getAppContext();

    }

    private Activity mCurrentActivity = null;
    public Activity getCurrentActivity(){
        return mCurrentActivity;
    }
    public void setCurrentActivity(Activity mCurrentActivity){
        this.mCurrentActivity = mCurrentActivity;
    }
    public static Context getAppContext() {
        if(context != null){
            return context;
        }
        return null;
    }
}
