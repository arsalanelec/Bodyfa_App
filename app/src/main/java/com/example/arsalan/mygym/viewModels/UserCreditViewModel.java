package com.example.arsalan.mygym.viewModels;

import com.example.arsalan.mygym.models.UserCredit;
import com.example.arsalan.mygym.repository.CreditRepository;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

public class UserCreditViewModel extends ViewModel {
    private CreditRepository repository;
    private LiveData<UserCredit> userCreditLiveData;

    private MutableLiveData<Long> trainerIdLD = new MutableLiveData<>();

    @Inject //  parameter is provided by Dagger 2
    public UserCreditViewModel(CreditRepository repository) {
        this.repository = repository;
        userCreditLiveData = Transformations.switchMap(trainerIdLD, id -> this.repository.getUserCredit(id));
    }

    public void init(long trainerId) {
        //if (this.userCreditLiveData!=null)return;
        this.trainerIdLD.setValue(trainerId);
    }

    public LiveData<UserCredit> getCredit() {
        return this.userCreditLiveData;
    }
}
