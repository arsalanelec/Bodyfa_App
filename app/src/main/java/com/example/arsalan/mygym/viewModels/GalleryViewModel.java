package com.example.arsalan.mygym.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.arsalan.mygym.models.GalleryItem;
import com.example.arsalan.mygym.repository.GalleryRepository;

import java.util.List;

import javax.inject.Inject;

public class GalleryViewModel extends ViewModel {
    private GalleryRepository galleryItemRepo;
    private LiveData<List<GalleryItem>> galleryItemList;

    private MutableLiveData<Integer> galleryItemTypeLD = new MutableLiveData<>();

    @Inject //  parameter is provided by Dagger 2
    public GalleryViewModel(GalleryRepository galleryItemRepo) {
        this.galleryItemRepo = galleryItemRepo;
        // galleryItemList = Transformations.switchMap(galleryItemTypeLD, cityId -> this.galleryItemRepo.getGalleryItem(cityId));
    }

    public void init(String token, long userId) {
        //if (this.galleryItemList!=null)return;
        galleryItemList = galleryItemRepo.getGalleryItem(token, userId);
    }

    public LiveData<List<GalleryItem>> getGalleryItemList() {
        return this.galleryItemList;
    }
}
