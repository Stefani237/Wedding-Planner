package com.example.stefani.weddingplanner.BasicClasses;

import java.util.List;


public class TableClass implements IdClasses {

    private String mID;
    private String mTableTitle; // belonging group
    private int mNumOfSeatedPeople; // mNumOfSeatedPeople != mTableGuestsList.size().
    private List<GuestClass> mTableGuestsList;


    public TableClass(String mID, String mTableTitle, List<GuestClass> mPeopleInTableList) {
        this.mID = mID;
        this.mTableTitle = mTableTitle;
        this.mTableGuestsList = mPeopleInTableList;
    }


    public String getmID() {
        return mID;
    }

    public void setmID(String mID) {
        this.mID = mID;
    }

    public String getmTableTitle() {
        return mTableTitle;
    }

    public void setmTableTitle(String mTableTitle) {
        this.mTableTitle = mTableTitle;
    }

    public List<GuestClass> getmTablesGuestsList() {
        return mTableGuestsList;
    }

    public void setmTablesGuestsList(List<GuestClass> mTablesGuestsList) {
        this.mTableGuestsList = mTablesGuestsList;
    }

    public int getmNumOfSeatedPeople() {
        return mNumOfSeatedPeople;
    }

    public void setmNumOfSeatedPeople(int mNumOfSeatedPeople) {
        this.mNumOfSeatedPeople = mNumOfSeatedPeople;
    }

    public void addGuestsToTable(GuestClass mPeopleInTable) {
        this.mTableGuestsList.add(mPeopleInTable);
    }
}
