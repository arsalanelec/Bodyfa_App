package com.example.arsalan.mygym.repository;

import androidx.lifecycle.LiveData;
import android.util.Log;

import com.example.arsalan.mygym.models.InboxItem;
import com.example.arsalan.mygym.models.RetInboxList;
import com.example.arsalan.mygym.retrofit.ApiClient;
import com.example.arsalan.mygym.retrofit.ApiInterface;
import com.example.arsalan.room.InboxItemDao;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Response;


@Singleton  // informs Dagger that this class should be constructed once
public class InboxItemListRepository {
    private final InboxItemDao inboxItemDao;
    private final Executor executor;

    @Inject
    public InboxItemListRepository(InboxItemDao inboxItemDao, Executor executor) {
        this.inboxItemDao = inboxItemDao;
        this.executor = executor;
    }

    public LiveData<List<InboxItem>> getInboxItemList(String token, long userId) {
        refreshInboxItemList(token, userId);
        // return a LiveData directly from the database.
        // return inboxItemDao.getInboxItemListByCity(cityId);
        return inboxItemDao.loadAllList();
    }

    private void refreshInboxItemList(String token, long userId) {
        boolean inboxItemExist = (inboxItemDao.loadAllList().getValue() != null && inboxItemDao.loadAllList().getValue().size() > 0);
        if (!inboxItemExist) {
            Log.d("refreshInboxItemList", "!inboxItemExist");

            executor.execute(new Runnable() {

                @Override
                public void run() {

                    ApiInterface apiService =
                            ApiClient.getClient().create(ApiInterface.class);
                    Call<RetInboxList> call = apiService.getInboxList(token, userId, 0, 100);
                    try {
                        Response<RetInboxList> response = call.execute();
                        if (response.isSuccessful()) {
                            Log.d("refreshInboxItemList", "run: response.isSuccessful cnt:" + response.body().getRecordsCount());

                            Log.d("refreshInboxItemList", "run: newDao save:" + inboxItemDao.saveList(response.body().getRecords()).length);

                        } else {
                            Log.d("refreshInboxItemList", "run: response.error");

                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            });
        }


    }
}
