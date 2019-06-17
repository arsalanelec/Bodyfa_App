package com.example.arsalan.mygym.viewModels;

import com.example.arsalan.mygym.models.Trainer;
import com.example.arsalan.mygym.models.User;
import com.example.arsalan.mygym.repository.TrainerListRepository;
import com.example.arsalan.mygym.repository.UserRepository;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

public class UserViewModel extends ViewModel {
    private UserRepository repository;
    private LiveData<User> userLiveData;

    private MutableLiveData<String> userName =new MutableLiveData<>();

    @Inject //  parameter is provided by Dagger 2
    public UserViewModel(UserRepository repository) {
        this.repository = repository;
        userLiveData = Transformations.switchMap(userName, userName -> this.repository.getUser(userName));
    }

    public void init(String userName) {
        //if (this.trainer!=null)return;
        this.userName.setValue(userName);
    }
    public LiveData<User> getTrainer() {
        return this.userLiveData;
    }
}
