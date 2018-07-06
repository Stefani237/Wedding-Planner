package com.example.stefani.weddingplanner.Fragments.guestDetails;

import android.view.View;

import com.example.stefani.weddingplanner.BasicClasses.Constants;
import com.example.stefani.weddingplanner.BasicClasses.GuestClass;
import com.example.stefani.weddingplanner.BasicClasses.GuestListClass;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Iterator;

public class GuestDetailsPresenter implements IGuestDetails.Presenter {


    private IGuestDetails.View mView;
    private GuestClass mGuestDetails;

    public GuestDetailsPresenter(IGuestDetails.View guestDetailsFragmentView) {
        mView = guestDetailsFragmentView;
    }

    @Override
    public void initializeViews() {
        mView.initializeViews();
    }

    @Override
    public void setGuestClass(GuestClass guestDetails) {
        // set selected guest's details
        mGuestDetails = guestDetails;
    }

    @Override
    public void setGuestDetails() {
        // get guest details and send them to view to show
        guestFullName();
        guestPhone();
        if (mGuestDetails.ismIsComing().equalsIgnoreCase("true")) {
            mView.setGuestComing(true);
        } else {
            mView.setGuestNotComing(true);
        }
        mView.setNumberOfGuest(mGuestDetails.getmNumOfGuest());
        switch (mGuestDetails.getmBelongingGroup()) {
            case "Friends":
                mView.setBelongingGroup(0);
                break;
            case "Family":
                mView.setBelongingGroup(1);
                break;
            case "Friends of the parents":
                mView.setBelongingGroup(2);
                break;
        }
        if (mGuestDetails.getmSide().equalsIgnoreCase("Bride")) {
            mView.setBrideSide(true);
        } else {
            mView.setGroomSide(true);
        }
    }

    @Override
    public void guestFullName() {
        mView.setGuestFullName(mGuestDetails.getmFullName());
    }

    @Override
    public void guestPhone() {
        mView.setGuestPhone(mGuestDetails.getmPhone());
    }

    private boolean isGuestExist(String phone, String id) {
        ArrayList<GuestClass> guestList = GuestListClass.getmArrGuest(); // get guest list

        for (Iterator<GuestClass> i = guestList.iterator(); i.hasNext(); ) {
            GuestClass guest = i.next();
            if (guest.getmPhone().equals(phone) && guest.getmID() != id) { // if the phone is already exist and it is not same guest, guest is already on the list
                return true;
            }
        }
        return false;
    }

    @Override
    public void saveGuestData(String guestName, String guestPhone, String guestIsComing, String guestNumOfGuest, String guestBelong, String guestSide) {
        if ((guestName.length() > 0) && (guestPhone.length() > 0) && (guestNumOfGuest.length() > 0)) { // if fields not empty
            String id = mGuestDetails.getmID();

            if (!isGuestExist(guestPhone, id)) { // if user isn't on list
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl(Constants.GUESTS_DB_URL);
                DatabaseReference guest = databaseReference.child(id);

                // change data in db
                guest.child(Constants.GUEST_NAME).setValue(guestName);
                guest.child(Constants.GUEST_BELONG).setValue(guestBelong);
                if (guestIsComing.equals("Coming")) {
                    guest.child(Constants.GUEST_COMING).setValue("TRUE");
                } else {
                    guest.child(Constants.GUEST_COMING).setValue("FALSE");
                }
                guest.child(Constants.GUEST_NUM).setValue(guestNumOfGuest);
                guest.child(Constants.GUEST_PHONE).setValue(guestPhone);
                guest.child(Constants.GUEST_SIDE).setValue(guestSide);
                mView.toastGuestDetailsSaved();

            } else { // Guest is already exist on list.
                mView.existGuestSetVisibility(View.VISIBLE);
            }
        } else { // there are empty fields
            mView.emptyGuestFieldsSetVisibility(View.VISIBLE);
        }
    }
}
