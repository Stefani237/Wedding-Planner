package com.example.stefani.weddingplanner.Fragments.supplierDetails;

import android.net.Uri;
import android.view.View;

import com.example.stefani.weddingplanner.BasicClasses.Constants;
import com.example.stefani.weddingplanner.BasicClasses.SupplierClass;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class SupplierDetailsPresenter implements ISupplierDetails.Presenter {

    private StorageReference mStorageReference;
    private ISupplierDetails.View mView;
    private SupplierClass mSupplierDetails;

    public SupplierDetailsPresenter(ISupplierDetails.View guestDetailsFragmentView) {
        mView = guestDetailsFragmentView;
        mStorageReference = FirebaseStorage.getInstance().getReferenceFromUrl(Constants.DB_FIREBASE_STORAGE); // reference to firebase storage location
    }

    @Override
    public void initializeViews() {
        // init fields with the suppliers details
        mView.setSuppCompanyName(mSupplierDetails.getmCompanyName());
        mView.setContactName(mSupplierDetails.getmContactName());
        mView.setSuppPhone(mSupplierDetails.getmPhone());
        mView.setSuppAddress(mSupplierDetails.getmAddress());
        mView.setSuppPrice(mSupplierDetails.getmPrice());
        mView.setSuppRemarks(mSupplierDetails.getmRemarks());

        if (mSupplierDetails.getmPaid().equalsIgnoreCase("TRUE")) {
            mView.setPaidChecked(true);
        } else {
            mView.setNotPaid(true);
        }

        mView.initializeViews();
    }

    @Override
    public void setSupplier(SupplierClass supplierDetails) {
        // set selected supplier details
        mSupplierDetails = supplierDetails;
    }

    @Override
    public void getSupplierImageFromDB() {
        String location = "images/suppliers/" + mSupplierDetails.getmID() + "/" + mSupplierDetails.getmID() + ".png";

        mStorageReference.child(location).getDownloadUrl() // specific supplier location
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        mView.setSupplierImage(uri); // update view with image as read from DB

                    }
                });
    }

    @Override
    public void saveImageToStorage(Uri mSelectedImageUri) {
        if (mSelectedImageUri != null) {
            String supplierID = mSupplierDetails.getmID();
            // get reference to specific supplier in firebase storage
            StorageReference storageReference = mStorageReference.child("images/suppliers/" + supplierID + "/" + supplierID + ".png");
            storageReference.putFile(mSelectedImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) { // if image saved successfully
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                }
            });
        }

    }

    @Override
    public void saveSupplierToDB(SupplierClass supplierClass) {

        if (supplierClass.getmCompanyName().length() > 0 && supplierClass.getmPrice().length() > 0) { // if required fields are filled
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl(Constants.SUPPLIERS_DB_URL); // get suppliers location in db
            DatabaseReference supplier = databaseReference.child(mSupplierDetails.getmID()); // get specific supplier reference
            supplier.child(Constants.SUPPLIER_COMPANY_NAME).setValue(supplierClass.getmCompanyName());
            supplier.child(Constants.SUPPLIER_PRICE).setValue(supplierClass.getmPrice());
            supplier.child(Constants.SUPPLIER_CONTACT_NAME).setValue(supplierClass.getmContactName());
            supplier.child(Constants.SUPPLIER_PHONE).setValue(supplierClass.getmPhone());
            supplier.child(Constants.SUPPLIER_ADDRESS).setValue(supplierClass.getmAddress());
            supplier.child(Constants.SUPPLIER_REMARKS).setValue(supplierClass.getmRemarks());
            if (supplierClass.getmPaid().equals("Paid")) {
                supplier.child(Constants.SUPPLIER_PAID).setValue("TRUE");
            } else {
                supplier.child(Constants.SUPPLIER_PAID).setValue("FALSE");
            }
            mView.toastSupplierDetailsSaved();
        } else {
            mView.setEmptyFieldVisibility(View.VISIBLE);
        }

    }


}
