package com.example.stefani.weddingplanner.Fragments.suppllierNew;

import android.net.Uri;

public interface ISupplierNew {

    interface Presenter {

        void initializeViews();

        void addSupplierToDB(String companyName, String price, String paid, String name, String phone, String address, String remarks);

        void saveImageToStorage(Uri selectedImageUri);

    }


    interface View {
        void initializeViews();


        void insertImageToMedia(String getmID);

        void saveImageToStorage();

        void clearFields();
    }

}
