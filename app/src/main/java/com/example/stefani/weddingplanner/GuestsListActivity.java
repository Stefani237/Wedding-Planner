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
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class GuestsListActivity extends AppCompatActivity {

    private ListView mListView;
    private Button mAddBtn;
    private Button send_sms_btn;
    private static final int PERMISSION_SEND_SMS = 1;

    private BroadcastReceiver intentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String smsFromOutsideUser = intent.getExtras().getString("sms");
            System.out.println("Stef smsFromOutsideUser =" + smsFromOutsideUser);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guests_list);

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


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://weddingplanner-61cad.firebaseio.com/Guests");

        FirebaseListAdapter<Object> firbaseListAdaper = new FirebaseListAdapter<Object>(
                this,
                Object.class,
                R.layout.guest_list_items,
                databaseReference
        ) {
            @Override
            protected void populateView(View v, Object object, int position) {

                HashMap<String, String> guestHashMap =  (HashMap<String, String>)object;
                GuestListClass.mGuestCounter++;
                TextView guest_list_fullname = (TextView) v.findViewById(R.id.guest_list_fullname);
                guest_list_fullname.setText(guestHashMap.get("mFullName"));
                guest_list_fullname.setTag(guestHashMap.get("mID"));

                TextView guest_list_phoneNumber = (TextView) v.findViewById(R.id.guest_list_phoneNumber);
                guest_list_phoneNumber.setText(guestHashMap.get("mPhone"));



       /*         for (Map.Entry<String,String> entry : guestHashMap.entrySet()) {
                    String model= entry.getValue();
                    System.out.println("Stef populateView");
                    String guestName = model.getmFullName();
                    TextView guest_list_fullname = (TextView) v.findViewById(R.id.guest_list_fullname);
                    guest_list_fullname.setText(guestName);

                    String guestPhone = model.getmPhone();
                    TextView guest_list_phoneNumber = (TextView) v.findViewById(R.id.guest_list_phoneNumber);
                    guest_list_phoneNumber.setText(guestPhone);

                    int guestNumOfInvited = model.getmNumOfInvited();
                    TextView guest_list_numberOfInvited = (TextView) v.findViewById(R.id.guest_list_numberOfInvited);
                    guest_list_numberOfInvited.setText(guestNumOfInvited);
                    System.out.printf("%s -> %s%n", entry.getKey(), entry.getValue());
                }*/

            }
        };
        mListView.setAdapter(firbaseListAdaper);

        addNewGuest();
        longPressDialog();
    }

    private void sendSMS() {

        String SENT = "Message send";
        String DELIVERED = "Message delivered";
        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,new Intent(SENT),0);
        PendingIntent deliveryPI = PendingIntent.getBroadcast(this, 0,new Intent(DELIVERED),0);


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
        }, new IntentFilter(SENT));

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage("+972542095924",null,"Hello it's your sms", sentPI, deliveryPI);
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

}
