package com.hotel.baitapnhomandroind.dal.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.hotel.baitapnhomandroind.entities.Ticket;

import java.util.List;

@Dao
public interface TicketDao {

    @Insert
    long insert(Ticket ticket);

    @Query("SELECT * FROM tickets WHERE userId = :userId")
    List<Ticket> getByUser(int userId);

    @Query("SELECT seatNumber FROM tickets WHERE showtimeId = :showtimeId")
    List<String> getBookedSeats(int showtimeId);

    @Query("SELECT * FROM tickets WHERE id = :id")
    Ticket getById(int id);
}
