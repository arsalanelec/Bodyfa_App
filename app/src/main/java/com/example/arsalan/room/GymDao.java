package com.example.arsalan.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.arsalan.mygym.models.Gym;

import java.util.List;

import static androidx.room.OnConflictStrategy.IGNORE;
import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface GymDao {
    @Insert(onConflict = IGNORE)
    long[] saveList(List<Gym> gymList);

    @Insert(onConflict = REPLACE)
    long save(Gym gym);

    @Query("SELECT * FROM Gym")
    LiveData<List<Gym>> loadAllList();

    @Query("Select * From Gym WHERE id= :id")
    LiveData<Gym> getGymById(long id);

    @Query("Select * From Gym WHERE id= :id")
   Gym getGymByIdMain(long id);
}
