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
    private NewsListRepository newsRepo;
    private LiveData<List<NewsHead>> newsList;

    private MutableLiveData<Integer> newsTypeLD=new MutableLiveData<>();

    @Inject //  parameter is provided by Dagger 2
    public NewsListViewModel(NewsListRepository newRepo) {
        this.newsRepo = newRepo;
        newsList = Transformations.switchMap(newsTypeLD, newType -> newsRepo.getNewsList(newType));
    }

    public void init(int newsType) {
        //if (this.newsList!=null)return;
        this.newsTypeLD.setValue(newsType);
    }
    public LiveData<List<NewsHead>> getNewsList() {
        return this.newsList;
    }

}
