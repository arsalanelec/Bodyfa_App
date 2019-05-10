package com.example.arsalan.mygym.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.arsalan.mygym.models.InboxItem;
import com.example.arsalan.mygym.repository.InboxItemListRepository;

import java.util.List;

import javax.inject.Inject;

public class InboxItemListViewModel extends ViewModel {
    private InboxItemListRepository inboxItemRepo;
    private LiveData<List<InboxItem>> inboxItemList;

    private MutableLiveData<Integer> inboxItemToken = new MutableLiveData<>();

    @Inject //  parameter is provided by Dagger 2
    public InboxItemListViewModel(InboxItemListRepository inboxItemRepo) {
        this.inboxItemRepo = inboxItemRepo;
    }

    public void init(String token,long userId) {
        //if (this.inboxItemList!=null)return;
        inboxItemList = inboxItemRepo.getInboxItemList(token, userId);

    }

    public LiveData<List<InboxItem>> getInboxItemList() {
        return this.inboxItemList;
    }
}
