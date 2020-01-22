package com.zalack.android.data.room.table;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity (tableName="user")
public class User {

    @ColumnInfo(name="id")
    @PrimaryKey
    @NonNull
    private String id;

    @ColumnInfo(name="password")
    private String password;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Ignore
    public User() {

    }

    public User(String id, String password) {
        this.id = id;
        this.password = password;
    }
}
