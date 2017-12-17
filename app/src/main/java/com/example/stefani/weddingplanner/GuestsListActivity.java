package com.example.stefani.weddingplanner;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GuestsListActivity extends AppCompatActivity {

    private ListView mListView;
    private Button mAddBtn;
    private Button send_sms_btn;
    private static final int PERMISSION_SEND_SMS = 1;
    private SharedPreferences mSharedPreferencesFile;
    private IntentFilter intentFilter;
    private Spinner mSortList;

    private BroadcastReceiver intentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String smsFromOutsideUser = intent.getExtras().getString("sms");

            TextView sms_message = (TextView)findViewById(R.id.sms_message);
            sms_message.setText(smsFromOutsideUser);
            String segments[] = smsFromOutsideUser.split(":");
            String smsReplay = segments[2].trim(); //sms
            String phoneNumber = segments[1].trim();

            try { // message view From:+972542095929:2
                Integer numberOfInvited = Integer.valueOf(smsReplay);
                if(numberOfInvited < 10 && numberOfInvited >= 0){
                    // update list with number and that arriving
                    // 0 - not ariving
                    //1 - arriving
                    updateGuestList(phoneNumber, numberOfInvited);
                }else{
                    wrongMessageFromInvited();
                    //popup bed response
                }

            }catch(Exception e){
                wrongMessageFromInvited();
            }
        }
    };


    final List<GuestClass> guestClassList = new ArrayList<>();
    private SmsManager sms;

    private void updateGuestList(String phoneNumber, Integer numberOfGuests) {
        // find guest with same phone number
        // get his id
        // change number of invited in DB

        System.out.println("Stef phoneNumber = "  +phoneNumber);
        System.out.println("Stef numberOfGuests = "  +numberOfGuests);

        //1555521 5556
        for (GuestClass guest: guestClassList) {
            if(guest.getmPhone().equalsIgnoreCase(phoneNumber) ){
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl(Constants.DB_URL);
                databaseReference.child(guest.getmID()).child("mNumOfGuest").setValue(numberOfGuests);
                if(numberOfGuests > 0)
                    databaseReference.child(guest.getmID()).child("mIsComing").setValue(true);
                else if(numberOfGuests == 0){
                    databaseReference.child(guest.getmID()).child("mIsComing").setValue(false);
                }
                return;
            }
        }

    }

    private void wrongMessageFromInvited(){
        LayoutInflater li = LayoutInflater.from(GuestsListActivity.this);
        final View promptsView = li.inflate(R.layout.sms_error_message, null);
        final AlertDialog.Builder alert = new AlertDialog.Builder(this, R.style.AlertDialogTheme);
        alert.setCancelable(false);
        alert.setView(promptsView);

        final AlertDialog dialog = alert.create();
        Button error_msg_sms = (Button)promptsView.findViewById(R.id.error_msg_sms);
        error_msg_sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case PERMISSION_SEND_SMS:
                if(grantResults != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    sendSMS();
                }
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guests_list);

        mSharedPreferencesFile = getBaseContext().getSharedPreferences(Constants.SHARED_PREFS, Context.MODE_PRIVATE);

        intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.SMS_RECEIVED_ACTION);

        mSortList = (Spinner) findViewById(R.id.guest_sort);

        mListView = (ListView) findViewById(R.id.guestsListView);
        mListView.setLongClickable(true);

        mAddBtn = (Button) findViewById(R.id.addGuest);
        send_sms_btn = (Button)findViewById(R.id.send_sms_btn);
        send_sms_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ActivityCompat.checkSelfPermission(GuestsListActivity.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(GuestsListActivity.this,new String[]{Manifest.permission.SEND_SMS}, PERMISSION_SEND_SMS);
                }else{
                    sendSMS();
                }
            }
        });




        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl(Constants.DB_URL);

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {



                GuestClass username = dataSnapshot.getValue(GuestClass.class);


                GuestClass guestClass = new GuestClass();
                guestClass.setmFullName(username.getmFullName());
                guestClass.setmID(username.getmID());
                guestClass.setmPhone(username.getmPhone());

                guestClassList.add(guestClass);
                System.out.println("Stef guestClassList =" + guestClassList.size());

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                GuestClass username = dataSnapshot.getValue(GuestClass.class);
                for (GuestClass guest: guestClassList) {
                    if(guest.getmPhone().equalsIgnoreCase(username.getmPhone())){
                        guest.setmNumOfGuest(username.getmNumOfGuest());
                        guest.setmIsComing(username.ismIsComing());
                        return;
                    }
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        FirebaseListAdapter<Object> firbaseListAdaper = new FirebaseListAdapter<Object>(
                this,
                Object.class,
                R.layout.guest_list_items,
                databaseReference
        ) {
            @Override
            protected void populateView(View v, Object object, int position) {

                HashMap<String, String> guestHashMap =  (HashMap<String, String>)object;

                TextView guest_list_fullname =  v.findViewById(R.id.guest_list_fullname);
                guest_list_fullname.setText(guestHashMap.get("mFullName"));
                guest_list_fullname.setTag(guestHashMap.get("mID"));

                TextView guest_list_phoneNumber =  v.findViewById(R.id.guest_list_phoneNumber);
                guest_list_phoneNumber.setText(guestHashMap.get("mPhone"));
                GuestListClass.mGuestCounter++;

            }
        };



        mListView.setAdapter(firbaseListAdaper);

        addNewGuest();
        longPressDialog();
        createDropDown();

        //050-3844575
     //   updateGuestList("050-3844575", 2);
    }

    private void sendSMS() {
        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,new Intent(Constants.MESSAGE_SEND),0);
        PendingIntent deliveryPI = PendingIntent.getBroadcast(this, 0,new Intent(Constants.MESSAGE_DELIVERED),0);


/*        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                 switch (getResultCode()){
                     case Activity.RESULT_OK:
                         System.out.println("Stef OK");
                         break;
                     case Activity.RESULT_CANCELED:
                         System.out.println("Stef CANCEL");
                         break;
                 }
            }
        }, new IntentFilter(Constants.MESSAGE_SEND));


        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()){
                    case Activity.RESULT_OK:
                        System.out.println("Stef OK");
                        break;
                    case Activity.RESULT_CANCELED:
                        System.out.println("Stef CANCEL");
                        break;
                }
            }
        }, new IntentFilter(Constants.MESSAGE_DELIVERED));*/

        String smsMessage = mSharedPreferencesFile.getString(Constants.SMS_MESSAGE, "");
        sms = SmsManager.getDefault();
        for (GuestClass guest: guestClassList ) {
            if(!guest.ismIsComing())
                System.out.println("Stef guest.getmPhone() =" + guest.getmPhone());
                sms.sendTextMessage(guest.getmPhone(),null,"Hello", sentPI, deliveryPI);
        }

    }


    public void addNewGuest(){
        mAddBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GuestsListActivity.this, NewGuestActivity.class);
                startActivity(intent);
            }
        });
    }

    public void longPressDialog(){
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(final AdapterView<?> parent,final View v, final int position, long id) {

                AlertDialog.Builder alert = new AlertDialog.Builder(
                        GuestsListActivity.this);
                alert.setTitle("Confirm delete");
                alert.setMessage("Are you sure you want to delete the guest from the invite list?");
                alert.setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        TextView guest_list_fullname = (TextView) v.findViewById(R.id.guest_list_fullname);
                        String guestID = guest_list_fullname.getTag().toString();
                        FirebaseDatabase.getInstance().getReference("Guests").child(guestID).removeValue();

                   // GuestListClass.removeGuest(position);


                    dialog.dismiss();

                    }
                });
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                alert.show();

                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        registerReceiver(intentReceiver, intentFilter);
        super.onResume();
    }

    @Override
    protected void onPause() {
        System.out.println("Stef onPause");
        unregisterReceiver(intentReceiver);
        super.onPause();
    }

    public void createDropDown(){
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(GuestsListActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.guest_sort_arr));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSortList.setAdapter(myAdapter);
    }
}
