package com.example.arsalan.mygym.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.arsalan.mygym.models.News;
import com.example.arsalan.mygym.models.NewsHead;
import com.example.arsalan.mygym.repository.NewsDetailRepository;
import com.example.arsalan.mygym.repository.NewsListRepository;

import java.util.List;

import javax.inject.Inject;

public class NewsDetailViewModel extends ViewModel {
    private NewsDetailRepository newsRepo;
    private LiveData<News> news;


    @Inject //  parameter is provided by Dagger 2
    public NewsDetailViewModel(NewsDetailRepository newsRepo) {
        this.newsRepo = newsRepo;
    }

    public void init(long userId,long newsId) {
        news=newsRepo.getNewsDetail(userId,newsId);

    }
    public LiveData<News> getNews() {
        return news;
    }

}
