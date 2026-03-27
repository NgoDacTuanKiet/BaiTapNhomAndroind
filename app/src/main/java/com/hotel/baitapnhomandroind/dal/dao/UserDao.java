package com.hotel.baitapnhomandroind.dal.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.hotel.baitapnhomandroind.entities.User;

import java.util.List;

@Dao
public interface UserDao {

    @Insert
    void insert(User user);

    @Query("SELECT * FROM users WHERE username = :username AND password = :password LIMIT 1")
    User login(String username, String password);

    @Query("SELECT * FROM users")
    List<User> getAll();
}