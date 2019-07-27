package com.example.arsalan.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.arsalan.mygym.models.WorkoutPlanReq;

import java.util.List;

import static androidx.room.OnConflictStrategy.IGNORE;
import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface WorkoutPlanRequestDao {
    @Insert(onConflict = IGNORE)
    long[] saveList(List<WorkoutPlanReq> workoutPlanReqs);

    @Insert(onConflict = REPLACE)
    long save(WorkoutPlanReq workoutPlanReq);


    @Query("SELECT * FROM WorkoutPlanReq WHERE status='waiting'")
    LiveData<List<WorkoutPlanReq>> loadAllWaitingList();

    @Query("SELECT * FROM WorkoutPlanReq")
    LiveData<List<WorkoutPlanReq>> loadAll();

    @Query("SELECT * FROM WorkoutPlanReq WHERE id = :id")
    LiveData<WorkoutPlanReq> getWorkoutPlanReqById(long id);


    @Query("SELECT * From WorkoutPlanReq WHERE parentUserId = :trainerId")
    LiveData<List<WorkoutPlanReq>> getWorkoutPlanReqByTrainer(long trainerId);

    @Query("DELETE FROM WorkoutPlanReq")
    void deleteAll();

    @Query("DELETE FROM WorkoutPlanReq WHERE id=:planId")
    int deleteById(long planId);

    @Query("UPDATE WorkoutPlanReq SET status=:status WHERE id=:mRequestId")
    void updateStatus(long mRequestId, String status);
}
