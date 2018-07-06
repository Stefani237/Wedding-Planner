package com.example.stefani.weddingplanner.Fragments.arrangment;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stefani.weddingplanner.R;
import com.example.stefani.weddingplanner.Adapters.TableGuestsListAdapter;
import com.example.stefani.weddingplanner.BasicClasses.Constants;
import com.example.stefani.weddingplanner.BasicClasses.GuestClass;
import com.example.stefani.weddingplanner.BasicClasses.TableGuestsListClass;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SeatingArrangementFragment extends Fragment implements ISeatingArrangement.View {


    @BindView(R.id.imageView)
    ImageView mLoader;
    @BindView(R.id.info_btn)
    ImageButton mInfo;
    @BindView(R.id.startSeatingArrangement)
    Button mStartSeatingArrangement;
    @BindView(R.id.max_seat)
    Spinner mSetMaxSeatAllTables;

    private Button mTablesButtons[] = new Button[Constants.MAX_TABLES];
    private ISeatingArrangement.Presenter mPresenter;
    private View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_seating_arrangement, container, false);
        ButterKnife.bind(this, rootView);

        mPresenter = new SeatingArrangementPresenter(this);
        initializeTableButtons();
        mPresenter.initializeViews();
        mLoader.setImageResource(R.drawable.loader_animation);
        setAllTablesMaxSeat();

        return rootView;
    }


    private void initializeTableButtons() {
        mTablesButtons[0] = rootView.findViewById(R.id.table_1);
        mTablesButtons[1] = rootView.findViewById(R.id.table_2);
        mTablesButtons[2] = rootView.findViewById(R.id.table_3);
        mTablesButtons[3] = rootView.findViewById(R.id.table_4);
        mTablesButtons[4] = rootView.findViewById(R.id.table_5);
        mTablesButtons[5] = rootView.findViewById(R.id.table_6);
        mTablesButtons[6] = rootView.findViewById(R.id.table_7);
        mTablesButtons[7] = rootView.findViewById(R.id.table_8);
        mTablesButtons[8] = rootView.findViewById(R.id.table_9);
        mTablesButtons[9] = rootView.findViewById(R.id.table_10);
        mTablesButtons[10] = rootView.findViewById(R.id.table_11);
        mTablesButtons[11] = rootView.findViewById(R.id.table_12);
        mTablesButtons[12] = rootView.findViewById(R.id.table_13);
        mTablesButtons[13] = rootView.findViewById(R.id.table_14);
        mTablesButtons[14] = rootView.findViewById(R.id.table_15);
        mTablesButtons[15] = rootView.findViewById(R.id.table_16);
        mTablesButtons[16] = rootView.findViewById(R.id.table_17);
        mTablesButtons[17] = rootView.findViewById(R.id.table_18);
        mTablesButtons[18] = rootView.findViewById(R.id.table_19);
        mTablesButtons[19] = rootView.findViewById(R.id.table_20);
        mTablesButtons[20] = rootView.findViewById(R.id.table_21);
        mTablesButtons[21] = rootView.findViewById(R.id.table_22);
        mTablesButtons[22] = rootView.findViewById(R.id.table_23);
        mTablesButtons[23] = rootView.findViewById(R.id.table_24);
        mTablesButtons[24] = rootView.findViewById(R.id.table_25);
        mTablesButtons[25] = rootView.findViewById(R.id.table_26);
        mTablesButtons[26] = rootView.findViewById(R.id.table_27);
        mTablesButtons[27] = rootView.findViewById(R.id.table_28);
        mTablesButtons[28] = rootView.findViewById(R.id.table_29);
        mTablesButtons[29] = rootView.findViewById(R.id.table_30);
        mTablesButtons[30] = rootView.findViewById(R.id.table_31);
        mTablesButtons[31] = rootView.findViewById(R.id.table_32);
        mTablesButtons[32] = rootView.findViewById(R.id.table_33);
        mTablesButtons[33] = rootView.findViewById(R.id.table_34);
        mTablesButtons[34] = rootView.findViewById(R.id.table_35);
        mTablesButtons[35] = rootView.findViewById(R.id.table_36);
        mTablesButtons[36] = rootView.findViewById(R.id.table_37);
        mTablesButtons[37] = rootView.findViewById(R.id.table_38);
        mTablesButtons[38] = rootView.findViewById(R.id.table_39);
        mTablesButtons[39] = rootView.findViewById(R.id.table_40);
        mTablesButtons[40] = rootView.findViewById(R.id.table_41);
        mTablesButtons[41] = rootView.findViewById(R.id.table_42);
        mTablesButtons[42] = rootView.findViewById(R.id.table_43);
        mTablesButtons[43] = rootView.findViewById(R.id.table_44);
        mTablesButtons[44] = rootView.findViewById(R.id.table_45);
        mTablesButtons[45] = rootView.findViewById(R.id.table_46);
        mTablesButtons[46] = rootView.findViewById(R.id.table_47);
        mTablesButtons[47] = rootView.findViewById(R.id.table_48);
        mTablesButtons[48] = rootView.findViewById(R.id.table_49);
        mTablesButtons[49] = rootView.findViewById(R.id.table_50);
        mTablesButtons[50] = rootView.findViewById(R.id.table_51);
        mTablesButtons[51] = rootView.findViewById(R.id.table_52);
        mTablesButtons[52] = rootView.findViewById(R.id.table_53);
        mTablesButtons[53] = rootView.findViewById(R.id.table_54);
        mTablesButtons[54] = rootView.findViewById(R.id.table_55);
        mTablesButtons[55] = rootView.findViewById(R.id.table_56);
        setOnClickListenerForTables();
    }

    private void setOnClickListenerForTables() {
        for (int i = 0; i < mTablesButtons.length; i++) {
            mTablesButtons[i].setOnClickListener(tableClicked);
        }
    }

    private View.OnClickListener tableClicked = view -> showTableDetails(view);


    private void showTableDetails(View v) {
        LayoutInflater li = LayoutInflater.from(getActivity());
        final View promptsView = li.inflate(R.layout.table_details_layout, null);

        // init details:
        final String tableTag = v.getTag().toString(); // get pressed table

        List<GuestClass> guestList = mPresenter.getClickedTableGuestsList(tableTag); // get table's guests list
        ArrayList<TableGuestsListClass> items = mPresenter.getGuestItem(guestList); // get guests details to show

        // table's id
        TextView tableID = promptsView.findViewById(R.id.table_id);
        tableID.setText(tableTag);

        // table's title
        TextView title = promptsView.findViewById(R.id.table_belog_group);
        String tableTitle = mPresenter.getTableTitle(tableTag); // get table's title
        title.setText("" + tableTitle);

        // table's number of guests
        TextView tableNumOfGuests = promptsView.findViewById(R.id.table_num_of_guest);
        int numOfGuests = mPresenter.getGuestsNumInTable(tableTag);
        String numOfSeats = mPresenter.getSpecificTableMaxSeat(Integer.parseInt(tableTag));
        tableNumOfGuests.setText("" + numOfGuests + "/" + numOfSeats);

        //table's guest list
        ListView tableGuestList = promptsView.findViewById(R.id.table_guest_list);
        TableGuestsListAdapter adapter = new TableGuestsListAdapter(items, getActivity());
        tableGuestList.setAdapter(adapter);


        final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
        alert.setCancelable(false);
        alert.setView(promptsView);

        final AlertDialog dialog = alert.create();

        Button table_details_positive_btn = promptsView.findViewById(R.id.ok);
        table_details_positive_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        Button table_details_negative_btn = promptsView.findViewById(R.id.seat_num);
        table_details_negative_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setMaxSeatDialog(tableTag);
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    @OnClick(R.id.startSeatingArrangement)
    public void createSeatingArrangement() {

        // show loader:
        mLoader.setVisibility(View.VISIBLE);
        AnimationDrawable animationDrawable = (AnimationDrawable) mLoader.getDrawable();
        animationDrawable.start();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mLoader.setVisibility(View.INVISIBLE);
            }
        }, 1000);

        mPresenter.startSeatingArrangement();
    }


    @OnClick(R.id.info_btn)
    public void onInfoClicked() { // show hall information
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle(R.string.str_info);
        alert.setMessage(R.string.str_info_msg);
        alert.setNegativeButton(R.string.str_ok, (dialog, which) -> dialog.dismiss());
        alert.show();
    }


    private void setAllTablesMaxSeat() { // change seats at all of the hall's tables
        // drop down adapter:
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.max_seat_all_tables));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSetMaxSeatAllTables.setAdapter(myAdapter);

        mSetMaxSeatAllTables.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                Object item = adapterView.getItemAtPosition(position);
                String numOfSeats = item.toString();
                mPresenter.setTableMaxSeat(item, position, numOfSeats);

                adapterView.setSelection(0); // reset selection
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }


    private void setMaxSeatDialog(final String tableTag) {// change seats at specific table
        LayoutInflater li = LayoutInflater.from(getActivity());
        final View promptsView = li.inflate(R.layout.set_max_seat_layout, null);

        promptsView.findViewById(R.id.textView);
        Spinner mSetMaxSeatSpecificTable = promptsView.findViewById(R.id.spinner);

        // drop down adapter:
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.max_seat));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSetMaxSeatSpecificTable.setAdapter(myAdapter);


        final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
        alert.setCancelable(false); // doesn't disappear by click out side.
        alert.setView(promptsView);

        final AlertDialog dialog = alert.create();

        // negative_btn is the cancel button.
        Button mCancelBtn = promptsView.findViewById(R.id.cancel);
        mCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss(); // close dialog;
            }
        });

        // positive_btn is the ok button.
        Button mOkBtn = promptsView.findViewById(R.id.ok);
        mOkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String numOfSeats = mSetMaxSeatSpecificTable.getSelectedItem().toString();

                mPresenter.changeSeats(numOfSeats, tableTag);

                dialog.dismiss(); // close dialog.
            }
        });

        dialog.show();
    }


    @Override
    public void initializeViews() {

    }

    @Override
    public void toastResetSeatingArrangement() {
        Toast.makeText(getActivity(), R.string.reset_seating_arrangement, Toast.LENGTH_LONG).show();
    }

    @Override
    public void setEnableSpecificTable(int position) {
        mTablesButtons[position].setEnabled(true);
    }

    @Override
    public void setDisableSpecificTableButton(int position) {
        mTablesButtons[position].setEnabled(false);
    }

    @Override
    public void toastTooManyGuest() {
        Toast.makeText(getContext(), R.string.too_many_coming_guests, Toast.LENGTH_LONG).show();
    }

    @Override
    public void toastListEmpty() {
        Toast.makeText(getContext(), R.string.empty_coming_guests, Toast.LENGTH_LONG).show();
    }

    @Override
    public void setSeatingArrangementButtonEnable() {
        mStartSeatingArrangement.setEnabled(true);
    }

    @Override
    public void setSeatingArrangementButtonDisable() {
        mStartSeatingArrangement.setEnabled(false);
    }

    @Override
    public void toastLongTable() {
        Toast.makeText(getActivity(), R.string.max_long_tables, Toast.LENGTH_LONG).show();
    }

    @Override
    public void toastNotEnoughSeats() {
        Toast.makeText(getContext(), R.string.not_enough_seats, Toast.LENGTH_LONG).show();
    }

}
