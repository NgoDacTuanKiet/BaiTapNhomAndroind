package com.hotel.baitapnhomandroind.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "theaters")
public class Theater {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;
    public String location;
}
