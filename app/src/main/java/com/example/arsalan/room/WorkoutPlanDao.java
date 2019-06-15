package com.example.arsalan.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.arsalan.mygym.models.WorkoutPlan;

import java.util.List;

import static androidx.room.OnConflictStrategy.IGNORE;

@Dao
public interface WorkoutPlanDao {
    @Insert(onConflict = IGNORE)
    long[] saveList(List<WorkoutPlan> workoutPlans);
    @Insert(onConflict = IGNORE)
    long save(WorkoutPlan workoutPlan);

    @Query("SELECT * FROM WorkoutPlan")
    LiveData<List<WorkoutPlan>> loadList();

    @Query("Select * From WorkoutPlan WHERE userId= :id")
    LiveData<List<WorkoutPlan>> getWorkoutPlanListByUserId(long id);

    @Query("Select * From WorkoutPlan WHERE userId= :id")
    LiveData<List<WorkoutPlan>> getWorkoutPlanByUserId(long id);

    @Query("Select * From WorkoutPlan WHERE trainerWorkoutPlanId= :id")
    WorkoutPlan getWorkoutPlanById(long id);

    @Query("Select * From WorkoutPlan WHERE athleteWorkoutPlanId= :id")
    WorkoutPlan getWorkoutPlanByAthletePlanId(long id);

    @Query("DELETE From WorkoutPlan")
    void deleteAll();


    @Update
    int updatePlan(WorkoutPlan workoutPlan);


}
