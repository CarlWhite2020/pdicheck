package com.eucleia.pdicheck.room.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.eucleia.pdicheck.room.entity.User;

import java.util.List;

@Dao
public interface UserDao {

    @Query("SELECT * FROM user")
    List<User> loadAll();

    @Query("SELECT * FROM user WHERE  userCode is not null")
    User getCurrUser();

    @Query("DELETE From user")
    void resetUser();

    @Insert
    void insertUser(User user);


}
