package com.example.arsalan.room;

import com.example.arsalan.mygym.models.GalleryItem;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface GalleryItemDao {
    @Insert(onConflict = REPLACE)
    long[] saveList(List<GalleryItem> galleryItemList);

    @Query("SELECT * FROM GalleryItem")
    LiveData<List<GalleryItem>> loadAllList();

    @Query("SELECT * FROM GalleryItem WHERE userId= :userId")
    LiveData<List<GalleryItem>> loadAllListById(long userId);

    @Query("DELETE FROM GalleryItem WHERE userId= :userId")
    void deleteAllListById(long userId);

}
