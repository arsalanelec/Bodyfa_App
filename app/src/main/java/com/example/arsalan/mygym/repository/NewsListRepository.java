package com.example.arsalan.mygym.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.arsalan.mygym.models.NewsHead;
import com.example.arsalan.mygym.models.RetNewsList;
import com.example.arsalan.mygym.retrofit.ApiClient;
import com.example.arsalan.mygym.retrofit.ApiInterface;
import com.example.arsalan.room.NewsHeadDao;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Response;


@Singleton  // informs Dagger that this class should be constructed once
public class NewsListRepository {
    private final NewsHeadDao newsHeadDao;
    private final Executor executor;

    @Inject
    public NewsListRepository(NewsHeadDao newsHeadDao, Executor executor) {
        this.newsHeadDao = newsHeadDao;
        this.executor = executor;
    }

    public LiveData<List<NewsHead>> getNewsList(int newsType) {
        refreshNewsList(newsType);
        // return a LiveData directly from the database.
        return newsHeadDao.getNewsListByType(newsType);
    }

    private void refreshNewsList(int newsType) {
            executor.execute(new Runnable() {
                @Override
                public void run() {

                    ApiInterface apiService =
                            ApiClient.getClient().create(ApiInterface.class);
                    Call<RetNewsList> call = apiService.getNewsList(0, 100, newsType, 0);
                    try {
                        Response<RetNewsList> response = call.execute();
                        if (response.isSuccessful()) {
                            Log.d("refreshNewsList", "run: response.isSuccessful cnt:" + response.body().getRecordsCount());
                            newsHeadDao.deleteAll();
                            Log.d("refreshNewsList", "run: newDao save:" + newsHeadDao.saveList(response.body().getRecords()).length);

                        } else {
                            Log.d("refreshNewsList", "run: response.error");

                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            });
        }
}
