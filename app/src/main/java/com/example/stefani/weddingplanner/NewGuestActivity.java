package com.example.stefani.weddingplanner;

import android.content.Intent;
import android.icu.text.StringPrepParseException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.firebase.client.Firebase;

public class NewGuestActivity extends AppCompatActivity {

    private EditText mFullNameField;
    private EditText mPhoneNumberField;
    private EditText mNumOfInvited;
    private RadioGroup mRadioGroup;
    private RadioButton mSideField;
    private Spinner mBelongGroupField;
    private Button mAddButton;
    private Button mCancelButton;

    private Firebase mRootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_guest);

        mFullNameField = (EditText) findViewById(R.id.fullNameField);
        mPhoneNumberField = (EditText) findViewById(R.id.phoneNumberField);
        mNumOfInvited = (EditText) findViewById(R.id.NumOfInvitedField);
        mRadioGroup = (RadioGroup) findViewById(R.id.radio_group);
        mBelongGroupField = (Spinner) findViewById(R.id.belongGroupField);
        mAddButton = (Button) findViewById(R.id.addToDB);
        mCancelButton = (Button) findViewById(R.id.cancelDB);

       // Log.e("Im in NewGuestActivity", "------------------");
        mRootRef = new Firebase("https://weddingplanner-61cad.firebaseio.com/Guests");
        addNewGuest();
        cancelAddingDB();
        createDropDown();
    }

    public void clearFields(){
        mFullNameField.setText("");
        mPhoneNumberField.setText("");
        mNumOfInvited.setText("");
        mSideField.setText("");
        //mBelongGroupField.setText("");
    }


    public void addNewGuest() {
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = mFullNameField.getText().toString();
                String phone = mPhoneNumberField.getText().toString();
                String numInvited = mNumOfInvited.getText().toString();
                String side = mSideField.getText().toString();
                String group = mBelongGroupField.getSelectedItem().toString();
             //   String numOfInvited = mNumOfInvitedField.getText().toString();


                GuestClass guest = new GuestClass(""+ ++GuestListClass.mGuestCounter,name, phone, Integer.valueOf(numInvited), side, group);
              //  guest.setmFullName(name);
               // guest.setmPhone(phone);
              //  guest.setmNumOfInvited(Integer.valueOf(numOfInvited));

                GuestListClass.addGuest(guest);

                Firebase userRef = mRootRef.child(""+GuestListClass.mGuestCounter);
                userRef.setValue(guest);

                clearFields();
            }
        });
    }

    public void cancelAddingDB(){
        mCancelButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewGuestActivity.this, GuestsListActivity.class);
                startActivity(intent);
            }
        });
    }

    public void createDropDown(){
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(NewGuestActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.belong_group));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBelongGroupField.setAdapter(myAdapter);
    }

    public void getSelectedRadioButton(View v){
        int selected = mRadioGroup.getCheckedRadioButtonId();
        mSideField = (RadioButton) findViewById(selected);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        GuestListClass.mGuestCounter = 0;
        System.out.println("Stef onBack");
    }
}
