package com.example.clase10;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Weather {

    @PrimaryKey
    @NonNull
    public String fecha;

    public double grados;
    public String condicion;

    // Constructor
    public Weather(@NonNull String fecha, double grados, String condicion) {
        this.fecha = fecha;
        this.grados = grados;
        this.condicion = condicion;
    }
}