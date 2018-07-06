package com.example.stefani.weddingplanner.Algorithm;

import android.content.Intent;
import android.util.Log;

import com.example.stefani.weddingplanner.Fragments.arrangment.SeatingArrangementPresenter;
import com.example.stefani.weddingplanner.Helpers.SharedPreferencesHelper;
import com.example.stefani.weddingplanner.BasicClasses.Constants;
import com.example.stefani.weddingplanner.BasicClasses.GuestClass;
import com.example.stefani.weddingplanner.BasicClasses.GuestListClass;
import com.example.stefani.weddingplanner.BasicClasses.TableClass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


public class SeatingArrangementAlg {
    public ArrayList<GuestClass> mComingGuests = new ArrayList<>();
    public ArrayList<TableClass> mTables = SeatingArrangementPresenter.mTables;
    public int mGuestCounter = 0;
    private int[][] mAdjacency;

    public int comingGuestList() {
        // creates a new list of coming guests only
        ArrayList<GuestClass> guestList = GuestListClass.getmArrGuest(); // get guest list

        for (Iterator<GuestClass> i = guestList.iterator(); i.hasNext(); ) {
            GuestClass guest = i.next();
            if (guest.ismIsComing().toLowerCase().equals("true")) { // if guest is coming to the wedding
                mComingGuests.add(guest); // add him to coming guests list
                guest.setmCloseToDanceFloor();

                mGuestCounter += Integer.parseInt(guest.getmNumOfGuest());
            }
        }

        if (mGuestCounter > Constants.MAX_COMING_GUESTS) { // too many guests
            return 0;
        } else if (mGuestCounter == 0) { // list is empty
            return -1;
        }

        Collections.sort(mComingGuests); // sort list according to close to floor value
        for (int i = 0; i < mComingGuests.size(); i++) {
            GuestClass guest = mComingGuests.get(i);
            guest.setmID(i + ""); // change his id
        }

        return 1;
    }


    // create mAdjacency matrix: 0 means can't sit together, 1 can sit.
    public void initializeAdjacencyMatrix() {
        int size = mComingGuests.size();
        mAdjacency = new int[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                String belongGroup_i = mComingGuests.get(i).getmBelongingGroup();
                String belongGroup_j = mComingGuests.get(j).getmBelongingGroup();

                if (mComingGuests.get(i).getmID() == mComingGuests.get(j).getmID()) { // same guest
                    mAdjacency[i][j] = 0;
                } else if (belongGroup_i.equals("Family") && (belongGroup_j.equals("Family")
                        && !(mComingGuests.get(i).getmSide().equals(mComingGuests.get(j).getmSide())))) {
                    // family of groom's side can't sit with the bride's side
                    mAdjacency[i][j] = 0;
                } else if ((belongGroup_i.equals("Friends")) &&
                        (belongGroup_j.equals("Friends of the parents") || belongGroup_j.equals("Family"))) {
                    // friends can't sit with family or friends of the parents
                    mAdjacency[i][j] = 0;
                } else if ((belongGroup_i.equals("Friends of the parents") || belongGroup_i.equals("Family"))
                        && (belongGroup_j.equals("Friends"))) {
                    // family or friends of the parents can't sit with friends - same as the previous condition
                    mAdjacency[i][j] = 0;
                } else if (belongGroup_i.equals("Friends of the parents") && belongGroup_j.equals("Family")) {
                    // friends of the parents can't sit with family
                    mAdjacency[i][j] = 0;
                } else if (belongGroup_i.equals("Family") && belongGroup_j.equals("Friends of the parents")) {
                    // friends of the parents can't sit with family - same as the previous condition
                    mAdjacency[i][j] = 0;
                } else {
                    mAdjacency[i][j] = 1;
                }
            }
        }
    }

    public void resetSettingTables() { // reset coming guests list by reset table number
        for (int guestNum = 0; guestNum < mComingGuests.size(); guestNum++) {
            mComingGuests.get(guestNum).setmTableNum("-1");
        }
    }

    public void settingTables() { // setting into tables
        for (int guestNum = 0; guestNum < mComingGuests.size(); guestNum++) {
            GuestClass guest = mComingGuests.get(guestNum);

            if (Integer.valueOf(guest.getmTableNum()) == -1) { // if this guest has not yet been assigned to table
                for (int tableId = 0; tableId < Constants.MAX_TABLES; tableId++) {
                    TableClass table = mTables.get(tableId);

                    // check constraints:
                    boolean isCanSeatTogether = canSit(guest, table);
                    int numOfGuest = table.getmNumOfSeatedPeople() + Integer.valueOf(guest.getmNumOfGuest());
                    String str = SharedPreferencesHelper.getInstance().getPreferenceString("Table " + (tableId + 1));
                    int maxSeat = Integer.parseInt(str);

                    if (isCanSeatTogether && (numOfGuest <= maxSeat)) {
                        table.addGuestsToTable(guest);
                        guest.setmTableNum("" + (tableId + 1));
                        table.setmNumOfSeatedPeople(numOfGuest);
                        break;
                    }
                }
            }
        }
    }

    private boolean canSit(GuestClass guestClass, TableClass table) {
        // checks whether the guestClass's belonging group is equal to the guests who are already on the table
        List<GuestClass> guestsInTheTable = table.getmTablesGuestsList();
        int guestNum = Integer.parseInt(guestClass.getmID());

        if (guestsInTheTable.size() > 0) { // table isn't empty
            GuestClass guest = guestsInTheTable.get(0);
            int guestInTableNum = Integer.parseInt(guest.getmID());
            if (mAdjacency[guestNum][guestInTableNum] == 0) { // check adjacency matrix
                return false;
            } else {
                return true;
            }
        }
        return true; // table is empty
    }

    public int checkSeatingArrangement() { // if there are guests without seat
        for (int guestNum = 0; guestNum < mComingGuests.size(); guestNum++) {
            String tableNum = mComingGuests.get(guestNum).getmTableNum();
            if (tableNum.equals("-1")) {
                return 0;
            }
        }
        return 1;
    }

}

