package com.example.stefani.weddingplanner.Fragments.guestlist;

import android.content.Context;

import com.example.stefani.weddingplanner.Fragments.guestDetails.GuestDetailsFragment;
import com.example.stefani.weddingplanner.BasicClasses.GuestClass;

import java.util.List;

public interface IGuestsList {

    interface Presenter {

        void initializeViews();

        void updateListAccordingSMS(String phoneNumber, Integer numberOfGuests);

        void wrongMessageFromGuest(String phoneNumber);

        void sendSMS();

        void onItemSelected(int positionFilter);

        void startGuestDetailFragment(GuestClass mGuest);

        void removeGuest(String getmID);
    }


    interface View {
        void initializeViews(List<GuestClass> guestList);

        void showWrongMessage(String senderName);

        Context getViewContext();

        void updateAdapterData(List<GuestClass> guestClassList);

        void setNewFragment(GuestDetailsFragment guestsListFragment, String string);

        void toastSmsSent();

        void toastSmsNotSent();

        void toastEmptyList();
    }

}
