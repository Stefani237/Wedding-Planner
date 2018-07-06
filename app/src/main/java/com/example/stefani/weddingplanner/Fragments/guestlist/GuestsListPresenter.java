package com.example.stefani.weddingplanner.Fragments.guestlist;

import android.util.Log;

import com.example.stefani.weddingplanner.Fragments.guestDetails.GuestDetailsFragment;
import com.example.stefani.weddingplanner.Net.ConnectionToFirebase;
import com.example.stefani.weddingplanner.SMS.SmsSenderHelper;
import com.example.stefani.weddingplanner.BasicClasses.Constants;
import com.example.stefani.weddingplanner.BasicClasses.GuestClass;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class GuestsListPresenter implements IGuestsList.Presenter {

    private IGuestsList.View mView;
    private List<GuestClass> mGuestClassList;
    private SmsSenderHelper mSmsSenderHelper;

    public GuestsListPresenter(IGuestsList.View guestDetailsFragmentView) {
        mView = guestDetailsFragmentView;
        mGuestClassList = new ArrayList<>();
        initializeViews();
        mSmsSenderHelper = new SmsSenderHelper(mView.getViewContext());
        getAllDetailsFromDB();
    }


    private void getAllDetailsFromDB() { // reads guests list from DB
        ConnectionToFirebase.getInstance().getAllGuestDetailsFromDB(guestList -> {
            mGuestClassList = new ArrayList<>(guestList);
            mView.updateAdapterData(mGuestClassList); // update list view
        });

    }

    @Override
    public void initializeViews() {
        mView.initializeViews(mGuestClassList);
    }

    @Override
    public void updateListAccordingSMS(String phoneNumber, Integer numberOfGuests) { // for correct message content
        for (GuestClass guest : mGuestClassList) {
            if (guest.getmPhone().equalsIgnoreCase(phoneNumber)) {
                // to read or write data from the database, you need an instance of DatabaseReference:
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl(Constants.GUESTS_DB_URL);

                // update number of guest that are coming:
                databaseReference.child(guest.getmID()).child(Constants.GUEST_NUM).setValue("" + numberOfGuests);

                // update isComing to true or false:
                if (numberOfGuests > Constants.NOT_COMING_SMS_RESPONSE) // is coming
                    databaseReference.child(guest.getmID()).child(Constants.GUEST_COMING).setValue("TRUE");
                else if (numberOfGuests == Constants.NOT_COMING_SMS_RESPONSE) { // not coming
                    databaseReference.child(guest.getmID()).child(Constants.GUEST_COMING).setValue("FALSE");
                }
                return;
            }
        }
    }

    @Override
    public void wrongMessageFromGuest(String phoneNumber) {
        // we want to show the name of the guest that returned a bad SMS in the sms_error_message
        for (GuestClass guest : mGuestClassList) {
            if (guest.getmPhone().equalsIgnoreCase(phoneNumber)) { // if sender is on guests list, get his name
                mView.showWrongMessage("" + guest.getmFullName());
                break;
            }
        }
    }

    @Override
    public void sendSMS() {
        if(mGuestClassList.size() > 0){
            int res = mSmsSenderHelper.sendSMS(mGuestClassList);
            if (res == 0){ // not sent
                mView.toastSmsNotSent();
            }
            else{ // sent
                mView.toastSmsSent();
            }
        }
        else{
            mView.toastEmptyList();
        }
        /*int res = mSmsSenderHelper.sendSMS(mGuestClassList);
        if (res == 0){ // not sent
            mView.toastSmsNotSent();
        }
        else{ // sent
            mView.toastSmsSent();
        }*/
    }

    @Override
    public void onItemSelected(int positionFilter) { // get filter selection
        ArrayList<GuestClass> filteredGuests;

        switch (positionFilter) {
            case 0: // All guests
                filteredGuests = new ArrayList<>(mGuestClassList);
                break;
            case 1: // arriving
                filteredGuests = filter("true");
                break;
            case 2: // not arriving
                filteredGuests = filter("false");
                break;
            default:
                filteredGuests = new ArrayList<>(mGuestClassList);

        }
        mView.updateAdapterData(filteredGuests);
    }

    private ArrayList<GuestClass> filter(String isArriving){
        // create new list to show according to filter
        ArrayList<GuestClass> guestsList = new ArrayList<>();
        for (GuestClass guest : mGuestClassList) {
            if (guest.ismIsComing().equalsIgnoreCase(isArriving)) {
                guestsList.add(guest);
            }
        }
        return guestsList;
    }

    @Override
    public void startGuestDetailFragment(GuestClass mGuest) { // move to GuestDetailsFragment
        GuestDetailsFragment guestsListFragment = new GuestDetailsFragment();
        guestsListFragment.setGuestDetails(mGuest);
        mView.setNewFragment(guestsListFragment, guestsListFragment.getClass().toString());

    }

    @Override
    public void removeGuest(String getmID) {
        FirebaseDatabase.getInstance().getReferenceFromUrl(Constants.GUESTS_DB_URL).child(getmID).removeValue(); // remove from Firebase.
    }

}
