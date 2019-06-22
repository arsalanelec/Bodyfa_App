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
    private static final String TAG = "NewsListRepository";
    private final NewsHeadDao newsHeadDao;
    private final Executor executor;

    @Inject
    public NewsListRepository(NewsHeadDao newsHeadDao, Executor executor) {
        this.newsHeadDao = newsHeadDao;
        this.executor = executor;
    }

    public LiveData<List<NewsHead>> getNewsList(long publisherId, int newsType) {
        refreshNewsList(newsType);
        // return a LiveData directly from the database.
        Log.d(TAG, "getNewsList: publisherId:"+publisherId+" newsType:"+newsType);
        if (publisherId != 0) { // if we need news list of a publisher
            return newsHeadDao.getNewsListByPublisher(publisherId);
        } else { // else if we need news list of one type
            return newsHeadDao.getNewsListByType(newsType);
        }
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
                        Log.d("refreshNewsList", "run: response.isSuccessful type:"+newsType+" cnt:" + response.body().getRecordsCount());
                        newsHeadDao.deleteByType(newsType);
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
