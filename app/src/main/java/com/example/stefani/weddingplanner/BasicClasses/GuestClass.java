package com.example.stefani.weddingplanner.BasicClasses;

import android.support.annotation.NonNull;


public class GuestClass implements IdClasses, Comparable<GuestClass> {
    private String mID;
    private String mFullName;
    private String mPhone;
    private String mNumOfGuest = "1";
    private String mIsComing = "FALSE";
    private String mSide;
    private String mBelongingGroup;
    private String mTableNum = "-1";
    private String mCloseToDanceFloor = "100";

    public GuestClass() {
    }

    public GuestClass(String fullName, String phone, String numOfGuest, String side, String belongingGroup, String tableNum) {
        this.mFullName = fullName;
        this.mPhone = phone;
        this.mNumOfGuest = numOfGuest;
        this.mSide = side;
        this.mBelongingGroup = belongingGroup;
        this.mTableNum = tableNum;
    }

    public String getmID() {
        return mID;
    }

    public void setmID(String mID) {
        this.mID = mID;
    }

    public String getmFullName() {
        return mFullName;
    }

    public String getmPhone() {
        return mPhone;
    }

    public String getmNumOfGuest() {
        return mNumOfGuest;
    }

    public String ismIsComing() {
        return mIsComing;
    }

    public String getmSide() {
        return mSide;
    }

    public String getmBelongingGroup() {
        return mBelongingGroup;
    }

    public String getmTableNum() {
        return mTableNum;
    }

    public void setmTableNum(String mTableNum) {
        this.mTableNum = mTableNum;
    }

    public String getmCloseToDanceFloor() {
        return mCloseToDanceFloor;
    }

    public void setmCloseToDanceFloor() {
        switch (mBelongingGroup) {
            case "Friends":
                mCloseToDanceFloor = Constants.DANCE_FLOOR_1;
                break;
            case "Family":
                mCloseToDanceFloor = Constants.DANCE_FLOOR_2;
                break;
            case "Friends of the parents":
                mCloseToDanceFloor = Constants.DANCE_FLOOR_3;
                break;
        }
    }


    @Override
    public int compareTo(@NonNull GuestClass guestClass) {
        int closeToDanceFloor_1 = Integer.parseInt(mCloseToDanceFloor);
        int closeToDanceFloor_2 = Integer.parseInt(guestClass.getmCloseToDanceFloor());

        if (closeToDanceFloor_1 == closeToDanceFloor_2) {
            return 0;
        } else if (closeToDanceFloor_1 > closeToDanceFloor_2) {
            return 1;
        }
        return -1;
    }
}
