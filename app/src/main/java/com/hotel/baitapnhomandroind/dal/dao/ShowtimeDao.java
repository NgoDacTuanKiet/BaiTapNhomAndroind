package com.hotel.baitapnhomandroind.dal.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.hotel.baitapnhomandroind.entities.Showtime;

import java.util.List;

@Dao
public interface ShowtimeDao {

    @Insert
    void insert(Showtime showtime);

    @Query("SELECT * FROM showtimes WHERE movieId = :movieId")
    List<Showtime> getByMovie(int movieId);

    @Query("SELECT * FROM showtimes")
    List<Showtime> getAll();
}
