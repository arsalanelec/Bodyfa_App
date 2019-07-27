package com.example.arsalan.mygym.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.arsalan.mygym.models.WorkoutPlan;
import com.example.arsalan.mygym.models.WorkoutPlanReq;
import com.example.arsalan.mygym.repository.AthleteWorkoutPlanReqRepository;
import com.example.arsalan.mygym.repository.TrainerWorkoutRepository;

import java.util.List;

import javax.inject.Inject;

public class AthleteWorkoutRequestListViewModel extends ViewModel {
    private final AthleteWorkoutPlanReqRepository workoutPlanItemRepo;
    private LiveData<List<WorkoutPlanReq>> workoutPlanItemList;

    private MutableLiveData<Integer> workoutPlanItemTypeLD = new MutableLiveData<>();

    @Inject //  parameter is provided by Dagger 2
    public AthleteWorkoutRequestListViewModel(AthleteWorkoutPlanReqRepository workoutPlanItemRepo) {
        this.workoutPlanItemRepo = workoutPlanItemRepo;
        // workoutPlanItemList = Transformations.switchMap(workoutPlanItemTypeLD, cityId -> this.workoutPlanItemRepo.getWorkoutPlanItem(cityId));
    }

    public void init( long userId) {
        //if (this.workoutPlanItemList!=null)return;
        workoutPlanItemList = workoutPlanItemRepo.getAllListLive( userId);
    }

    public LiveData<List<WorkoutPlanReq>> getWorkoutPlanItemList() {
        return this.workoutPlanItemList;
    }
}
