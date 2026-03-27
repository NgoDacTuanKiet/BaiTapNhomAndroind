package com.hotel.baitapnhomandroind.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "showtimes",
        foreignKeys = {
                @ForeignKey(
                        entity = Movie.class,
                        parentColumns = "id",
                        childColumns = "movieId",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = Theater.class,
                        parentColumns = "id",
                        childColumns = "theaterId",
                        onDelete = ForeignKey.CASCADE
                )
        },
        indices = {@Index("movieId"), @Index("theaterId")}
)
public class Showtime {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public int movieId;
    public int theaterId;

    public String showDate; // yyyy-MM-dd
    public String showTime; // HH:mm
}
