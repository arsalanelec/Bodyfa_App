package com.example.arsalan.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.arsalan.mygym.models.Trainer;

import java.util.List;

import static androidx.room.OnConflictStrategy.IGNORE;
import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface TrainerDao {
    @Insert(onConflict = IGNORE)
    long[] saveList(List<Trainer> trainerList);

    @Insert(onConflict = REPLACE)
    long save(Trainer trainer);

    @Query("SELECT * FROM Trainer")
    LiveData<List<Trainer>> loadAllList();

    @Query("Select * From Trainer WHERE id= :id")
    LiveData<Trainer> getTrainerById(long id);

    @Query("Select * From Trainer WHERE id= :id")
    Trainer getTrainerByIdMain(long id);

    @Query("DELETE FROM Trainer")
    void deleteAll();
    @Query("SELECT * FROM Trainer ORDER BY point DESC")
    LiveData<List<Trainer>> loadAllListOrderByRate();
}
