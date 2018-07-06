package com.example.stefani.weddingplanner.BasicClasses;


public class TaskClass implements IdClasses {
    private String mID;
    private String mTaskName;
    private String mDescription;
    private String mEndDate;
    private String mTips;
    private String mIsDone = "FALSE";
    private String mEstimatedPrice;
    private String mMyBudget;


    public TaskClass() {
    }


    public TaskClass(String taskName) {
        this.mTaskName = taskName;
    }

    public String getmIsDone() {
        return mIsDone;
    }

    public void setmIsDone(String mIsDone) {
        this.mIsDone = mIsDone;
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
