package com.example.arsalan.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.arsalan.mygym.models.NewsHead;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface NewsHeadDao {
    @Insert(onConflict = REPLACE)
    long[] saveList(List<NewsHead> newsList);

    @Query("SELECT * FROM NewsHead")
    LiveData<List<NewsHead>> loadList();

    @Query("Select * From NewsHead WHERE typeId= :typeId")
    LiveData<List<NewsHead>> getNewsListByType(int typeId);

    @Query("DELETE FROM NewsHead")
    void deleteAll();
}
