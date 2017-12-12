package com.example.stefani.weddingplanner;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Stefani on 07/12/2017.
 */

public class GuestListClass {

    public static int mGuestCounter = 0;
    private static ArrayList<GuestClass> mArrGuest = new ArrayList<>();


    public static ArrayList<GuestClass> getmArrGuest() {
        return mArrGuest;
    }

    public static void setmArrGuest(ArrayList<GuestClass> guest) {
        mArrGuest = guest;
    }

    public static void addGuest(GuestClass guest) {
        mArrGuest.add(guest);
    }

    public static void removeGuest(int idx){
        Log.e("idx = " + idx, "------------------");
        Log.e("array size = " + mArrGuest.size(), "------------------");
        if(!mArrGuest.isEmpty()) {
            mArrGuest.remove(idx);
        }
    }
}
