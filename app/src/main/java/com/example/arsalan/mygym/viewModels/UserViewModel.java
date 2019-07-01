package com.example.arsalan.mygym.viewModels;

import android.net.Uri;

import com.example.arsalan.mygym.models.RetStatusProgress;
import com.example.arsalan.mygym.models.User;
import com.example.arsalan.mygym.repository.UserRepository;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

public class UserViewModel extends ViewModel {
    private final UserRepository repository;
    private final LiveData<User> userLiveData;
    private LiveData<RetStatusProgress> statusLiveData;

    private final MutableLiveData<String> userName =new MutableLiveData<>();

    @Inject //  parameter is provided by Dagger 2
    public UserViewModel(UserRepository repository) {
        this.repository = repository;
        userLiveData = Transformations.switchMap(userName, userName -> this.repository.getUser(userName));
    }

    public void init(String userName) {
        //if (this.trainer!=null)return;
        this.userName.setValue(userName);
    }
    public  LiveData<RetStatusProgress> save(User user, Uri imageUri, String imagePath){
        return repository.uploadUser(user, imageUri, imagePath);
    }
    public LiveData<User> getUserLive() {
        return this.userLiveData;
    }

}
