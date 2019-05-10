package com.example.arsalan.mygym.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.arsalan.mygym.models.User;
import com.example.arsalan.mygym.repository.AthleteListRepository;

import java.util.List;

import javax.inject.Inject;

public class AthleteListViewModel extends ViewModel {
    private AthleteListRepository userRepo;
    private LiveData<List<User>> userList;

    private MutableLiveData<Integer> userTypeLD = new MutableLiveData<>();

    @Inject //  parameter is provided by Dagger 2
    public AthleteListViewModel(AthleteListRepository userRepo) {
        this.userRepo = userRepo;
    }

    public void init(String token, long trainerId) {
        if (this.userList != null) return;
        userList=userRepo.getUserListByTrainerId(token, trainerId);
    }

    public LiveData<List<User>> getUserList() {
        return this.userList;
    }
}
