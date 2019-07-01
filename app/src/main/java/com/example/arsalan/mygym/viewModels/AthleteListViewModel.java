package com.example.arsalan.mygym.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.arsalan.mygym.models.TrainerAthlete;
import com.example.arsalan.mygym.repository.AthleteListRepository;

import java.util.List;

import javax.inject.Inject;

public class AthleteListViewModel extends ViewModel {
    private final AthleteListRepository userRepo;
    private LiveData<List<TrainerAthlete>> athleteList;

    @Inject //  parameter is provided by Dagger 2
    public AthleteListViewModel(AthleteListRepository userRepo) {
        this.userRepo = userRepo;
    }

    public void init(long trainerId, boolean isAccepted) {
        if (this.athleteList != null) return;
        athleteList = userRepo.getUserListByTrainerId(trainerId, isAccepted);
    }

    /**
     * cancel the request for membership
     * @param requestId
     * @return
     */
    public LiveData<Integer> cancelRequest(long requestId){
        return userRepo.cancelMembershipRequest(requestId);
    }

    /**
     * accept membership request
     * @param requestId
     * @return
     */
    public void acceptRequest(long requestId){
        userRepo.acceptMembershipRequest(requestId);
    }

    public LiveData<List<TrainerAthlete>> getAthleteList() {
        return this.athleteList;
    }
}
