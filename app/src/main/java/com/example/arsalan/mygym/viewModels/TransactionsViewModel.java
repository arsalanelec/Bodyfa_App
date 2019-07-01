package com.example.arsalan.mygym.viewModels;

import com.example.arsalan.mygym.models.Transaction;
import com.example.arsalan.mygym.repository.TransactionsRepository;

import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

public class TransactionsViewModel extends ViewModel {
    private final TransactionsRepository repository;
    private final LiveData<List<Transaction>> transactionListLd;

    private final MutableLiveData<Long> trainerIdLD = new MutableLiveData<>();

    @Inject //  parameter is provided by Dagger 2
    public TransactionsViewModel(TransactionsRepository repository) {
        this.repository = repository;
        transactionListLd = Transformations.switchMap(trainerIdLD, id -> this.repository.getTransaction(id));
    }

    public void init(long userId) {
        this.trainerIdLD.setValue(userId);
    }

    public LiveData<List<Transaction>> getTransactionList() {
        return this.transactionListLd;
    }
}
