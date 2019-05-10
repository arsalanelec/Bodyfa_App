package com.example.arsalan.mygym;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

public class SMSReceiver extends BroadcastReceiver {

    public static final String KEY_SMS = "mygym sms key";


    //interface
    private static SmsListener mListener;

    public static void bindListener(SmsListener listener) {
        mListener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            Log.d("SMSReceiver", "onReceive:");
            //---get the SMS message passed in---
            Bundle bundle = intent.getExtras();
            SmsMessage[] msgs = null;
            String msg = null;
            String str = null;
            if (bundle != null) {
                //---retrieve the SMS message received---
                Object[] pdus = (Object[]) bundle.get("pdus");
                msgs = new SmsMessage[pdus.length];
                for (int i = 0; i < msgs.length; i++) {
                    msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    msg = msgs[i].getMessageBody().toString();
                    str = msg.toUpperCase();
                    String actNumber = str.replaceAll("[^0-9]", "");
                   // PreferenceManager.getDefaultSharedPreferences(mContext).edit().putString(KEY_SMS, actNumber).apply();
                    Log.d("SMSReceiver", "messageReceived: "+msg+ " number:"+actNumber);
                    mListener.messageReceived(actNumber);
                }
            }
        }
    }

}
