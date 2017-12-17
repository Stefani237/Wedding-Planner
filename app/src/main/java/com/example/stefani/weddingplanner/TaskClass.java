package com.example.stefani.weddingplanner;

/**
 * Created by Stefani on 16/12/2017.
 */

public class TaskClass {
    private String mID;
    private String mTaskName;
    private String mDescription;
    private String mEndDate;
    private String mTips;
    private String mEstimatedPrice;
    private String mMyBudget;

    public TaskClass(String id, String taskName, String description, String endDate, String tips, String estimatedPrice, String myBudget) {
        this.mID = id;
        this.mTaskName = taskName;
        this.mDescription = description;
        this.mEndDate = endDate;
        this.mTips = tips;
        this.mEstimatedPrice = estimatedPrice;
        this.mMyBudget = myBudget;
    }

    public String getmID() {
        return mID;
    }

    public void setmID(String mID) {
        this.mID = mID;
    }

    public String getmTaskName() {
        return mTaskName;
    }

    public void setmTaskName(String mTaskName) {
        this.mTaskName = mTaskName;
    }

    public String getmDescription() {
        return mDescription;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getmEndDate() {
        return mEndDate;
    }

    public void setmEndDate(String mEndDate) {
        this.mEndDate = mEndDate;
    }

    public String getmTips() {
        return mTips;
    }

    public void setmTips(String mTips) {
        this.mTips = mTips;
    }

    public String getmEstimatedPrice() {
        return mEstimatedPrice;
    }

    public void setmEstimatedPrice(String mEstimatedPrice) {
        this.mEstimatedPrice = mEstimatedPrice;
    }

    public String getmMyBudget() {
        return mMyBudget;
    }

    public void setmMyBudget(String mMyBudget) {
        this.mMyBudget = mMyBudget;
    }
}
