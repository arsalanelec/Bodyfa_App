package com.example.arsalan.mygym.repository;

import androidx.lifecycle.LiveData;
import android.util.Log;

import com.example.arsalan.mygym.models.WorkoutPlan;
import com.example.arsalan.mygym.models.RetWorkoutPlanList;
import com.example.arsalan.mygym.retrofit.ApiClient;
import com.example.arsalan.mygym.retrofit.ApiInterface;
import com.example.arsalan.room.WorkoutPlanDao;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Response;


@Singleton  // informs Dagger that this class should be constructed once
public class AthleteWorkoutPlanRepository {
    private final WorkoutPlanDao workoutPlanDao;
    private final Executor executor;

    @Inject
    public AthleteWorkoutPlanRepository(WorkoutPlanDao workoutPlanDao, Executor executor) {
        this.workoutPlanDao = workoutPlanDao;
        this.executor = executor;
    }

    public LiveData<List<WorkoutPlan>> getWorkoutPlanList(String token, long userId) {
        refreshWorkoutPlanList(token, userId);

        return workoutPlanDao.getWorkoutPlanByUserId(userId);
    }

   /* public LiveData<WorkoutPlan> getWorkoutPlan(String token, long planId) {
        getTrainerWorkoutPlan(token, planId);

        return workoutPlanDao.getWorkoutPlanById(planId);
    }


    private void getTrainerWorkoutPlan(String token, long planId) {
        boolean workoutPlanExist = (workoutPlanDao.getWorkoutPlanById(planId).getValue() != null);
        if (!workoutPlanExist) {
            Log.d("refreshWorkoutPlanList", "!workoutPlanExist");

            executor.execute(new Runnable() {

                @Override
                public void run() {

                    ApiInterface apiService =
                            ApiClient.getClient().create(ApiInterface.class);
                    Call<RetWorkoutPlan> call = apiService.getTrainerWorkoutPlan(token, planId);
                    try {
                        Response<RetWorkoutPlan> response = call.execute();
                        if (response.isSuccessful()) {

                            Log.d("refreshWorkoutPlanList", "run: newDao update:" + workoutPlanDao.updateWorkoutPlan(response.body().getRecord()));

                        } else {
                            Log.d("refreshWorkoutPlanList", "run: response.error");

                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            });
        }


    }*/

    private void refreshWorkoutPlanList(String token, long userId) {
        boolean workoutPlanExist = (workoutPlanDao.loadList().getValue() != null && workoutPlanDao.loadList().getValue().size() > 0);
        if (!workoutPlanExist) {
            Log.d("refreshWorkoutPlanList", "!workoutPlanExist");

            executor.execute(new Runnable() {

                @Override
                public void run() {

                    ApiInterface apiService =
                            ApiClient.getClient().create(ApiInterface.class);
                    Call<RetWorkoutPlanList> call = apiService.getAthleteWorkoutPlanList(token, userId);
                    try {
                        Response<RetWorkoutPlanList> response = call.execute();
                        if (response.isSuccessful()) {
                            Log.d("refreshWorkoutPlanList", "run: response.isSuccessful cnt:" + response.body().getRecordsCount());

                            Log.d("refreshWorkoutPlanList", "run: workoutPlanDao save:" + workoutPlanDao.saveList(response.body().getRecords()).length);

                        } else {
                            Log.d("refreshWorkoutPlanList", "run: response.error");

                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            });
        }


    }
}
