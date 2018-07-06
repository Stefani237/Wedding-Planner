package com.example.stefani.weddingplanner.Helpers;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.stefani.weddingplanner.BasicClasses.Constants;


public class SharedPreferencesHelper {
    // SharedPreferences object points to a file containing key-value pairs and provides simple methods to read and write them
    private static SharedPreferencesHelper mInstance;
    private Context mContext;
    private SharedPreferences mSharedPreferences;

    private SharedPreferencesHelper() {
    }

    public static SharedPreferencesHelper getInstance() { // singleton
        if (mInstance == null) {
            mInstance = new SharedPreferencesHelper();
        }
        return mInstance;
    }

    public void Initialize(Context ctxt) {
        mContext = ctxt;
        // initialize shared preferences - file's name and mode
        mSharedPreferences = mContext.getSharedPreferences(Constants.SHARED_PREFS, Context.MODE_PRIVATE);
    }

    public void writePreference(String key, String value) {
        SharedPreferences.Editor e = mSharedPreferences.edit(); // create an editor to start edit the file
        e.putString(key, value); // add key-value to editor
        e.apply(); // writing changes to file
    }

    public String getPreferenceString(String key) {
        // returns value for this key or if not found it will return null
        return mSharedPreferences.getString(key, "");
    }

}
