package com.example.stefani.weddingplanner;

import android.app.Application;

import com.firebase.client.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Stefani on 03/12/2017.
 */

public class WeddingPlanner extends Application {

    @Override
    public void onCreate(){
        super.onCreate();

        if(!FirebaseApp.getApps(this).isEmpty()){
            Firebase.setAndroidContext(getBaseContext());
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }
    }
}
