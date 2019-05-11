package com.example.arsalan.mygym.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.arsalan.mygym.models.NextPrev;

public class NextPrevVm extends ViewModel {
    MutableLiveData<NextPrev> nextPrevMutableLiveData = new MutableLiveData<>();

    public LiveData<NextPrev> getNextPrevLiveData() {
        return nextPrevMutableLiveData;
    }

    public void setNextPrev(NextPrev nextPrev) {
        this.nextPrevMutableLiveData.setValue(nextPrev);
    }
}
