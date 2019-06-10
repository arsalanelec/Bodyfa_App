package com.example.arsalan.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.arsalan.mygym.models.News;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface NewsDetailDao {
    @Insert(onConflict = REPLACE)
    long[] saveList(List<News> newsList);

    @Insert(onConflict = REPLACE)
    long save(News news);


    @Query("Select * From News WHERE id= :id")
    LiveData<News> getNews(long id);

    @Query("DELETE FROM News WHERE id=:id")
    void delete(long id);
}
