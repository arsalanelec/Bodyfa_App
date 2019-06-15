package com.example.arsalan.mygym.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.arsalan.mygym.models.News;
import com.example.arsalan.mygym.models.RetNewsDetail;
import com.example.arsalan.mygym.models.Token;
import com.example.arsalan.mygym.retrofit.ApiClient;
import com.example.arsalan.mygym.retrofit.ApiInterface;
import com.example.arsalan.room.NewsDetailDao;

import java.io.IOException;
import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Response;


@Singleton  // informs Dagger that this class should be constructed once
public class NewsDetailRepository {
    private static final String TAG = "NewsDetailRepository";
    private final NewsDetailDao newsDao;
    private final Executor executor;
    private Token mToken;

    @Inject
    public NewsDetailRepository(Token token, NewsDetailDao newsDao, Executor executor) {
        this.newsDao = newsDao;
        this.executor = executor;
        this.mToken = token;
    }

    public LiveData<News> getNewsDetail(long userId, long newsId) {
        refreshNewsDetail(userId, newsId);
        return newsDao.getNews(newsId);
    }

    public LiveData<Integer> getNextNewsDetail(long newsId) {
        return newsDao.getNextNewsId(newsId);
    }

    public LiveData<Integer> getPrevNewsDetail(long newsId) {
        return newsDao.getPrevNewsId(newsId);
    }

    private void refreshNewsDetail(long userId, long newsId) {
        executor.execute(new Runnable() {

            @Override
            public void run() {

                ApiInterface apiService =
                        ApiClient.getClient().create(ApiInterface.class);
                Call<RetNewsDetail> call = apiService.getNewsDetail(mToken.getTokenBearer(), userId, newsId);
                try {
                    Response<RetNewsDetail> response = call.execute();
                    if (response.isSuccessful()) {
                        newsDao.delete(newsId);
                        Log.d(TAG, "run: newsHeadDao save:" + newsDao.save(response.body().getRecord()));
                    } else {
                        Log.d(TAG, "run: response.error");

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });
    }
}
