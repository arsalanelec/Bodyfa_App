package com.example.arsalan.mygym.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.arsalan.mygym.models.WorkoutPlanReq;
import com.example.arsalan.mygym.repository.TrainerWorkoutPlanReqRepository;

import java.util.List;

import javax.inject.Inject;

public class TrainerWorkoutPlanReqVm extends ViewModel {
    TrainerWorkoutPlanReqRepository mRepository;
    LiveData<List<WorkoutPlanReq>> mWorkoutPlanListLv;


    @Inject
    public TrainerWorkoutPlanReqVm(TrainerWorkoutPlanReqRepository repository) {
        mRepository = repository;

    }

    public void init(long trainerId){
        mWorkoutPlanListLv=  mRepository.getListLive(trainerId);
    }
    public LiveData<Integer> cancelRequest(long requestId){
        return mRepository.cancelWorkoutRequest(requestId);
    }

    public LiveData<List<WorkoutPlanReq>> getWorkoutPlanListLv() {
        return mWorkoutPlanListLv;
    }
}
