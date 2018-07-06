package com.example.stefani.weddingplanner.BasicClasses;


public class SupplierClass implements IdClasses {
    private String mID;
    private String mCompanyName;
    private String mContactName;
    private String mPhone;
    private String mAddress;
    private String mPrice;
    private String mPaid = "FALSE";
    private String mRemarks;
    private String imgUrl;


    public SupplierClass() {
    }

    public SupplierClass(String companyName, String price) {
        this.mCompanyName = companyName;
        this.mPrice = price;
    }

    public String getmID() {
        return mID;
    }

    public void setmID(String mID) {
        this.mID = mID;
    }

    public String getmCompanyName() {
        return mCompanyName;
    }

    public void setmCompanyName(String mCompanyName) {
        this.mCompanyName = mCompanyName;
    }

    public String getmContactName() {
        return mContactName;
    }

    public void setmContactName(String mContactName) {
        this.mContactName = mContactName;
    }

    public String getmPhone() {
        return mPhone;
    }

    public void setmPhone(String mPhone) {
        this.mPhone = mPhone;
    }

    public String getmAddress() {
        return mAddress;
    }

    public void setmAddress(String mAddress) {
        this.mAddress = mAddress;
    }

    public String getmPrice() {
        return mPrice;
    }

    public void setmPrice(String mPrice) {
        this.mPrice = mPrice;
    }

    public String getmPaid() {
        return mPaid;
    }

    public void setmPaid(String mPaid) {
        this.mPaid = mPaid;
    }

    public String getmRemarks() {
        return mRemarks;
    }

    public void setmRemarks(String mRemarks) {
        this.mRemarks = mRemarks;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
