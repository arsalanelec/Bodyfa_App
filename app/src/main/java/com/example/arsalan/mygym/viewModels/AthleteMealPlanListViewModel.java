package com.example.arsalan.mygym.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.arsalan.mygym.models.MealPlan;
import com.example.arsalan.mygym.repository.AthleteMealPlanRepository;

import java.util.List;

import javax.inject.Inject;

public class AthleteMealPlanListViewModel extends ViewModel {
    private final AthleteMealPlanRepository mealPlanItemRepo;
    private LiveData<List<MealPlan>> mealPlanItemList;

    private MutableLiveData<Integer> mealPlanItemTypeLD = new MutableLiveData<>();

    @Inject //  parameter is provided by Dagger 2
    public AthleteMealPlanListViewModel(AthleteMealPlanRepository mealPlanItemRepo) {
        this.mealPlanItemRepo = mealPlanItemRepo;
        // mealPlanItemList = Transformations.switchMap(mealPlanItemTypeLD, cityId -> this.mealPlanItemRepo.getMealPlanItem(cityId));
    }

    public void init(String token, long userId) {
        //if (this.mealPlanItemList!=null)return;
        mealPlanItemList = mealPlanItemRepo.getMealPlanList(token, userId);
    }

    public LiveData<List<MealPlan>> getMealPlanItemList() {
        return this.mealPlanItemList;
    }
}
