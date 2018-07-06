package com.example.stefani.weddingplanner.Fragments.supplierlist;


import com.example.stefani.weddingplanner.Net.ConnectionToFirebase;
import com.example.stefani.weddingplanner.BasicClasses.Constants;
import com.example.stefani.weddingplanner.BasicClasses.SupplierClass;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class SuppliersListPresenter implements ISuppliersList.Presenter {

    private ISuppliersList.View mView;
    public static List<SupplierClass> mSuppliersClassList;

    public SuppliersListPresenter(ISuppliersList.View guestDetailsFragmentView) {
        mView = guestDetailsFragmentView;
        getAllDetailsFromDB();

    }

    @Override
    public void initializeViews() {
        mView.initializeViews(mSuppliersClassList);
    }


    @Override
    public void deleteSupplier(String supplierID) {
        // get reference to required location in storage
        StorageReference mStorageReference = FirebaseStorage.getInstance().getReferenceFromUrl(Constants.DB_FIREBASE_STORAGE);
        // get specific image according to supplier id
        StorageReference storageReference = mStorageReference.child("images/suppliers/" + supplierID + "/" + supplierID + ".png");
        storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() { // delete image from storage
            @Override
            public void onSuccess(Void aVoid) {

            }
        });
        FirebaseDatabase.getInstance().getReferenceFromUrl(Constants.SUPPLIERS_DB_URL).child(supplierID).removeValue(); // remove supplier from firebase.
    }

    @Override
    public void onItemSortSelectedClick(int positionFilter) {
        ArrayList<SupplierClass> filteredSuppliers; // create new list ro show according to selected value

        switch (positionFilter) {
            case 0: // All tasks
                filteredSuppliers = new ArrayList<>(mSuppliersClassList);
                break;
            case 1: // done
                filteredSuppliers = filter("true");
                break;
            case 2: // not done
                filteredSuppliers = filter("false");
                break;
            default:
                filteredSuppliers = new ArrayList<>(mSuppliersClassList);
        }

        mView.updateAdapter(filteredSuppliers);

    }

    private ArrayList<SupplierClass> filter(String isPaid) {
        // add supplier to list according to isPaid parameter
        ArrayList<SupplierClass> suppliersList = new ArrayList<>();
        for (SupplierClass sup : mSuppliersClassList) {
            if (sup.getmPaid().equalsIgnoreCase(isPaid)) {
                suppliersList.add(sup);
            }
        }
        return suppliersList;
    }


    private void getAllDetailsFromDB() {
        // read data from firebase and update the view list
        ConnectionToFirebase.getInstance().getAllSupplierDetailsFromDB(supList -> {
            mSuppliersClassList = new ArrayList<>(supList);
            mView.updateAdapter(mSuppliersClassList);
        });

    }

}

