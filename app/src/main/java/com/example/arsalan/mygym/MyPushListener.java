package com.example.arsalan.mygym;

import android.util.Log;

import com.example.arsalan.mygym.di.Injectable;
import com.example.arsalan.mygym.models.Trainer;
import com.example.arsalan.room.TrainerDao;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

import co.ronash.pushe.PusheListenerService;
import dagger.android.AndroidInjection;

/**
 * Created on 16-05-09, 6:20 PM.
 *
 * @author Akram Shokri
 */
public class MyPushListener extends PusheListenerService implements Injectable {
    private static final String TAG = "MyPushListener";

    @Inject
    TrainerDao mTrainerDao;

    @Override
    public void onCreate() {
        super.onCreate();
        AndroidInjection.inject(this);
    }

    @Override
    public void onMessageReceived(final JSONObject message, JSONObject content) {
        if (message != null && message.length() > 0) {
            //    search for received key and objects
            try {
                if (message.has("trainer")) {
                    Gson gson = new Gson();
                    Log.d(TAG, "onMessageReceived: user:" + message.getString("trainer"));
                    Trainer trainer = gson.fromJson(message.getString("trainer"), Trainer.class);
                    Log.d(TAG, "onMessageReceived: userDao saves:" + mTrainerDao.save(trainer) + " user:" + trainer.getName());
                }
                if (message.has("key1")) {
                    String s1 = message.getString("key1");
                    android.util.Log.i("Pushe", "Custom json Message: " + message.toString() + "\ns1:" + s1);
                }

            } catch (JSONException e) {
                android.util.Log.e("", "Exception in parsin json", e);

            }
        }
    }
}
