package com.example.stefani.weddingplanner.Fragments.guestNew;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
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

import com.example.stefani.weddingplanner.BasicClasses.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GuestNewFragment extends Fragment implements IGuestNew.View {

    @BindView(R.id.fullNameField)
    EditText mFullNameField;
    @BindView(R.id.phoneNumberField)
    EditText mPhoneNumberField;
    @BindView(R.id.NumOfInvitedField)
    EditText mNumOfInvited;
    @BindView(R.id.radio_group)
    RadioGroup mRadioGroup;
    @BindView(R.id.belongGroupFieldNewGuest)
    Spinner mBelongGroupField;
    @BindView(R.id.addToGuestDB)
    Button mAddButton;
    @BindView(R.id.addFromContacts)
    Button addFromContacts;
    @BindView(R.id.new_task_empty_fields)
    TextView mEmptyFields;
    @BindView(R.id.exist_guest)
    TextView mExistGuest;


    private String cNumber;
    private View rootView;
    private IGuestNew.Presenter mPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_new_guest, container, false);
        ButterKnife.bind(this, rootView);

        mPresenter = new GuestNewPresenter(this);

        return rootView;
    }


    // get permission from the user to access contact list.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Constants.PERMISSION_CONTACT: // get answer to request
                if (grantResults != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) { // access approved
                    addGuestFromContactList();
                }
                else { // access denied
                    Toast.makeText(getContext(), R.string.str_no_access_to_contacts, Toast.LENGTH_LONG).show();
                    return;
                }
        }
    }


    @OnClick(R.id.addFromContacts)
    public void onContactsClick() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            // if not approved before, ask for permission
            //ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_CONTACTS}, Constants.PERMISSION_CONTACT);
        this.requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, Constants.PERMISSION_CONTACT);
        } else {
            // if already approved before
            addGuestFromContactList();
        }
    }


    private void addGuestFromContactList() {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI); // get selected contact
        startActivityForResult(intent, Constants.PICK_CONTACT); // getting a result from an activity
    }

    // return if successfully moved from one activity to another. needed when activity returns results = data.
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (Constants.PICK_CONTACT):
                if (resultCode == Activity.RESULT_OK) { // if data received in the activity
                    Uri contactData = data.getData(); // get the data
                    Cursor c = getActivity().managedQuery(contactData, null, null, null, null); // create cursor to read data. the nulls means all data without filters.
                    if (c.moveToFirst()) { // move the cursor to the first row (return false if the cursor is empty)
                        String id = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID)); // returns the index for the given column name or throws exception
                        String hasPhone = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)); // return 1 there is at least one phone number for this contact

                        if (hasPhone.equalsIgnoreCase("1")) {
                            Cursor phones = getActivity().getContentResolver().query(   // retrieves set of phones for a specific contact (according to id)
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id,
                                    null, null);
                            phones.moveToFirst();
                            cNumber = phones.getString(phones.getColumnIndex("data1")); // get first number
                        }
                        String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)); // return contact name

                        mFullNameField.setText(name);
                        mPhoneNumberField.setText(cNumber);
                    }
                }
                break;
        }
    }

    @Override
    public void clearFields() {
        mFullNameField.setText("");
        mPhoneNumberField.setText("");
        mNumOfInvited.setText("");
    }


    @OnClick(R.id.addToGuestDB)
    public void addNewGuestClick() {
        // get values from edit fields:
        String name = mFullNameField.getText().toString().trim();
        String phone = mPhoneNumberField.getText().toString().trim();
        String numInvited = mNumOfInvited.getText().toString();
        String side = getSelectedRadioButton().getText().toString();
        String group = mBelongGroupField.getSelectedItem().toString();


        mPresenter.addNewGuest(name, phone, numInvited, side, group);
    }


    private void createDropDown() {
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.belong_group_arr));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBelongGroupField.setAdapter(myAdapter);
    }

    // get selected radio button.
    private RadioButton getSelectedRadioButton() {
        int selected = mRadioGroup.getCheckedRadioButtonId();
        return (RadioButton) rootView.findViewById(selected);
    }


    @Override
    public void initializeViews() {
        createDropDown();
    }

    @Override
    public void toastGuestAddedSuccessfully() {
        Toast.makeText(getContext(), R.string.str_added_guest, Toast.LENGTH_LONG).show();
    }

    @Override
    public void existGuestSetVisibility(int visible) {
        mExistGuest.setVisibility(visible);
    }

    @Override
    public void emptyFieldsSetVisibility(int visible) {
        mEmptyFields.setVisibility(visible);
    }
}
