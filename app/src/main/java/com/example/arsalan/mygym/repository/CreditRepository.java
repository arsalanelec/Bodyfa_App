package com.example.arsalan.mygym.repository;

import android.content.SharedPreferences;
import android.util.Log;

import com.example.arsalan.mygym.models.MyConst;
import com.example.arsalan.mygym.models.UserCredit;
import com.example.arsalan.mygym.retrofit.ApiInterface;
import com.example.arsalan.room.CreditDao;

import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Singleton;

import androidx.lifecycle.LiveData;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;


@Singleton  // informs Dagger that this class should be constructed once
public class CreditRepository {
    private static final String TAG = "CreditRepository";
    private final CreditDao creditDao;
    private final Executor executor;
    private String mToken;
    private Retrofit mRetrofit;

    @Inject
    public CreditRepository(CreditDao creditDao, Executor executor, Retrofit retrofit, SharedPreferences sfp) {
        this.creditDao = creditDao;
        this.executor = executor;
        this.mRetrofit = retrofit;
        this.mToken = "Bearer " + sfp.getString(MyConst.KEY_TOKEN, "");
    }

    public LiveData<UserCredit> getUserCredit(long userId) {
        refreshCreditValue(userId);
        // return a LiveData directly from the database.
        return creditDao.getCreadit(userId);
    }

    private void refreshCreditValue(long userId) {

        executor.execute(() -> {
            Log.d(TAG, "refreshCreditValue: userId:" + userId + " token:" + mToken);
            Call<UserCredit> call = mRetrofit.create(ApiInterface.class).getUserCredit(mToken, userId);
            try {
                Response<UserCredit> response = call.execute();
                if (response.isSuccessful()) {
                    creditDao.deleteAllCredits();
                    UserCredit userCredit = new UserCredit();
                    userCredit.setCredit(response.body().getCredit());
                    userCredit.setUserId(userId);
                    creditDao.save(userCredit);
                    Log.d(TAG, "refreshCreditValue: credit:" + response.body().getCredit());

                } else {
                    Log.d(TAG, "run: response.error:" + response.errorBody().string());

                }

            } catch (Throwable t) {
                Log.d(TAG, "run: throws:" + t.getCause());
            }
        });
    }


}
