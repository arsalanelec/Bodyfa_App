package com.example.arsalan.mygym.repository;

import androidx.lifecycle.LiveData;
import android.util.Log;

import com.example.arsalan.mygym.models.RetHonorList;
import com.example.arsalan.mygym.models.Honor;
import com.example.arsalan.mygym.retrofit.ApiClient;
import com.example.arsalan.mygym.retrofit.ApiInterface;
import com.example.arsalan.room.HonorDao;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Response;


@Singleton  // informs Dagger that this class should be constructed once
public class HonorRepository {
    private final HonorDao honorDao;
    private final Executor executor;

    @Inject
    public HonorRepository(HonorDao honorDao, Executor executor) {
        this.honorDao = honorDao;
        this.executor = executor;
    }

    public LiveData<List<Honor>> getHonorListByTrainerId(String token, long trainerId) {
        refreshHonorListByTrainerId(token, trainerId);
        return honorDao.getHonorByTrainer(trainerId);
    }


    private void refreshHonorListByTrainerId(String token, long trainerId) {
        boolean honorExist = (honorDao.loadAllList().getValue() != null && honorDao.loadAllList().getValue().size() > 0);
        if (!honorExist) {
            Log.d("refreshHonorListCity", "!honorExist");

            executor.execute(() -> {

                ApiInterface apiService =
                        ApiClient.getClient().create(ApiInterface.class);
                Call<RetHonorList> call = apiService.getHonorList(token, trainerId);
                try {
                    Response<RetHonorList> response = call.execute();
                    if (response.isSuccessful()&&response.body()!=null) {
                        Log.d("refreshHonorListCity", "run: response.isSuccessful cnt:" + response.body().getRecordsCount());

                        Log.d("refreshHonorListCity", "run: honorDao save:" + honorDao.saveList(response.body().getRecords()).length);

                    } else {
                        Log.d("refreshHonorListCity", "run: response.error");

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }


    }


    // }
}
