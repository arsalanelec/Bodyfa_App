package com.example.arsalan.mygym.repository;

import androidx.lifecycle.LiveData;
import android.util.Log;

import com.example.arsalan.mygym.models.MealPlan;
import com.example.arsalan.mygym.models.RetMealPlanList;
import com.example.arsalan.mygym.retrofit.ApiClient;
import com.example.arsalan.mygym.retrofit.ApiInterface;
import com.example.arsalan.room.MealPlanDao;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Response;


@Singleton  // informs Dagger that this class should be constructed once
public class AthleteMealPlanRepository {
    private final MealPlanDao mealPlanDao;
    private final Executor executor;

    @Inject
    public AthleteMealPlanRepository(MealPlanDao mealPlanDao, Executor executor) {
        this.mealPlanDao = mealPlanDao;
        this.executor = executor;
    }

    public LiveData<List<MealPlan>> getMealPlanList(String token, long userId) {
        refreshMealPlanList(token, userId);

        return mealPlanDao.getMealPlanByUserId(userId);
    }

   /* public LiveData<MealPlan> getMealPlan(String token, long planId) {
        getTrainerMealPlan(token, planId);

        return mealPlanDao.getMealPlanById(planId);
    }


    private void getTrainerMealPlan(String token, long planId) {
        boolean mealPlanExist = (mealPlanDao.getMealPlanById(planId).getValue() != null);
        if (!mealPlanExist) {
            Log.d("refreshMealPlanList", "!mealPlanExist");

            executor.execute(new Runnable() {

                @Override
                public void run() {

                    ApiInterface apiService =
                            ApiClient.getClient().create(ApiInterface.class);
                    Call<RetMealPlan> call = apiService.getTrainerMealPlan(token, planId);
                    try {
                        Response<RetMealPlan> response = call.execute();
                        if (response.isSuccessful()) {

                            Log.d("refreshMealPlanList", "run: newDao update:" + mealPlanDao.updateMealPlan(response.body().getRecord()));

                        } else {
                            Log.d("refreshMealPlanList", "run: response.error");

                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            });
        }


    }*/

    private void refreshMealPlanList(String token, long userId) {
        boolean mealPlanExist = (mealPlanDao.loadList().getValue() != null && mealPlanDao.loadList().getValue().size() > 0);
        if (!mealPlanExist) {
            Log.d("refreshMealPlanList", "!mealPlanExist");

            executor.execute(new Runnable() {

                @Override
                public void run() {

                    ApiInterface apiService =
                            ApiClient.getClient().create(ApiInterface.class);
                    Call<RetMealPlanList> call = apiService.getAthleteMealPlanList(token, userId);
                    try {
                        Response<RetMealPlanList> response = call.execute();
                        if (response.isSuccessful()) {
                            Log.d("refreshMealPlanList", "run: response.isSuccessful cnt:" + response.body().getRecordsCount());

                            Log.d("refreshMealPlanList", "run: mealPlanDao save:" + mealPlanDao.saveList(response.body().getRecords()).length);

                        } else {
                            Log.d("refreshMealPlanList", "run: response.error");

                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            });
        }


    }
}
