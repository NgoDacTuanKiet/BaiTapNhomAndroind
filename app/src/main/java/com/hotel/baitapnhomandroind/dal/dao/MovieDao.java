package com.hotel.baitapnhomandroind.dal.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.hotel.baitapnhomandroind.entities.Movie;

import java.util.List;

@Dao
public interface MovieDao {

    @Insert
    void insert(Movie movie);

    @Query("SELECT * FROM movies")
    LiveData<List<Movie>> getAll();

    @Query("SELECT * FROM movies")
    List<Movie> getAllSync();

    @Query("SELECT * FROM movies WHERE id = :id")
    Movie getById(int id);
}
