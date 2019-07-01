package com.example.arsalan.mygym.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.arsalan.mygym.models.GalleryItem;
import com.example.arsalan.mygym.repository.GalleryRepository;

import java.util.List;

import javax.inject.Inject;

public class GalleryViewModel extends ViewModel {
    private final GalleryRepository galleryItemRepo;
    private final LiveData<List<GalleryItem>> galleryItemList;

    private final MutableLiveData<Long> galleryItemTypeLD = new MutableLiveData<>();

    @Inject //  parameter is provided by Dagger 2
    public GalleryViewModel(GalleryRepository galleryItemRepo) {
        this.galleryItemRepo = galleryItemRepo;
         galleryItemList = Transformations.switchMap(galleryItemTypeLD, userId -> this.galleryItemRepo.getGalleryItem(userId));
    }

    public void init(long userId) {
        //if (this.galleryItemList!=null)return;
      galleryItemTypeLD.setValue(userId);
    }

    public LiveData<List<GalleryItem>> getGalleryItemList() {
        return this.galleryItemList;
    }
}
