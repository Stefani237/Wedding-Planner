package com.example.stefani.weddingplanner.Fragments.guestDetails;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stefani.weddingplanner.R;
import com.example.stefani.weddingplanner.BasicClasses.GuestClass;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class GuestDetailsFragment extends Fragment implements IGuestDetails.View {


    @BindView(R.id.guest_details_name)
    EditText mGuestDetailsName;
    @BindView(R.id.guest_details_phone)
    EditText mGuestDetailsPhone;
    @BindView(R.id.radio_group_coming)
    RadioGroup mGuestDetailsIsComing;
    @BindView(R.id.radio_btn_coming)
    RadioButton mGuestComing;
    @BindView(R.id.radio_btn_not_coming)
    RadioButton mGuestNotComing;
    @BindView(R.id.guest_details_number_of_guest)
    EditText mGuestDetailsNumberOfGuest;
    @BindView(R.id.belongGroupField)
    Spinner mGuestDetailsBelongingGroup;
    @BindView(R.id.radio_group_side)
    RadioGroup mGuestDetailsSide;
    @BindView(R.id.radio_btn_bride)
    RadioButton mBrideSide;
    @BindView(R.id.radio_btn_groom)
    RadioButton mGroomSide;
    @BindView(R.id.guest_details_save_data)
    Button mGuestDetailsSaveData;
    @BindView(R.id.guest_details_edit)
    Button mGuestDetailsEdit;
    @BindView(R.id.guest_details_empty_fields)
    TextView mEmptyFields;
    @BindView(R.id.exist_guest)
    TextView mExistGuests;


    private GuestClass mGuestDetails;
    private IGuestDetails.Presenter mPresenter;
    private View rootView;

    public void setGuestDetails(GuestClass guestDetails) {
        // set selected guest's details
        this.mGuestDetails = guestDetails;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = inflater.inflate(R.layout.guest_details_layout, container, false);
        ButterKnife.bind(this, rootView);

        mPresenter = new GuestDetailsPresenter(this);
        mPresenter.setGuestClass(mGuestDetails);
        mPresenter.initializeViews();
        mPresenter.setGuestDetails();
        Toast.makeText(getContext(), R.string.edit_mode, Toast.LENGTH_SHORT).show();

        return rootView;
    }


    @OnClick(R.id.guest_details_edit)
    public void setEditTextClick() {
        // enable editing text fields
        mGuestDetailsName.setEnabled(true);
        mGuestDetailsPhone.setEnabled(true);
        mGuestDetailsIsComing.setEnabled(true);
        mGuestDetailsNumberOfGuest.setEnabled(true);
        mGuestDetailsBelongingGroup.setEnabled(true);
        mGuestDetailsSide.setEnabled(true);
        mGuestDetailsSaveData.setEnabled(true);
    }


    @OnClick(R.id.guest_details_save_data)
    public void saveButtonClicked() {

        saveGuestNewData();
    }


    private void saveGuestNewData() {
        // get data from text fields
        String guestName = mGuestDetailsName.getText().toString().trim();
        String guestPhone = mGuestDetailsPhone.getText().toString().trim();
        String guestIsComing = getSelectedRadioButton(mGuestDetailsIsComing).getText().toString();
        String guestNumOfGuest = mGuestDetailsNumberOfGuest.getText().toString();
        String guestBelong = mGuestDetailsBelongingGroup.getSelectedItem().toString();
        String guestSide = getSelectedRadioButton(mGuestDetailsSide).getText().toString();
        mPresenter.saveGuestData(guestName, guestPhone, guestIsComing, guestNumOfGuest,
                guestBelong, guestSide);
    }


    private void createDropDown() {
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.belong_group_arr));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mGuestDetailsBelongingGroup.setAdapter(myAdapter);
    }

    // get selected radio button.
    private RadioButton getSelectedRadioButton(RadioGroup group) {
        int selected = group.getCheckedRadioButtonId();
        return (RadioButton) rootView.findViewById(selected);
    }

    @Override
    public void initializeViews() {
        createDropDown();
    }

    @Override
    public void setGuestFullName(String guestName) {
        mGuestDetailsName.setText(guestName);
    }

    @Override
    public void setGuestPhone(String guestPhone) {
        mGuestDetailsPhone.setText(guestPhone);
    }

    @Override
    public void setGuestComing(boolean isComing) {
        mGuestComing.setChecked(isComing);
    }

    @Override
    public void setGuestNotComing(boolean isNotComing) {
        mGuestNotComing.setChecked(isNotComing);
    }

    @Override
    public void setNumberOfGuest(String numberOfGuest) {
        mGuestDetailsNumberOfGuest.setText(numberOfGuest);
    }

    @Override
    public void setBelongingGroup(int selection) {
        mGuestDetailsBelongingGroup.setSelection(selection);
    }

    @Override
    public void setBrideSide(boolean isBrideSide) {
        mBrideSide.setChecked(isBrideSide);
    }

    @Override
    public void setGroomSide(boolean isGroomSide) {
        mGroomSide.setChecked(isGroomSide);
    }

    @Override
    public void existGuestSetVisibility(int visible) {
        // show label if guest is already exists
        mExistGuests.setVisibility(visible);
    }

    @Override
    public void emptyGuestFieldsSetVisibility(int visible) {
        // show label if user is trying to save without filling all fields
        mEmptyFields.setVisibility(visible);
    }

    @Override
    public void toastGuestDetailsSaved() {
        Toast.makeText(getContext(), R.string.str_changes_saved, Toast.LENGTH_SHORT).show();
    }
}
