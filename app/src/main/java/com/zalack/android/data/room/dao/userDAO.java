package com.zalack.android.data.room.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.zalack.android.data.room.table.User;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface userDAO {

    @Insert
    public long addUser(User user);

    @Update
    public void update(User user);

    @Delete
    public void deleteUser(User user);

    @Query("select * from user")
    Flowable<List<User>> getUser();

}
