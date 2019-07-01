package com.example.arsalan.mygym.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.arsalan.mygym.models.DownloadStatus;

class DownloadStatusVM extends ViewModel {
    private MutableLiveData<DownloadStatus> mutableLiveData;
    public void init(){
        mutableLiveData = new MutableLiveData<>();
    }
    public void setValue(DownloadStatus status) {
        this.mutableLiveData.setValue(status);
    }

    public LiveData<DownloadStatus> getMutableLiveData() {
        return mutableLiveData;
    }
}
