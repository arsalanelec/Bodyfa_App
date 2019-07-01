package com.example.arsalan.mygym.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.arsalan.mygym.models.Gym;
import com.example.arsalan.mygym.repository.GymListRepository;

import java.util.List;

import javax.inject.Inject;

public class GymListViewModel extends ViewModel {
    private final GymListRepository gymRepo;
    private final LiveData<List<Gym>> gymList;

    private final MutableLiveData<Integer> gymTypeLD=new MutableLiveData<>();

    @Inject //  parameter is provided by Dagger 2
    public GymListViewModel(GymListRepository gymRepo) {
        this.gymRepo = gymRepo;
        gymList = Transformations.switchMap(gymTypeLD, cityId -> this.gymRepo.getGym(cityId));
    }

    public void init(int sortType) {
        //if (this.gymList!=null)return;
        this.gymTypeLD.setValue(sortType);
    }
    public LiveData<List<Gym>> getGymList() {
        return this.gymList;
    }
}
