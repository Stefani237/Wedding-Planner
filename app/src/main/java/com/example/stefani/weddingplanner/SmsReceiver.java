package com.example.stefani.weddingplanner;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

/**
 * Created by Stefani on 10/12/2017.
 */

public class SmsReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        SmsMessage[] messages = null;
        String str="";

        if(bundle != null){
            Object[] pdus = (Object[])bundle.get("pdus");
            messages = new SmsMessage[pdus.length];
            for (int i = 0; i < messages.length; i++) {
                messages[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                str+="From:" + messages[i].getOriginatingAddress(); // message : From:phoneNumber:message
                str+=":";
                str+=messages[i].getMessageBody().toString();
                str+="\n";
            }
        }

        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(Constants.SMS_RECEIVED_ACTION);
        broadcastIntent.putExtra("sms", str);
        context.sendBroadcast(broadcastIntent);

    }
}
