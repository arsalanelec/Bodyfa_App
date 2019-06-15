package com.example.arsalan.mygym.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.arsalan.mygym.models.News;
import com.example.arsalan.mygym.repository.NewsDetailRepository;

import javax.inject.Inject;

public class NewsDetailViewModel extends ViewModel {
    private NewsDetailRepository newsRepo;
    private LiveData<News> news;
    private LiveData<Integer> nextNewsId;
    private LiveData<Integer> prevNewsId;
    private MutableLiveData<Input> inputLiveData;


    @Inject //  parameter is provided by Dagger 2
    public NewsDetailViewModel(NewsDetailRepository newsRepo) {
        this.newsRepo = newsRepo;
        inputLiveData = new MutableLiveData<>();
        news=Transformations.switchMap(inputLiveData,input -> {
            return newsRepo.getNewsDetail(input.userId, input.newsId);
        });
        nextNewsId=Transformations.switchMap(inputLiveData,input->newsRepo.getNextNewsDetail(input.newsId));
        prevNewsId=Transformations.switchMap(inputLiveData,input->newsRepo.getPrevNewsDetail(input.newsId));

    }

    public void init(long userId, long newsId) {
        inputLiveData.setValue(new Input(userId,newsId));
    }

    public LiveData<News> getNews() {
        return news;
    }

    public LiveData<Integer> getNextNews() {
        return nextNewsId;
    }

    public LiveData<Integer> getPrevNewsId() {
        return prevNewsId;
    }

    private class Input {
        private long userId;
        private long newsId;

        public Input(long userId, long newsId) {
            this.userId = userId;
            this.newsId = newsId;
        }

        public long getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public long getNewsId() {
            return newsId;
        }

        public void setNewsId(int newsId) {
            this.newsId = newsId;
        }
    }
}
