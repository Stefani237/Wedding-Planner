package com.example.stefani.weddingplanner.Fragments.supplierDetails;

import android.net.Uri;

import com.example.stefani.weddingplanner.BasicClasses.SupplierClass;

public interface ISupplierDetails {

    interface Presenter {

        void initializeViews();

        void setSupplier(SupplierClass supplierDetails);

        void getSupplierImageFromDB();

        void saveImageToStorage(Uri selectedImageUri);

        void saveSupplierToDB(SupplierClass supplierClass);
    }


    interface View {
        void initializeViews();

        void setSupplierImage(Uri uri);

        void setSuppCompanyName(String companyName);

        void setContactName(String contactName);

        void setSuppPhone(String suppPhone);

        void setSuppAddress(String address);

        void setSuppPrice(String price);

        void setSuppRemarks(String remarks);

        void setPaidChecked(boolean isPaid);

        void setNotPaid(boolean isNotPaid);

        void setEmptyFieldVisibility(int visible);

        void toastSupplierDetailsSaved();
    }

}
