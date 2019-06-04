package com.example.arsalan.mygym.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.arsalan.mygym.models.Token;
import com.example.arsalan.mygym.models.TrainerAthlete;
import com.example.arsalan.mygym.repository.TrainerAthletesRepository;

import java.util.List;

import javax.inject.Inject;

public class TrainerAthleteViewModel extends ViewModel {
    private TrainerAthletesRepository repository;
    private LiveData<List<TrainerAthlete>> activeTrainerList;

    private MutableLiveData<Long> userId =new MutableLiveData<>();

    @Inject //  parameter is provided by Dagger 2
    public TrainerAthleteViewModel(TrainerAthletesRepository repository, Token token) {
        this.repository = repository;
        activeTrainerList = Transformations.switchMap(userId, id -> this.repository.getTrainerAthlete(token.getTokenBearer(),id));
    }


    public void initByUser(long userId) {
        this.userId.setValue(userId);
    }

    public LiveData<List<TrainerAthlete>> getActiveTrainerList() {
        return this.activeTrainerList;
    }
}
