package com.hotel.baitapnhomandroind.dal.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.hotel.baitapnhomandroind.entities.Movie;
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

    @Query("SELECT DISTINCT showDate, showTime FROM showtimes WHERE theaterId = :theaterId")
    List<ShowtimeDateGroup> getDistinctDateTimesByTheater(int theaterId);

    @Query("SELECT m.* FROM movies m INNER JOIN showtimes s ON m.id = s.movieId WHERE s.theaterId = :theaterId AND s.showDate = :showDate AND s.showTime = :showTime")
    List<Movie> getMoviesByShowtime(int theaterId, String showDate, String showTime);

    @Query("SELECT id FROM showtimes WHERE theaterId = :theaterId AND movieId = :movieId AND showDate = :showDate AND showTime = :showTime LIMIT 1")
    int getShowtimeId(int theaterId, int movieId, String showDate, String showTime);

    @Query("SELECT * FROM showtimes WHERE id = :id")
    Showtime getById(int id);

    class ShowtimeDateGroup {
        public String showDate;
        public String showTime;
    }
}
