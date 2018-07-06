package com.example.stefani.weddingplanner;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;

import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.stefani.weddingplanner.Helpers.SharedPreferencesHelper;
import com.example.stefani.weddingplanner.BasicClasses.Constants;
import com.example.stefani.weddingplanner.BasicClasses.WeddingDetailsClass;

import java.util.Locale;


public class WeddingNotificationDetails {
    private String[] mMessageFields = new String[Constants.SMS_FIELDS_NUM];
    private WeddingDetailsClass mWeddingDetails;
    private EditText weddingDetailsDate;
    private EditText weddingDetailsTime;
    private Calendar myCalendar = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }

    };


    private void onDateClicked(Context context) { // show calendar
        weddingDetailsDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(context, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void updateLabel() {

        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        weddingDetailsDate.setText(sdf.format(myCalendar.getTime()));
        mWeddingDetails.setmDate(sdf.format(myCalendar.getTime()));
    }

    private void onTimeClicked(Context context) { // show clock
        weddingDetailsTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String time = hourOfDay + ":" + minute;
                        weddingDetailsTime.setText(time);
                        mWeddingDetails.setmTime(time);
                    }
                }, 0, 0, true);
                timePickerDialog.show();
            }
        });
    }

    private void getWeddingDetailsFromPref() {
        mWeddingDetails = new WeddingDetailsClass();

        // Read each value in file with the key and save it into the WeddingDetailsClass object:
        String groomName = SharedPreferencesHelper.getInstance().getPreferenceString(Constants.PREF_GROOM_NAME);
        mWeddingDetails.setmGroomName(groomName);

        String brideName = SharedPreferencesHelper.getInstance().getPreferenceString(Constants.PREF_BRIDE_NAME);
        mWeddingDetails.setmBrideName(brideName);

        String weddingHall = SharedPreferencesHelper.getInstance().getPreferenceString(Constants.PREF_WEDDING_HALL);
        mWeddingDetails.setmWeddingHall(weddingHall);

        String address = SharedPreferencesHelper.getInstance().getPreferenceString(Constants.PREF_ADDRESS);
        mWeddingDetails.setmAddress(address);

        String date = SharedPreferencesHelper.getInstance().getPreferenceString(Constants.PREF_DATE);
        mWeddingDetails.setmDate(date);

        String time = SharedPreferencesHelper.getInstance().getPreferenceString(Constants.PREF_TIME);
        mWeddingDetails.setmTime(time);
    }


    public void weddingDetailsDialog(Context context) {
        getWeddingDetailsFromPref();

        // LayoutInflater convert XML into view object in code.
        LayoutInflater li = LayoutInflater.from(context);

        // R.layout.wedding_details_layout - the XML layout file we want to inflate (convert).
        // the second value (optional)- a layout that would use as a parent for the promptsView in the ViewHierarchy.
        final View promptsView = li.inflate(R.layout.wedding_details_layout, null);

        final AlertDialog.Builder alert = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
        alert.setCancelable(false); // doesn't disappear by click out side.
        alert.setView(promptsView);

        final AlertDialog dialog = alert.create();

        // show dialog with data from pref:
        final EditText weddingDetailsGroomName = promptsView.findViewById(R.id.wedding_details_groom_name);
        weddingDetailsGroomName.setText(mWeddingDetails.getmGroomName());

        final EditText weddingDetailsBrideName = promptsView.findViewById(R.id.wedding_details_bride_name);
        weddingDetailsBrideName.setText(mWeddingDetails.getmBrideName());

        final EditText weddingDetailsWedHall = promptsView.findViewById(R.id.wedding_details_wed_hall);
        weddingDetailsWedHall.setText(mWeddingDetails.getmWeddingHall());

        final EditText weddingDetailsAddress = promptsView.findViewById(R.id.wedding_details_address);
        weddingDetailsAddress.setText(mWeddingDetails.getmAddress());

        weddingDetailsDate = promptsView.findViewById(R.id.wedding_details_date);
        weddingDetailsDate.setText(mWeddingDetails.getmDate());
        onDateClicked(context);

        weddingDetailsTime = promptsView.findViewById(R.id.wedding_details_time);
        weddingDetailsTime.setText(mWeddingDetails.getmTime());
        onTimeClicked(context);

        final TextView emptyFields = promptsView.findViewById(R.id.new_task_empty_fields);
        emptyFields.setVisibility(View.INVISIBLE);

       /* // save each wedding detail in mMessageFields array:
        mMessageFields[0] = mWeddingDetails.getmGroomName();
        mMessageFields[1] = mWeddingDetails.getmBrideName();
        mMessageFields[2] = mWeddingDetails.getmWeddingHall();
        mMessageFields[3] = mWeddingDetails.getmAddress();
        mMessageFields[4] = mWeddingDetails.getmDate();
        mMessageFields[5] = mWeddingDetails.getmTime();
        creatingInvitationMessage();*/

        // negative_btn is the cancel button.
        Button wedding_details_negative_btn = promptsView.findViewById(R.id.wedding_details_negative_btn);
        wedding_details_negative_btn.setOnClickListener(view -> {
            dialog.dismiss(); // close dialog;
        });

        // positive_btn is the ok button.
        Button wedding_details_positive_btn = (Button) promptsView.findViewById(R.id.wedding_details_positive_btn);
        wedding_details_positive_btn.setOnClickListener(view -> {


            // get new values from textFields:
            String str_Wedding_details_groom_name = weddingDetailsGroomName.getText().toString().trim();
            String str_Wedding_details_bride_name = weddingDetailsBrideName.getText().toString().trim();
            String str_Wedding_details_wed_hall = weddingDetailsWedHall.getText().toString().trim();
            String str_Wedding_details_address = weddingDetailsAddress.getText().toString().trim();
            String str_Wedding_details_date = weddingDetailsDate.getText().toString();
            String str_Wedding_details_time = weddingDetailsTime.getText().toString();

            // if all fields are not empty:
            if (str_Wedding_details_groom_name.length() > 0 && str_Wedding_details_bride_name.length() > 0 &&
                    str_Wedding_details_wed_hall.length() > 0 && str_Wedding_details_address.length() > 0 &&
                    str_Wedding_details_date.length() > 0 && str_Wedding_details_time.length() > 0) {

                // save new values to SharedPref
                SharedPreferencesHelper.getInstance().writePreference(Constants.PREF_GROOM_NAME, str_Wedding_details_groom_name);
                SharedPreferencesHelper.getInstance().writePreference(Constants.PREF_BRIDE_NAME, str_Wedding_details_bride_name);
                SharedPreferencesHelper.getInstance().writePreference(Constants.PREF_WEDDING_HALL, str_Wedding_details_wed_hall);
                SharedPreferencesHelper.getInstance().writePreference(Constants.PREF_ADDRESS, str_Wedding_details_address);
                SharedPreferencesHelper.getInstance().writePreference(Constants.PREF_DATE, str_Wedding_details_date);
                SharedPreferencesHelper.getInstance().writePreference(Constants.PREF_TIME, str_Wedding_details_time);

                // save new values to mMessageFields
                mMessageFields[0] = str_Wedding_details_groom_name;
                mMessageFields[1] = str_Wedding_details_bride_name;
                mMessageFields[2] = str_Wedding_details_wed_hall;
                mMessageFields[3] = str_Wedding_details_address;
                mMessageFields[4] = str_Wedding_details_date;
                mMessageFields[5] = str_Wedding_details_time;

                creatingInvitationMessage();

                dialog.dismiss(); // close dialog.
            } else {
                emptyFields.setVisibility(View.VISIBLE);
            }

        });

        dialog.show();
    }


    private void creatingInvitationMessage() {
        // creates an SMS for arrival confirmation
        String smsMessage = "Hello, You were invited to " + mMessageFields[1] + " and " + mMessageFields[0] + "'s wedding. "
                + "The wedding will take place on " + mMessageFields[4] + " at " + mMessageFields[5] + " at the " +
                mMessageFields[2] + ". To confirm your arrival, please text back the number of people to arrive. 0 if you do not arrive.";

        // saves the massage in Shared Preferences File:
        SharedPreferencesHelper.getInstance().writePreference(Constants.SMS_MESSAGE, smsMessage);
    }

}
