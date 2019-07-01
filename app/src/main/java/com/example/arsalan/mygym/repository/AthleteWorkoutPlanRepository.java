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



    private void refreshWorkoutPlanList(String token, long userId) {
        boolean workoutPlanExist = (workoutPlanDao.loadList().getValue() != null && workoutPlanDao.loadList().getValue().size() > 0);
        if (!workoutPlanExist) {
            Log.d("refreshWorkoutPlanList", "!workoutPlanExist");

            executor.execute(() -> {

                ApiInterface apiService =
                        ApiClient.getClient().create(ApiInterface.class);
                Call<RetWorkoutPlanList> call = apiService.getAthleteWorkoutPlanList(token, userId);
                try {
                    Response<RetWorkoutPlanList> response = call.execute();
                    if (response.isSuccessful()) {
                        Log.d("refreshWorkoutPlanList", "run: response.isSuccessful cnt:" + response.body().getRecordsCount());
                        workoutPlanDao.deleteAll();
                        Log.d("refreshWorkoutPlanList", "run: workoutPlanDao save:" + workoutPlanDao.saveList(response.body().getRecords()).length);

                    } else {
                        Log.d("refreshWorkoutPlanList", "run: response.error");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
