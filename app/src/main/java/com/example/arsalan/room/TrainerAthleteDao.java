package com.example.arsalan.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.arsalan.mygym.models.TrainerAthlete;

import java.util.List;

import static androidx.room.OnConflictStrategy.IGNORE;

@Dao
public interface TrainerAthleteDao {
    @Insert(onConflict = IGNORE)
    long[] saveList(List<TrainerAthlete> userList);



    @Query("SELECT * FROM TrainerAthlete")
    LiveData<List<TrainerAthlete>> loadAllList();

    @Query("SELECT * FROM TrainerAthlete WHERE athleteId = :id")
    LiveData<TrainerAthlete> getAthleteById(long id);


    @Query("SELECT * From TrainerAthlete WHERE trainerId = :trainerId AND status=:status")
    LiveData<List<TrainerAthlete>> getAthleteListByTrainerStatus(long trainerId,String status);

    @Query("DELETE FROM TrainerAthlete WHERE trainerId=:trainerId")
    int deleteByTrainerId(long trainerId);

    @Query("DELETE FROM TrainerAthlete WHERE id=:requestId")
    int deleteById(long requestId);

    @Query("UPDATE TrainerAthlete SET status=:status WHERE id=:requestId")
    int updateStatus(long requestId, String status);
}
