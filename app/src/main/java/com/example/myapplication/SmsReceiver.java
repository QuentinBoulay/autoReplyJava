package com.example.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.telephony.PhoneNumberUtils;

public class SmsReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences prefs = context.getSharedPreferences("AutoReplyPrefs", Context.MODE_PRIVATE);
        boolean isAutoReplyEnabled = prefs.getBoolean("auto_reply_enabled", false);

        if (!isAutoReplyEnabled) {
            return;
        }

        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Object[] pdus = (Object[]) bundle.get("pdus");
            if (pdus != null) {
                for (Object pdu : pdus) {
                    SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu);
                    String sender = smsMessage.getDisplayOriginatingAddress();

                    String selectedContact = prefs.getString("selected_contact_number", null);

                    if (selectedContact != null && PhoneNumberUtils.compare(sender, selectedContact)) {
                        String autoReplyMessage = prefs.getString("selected_auto_reply_message", null);
                        if (autoReplyMessage != null && !autoReplyMessage.isEmpty()) {
                            SmsManager smsManager = SmsManager.getDefault();
                            smsManager.sendTextMessage(sender, null, autoReplyMessage, null, null);
                        }
                    }
                }
            }
        }
    }
}
