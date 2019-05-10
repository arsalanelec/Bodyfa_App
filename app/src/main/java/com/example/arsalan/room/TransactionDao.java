package com.example.arsalan.room;

import com.example.arsalan.mygym.models.News;
import com.example.arsalan.mygym.models.Transaction;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface TransactionDao {
    @Insert(onConflict = REPLACE)
    long[] saveList(List<Transaction> transactions);

    @Query("SELECT * FROM utransaction")
    LiveData<List<Transaction>> loadList();

    @Query("DELETE From utransaction")
    void deleteAll();
}
