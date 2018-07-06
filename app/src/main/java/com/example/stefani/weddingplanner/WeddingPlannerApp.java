package com.example.stefani.weddingplanner;

import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.provider.Telephony;

import com.example.stefani.weddingplanner.SMS.SmsReceiver;
import com.example.stefani.weddingplanner.Helpers.SharedPreferencesHelper;
import com.firebase.client.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

// this class lives as long as the app lives - Pre-initializes all components that the app will need.
public class WeddingPlannerApp extends Application {

    private static SmsReceiver smsBroadcastReceiver = new SmsReceiver();
    private static WeddingPlannerApp mApp;

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;

        // when a new SMS message is received by the device, a new broadcast Intent is fired:
        registerReceiver(smsBroadcastReceiver, new IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION));

        SharedPreferencesHelper.getInstance().Initialize(getApplicationContext());

        if (!FirebaseApp.getApps(this).isEmpty()) {
            Firebase.setAndroidContext(getBaseContext()); // basecontext provides to firebase the resources of the specific fragment that is using it
            FirebaseDatabase.getInstance().setPersistenceEnabled(true); // enabling persistence allows our app to also keep all of its state even after an app restart
        }
    }

    @Override
    public void onTerminate() {
        unregisterReceiver(smsBroadcastReceiver);
        super.onTerminate();
    }

    public static Context getContext() {
        // getApplicationContext provides the resources of the app as long as the app is running
        return mApp.getApplicationContext();
    }

    public static SmsReceiver getSmsBroadcastReceiver() {
        return smsBroadcastReceiver;
    }
}
