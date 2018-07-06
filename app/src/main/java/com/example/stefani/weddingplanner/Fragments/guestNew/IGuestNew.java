package com.example.stefani.weddingplanner.Fragments.guestNew;

public interface IGuestNew {

    interface Presenter {

        void initializeViews();

        void addNewGuest(String name, String phone, String numInvited, String side, String group);
    }


    interface View {
        void initializeViews();

        void toastGuestAddedSuccessfully();

        void existGuestSetVisibility(int visible);

        void emptyFieldsSetVisibility(int visible);

        void clearFields();
    }

}
