package com.example.arsalan.mygym.repository;

import androidx.lifecycle.LiveData;
import android.util.Log;

import com.example.arsalan.mygym.models.GalleryItem;
import com.example.arsalan.mygym.models.RetGalleryList;
import com.example.arsalan.mygym.models.Token;
import com.example.arsalan.mygym.retrofit.ApiClient;
import com.example.arsalan.mygym.retrofit.ApiInterface;
import com.example.arsalan.room.GalleryItemDao;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Response;


@Singleton  // informs Dagger that this class should be constructed once
public class GalleryRepository {
    private final GalleryItemDao galleryItemDao;
    private final Executor executor;
private final Token mToken;
    @Inject
    public GalleryRepository(GalleryItemDao galleryItemDao, Executor executor, Token token) {
        this.galleryItemDao = galleryItemDao;
        this.executor = executor;
        mToken=token;
    }

    public LiveData<List<GalleryItem>> getGalleryItem( long userId) {
        refreshGalleryItemList( userId);
        // return a LiveData directly from the database.
        // return galleryItemDao.getGalleryItemListByCity(cityId);
        return galleryItemDao.loadAllListById(userId);
    }

    private void refreshGalleryItemList( long userId) {
        boolean galleryItemExist = (galleryItemDao.loadAllList().getValue() != null && galleryItemDao.loadAllList().getValue().size() > 0);
        if (!galleryItemExist) {
            Log.d("refreshGalleryItemList", "!galleryItemExist");

            executor.execute(() -> {

                ApiInterface apiService =
                        ApiClient.getClient().create(ApiInterface.class);
                Call<RetGalleryList> call = apiService.getMyGallery(mToken.getTokenBearer(), userId);
                try {
                    Response<RetGalleryList> response = call.execute();
                    if (response.isSuccessful()) {
                        Log.d("refreshGalleryItemList", "run: response.isSuccessful cnt:" + response.body().getRecordsCount());
                        galleryItemDao.deleteAllListById(userId);
                        Log.d("refreshGalleryItemList", "run: newDao save:" + galleryItemDao.saveList(response.body().getRecords()).length);

                    } else {
                        Log.d("refreshGalleryItemList", "run: response.error");

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }


    }
}
