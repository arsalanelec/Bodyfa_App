package com.example.arsalan.mygym.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.arsalan.mygym.models.News;
import com.example.arsalan.mygym.repository.NewsDetailRepository;

import javax.inject.Inject;

public class NewsDetailViewModel extends ViewModel {
    private final NewsDetailRepository newsRepo;
    private final LiveData<News> news;
    private final LiveData<Integer> nextNewsId;
    private final LiveData<Integer> prevNewsId;
    private final MutableLiveData<Input> inputLiveData;


    @Inject //  parameter is provided by Dagger 2
    public NewsDetailViewModel(NewsDetailRepository newsRepo) {
        this.newsRepo = newsRepo;
        inputLiveData = new MutableLiveData<>();
        news = Transformations.switchMap(inputLiveData, input -> newsRepo.getNewsDetail(input.userId, input.newsId));
        prevNewsId = Transformations.switchMap(inputLiveData, input -> newsRepo.getPrevNewsDetail(input.newsId,input.catType));
        nextNewsId = Transformations.switchMap(inputLiveData, input -> newsRepo.getNextNewsDetail(input.newsId,input.catType));
    }

    public void init(long userId, long newsId, int catType) {
        inputLiveData.setValue(new Input(userId, newsId, catType));
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
        private int catType;

        public Input(long userId, long newsId, int catType) {
            this.userId = userId;
            this.newsId = newsId;
            this.catType = catType;
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

        public int getCatType() {
            return catType;
        }

        public void setCatType(int catType) {
            this.catType = catType;
        }
    }
}
