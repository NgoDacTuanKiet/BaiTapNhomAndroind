package com.hotel.baitapnhomandroind.dal.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.hotel.baitapnhomandroind.entities.Ticket;

import java.util.List;

@Dao
public interface TicketDao {

    @Insert
    void insert(Ticket ticket);

    @Query("SELECT * FROM tickets WHERE userId = :userId")
    List<Ticket> getByUser(int userId);
}