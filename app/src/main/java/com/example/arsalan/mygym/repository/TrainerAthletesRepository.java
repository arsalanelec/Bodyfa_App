package com.example.arsalan.mygym.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.arsalan.mygym.models.RetTrainerAthleteList;
import com.example.arsalan.mygym.models.TrainerAthlete;
import com.example.arsalan.mygym.retrofit.ApiClient;
import com.example.arsalan.mygym.retrofit.ApiInterface;
import com.example.arsalan.room.TrainerAthleteDao;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;


@Singleton  // informs Dagger that this class should be constructed once
public class TrainerAthletesRepository {
    private static final String TAG = "TrainerAthletesReposito";
    private final TrainerAthleteDao trainerAthleteDao;
    private final Executor executor;

    @Inject
    public TrainerAthletesRepository(TrainerAthleteDao trainerAthleteDao, Executor executor) {
        this.trainerAthleteDao=trainerAthleteDao;
        this.executor = executor;
    }

    public LiveData<List<TrainerAthlete>> getTrainerAthlete(String token, long userId) {
        refreshList(token, userId);

        return trainerAthleteDao.getByAthleteId(userId);
    }


    private void refreshList(String token, long userId) {

            executor.execute(() -> {

                ApiInterface apiService =
                        ApiClient.getClient().create(ApiInterface.class);
                MediaType plainMT = MediaType.parse("text/plain");
                Map<String, RequestBody> requestBodyMap = new HashMap<>();
                requestBodyMap.put("UserId", RequestBody.create(plainMT, String.valueOf(userId)));
                requestBodyMap.put("type", RequestBody.create(plainMT, "trainer"));
                Call<RetTrainerAthleteList> call = apiService.getAthleteMembershipRequests(token, requestBodyMap);
                try {
                    Response<RetTrainerAthleteList> response = call.execute();
                    if (response.isSuccessful()) {
                        Log.d(TAG, "run: response.isSuccessful cnt:" + response.body().getRecordsCount());
                        trainerAthleteDao.deleteAll();
                        trainerAthleteDao.saveList(response.body().getRecords());
                    } else {
                        Log.d(TAG, "run: response.error");

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
}
