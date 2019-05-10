package com.example.arsalan.mygym.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.arsalan.mygym.models.Honor;
import com.example.arsalan.mygym.repository.HonorRepository;

import java.util.List;

import javax.inject.Inject;

public class HonorViewModel extends ViewModel {
    private HonorRepository honorRepo;
    private LiveData<List<Honor>> honorList;

    private MutableLiveData<Integer> honorTypeLD = new MutableLiveData<>();

    @Inject //  parameter is provided by Dagger 2
    public HonorViewModel(HonorRepository honorRepo) {
        this.honorRepo = honorRepo;
    }

    public void init(String token, long trainerId) {
        if (this.honorList != null) return;
        honorList=honorRepo.getHonorListByTrainerId(token, trainerId);
    }

    public LiveData<List<Honor>> getHonorList() {
        return this.honorList;
    }
}
