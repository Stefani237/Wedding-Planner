package com.example.stefani.weddingplanner.Fragments.guestNew;

import android.view.View;

import com.example.stefani.weddingplanner.Net.ConnectionToFirebase;
import com.example.stefani.weddingplanner.BasicClasses.Constants;
import com.example.stefani.weddingplanner.BasicClasses.GuestClass;
import com.example.stefani.weddingplanner.BasicClasses.GuestListClass;

import java.util.ArrayList;
import java.util.Iterator;

public class GuestNewPresenter implements IGuestNew.Presenter {

    private IGuestNew.View mView;


    public GuestNewPresenter(IGuestNew.View guestDetailsFragmentView) {
        mView = guestDetailsFragmentView;
        initializeViews();

    }

    @Override
    public void initializeViews() {

        mView.initializeViews();
    }


    private boolean isGuestExist(String phone) {
        // checks if current phone exist in guest list
        ArrayList<GuestClass> guestList = GuestListClass.getmArrGuest();

        for (Iterator<GuestClass> i = guestList.iterator(); i.hasNext(); ) {
            GuestClass guest = i.next();
            if (guest.getmPhone().equals(phone)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void addNewGuest(String name, String phone, String numInvited, String side, String group) {
        if (name.length() > 0 && phone.length() > 0 && numInvited.length() > 0 && side.length() > 0 && group.length() > 0) {
            if (!isGuestExist(phone)) {
                mView.existGuestSetVisibility(View.INVISIBLE);
                mView.emptyFieldsSetVisibility(View.INVISIBLE);

                GuestClass guest = new GuestClass(name, phone, "" + numInvited, side, group, "-1");

                ConnectionToFirebase.getInstance().enterDataToDB(guest, Constants.GUESTS);

                mView.clearFields();
                mView.toastGuestAddedSuccessfully();

            } else { // guest is already exist on list.
                mView.emptyFieldsSetVisibility(View.INVISIBLE);
                mView.existGuestSetVisibility(View.VISIBLE);
            }
        } else { // not all fields filled
            mView.existGuestSetVisibility(View.INVISIBLE);
            mView.emptyFieldsSetVisibility(View.VISIBLE);
        }
    }

}
