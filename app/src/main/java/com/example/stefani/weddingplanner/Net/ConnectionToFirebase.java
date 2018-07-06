package com.example.stefani.weddingplanner.Net;


import com.example.stefani.weddingplanner.BasicClasses.Constants;
import com.example.stefani.weddingplanner.BasicClasses.IdClasses;
import com.example.stefani.weddingplanner.BasicClasses.GuestClass;
import com.example.stefani.weddingplanner.BasicClasses.GuestListClass;
import com.example.stefani.weddingplanner.BasicClasses.SupplierClass;
import com.example.stefani.weddingplanner.BasicClasses.TaskClass;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ConnectionToFirebase {
    // reads all data from firebase and saves it in the appropriate array list / writes to firbase

    private static ConnectionToFirebase mInstance = null;
    private ArrayList<TaskClass> mTaskClassList;
    private ArrayList<SupplierClass> mSuppliersClassList;
    private ArrayList<GuestClass> mGuestClassList;


    public ConnectionToFirebase() {
    }

    public static ConnectionToFirebase getInstance() { // create a singleton instance of firebase connection
        if (mInstance == null) {
            mInstance = new ConnectionToFirebase();
        }
        return mInstance;
    }

    public void getAllTasksDetailsFromDB(final IReceivingDataFirebaseTask taskListInterface) {
        // reference gets tasks's location in db and enable you to read and write to that location.
        DatabaseReference ref = FirebaseDatabase.getInstance().getReferenceFromUrl(Constants.TASKS_DB_URL);
        ref.addValueEventListener(new ValueEventListener() { // events about data changes at a location
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mTaskClassList = new ArrayList<>();
                // any time you read Database data, you receive the data as a DataSnapshot
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    mTaskClassList.add(userSnapshot.getValue(TaskClass.class)); // add task to this.mTaskClassList
                }

                if (taskListInterface != null) {
                    taskListInterface.getTaskList(mTaskClassList); // add mTaskClassList to the tasks list received
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { // triggered in the event that this listener failed at the server

            }
        });
    }


    public void getAllSupplierDetailsFromDB(final IReceivingDataFirebaseSuppliers supListInterface) {
        // reference gets suppliers's location in db and enable you to read and write to that location.
        DatabaseReference ref = FirebaseDatabase.getInstance().getReferenceFromUrl(Constants.SUPPLIERS_DB_URL);
        ref.addValueEventListener(new ValueEventListener() { // events about data changes at a location
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mSuppliersClassList = new ArrayList<>();
                // any time you read Database data, you receive the data as a DataSnapshot
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    mSuppliersClassList.add(userSnapshot.getValue(SupplierClass.class)); // add supplier to this.mSuppliersClassList
                }

                if (supListInterface != null) {
                    supListInterface.getSupplierList(mSuppliersClassList); // add mSuppliersClassList to the suppliers list received
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { // triggered in the event that this listener failed at the server

            }
        });
    }

    public void getAllGuestDetailsFromDB(final IReceivingDataFirebaseGuest guestListInterface) {
    // reference gets guests's location in db and enable you to read and write to that location.
        DatabaseReference ref = FirebaseDatabase.getInstance().getReferenceFromUrl(Constants.GUESTS_DB_URL);
        ref.addValueEventListener(new ValueEventListener() { // events about data changes at a location
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mGuestClassList = new ArrayList<>();
                // any time you read Database data, you receive the data as a DataSnapshot
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    mGuestClassList.add(userSnapshot.getValue(GuestClass.class)); // add guest to this.mGuestClassList
                }

                GuestListClass.mArrGuest = new ArrayList<>(mGuestClassList);
                if (guestListInterface != null) {
                    guestListInterface.getGuestList(mGuestClassList); // add mGuestClassList to mArrGuest
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { // triggered in the event that this listener failed at the server

            }
        });
    }


    public void enterDataToDB(IdClasses id, String DB_CONNECTION) {
        // DB_CONNECTION completes the path of the firebase and makes it appropriate to all three classes - guest, task and supplier
        DatabaseReference homeTrackersRef = FirebaseDatabase
                .getInstance()
                .getReferenceFromUrl(Constants.DB_URL + DB_CONNECTION);

        DatabaseReference pushRef = homeTrackersRef.push(); // every time you push a new node onto a list, your database generates a unique key
        String pushId = pushRef.getKey();
        id.setmID(pushId);
        pushRef.setValue(id); // update id of the item pushed to be same as the generated one
    }

}
