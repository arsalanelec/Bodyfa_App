package com.example.arsalan.mygym.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.arsalan.mygym.models.RetTrainerWorkoutPlanReqList;
import com.example.arsalan.mygym.models.RetroResult;
import com.example.arsalan.mygym.models.Token;
import com.example.arsalan.mygym.models.WorkoutPlanReq;
import com.example.arsalan.mygym.retrofit.ApiClient;
import com.example.arsalan.mygym.retrofit.ApiInterface;
import com.example.arsalan.room.TrainerWorkoutPlanRequestDao;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Response;


@Singleton  // informs Dagger that this class should be constructed once
public class TrainerWorkoutPlanReqRepository {
    private final TrainerWorkoutPlanRequestDao planRequestDao;
    private final Executor executor;
    private final Token mToken;
    private MutableLiveData<Integer> cancelStatus=new MutableLiveData<>();
    private static final String TAG = "TrainerWorkoutPlanReqRe";

    @Inject
    public TrainerWorkoutPlanReqRepository(TrainerWorkoutPlanRequestDao planRequestDao,Token token, Executor executor) {
        this.planRequestDao = planRequestDao;
        this.executor = executor;
        mToken=token;
    }

    public LiveData<List<WorkoutPlanReq>> getListLive( long  parentId) {
        refreshList(mToken.getToken(),parentId);
        // return a LiveData directly from the database.
        return planRequestDao.loadAllWaitingList();
    }

    public LiveData<Integer> cancelWorkoutRequest(long planId){
        cancelWorkoutRequest(mToken.getToken(),planId);
        return cancelStatus;
    }
    private void refreshList(String token, long id) {
            executor.execute(new Runnable() {
                @Override
                public void run() {

                    ApiInterface apiService =
                            ApiClient.getClient().create(ApiInterface.class);
                    Call<RetTrainerWorkoutPlanReqList> call = apiService.getTrainerWorkoutPlanRequests("Bearer "+token, id);
                    try {
                        Response<RetTrainerWorkoutPlanReqList> response = call.execute();
                        if (response.isSuccessful()) {
                            planRequestDao.deleteAll();
                            Log.d(TAG, "run: response.isSuccessful cnt:"+response.body().getRecordsCount());
                            Log.d(TAG, "run: newDao save:"+ planRequestDao.saveList(response.body().getRecords()).length);

                        } else {
                            Log.d(TAG, "run: response.error");

                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            });
    }

    private void cancelWorkoutRequest(String token, long planId){
        executor.execute(new Runnable() {
            @Override
            public void run() {

                ApiInterface apiService =
                        ApiClient.getClient().create(ApiInterface.class);
                Call<RetroResult> call = apiService.trainerCancelWorkoutPlanRequest("Bearer "+token, planId);
                try {
                    Response<RetroResult> response = call.execute();
                    if (response.isSuccessful()) {
                        cancelStatus.postValue(1);

                        Log.d(TAG, "cancelWorkoutRequest: response.isSuccessful:"+planRequestDao.deleteById(planId));
                    } else {
                        Log.d(TAG, "cancelWorkoutRequest: response.error:"+response.raw().toString());
                        cancelStatus.postValue(-1);
                    }

                } catch (IOException e) {
                    cancelStatus.postValue(-1);
                    e.printStackTrace();
                }
            }

        });
    }
}
