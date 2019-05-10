package com.example.arsalan.mygym.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.arsalan.mygym.models.News;
import com.example.arsalan.mygym.models.RetNewsList;
import com.example.arsalan.mygym.retrofit.ApiClient;
import com.example.arsalan.mygym.retrofit.ApiInterface;
import com.example.arsalan.room.NewsDao;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Response;


@Singleton  // informs Dagger that this class should be constructed once
public class NewsListRepository {
    private final NewsDao newsDao;
    private final Executor executor;

    @Inject
    public NewsListRepository(NewsDao newsDao, Executor executor) {
        this.newsDao = newsDao;
        this.executor = executor;
    }

    public LiveData<List<News>> getNews(int newsType) {
        refreshNewsList(newsType);
        // return a LiveData directly from the database.
        return newsDao.getNewsListByType(newsType);
    }

    private void refreshNewsList(int newsType) {
        boolean newsExist = (newsDao.loadList().getValue() != null && newsDao.loadList().getValue().size() > 0);
        if (!newsExist) {
            Log.d("refreshNewsList", "!newsExist");

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
                            newsDao.deleteAll();
                            Log.d("refreshNewsList", "run: newDao save:" + newsDao.saveList(response.body().getRecords()).length);

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
}
