package com.example.arsalan.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.arsalan.mygym.models.InboxItem;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface InboxItemDao {
    @Insert(onConflict = REPLACE)
    long[] saveList(List<InboxItem> inboxItems);

    @Query("SELECT * FROM InboxItem")
    LiveData<List<InboxItem>> loadAllList();

    @Query("DELETE FROM InboxItem WHERE userId=:userID")
    int deleteByUserId(long userID  );

    @Query("DELETE FROM InboxItem")
    int deleteAll();

    @Query("SELECT * FROM InboxItem WHERE userId=:uerId")
    LiveData<List<InboxItem>> loadAllListByUserId(long uerId);
}
