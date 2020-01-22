package com.zalack.android.data.room;

import android.service.autofill.UserData;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.zalack.android.data.room.dao.userDAO;
import com.zalack.android.data.room.table.User;

@Database(entities = {User.class}, version = 1)
public abstract class UserDatabase extends RoomDatabase {

    public abstract userDAO getUserDao();
}
