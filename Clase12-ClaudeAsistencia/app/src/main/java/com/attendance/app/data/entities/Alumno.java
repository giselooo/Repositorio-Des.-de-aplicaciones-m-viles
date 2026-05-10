package com.attendance.app.data.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Entidad que representa a un alumno en la base de datos.
 */
@Entity(tableName = "alumnos")
public class Alumno {

    @PrimaryKey(autoGenerate = true)
    public long id;

    public String nombre;
    public String matricula;

    public Alumno() {}

    public Alumno(String nombre, String matricula) {
        this.nombre = nombre;
        this.matricula = matricula;
    }

    // ── Getters ──────────────────────────────────────────────────────────────

    public long getId()          { return id; }
    public String getNombre()    { return nombre; }
    public String getMatricula() { return matricula; }

    // ── Setters ──────────────────────────────────────────────────────────────

    public void setId(long id)              { this.id = id; }
    public void setNombre(String nombre)    { this.nombre = nombre; }
    public void setMatricula(String mat)    { this.matricula = mat; }
}
