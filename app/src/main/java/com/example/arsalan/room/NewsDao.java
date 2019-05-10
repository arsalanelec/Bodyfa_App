package com.example.arsalan.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.arsalan.mygym.models.News;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface NewsDao {
    @Insert(onConflict = REPLACE)
    long[] saveList(List<News> newsList);

    @Query("SELECT * FROM News")
    LiveData<List<News>> loadList();

    @Query("Select * From News WHERE typeId= :typeId")
    LiveData<List<News>> getNewsListByType(int typeId);

    @Query("DELETE FROM News")
    void deleteAll();
}
