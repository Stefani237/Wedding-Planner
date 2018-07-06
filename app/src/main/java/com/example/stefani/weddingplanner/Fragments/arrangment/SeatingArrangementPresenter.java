package com.example.stefani.weddingplanner.Fragments.arrangment;

import com.example.stefani.weddingplanner.Algorithm.SeatingArrangementAlg;
import com.example.stefani.weddingplanner.Helpers.SharedPreferencesHelper;
import com.example.stefani.weddingplanner.BasicClasses.Constants;
import com.example.stefani.weddingplanner.BasicClasses.GuestClass;
import com.example.stefani.weddingplanner.BasicClasses.TableClass;
import com.example.stefani.weddingplanner.BasicClasses.TableGuestsListClass;
import java.util.ArrayList;
import java.util.List;

public class SeatingArrangementPresenter implements ISeatingArrangement.Presenter{

    public static ArrayList<TableClass> mTables = new ArrayList<>();
    private ISeatingArrangement.View mView;
    private SeatingArrangementAlg settingArrangementAlg;

    public SeatingArrangementPresenter(ISeatingArrangement.View guestDetailsFragmentView) {
        mView = guestDetailsFragmentView;

    }


    @Override
    public void initializeViews() {
        settingArrangementAlg = new SeatingArrangementAlg();
        int res = settingArrangementAlg.comingGuestList();
        initializeTableList();
        checkComingGuestsList(res);
    }


    private void checkComingGuestsList(int res){
        if(res == 0){ // too many guests
            mView.toastTooManyGuest();
            mView.setSeatingArrangementButtonDisable();
        }
        else if(res == -1){ // list is empty
            mView.toastListEmpty();
            mView.setSeatingArrangementButtonDisable();

        }
        else {
            settingArrangementAlg.initializeAdjacencyMatrix();
            mView.setSeatingArrangementButtonEnable();
        }
    }

    private void initializeTableList(){
        for (int i = 0; i < Constants.MAX_TABLES; i++) {
            int tableID = i + 1; // set id to table 1..56
            TableClass tableClass = new TableClass("" + tableID, "", new ArrayList<>());
            mTables.add(tableClass);
            getSpecificTableMaxSeat(tableID); // init max seat
        }
        adjustingHallToLongTables();
    }

    @Override
    public String getTableTitle(String tag){
        for(int i = 0; i <mTables.size(); i++){
            TableClass table = mTables.get(i);
            if(table != null && table.getmID().equals(tag)){
                return table.getmTableTitle();
            }
        }
        return "";
    }

    @Override
    public int getGuestsNumInTable(String tag){
        for(int i = 0; i <mTables.size(); i++){
            TableClass table = mTables.get(i);
            if(table != null && table.getmID().equals(tag)){
                return table.getmNumOfSeatedPeople();
            }
        }
        return 0;
    }


    @Override
    public String getSpecificTableMaxSeat(int tableNum){
        String numOfSeats = "" + Constants.MAX_SEAT; // default = 12
        String tableSeats = SharedPreferencesHelper.getInstance().getPreferenceString("Table " + tableNum); // get from sharedPreferences

        if(!tableSeats.isEmpty()){
            numOfSeats = tableSeats;
        }
        else{ // if no value is saved for the table - save default
            SharedPreferencesHelper.getInstance().writePreference("Table " + tableNum, numOfSeats);
        }

        return numOfSeats;
    }

    @Override
    public void setTableMaxSeat(Object item, int position, String numOfSeats) { // change seats at all of the hall's tables
        if (item != null && position != 0) {
            for (int tableId = 0; tableId < Constants.MAX_TABLES; tableId++) {
                SharedPreferencesHelper.getInstance().writePreference("Table " + (tableId+1),numOfSeats); // save new seat number in sharedPreferences
            }

            resetTables();
            mView.toastResetSeatingArrangement();
        }
    }

    @Override
    public void setTableTitles(){
        for (int i = 0; i < Constants.MAX_TABLES; i++) {
            TableClass table = mTables.get(i);
            List<GuestClass> list = table.getmTablesGuestsList();
            if(!list.isEmpty()){ // if people sit at the table
                String title = list.get(0).getmBelongingGroup();
                if(title.equals("Family")){
                    title += " " + list.get(0).getmSide();
                }
                mTables.get(i).setmTableTitle(title);
            }
            else{ // if table is empty
                mView.setDisableSpecificTableButton(i);
            }
        }
    }

    private void startSettingTables() {
        settingArrangementAlg.settingTables();
    }


    public int checkSeatingArrangement() {
        return settingArrangementAlg.checkSeatingArrangement();
    }

    @Override
    public void startSeatingArrangement() {
        resetTables();
        startSettingTables();
        int status = checkSeatingArrangement();

        if(status == 0){ // can't create seating arrangement
            resetTables();
            mView.toastNotEnoughSeats();
        }
        else{
            setTableTitles();
        }
    }


    private void resetTables(){
        settingArrangementAlg.resetSettingTables();
        for (int i = 0; i < Constants.MAX_TABLES; i++){
            if(!mTables.isEmpty()) {
                mTables.get(i).setmTableTitle("");
                mTables.get(i).getmTablesGuestsList().clear();
                mTables.get(i).setmNumOfSeatedPeople(0);
                mView.setEnableSpecificTable(i);
                adjustingHallToLongTables();
            }
        }
    }


    private void adjustingHallToLongTables(){ // accommodate hall to long tables
        for(int i = 0; i < Constants.MAX_TABLES; i++){
            String currentTableSeats = SharedPreferencesHelper.getInstance().getPreferenceString("Table " + (i+1));
            if(!currentTableSeats.isEmpty()) {
                if (currentTableSeats.equals("24")) {
                    String disableTable = SharedPreferencesHelper.getInstance().getPreferenceString("Table " + (Constants.MAX_TABLES-i));
                    mTables.get(Constants.MAX_TABLES-(i+1)).setmNumOfSeatedPeople(Integer.parseInt(disableTable));
                    int position = Constants.MAX_TABLES-(i+1);
                    mView.setDisableSpecificTableButton(position);
                }
            }
        }
    }


    @Override
    public List<GuestClass> getClickedTableGuestsList(String tag){ // get the guests list of the selected table
        for(int i = 0; i <mTables.size(); i++){
            TableClass table = mTables.get(i);
            if(table != null && table.getmID().equals(tag)){ // compare tag of chosen table with current table's id
                return table.getmTablesGuestsList();
            }
        }
        return null;
    }

    @Override
    public ArrayList<TableGuestsListClass> getGuestItem(List<GuestClass> guestList){
        ArrayList<TableGuestsListClass> items = new ArrayList<>();
        if(guestList != null) {
            for (GuestClass guest : guestList) {
                items.add(new TableGuestsListClass(guest.getmFullName(), guest.getmNumOfGuest()));
            }
        }
        return items;
    }

    @Override
    public void changeSeats(String numOfSeats, String tableTag) {
        if(numOfSeats.equals("" + Constants.LONG_TABLE) && checkLongTablesAmount() == 0){
            mView.toastLongTable();
        }
        else{
            SharedPreferencesHelper.getInstance().writePreference("Table " + tableTag, numOfSeats); // save new seat number in sharedPreferences
            resetTables();
            adjustingHallToLongTables();
            mView.toastResetSeatingArrangement();
        }

    }


    private int checkLongTablesAmount(){
        int counter = 0;
        for (int i = 0; i < Constants.MAX_TABLES; i++){
            String numOfSeats = SharedPreferencesHelper.getInstance().getPreferenceString("Table " + (i+1));
            if(numOfSeats.equals("" + Constants.LONG_TABLE)){
                counter++;
            }
            if(counter == Constants.MAX_LONG_TABLES){ // number of long table is 4 (maximum allowed)
                return 0;
            }
        }
        return 1;
    }
}
