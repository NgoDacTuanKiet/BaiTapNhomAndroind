package com.hotel.baitapnhomandroind.dal.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.hotel.baitapnhomandroind.entities.User;

import java.util.List;

@Dao
public interface UserDao {

    @Insert
    long insert(User user);

    @Query("SELECT * FROM users WHERE username = :username AND password = :password LIMIT 1")
    User login(String username, String password);

    @Query("SELECT * FROM users")
    List<User> getAll();

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    User getByUsername(String username);

    @Query("SELECT COUNT(*) FROM users WHERE username = :username")
    int countUsername(String username);
}
