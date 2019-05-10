package com.example.arsalan.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.arsalan.mygym.models.TutorialVideo;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface TutorialVideoDao {
    @Insert(onConflict = REPLACE)
    long[] saveList(List<TutorialVideo> tutorialVideoList);

    @Insert(onConflict = REPLACE)
    long save(TutorialVideo tutorialVideo);

    @Query("SELECT * FROM TutorialVideo")
    LiveData<List<TutorialVideo>> loadAllList();

    @Query("Select * From TutorialVideo WHERE id= :id")
    LiveData<TutorialVideo> getTutorialVideoById(long id);

    @Query("Select * From TutorialVideo WHERE subCatId= :subCatId")
    LiveData<List<TutorialVideo>> getTutorialVideoBySubCat(long subCatId);


}
