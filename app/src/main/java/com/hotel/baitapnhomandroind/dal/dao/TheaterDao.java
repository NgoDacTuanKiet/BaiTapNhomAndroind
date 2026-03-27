package com.hotel.baitapnhomandroind.dal.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.hotel.baitapnhomandroind.entities.Theater;

import java.util.List;

@Dao
public interface TheaterDao {

    @Insert
    void insert(Theater theater);

    @Query("SELECT * FROM theaters")
    List<Theater> getAll();

    @Query("SELECT * FROM theaters")
    List<Theater> getAllSync();
}
