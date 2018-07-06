package com.example.stefani.weddingplanner.Fragments.suppllierNew;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.example.stefani.weddingplanner.Net.ConnectionToFirebase;
import com.example.stefani.weddingplanner.BasicClasses.Constants;
import com.example.stefani.weddingplanner.BasicClasses.SupplierClass;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class SupplierNewPresenter implements ISupplierNew.Presenter {

    private final StorageReference mStorageReference;
    private ISupplierNew.View mView;
    private SupplierClass mSupplierClass;

    public SupplierNewPresenter(ISupplierNew.View guestDetailsFragmentView) {
        mView = guestDetailsFragmentView;
        mStorageReference = FirebaseStorage.getInstance().getReferenceFromUrl(Constants.DB_FIREBASE_STORAGE); // reference to firebase storage location
    }


    @Override
    public void initializeViews() {

    }

    @Override
    public void addSupplierToDB(String companyName, String price, String paid, String name, String phone, String address, String remarks) {
        mSupplierClass = new SupplierClass(companyName, price);
        mSupplierClass.setmContactName(name);
        mSupplierClass.setmPhone(phone);
        mSupplierClass.setmAddress(address);
        mSupplierClass.setmRemarks(remarks);
        if (paid.equals("Paid")) {
            mSupplierClass.setmPaid("TRUE");
        } else {
            mSupplierClass.setmPaid("FALSE");
        }

        ConnectionToFirebase.getInstance().enterDataToDB(mSupplierClass, Constants.SUPPLIERS);

        mView.insertImageToMedia(mSupplierClass.getmID());
        mView.saveImageToStorage();
        mView.clearFields();
    }


    public void saveImageToStorage(Uri selectedImageUri) {
        String supplierID = mSupplierClass.getmID();
        // get reference to specific supplier in firebase storage
        StorageReference storageReference = mStorageReference.child("images/suppliers/" + supplierID + "/" + supplierID + ".png");
        storageReference.putFile(selectedImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) { // if image saved successfully
                Uri downloadUrl = taskSnapshot.getDownloadUrl(); // get uri of uploaded image
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) { // if could not save image
            }
        });
    }
}
