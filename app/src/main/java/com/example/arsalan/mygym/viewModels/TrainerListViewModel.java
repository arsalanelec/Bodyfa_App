package com.example.arsalan.mygym.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.arsalan.mygym.models.Trainer;
import com.example.arsalan.mygym.repository.TrainerListRepository;

import java.util.List;

import javax.inject.Inject;

public class TrainerListViewModel extends ViewModel {
    private final TrainerListRepository trainerRepo;
    private final LiveData<List<Trainer>> trainerList;

    private final MutableLiveData<Integer> trainerTypeLD=new MutableLiveData<>();

    @Inject //  parameter is provided by Dagger 2
    public TrainerListViewModel(TrainerListRepository trainerRepo) {
        this.trainerRepo = trainerRepo;
        trainerList = Transformations.switchMap(trainerTypeLD, cityId -> this.trainerRepo.getTrainerListByCity(cityId));
    }

    public void init(int cityId) {
        //if (this.trainerList!=null)return;
        this.trainerTypeLD.setValue(cityId);
    }
    public LiveData<List<Trainer>> getTrainerList() {
        return this.trainerList;
    }

}
