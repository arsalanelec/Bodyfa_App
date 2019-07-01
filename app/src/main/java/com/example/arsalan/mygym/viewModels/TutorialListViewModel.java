package com.example.arsalan.mygym.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.arsalan.mygym.models.TutorialVideo;
import com.example.arsalan.mygym.repository.TutorialVideoListRepository;

import java.util.List;

import javax.inject.Inject;

public class TutorialListViewModel extends ViewModel {
    private final TutorialVideoListRepository tutorialVideoRepo;
    private final LiveData<List<TutorialVideo>> tutorialVideoList;

    private final MutableLiveData<Long> tutorialVideoSubCat = new MutableLiveData<>();

    @Inject //  parameter is provided by Dagger 2
    public TutorialListViewModel(TutorialVideoListRepository tutorialVideoRepo) {
        this.tutorialVideoRepo = tutorialVideoRepo;
        tutorialVideoList = Transformations.switchMap(tutorialVideoSubCat, subCatId -> this.tutorialVideoRepo.getTutorialVideoListBySubCat(subCatId));
    }

    public void init(long subCatId) {
        //if (this.tutorialVideoList!=null)return;
        this.tutorialVideoSubCat.setValue(subCatId);
    }

    public LiveData<List<TutorialVideo>> getTutorialVideoList() {
        return this.tutorialVideoList;
    }
}
