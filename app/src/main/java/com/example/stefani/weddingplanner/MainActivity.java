package com.example.stefani.weddingplanner;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Button mGuestsBtn;
    private Button mTasksBtn;
    private TextView mHeadline;
    private SharedPreferences mSharedPreferencesFile;
    private WeddingDetailsClass mWeddingDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSharedPreferencesFile = getBaseContext().getSharedPreferences(Constants.SHARED_PREFS, Context.MODE_PRIVATE);

        getWeddingDetailsFromPref();

        TextView main_wedding_details = (TextView) findViewById(R.id.main_wedding_details);
        main_wedding_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                weddingDetailsDialog();
            }
        });

        mHeadline = (TextView) findViewById(R.id.headline);
        addCoupleNames();

        mGuestsBtn = (Button) findViewById(R.id.guests_btn);
        startGuestsListActivity();

        mTasksBtn = (Button) findViewById(R.id.tasks_btn);
        startTasksActivity();
    }

    public void startGuestsListActivity(){
        mGuestsBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, GuestsListActivity.class);
                startActivity(intent);
            }
        });
    }

    public void startTasksActivity()
    {
        mTasksBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TasksListActivity.class);
                startActivity(intent);
            }
        });
    }

    private void addCoupleNames(){
        if(mWeddingDetails.getmBrideName() != "" && mWeddingDetails.getmBrideName() != "") {
            mHeadline.setVisibility(View.VISIBLE);
            mHeadline.setText(mWeddingDetails.getmBrideName() + " & " + mWeddingDetails.getmGroomName());
        }
        else{
            mHeadline.setVisibility(View.INVISIBLE);
        }
    }


    private void getWeddingDetailsFromPref(){
        mWeddingDetails = new WeddingDetailsClass();

        String groomName = mSharedPreferencesFile.getString("Groom_Name", "");
        mWeddingDetails.setmGroomName(groomName);

        String brideName = mSharedPreferencesFile.getString("Bride_Name", "");
        mWeddingDetails.setmBrideName(brideName);

        String weddingHall = mSharedPreferencesFile.getString("Wedding_Hall", "");
        mWeddingDetails.setmWeddingHall(weddingHall);

        String address = mSharedPreferencesFile.getString("Address", "");
        mWeddingDetails.setmAddress(address);

        String date = mSharedPreferencesFile.getString("Date", "");
        mWeddingDetails.setmDate(date);

        String time = mSharedPreferencesFile.getString("Time", "");
        mWeddingDetails.setmTime(time);


    }


    private void weddingDetailsDialog(){

        getWeddingDetailsFromPref();

        LayoutInflater li = LayoutInflater.from(MainActivity.this);
        final View promptsView = li.inflate(R.layout.wedding_details_layout, null);

        final AlertDialog.Builder alert = new AlertDialog.Builder(this, R.style.AlertDialogTheme);
        alert.setCancelable(false);
        alert.setView(promptsView);

        final AlertDialog dialog = alert.create();

        final EditText wedding_details_groom_name = (EditText) promptsView.findViewById(R.id.wedding_details_groom_name);
        wedding_details_groom_name.setText(mWeddingDetails.getmGroomName());

        final EditText wedding_details_bride_name = (EditText) promptsView.findViewById(R.id.wedding_details_bride_name);
        wedding_details_bride_name.setText(mWeddingDetails.getmBrideName());

        final EditText wedding_details_wed_hall = (EditText) promptsView.findViewById(R.id.wedding_details_wed_hall);
        wedding_details_wed_hall.setText(mWeddingDetails.getmWeddingHall());

        final EditText wedding_details_address = (EditText) promptsView.findViewById(R.id.wedding_details_address);
        wedding_details_address.setText(mWeddingDetails.getmAddress());

        final EditText wedding_details_date = (EditText) promptsView.findViewById(R.id.wedding_details_date);
        wedding_details_date.setText(mWeddingDetails.getmDate());

        final EditText wedding_details_time = (EditText) promptsView.findViewById(R.id.wedding_details_time);
        wedding_details_time.setText(mWeddingDetails.getmTime());

        messageFields[0] = mWeddingDetails.getmGroomName();
        messageFields[1] = mWeddingDetails.getmBrideName();
        messageFields[2] = mWeddingDetails.getmWeddingHall();
        messageFields[3] = mWeddingDetails.getmAddress();
        messageFields[4] = mWeddingDetails.getmDate();
        messageFields[5] = mWeddingDetails.getmTime();
        creatingInvitationMessage( messageFields);

        Button wedding_details_negative_btn = (Button)promptsView.findViewById(R.id.wedding_details_negative_btn);
        wedding_details_negative_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        Button wedding_details_positive_btn = (Button)promptsView.findViewById(R.id.wedding_details_positive_btn);
        wedding_details_positive_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = mSharedPreferencesFile.edit();

                String str_Wedding_details_groom_name = wedding_details_groom_name.getText().toString();
                editor.putString("Groom_Name", str_Wedding_details_groom_name);

                String str_Wedding_details_bride_name = wedding_details_bride_name.getText().toString();
                editor.putString("Bride_Name", str_Wedding_details_bride_name);

                String str_Wedding_details_wed_hall = wedding_details_wed_hall.getText().toString();
                editor.putString("Wedding_Hall", str_Wedding_details_wed_hall);

                String str_Wedding_details_address = wedding_details_address.getText().toString();
                editor.putString("Address", str_Wedding_details_address);

                String str_Wedding_details_date = wedding_details_date.getText().toString();
                editor.putString("Date", str_Wedding_details_date);

                String str_Wedding_details_time = wedding_details_time.getText().toString();
                editor.putString("Time", str_Wedding_details_time);
                editor.apply();

                messageFields[0] = str_Wedding_details_groom_name;
                messageFields[1] = str_Wedding_details_bride_name;
                messageFields[2] = str_Wedding_details_wed_hall;
                messageFields[3] = str_Wedding_details_address;
                messageFields[4] = str_Wedding_details_date;
                messageFields[5] = str_Wedding_details_time;

                creatingInvitationMessage( messageFields);


                dialog.dismiss();
            }
        });

/*        alert.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialog.dismiss();
            }
        });

        alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                //  EditText wedding_details_bride_name = (EditText) promptsView.findViewById(R.id.wedding_details_bride_name);
                String strWedding_details_bride_name = wedding_details_bride_name.getText().toString();

                SharedPreferences.Editor editor = mSharedPreferencesFile.edit();
                editor.putString("Bride_Name", strWedding_details_bride_name);


                editor.commit();
            }
        });*/

        dialog.show();
    }

    private String[] messageFields = new String[6];
    private void creatingInvitationMessage(String[] messageFields){
        String smsMessage = "Hello, You were invited to " + messageFields[1] + " and " + messageFields[0] + "'s " +
                "wedding.\n The wedding will take place on " + messageFields[4] + " at " + messageFields[5] + " at the "+
                messageFields[2] + ". To confirm your arrival, please text back the number of people to arrive. 0 if you do not arrive.";


        SharedPreferences.Editor editor = mSharedPreferencesFile.edit();
        editor.putString(Constants.SMS_MESSAGE, smsMessage);
        editor.apply();
    }

}
