package com.example.arsalan.mygym.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.arsalan.mygym.models.Trainer;
import com.example.arsalan.mygym.repository.TrainerListRepository;

import javax.inject.Inject;

public class TrainerAthleteViewModel extends ViewModel {
    private TrainerListRepository trainerRepo;
    private LiveData<Trainer> trainer;

    private MutableLiveData<Long> trainerIdLD =new MutableLiveData<>();

    @Inject //  parameter is provided by Dagger 2
    public TrainerAthleteViewModel(TrainerListRepository trainerRepo) {
        this.trainerRepo = trainerRepo;
        trainer = Transformations.switchMap(trainerIdLD, id -> this.trainerRepo.getTrainerById(id));
    }

    public void init(long trainerId) {
        //if (this.trainer!=null)return;
        this.trainerIdLD.setValue(trainerId);
    }
    public LiveData<Trainer> getTrainer() {
        return this.trainer;
    }
}
