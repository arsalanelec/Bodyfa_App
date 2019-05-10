package com.example.arsalan.room;

import com.example.arsalan.mygym.models.User;
import com.example.arsalan.mygym.models.UserCredit;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import static androidx.room.OnConflictStrategy.IGNORE;
import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface CreditDao {

    @Insert(onConflict = REPLACE)
    long save(UserCredit userCredit);


    @Query("SELECT * FROM UserCredit WHERE userId=:userId ")
    LiveData<UserCredit> getCreadit(long userId);


    @Query("DELETE  From UserCredit ")
    void deleteAllCredits();
}
