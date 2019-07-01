package com.example.arsalan.mygym.repository;

import androidx.lifecycle.LiveData;
import android.util.Log;

import com.example.arsalan.mygym.models.RetGymList;
import com.example.arsalan.mygym.models.Gym;
import com.example.arsalan.mygym.retrofit.ApiClient;
import com.example.arsalan.mygym.retrofit.ApiInterface;
import com.example.arsalan.room.GymDao;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Response;


@Singleton  // informs Dagger that this class should be constructed once
public class GymListRepository {
    private final GymDao gymDao;
    private final Executor executor;

    @Inject
    public GymListRepository(GymDao gymDao, Executor executor) {
        this.gymDao = gymDao;
        this.executor = executor;
    }

    public LiveData<List<Gym>> getGym(int  sortType) {
        refreshGymList(sortType);
        // return a LiveData directly from the database.
       // return gymDao.getGymListByCity(cityId);
        return gymDao.loadAllList();
    }

    private void refreshGymList(int sortType) {
        boolean gymExist = (gymDao.loadAllList().getValue()!=null && gymDao.loadAllList().getValue().size() > 0 );
        if (!gymExist) {
            Log.d("refreshGymList", "!gymExist");

            executor.execute(() -> {

                ApiInterface apiService =
                        ApiClient.getClient().create(ApiInterface.class);
                Call<RetGymList> call = apiService.getGymList(0, 100,0, sortType);
                try {
                    Response<RetGymList> response = call.execute();
                    if (response.isSuccessful()) {
                        Log.d("refreshGymList", "run: response.isSuccessful cnt:"+response.body().getRecordsCount());

                        Log.d("refreshGymList", "run: newDao save:"+ gymDao.saveList(response.body().getRecords()).length);

                    } else {
                        Log.d("refreshGymList", "run: response.error");

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }


    }
}
