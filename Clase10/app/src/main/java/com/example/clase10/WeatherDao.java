package com.example.clase10;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import java.util.List;

@Dao
public interface WeatherDao {
    @Query("SELECT * FROM weather")
    List<Weather> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE) //para q no haya repetidos
    void insert(Weather weather);
}