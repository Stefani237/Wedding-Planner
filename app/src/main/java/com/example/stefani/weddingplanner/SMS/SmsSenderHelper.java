package com.example.stefani.weddingplanner.SMS;

import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.telephony.SmsManager;

import com.example.stefani.weddingplanner.R;
import com.example.stefani.weddingplanner.Helpers.SharedPreferencesHelper;
import com.example.stefani.weddingplanner.BasicClasses.Constants;
import com.example.stefani.weddingplanner.BasicClasses.GuestClass;

import java.util.ArrayList;
import java.util.List;


public class SmsSenderHelper {

    private Context mContext;

    public SmsSenderHelper(Context context) {
        mContext = context;
    }

    public int sendSMS(List<GuestClass> guestClassList) {

        // Retrieve a PendingIntent that will perform a broadcast
        PendingIntent sentPI = PendingIntent.getBroadcast(mContext, 0, new Intent(Constants.MESSAGE_SEND), 0);
        PendingIntent deliveryPI = PendingIntent.getBroadcast(mContext, 0, new Intent(Constants.MESSAGE_DELIVERED), 0);

        String smsMessage = SharedPreferencesHelper.getInstance().getPreferenceString(Constants.SMS_MESSAGE); // get sms content from SharedPreferences

        if (smsMessage != null && smsMessage.length() > 0) { // if wedding details was inserted
            SmsManager sms = SmsManager.getDefault(); // manages SMS operations such as sending data
            ArrayList<PendingIntent> sentPendingIntents = new ArrayList<PendingIntent>();
            ArrayList<PendingIntent> deliveredPendingIntents = new ArrayList<PendingIntent>();

            for (GuestClass guest : guestClassList) {
                if (guest.ismIsComing().equalsIgnoreCase("false") && !guest.getmNumOfGuest().equals("0")) { // If no response has been received for this guest
                    ArrayList<String> parts = sms.divideMessage(smsMessage); // divide long SMS to parts

                    for (int i = 0; i < parts.size(); i++) {
                        sentPendingIntents.add(i, sentPI);
                        deliveredPendingIntents.add(i, deliveryPI);
                    }

                    sms.sendMultipartTextMessage(guest.getmPhone(), null, parts, sentPendingIntents, deliveredPendingIntents);
                }
            }
           return 1;
        }

        else { // error dialog - wedding details must filled in before sending SMS
            AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
            alert.setTitle(R.string.str_error);
            alert.setMessage(R.string.fill_wedding_details);
            alert.setPositiveButton(R.string.str_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            alert.show();
        }
        return 0;
    }


}
