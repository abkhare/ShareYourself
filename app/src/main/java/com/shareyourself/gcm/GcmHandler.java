package com.shareyourself.gcm;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.shareyourself.base.Constants;

/**
 * Created by akhare on 9/15/15.
 */
public class GcmHandler {

    public static final String TAG = "GcmHandler";
    //public static HashMap<String, LocationEvent> groupMemebers = new HashMap<String, LocationEvent>();
    //public static HashMap<String, LinkedList<MessageEvent>> messageMap = new HashMap<String, LinkedList<MessageEvent>>();

    public void gcmRegistration(Activity activity){
        if (checkPlayServices(activity)) {
            // Start IntentService to register this application with GCM.
            SharedPreferences sharedPreferences = activity.getSharedPreferences("ShareYourself", Context.MODE_PRIVATE);
            boolean flag = sharedPreferences.getBoolean("GCMREGISTRATION", false);
            if(!flag){
                Intent intent = new Intent(activity, RegistrationIntentService.class);
                activity.startService(intent);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("GCMREGISTRATION", true);
                editor.commit();
            }
        }
    }

    private boolean checkPlayServices(Activity activity) {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, activity,
                        Constants.PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
            }
            return false;
        }
        return true;
    }
}
