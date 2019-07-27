package com.example.arsalan.mygym.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.arsalan.mygym.models.RetTrainerWorkoutPlanReqList;
import com.example.arsalan.mygym.models.RetWorkoutPlanRequestList;
import com.example.arsalan.mygym.models.RetroResult;
import com.example.arsalan.mygym.models.Token;
import com.example.arsalan.mygym.models.WorkoutPlanReq;
import com.example.arsalan.mygym.retrofit.ApiClient;
import com.example.arsalan.mygym.retrofit.ApiInterface;
import com.example.arsalan.room.WorkoutPlanRequestDao;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Response;


@Singleton  // informs Dagger that this class should be constructed once
public class AthleteWorkoutPlanReqRepository {
    private final WorkoutPlanRequestDao planRequestDao;
    private final Executor executor;
    private final Token mToken;
    private final MutableLiveData<Integer> cancelStatus=new MutableLiveData<>();
    private static final String TAG = "AthleteWorkoutPlanReqRe";
    @Inject
    public AthleteWorkoutPlanReqRepository(WorkoutPlanRequestDao planRequestDao, Token token, Executor executor) {
        this.planRequestDao = planRequestDao;
        this.executor = executor;
        mToken=token;
    }


    public LiveData<List<WorkoutPlanReq>> getAllListLive(long  parentId) {
        refreshList(mToken.getTokenBearer(),parentId);
        // return a LiveData directly from the database.
        return planRequestDao.loadAll();
    }


    private void refreshList(String token, long id) {
            executor.execute(() -> {

                ApiInterface apiService =
                        ApiClient.getClient().create(ApiInterface.class);
                Call<RetWorkoutPlanRequestList> call = apiService.getAthleteWorkoutPlanRequests(token, id);
                try {
                    Response<RetWorkoutPlanRequestList> response = call.execute();
                    if (response.isSuccessful()) {
                        planRequestDao.deleteAll();
                        Log.d(TAG, "run: response.isSuccessful cnt:"+response.body().getCount());
                        Log.d(TAG, "run: newDao save:"+ planRequestDao.saveList(response.body().getRecords()).length);
                    } else {
                        Log.d(TAG, "run: response.error");

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
    }


}
