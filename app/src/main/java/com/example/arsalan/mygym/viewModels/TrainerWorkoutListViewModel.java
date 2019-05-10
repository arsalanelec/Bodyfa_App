package com.example.arsalan.mygym.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.arsalan.mygym.models.WorkoutPlan;
import com.example.arsalan.mygym.repository.TrainerWorkoutRepository;

import java.util.List;

import javax.inject.Inject;

public class TrainerWorkoutListViewModel extends ViewModel {
    private TrainerWorkoutRepository workoutPlanItemRepo;
    private LiveData<List<WorkoutPlan>> workoutPlanItemList;

    private MutableLiveData<Integer> workoutPlanItemTypeLD = new MutableLiveData<>();

    @Inject //  parameter is provided by Dagger 2
    public TrainerWorkoutListViewModel(TrainerWorkoutRepository workoutPlanItemRepo) {
        this.workoutPlanItemRepo = workoutPlanItemRepo;
        // workoutPlanItemList = Transformations.switchMap(workoutPlanItemTypeLD, cityId -> this.workoutPlanItemRepo.getWorkoutPlanItem(cityId));
    }

    public void init(String token, long userId) {
        //if (this.workoutPlanItemList!=null)return;
        workoutPlanItemList = workoutPlanItemRepo.getWorkoutPlanList(token, userId);
    }

    public LiveData<List<WorkoutPlan>> getWorkoutPlanItemList() {
        return this.workoutPlanItemList;
    }
}
