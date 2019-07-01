package com.example.arsalan.mygym.repository;

import androidx.lifecycle.LiveData;
import android.util.Log;

import com.example.arsalan.mygym.models.RetTrainer;
import com.example.arsalan.mygym.models.Trainer;
import com.example.arsalan.mygym.models.RetTrainerList;
import com.example.arsalan.mygym.retrofit.ApiClient;
import com.example.arsalan.mygym.retrofit.ApiInterface;
import com.example.arsalan.room.TrainerDao;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Response;


@Singleton  // informs Dagger that this class should be constructed once
public class TrainerListRepository {
    private final TrainerDao trainerDao;
    private final Executor executor;

    @Inject
    public TrainerListRepository(TrainerDao trainerDao, Executor executor) {
        this.trainerDao = trainerDao;
        this.executor = executor;
    }

    public LiveData<List<Trainer>> getTrainerListByCity(int  cityId) {
        refreshTrainerListByCity(cityId);
        // return a LiveData directly from the database.
       // return trainerDao.getTrainerListByCity(cityId);
        return trainerDao.loadAllListOrderByRate();
    }

    public LiveData<Trainer> getTrainerById(long id) {
        refreshTrainer(id);
        return trainerDao.getTrainerById(id);
    }

    private void refreshTrainerListByCity(int cityId) {
            executor.execute(() -> {

                ApiInterface apiService =
                        ApiClient.getClient().create(ApiInterface.class);
                Call<RetTrainerList> call = apiService.getTrainerList(0, 100,0, cityId, 1);
                try {
                    Response<RetTrainerList> response = call.execute();
                    if (response.isSuccessful()) {
                        Log.d("refreshTrainerListCity", "run: response.isSuccessful cnt:"+response.body().getRecordsCount());
                        trainerDao.deleteAll();
                        Log.d("refreshTrainerListCity", "run: TrainerDao save:"+ trainerDao.saveList(response.body().getRecords()).length);

                    } else {
                        Log.d("refreshTrainerListCity", "run: response.error");

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

    }
    private void refreshTrainer(long trainerId) {
      //  boolean trainerExist = (trainerDao.getTrainerById(trainerId)!=null && trainerDao.getTrainerById(trainerId).getValue()!=null );
      //  if (!trainerExist) {


            executor.execute(() -> {

                ApiInterface apiService =
                        ApiClient.getClient().create(ApiInterface.class);
                Call<RetTrainer> call = apiService.getTrainerDetail(trainerId);
                try {
                    Response<RetTrainer> response = call.execute();
                    if (response.isSuccessful()) {
                        Log.d("refreshTrainerListById", "run: newDao save:" + trainerDao.save(response.body().getRecord()));

                    } else {
                        Log.d("refreshTrainerListById", "run: response.error");

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }


   // }
}
