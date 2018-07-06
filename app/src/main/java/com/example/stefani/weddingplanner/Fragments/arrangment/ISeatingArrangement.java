package com.example.stefani.weddingplanner.Fragments.arrangment;

import com.example.stefani.weddingplanner.BasicClasses.GuestClass;
import com.example.stefani.weddingplanner.BasicClasses.TableGuestsListClass;

import java.util.ArrayList;
import java.util.List;

public interface ISeatingArrangement {

    interface Presenter {

        void initializeViews();

        void setTableMaxSeat(Object item, int position, String numOfSeats);

        String getSpecificTableMaxSeat(int i);

        String getTableTitle(String tableTag);

        int getGuestsNumInTable(String tableTag);

        List<GuestClass> getClickedTableGuestsList(String tableTag);

      //  void resetTables();

        void setTableTitles();

       // void startSettingTables();

       // int checkSeatingArrangement();

        void startSeatingArrangement();

        ArrayList<TableGuestsListClass> getGuestItem(List<GuestClass> guestList);

        void changeSeats(String numOfSeats, String tableTag);
    }


    interface View {

        void initializeViews();

        void toastResetSeatingArrangement();

        void setEnableSpecificTable(int position);

        void setDisableSpecificTableButton(int position);

        void toastTooManyGuest();

        void toastListEmpty();

        void toastLongTable();

        void toastNotEnoughSeats();

        void setSeatingArrangementButtonEnable();
        void setSeatingArrangementButtonDisable();
    }

}
