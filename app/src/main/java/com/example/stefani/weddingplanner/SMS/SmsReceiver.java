package com.example.stefani.weddingplanner.SMS;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.example.stefani.weddingplanner.BasicClasses.Constants;


public class SmsReceiver extends BroadcastReceiver {

    private ISmsResponse mIsmsResponse;

    public void setSmsListener(ISmsResponse iSmsResponse) {
        mIsmsResponse = iSmsResponse;
    }

    // receives and handles messages
    @Override
    public void onReceive(Context context, Intent intent) {
        // the SMS broadcast intent includes the incoming SMS details.
        Bundle bundle = intent.getExtras(); // gets the current message data
        SmsMessage[] messages = null;
        String str = "";

        if (bundle != null) {
            // protocol description units — used to encapsulate an SMS message and its metadata
            Object[] pdus = (Object[]) bundle.get("pdus"); // extracts the array of SmsMessage objects packaged within the SMS broadcast Intent bundle.
            messages = new SmsMessage[pdus.length];
            for (int i = 0; i < messages.length; i++) {
                messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]); // convert each PDU byte array into an SMS Message object
                str += "From:" + messages[i].getOriginatingAddress(); // message : From:phoneNumber:message
                str += ":";
                str += messages[i].getMessageBody().toString();
                str += "\n";
            }
        }
        String segments[] = str.split(":");
       // String phoneNumber = "";
      //  phoneNumber = "0" + segments[1].trim().substring(Constants.PHONE_START_IDX, Constants.PHONE_END_IDX);
        String phoneNumber = "0" + segments[1].trim().substring(4, 13);
        String smsReplay = segments[2].trim();
        try { // example of message view From:+972503877745:2
            Integer numberOfInvited = Integer.valueOf(smsReplay); // convert the message content to int
            if (numberOfInvited < Constants.MAX_VAL_SMS_RESPONSE && numberOfInvited >= Constants.NOT_COMING_SMS_RESPONSE) { // 0 <= numberOfInvited < 10
                // update list with number and isComing = true / false
                // 0 - not coming
                // 1 and more - coming
                mIsmsResponse.smsResponse(phoneNumber, numberOfInvited);

            } else { // numberOfInvited < 0 , numberOfInvited >= 10
                // popup bed response.
                mIsmsResponse.smsErrorResponse(phoneNumber);

            }

        } catch (Exception e) { // any other chars
            // popup bed response.
            mIsmsResponse.smsErrorResponse(phoneNumber);
        }
    }
}
