package com.example.arsalan.mygym.repository;

import androidx.lifecycle.LiveData;
import android.util.Log;

import com.example.arsalan.mygym.models.RetTutorialVideoList;
import com.example.arsalan.mygym.models.TutorialVideo;
import com.example.arsalan.mygym.retrofit.ApiClient;
import com.example.arsalan.mygym.retrofit.ApiInterface;
import com.example.arsalan.room.TutorialVideoDao;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Response;


@Singleton  // informs Dagger that this class should be constructed once
public class TutorialVideoListRepository {
    private final TutorialVideoDao tutorialVideoDao;
    private final Executor executor;

    @Inject
    public TutorialVideoListRepository(TutorialVideoDao tutorialVideoDao, Executor executor) {
        this.tutorialVideoDao = tutorialVideoDao;
        this.executor = executor;
    }

    public LiveData<List<TutorialVideo>> getTutorialVideoListBySubCat(long subCatId) {
        refreshTutorialVideoListBySubCat(subCatId);
        // return a LiveData directly from the database.
        // return tutorialVideoDao.getTutorialVideoListBySubCat(cityId);
        return tutorialVideoDao.getTutorialVideoBySubCat(subCatId);
    }


    private void refreshTutorialVideoListBySubCat(long subCatId) {
        boolean tutorialVideoExist = (tutorialVideoDao.loadAllList().getValue() != null && tutorialVideoDao.loadAllList().getValue().size() > 0);
        if (!tutorialVideoExist) {
            Log.d("refreshTutorialVidList", "!tutorialVideoExist");

            executor.execute(() -> {

                ApiInterface apiService =
                        ApiClient.getClient().create(ApiInterface.class);
                Call<RetTutorialVideoList> call = apiService.getTutorialVideoList(0, 100, subCatId);
                try {
                    Response<RetTutorialVideoList> response = call.execute();
                    if (response.isSuccessful()) {
                        Log.d("refreshTutorialVidList", "run: response.isSuccessful cnt:" + response.body().getRecordsCount());

                        Log.d("refreshTutorialVidList", "run: newDao save:" + tutorialVideoDao.saveList(response.body().getRecords()).length);

                    } else {
                        Log.d("refreshTutorialVidList", "run: response.error");

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }


    }


    // }
}
