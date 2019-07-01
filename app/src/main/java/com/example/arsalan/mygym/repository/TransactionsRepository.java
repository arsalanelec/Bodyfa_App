package com.example.arsalan.mygym.repository;

import android.content.SharedPreferences;
import android.util.Log;

import com.example.arsalan.mygym.models.MyConst;
import com.example.arsalan.mygym.models.RetTransactionList;
import com.example.arsalan.mygym.models.Transaction;
import com.example.arsalan.mygym.retrofit.ApiInterface;
import com.example.arsalan.room.TransactionDao;

import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Singleton;

import androidx.lifecycle.LiveData;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;


@Singleton  // informs Dagger that this class should be constructed once
public class TransactionsRepository {
    private static final String TAG = "TransactionRepository";
    private final TransactionDao transactionDao;
    private final Executor executor;
    private final String mToken;
    private final Retrofit mRetrofit;

    @Inject
    public TransactionsRepository(TransactionDao transactionDao, Executor executor, Retrofit retrofit, SharedPreferences sfp) {
        this.transactionDao = transactionDao;
        this.executor = executor;
        this.mRetrofit = retrofit;
        this.mToken = "Bearer " + sfp.getString(MyConst.KEY_TOKEN, "");
    }

    public LiveData<List<Transaction>> getTransaction(long userId) {
        refreshTransactionValue(userId);
        // return a LiveData directly from the database.
        return transactionDao.loadList();
    }

    private void refreshTransactionValue(long userId) {

        executor.execute(() -> {
            Log.d(TAG, "refreshTransactionValue: userId:" + userId + " token:" + mToken);
            Call<RetTransactionList> call = mRetrofit.create(ApiInterface.class).getTransactionList(mToken, userId);
            try {
                Response<RetTransactionList> response = call.execute();
                if (response.isSuccessful()) {
                    transactionDao.deleteAll();
                    Log.d(TAG, "refreshTransactionValue: count:" + response.body().getRecordsCount() + " count2:" + transactionDao.saveList(response.body().getRecords()).length);

                } else {
                    Log.d(TAG, "run: response.error:" + response.errorBody().string());
                }

            } catch (Throwable t) {
                Log.d(TAG, "run: throws:" + t.getCause());
            }
        });
    }


}
