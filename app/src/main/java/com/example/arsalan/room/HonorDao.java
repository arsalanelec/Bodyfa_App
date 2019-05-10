package com.example.arsalan.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.arsalan.mygym.models.Honor;

import java.util.List;

import static androidx.room.OnConflictStrategy.IGNORE;
import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface HonorDao {
    @Insert(onConflict = IGNORE)
    long[] saveList(List<Honor> HonorList);

    @Insert(onConflict = REPLACE)
    long saveHonor(Honor Honor);


    @Query("SELECT * FROM Honor")
    LiveData<List<Honor>> loadAllList();

    @Query("SELECT * FROM Honor WHERE id = :id")
    LiveData<Honor> getHonorById(long id);


    @Query("SELECT * From Honor WHERE userId = :trainerId")
    LiveData<List<Honor>> getHonorByTrainer(long trainerId);
}
