package com.example.arsalan.mygym;

import org.json.JSONException;
import org.json.JSONObject;

import co.ronash.pushe.PusheListenerService;

/**
 * Created on 16-05-09, 6:20 PM.
 *
 * @author Akram Shokri
 */
public class MyPushListener extends PusheListenerService {
    @Override
    public void onMessageReceived(final JSONObject message, JSONObject content) {
        if (message != null && message.length() > 0) {
//            //    your code
            try {
                String s1 = message.getString("key1");
                android.util.Log.i("Pushe", "Custom json Message: " + message.toString()+"\ns1:"+s1);

            } catch (JSONException e) {
                android.util.Log.e("", "Exception in parsin json", e);

            }
        }
    }
}
