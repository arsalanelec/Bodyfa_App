package com.example.arsalan.mygym.repository;

import androidx.lifecycle.LiveData;
import android.util.Log;

import com.example.arsalan.mygym.models.RetUserList;
import com.example.arsalan.mygym.models.User;
import com.example.arsalan.mygym.retrofit.ApiClient;
import com.example.arsalan.mygym.retrofit.ApiInterface;
import com.example.arsalan.room.UserDao;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Response;


@Singleton  // informs Dagger that this class should be constructed once
public class AthleteListRepository {
    private final UserDao userDao;
    private final Executor executor;

    @Inject
    public AthleteListRepository(UserDao userDao, Executor executor) {
        this.userDao = userDao;
        this.executor = executor;
    }

    public LiveData<List<User>> getUserListByTrainerId(String token, long trainerId) {
        refreshUserListByTrainerId(token, trainerId);
        return userDao.getUserByTrainer(trainerId);
    }


    private void refreshUserListByTrainerId(String token, long trainerId) {
        boolean userExist = (userDao.loadAllList().getValue() != null && userDao.loadAllList().getValue().size() > 0);
        if (!userExist) {
            Log.d("refreshUserListCity", "!userExist");

            executor.execute(new Runnable() {

                @Override
                public void run() {

                    ApiInterface apiService =
                            ApiClient.getClient().create(ApiInterface.class);
                    Call<RetUserList> call = apiService.getMyAthleteList(token, trainerId);
                    try {
                        Response<RetUserList> response = call.execute();
                        if (response.isSuccessful()) {
                            Log.d("refreshUserListCity", "run: response.isSuccessful cnt:" + response.body().getRecordsCount());

                            Log.d("refreshUserListCity", "run: userDao save:" + userDao.saveList(response.body().getRecords()).length);

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


    // }
}
