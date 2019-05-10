package com.example.arsalan.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.arsalan.mygym.models.MealPlan;

import java.util.List;

import static androidx.room.OnConflictStrategy.IGNORE;

@Dao
public interface MealPlanDao {
    @Insert(onConflict = IGNORE)
    long[] saveList(List<MealPlan> mealPlans);

    @Query("SELECT * FROM MealPlan")
    LiveData<List<MealPlan>> loadList();

    @Query("Select * From MealPlan WHERE userId= :id")
    LiveData<List<MealPlan>> getMealPlanByUserId(long id);

    @Query("Select * From MealPlan WHERE trainerMealPlanId= :id")
    MealPlan getMealPlanById(long id);


    @Query("Select * From MealPlan WHERE athleteMealPlanId= :id")
    MealPlan getMealPlanByAthletePlanId(long id);

    @Update
    int updateMealPlan(MealPlan mealPlan);
}
