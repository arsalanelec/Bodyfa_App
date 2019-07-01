package com.example.arsalan.mygym.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.arsalan.mygym.models.NewsHead;
import com.example.arsalan.mygym.repository.NewsListRepository;

import java.util.List;

import javax.inject.Inject;

public class NewsListViewModel extends ViewModel {
    private final NewsListRepository newsRepo;
    private final LiveData<List<NewsHead>> newsList;

    private final MutableLiveData<NewsMapSource> newsSource =new MutableLiveData<>();

    @Inject //  parameter is provided by Dagger 2
    public NewsListViewModel(NewsListRepository newRepo) {
        this.newsRepo = newRepo;
        newsList = Transformations.switchMap(newsSource, source -> newsRepo.getNewsList(source.publisherId,source.newsType));
    }

    public void init(long publisherId,int newsType) {
        //if (this.newsList!=null)return;
        this.newsSource.setValue(new NewsMapSource(publisherId,newsType));
    }
    public LiveData<List<NewsHead>> getNewsList() {
        return this.newsList;
    }

    private class NewsMapSource{
        final long publisherId;
        final int newsType;

        public NewsMapSource(long publisherId, int newsType) {
            this.publisherId = publisherId;
            this.newsType = newsType;
        }

    }
}
