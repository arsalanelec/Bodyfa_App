package com.example.arsalan.mygym.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.arsalan.mygym.models.RetTrainerAthleteList;
import com.example.arsalan.mygym.models.RetroResult;
import com.example.arsalan.mygym.models.Token;
import com.example.arsalan.mygym.models.TrainerAthlete;
import com.example.arsalan.mygym.retrofit.ApiClient;
import com.example.arsalan.mygym.retrofit.ApiInterface;
import com.example.arsalan.room.TrainerAthleteDao;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Response;


@Singleton  // informs Dagger that this class should be constructed once
public class AthleteListRepository {
    private static final String TAG = "AthleteListRepository";
    private final TrainerAthleteDao trainerAthleteDao;
    private final Executor executor;
    private Token mToken;
    private MutableLiveData<Integer> cancelStatus = new MutableLiveData<>();
    private MutableLiveData<Integer> acceptStatus = new MutableLiveData<>();

    @Inject
    public AthleteListRepository(TrainerAthleteDao trainerAthleteDao, Executor executor, Token token) {
        this.trainerAthleteDao = trainerAthleteDao;
        this.executor = executor;
        this.mToken = token;
    }

    public LiveData<List<TrainerAthlete>> getUserListByTrainerId(long trainerId, boolean isAccepted) {
        refreshFromWeb(trainerId);
        return trainerAthleteDao.getAthleteListByTrainerStatus(trainerId, isAccepted ? "confirmed" : "waiting");
    }

    public LiveData<Integer> cancelMembershipRequest(long requestId) {
        cancelMembershipRequestweb(requestId);
        return cancelStatus;
    }

    private void cancelMembershipRequestweb(long requestId) {
        executor.execute(new Runnable() {
            @Override
            public void run() {

                ApiInterface apiService =
                        ApiClient.getClient().create(ApiInterface.class);
                Call<RetroResult> call = apiService.trainerCancelMembershipRequest(mToken.getTokenBearer(), requestId);
                try {
                    Response<RetroResult> response = call.execute();
                    if (response.isSuccessful()) {
                        cancelStatus.postValue(1);

                        Log.d(TAG, "cancelWorkoutRequest: response.isSuccessful:" + trainerAthleteDao.deleteById(requestId));
                    } else {
                        Log.d(TAG, "cancelWorkoutRequest: response.error:" + response.raw().toString());
                        cancelStatus.postValue(-1);
                    }

                } catch (IOException e) {
                    cancelStatus.postValue(-1);
                    e.printStackTrace();
                }
            }

        });
    }


    public LiveData<Integer> acceptMembershipRequest(long requestId) {
        acceptMembershipRequestWeb(requestId);
        return acceptStatus;
    }

    private void acceptMembershipRequestWeb(long requestId) {
        executor.execute(new Runnable() {
            @Override
            public void run() {

                ApiInterface apiService =
                        ApiClient.getClient().create(ApiInterface.class);
                Call<RetroResult> call = apiService.trainerAcceptMembershipRequest(mToken.getTokenBearer(), requestId);
                try {
                    Response<RetroResult> response = call.execute();
                    if (response.isSuccessful()) {
                        acceptStatus.postValue(1);

                        Log.d(TAG, "acceptMembershipRequest: response.isSuccessful:" + trainerAthleteDao.updateStatus(requestId, "confirmed")+" requestId:"+requestId);
                    } else {
                        Log.d(TAG, "acceptMembershipRequest: response.error:" + response.raw().toString());
                        acceptStatus.postValue(-1);
                    }

                } catch (IOException e) {
                    acceptStatus.postValue(-1);
                    e.printStackTrace();
                }
            }

        });
    }


    private void refreshFromWeb(long trainerId) {
        executor.execute(new Runnable() {

            @Override
            public void run() {

                ApiInterface apiService =
                        ApiClient.getClient().create(ApiInterface.class);
                Call<RetTrainerAthleteList> call = apiService.getMyAthleteList(mToken.getTokenBearer(), trainerId);
                try {
                    Response<RetTrainerAthleteList> response = call.execute();
                    if (response.isSuccessful()) {
                        Log.d("refreshUserListCity", "run: response.isSuccessful cnt:" + response.body().getRecordsCount());
                        trainerAthleteDao.deleteByTrainerId(trainerId);
                        Log.d("refreshUserListCity", "run: trainerAthleteDao save:" + trainerAthleteDao.saveList(response.body().getRecords()).length);

                    } else {
                        Log.d("refreshUserListCity", "run: response.error");

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });
    }

}
