package com.example.stefani.weddingplanner.Fragments.guestDetails;

import com.example.stefani.weddingplanner.BasicClasses.GuestClass;

public interface IGuestDetails {

    interface Presenter {

        void initializeViews();

        void setGuestClass(GuestClass guestDetails);

        void setGuestDetails();

        void guestFullName();

        void guestPhone();

        void saveGuestData(String guestName, String guestPhone, String guestIsComing, String guestNumOfGuest, String guestBelong, String guestSide);
    }


    interface View {
        void initializeViews();

        void setGuestFullName(String guestName);

        void setGuestPhone(String guestPhone);

        void setGuestComing(boolean isComing);

        void setGuestNotComing(boolean isNotComing);

        void setNumberOfGuest(String numberOfGuest);

        void setBelongingGroup(int selection);

        void setBrideSide(boolean isBrideSide);

        void setGroomSide(boolean isGroomSide);

        void existGuestSetVisibility(int visible);

        void emptyGuestFieldsSetVisibility(int visible);

        void toastGuestDetailsSaved();

    }

}
