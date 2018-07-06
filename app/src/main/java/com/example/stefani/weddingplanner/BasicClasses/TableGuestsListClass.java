package com.example.stefani.weddingplanner.BasicClasses;


public class TableGuestsListClass {
    private String mGuestName;
    private String mGuestsAmount;

    public TableGuestsListClass(String mGuestName, String mGuestsAmount) {
        this.mGuestName = mGuestName;
        this.mGuestsAmount = mGuestsAmount;
    }

    public String getmGuestName() {
        return mGuestName;
    }

    public String getmGuestsAmount() {
        return mGuestsAmount;
    }

}
