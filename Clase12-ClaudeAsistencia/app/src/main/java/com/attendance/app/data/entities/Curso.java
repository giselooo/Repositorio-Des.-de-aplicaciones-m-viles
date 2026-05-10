package com.attendance.app.data.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Entidad que representa un curso/grupo en la base de datos.
 */
@Entity(tableName = "cursos")
public class Curso {

    @PrimaryKey(autoGenerate = true)
    public long id;

    public String nombre;
    public String codigo;

    public Curso() {}

    public Curso(String nombre, String codigo) {
        this.nombre = nombre;
        this.codigo = codigo;
    }

    // ── Getters ──────────────────────────────────────────────────────────────

    public long getId()       { return id; }
    public String getNombre() { return nombre; }
    public String getCodigo() { return codigo; }

    // ── Setters ──────────────────────────────────────────────────────────────

    public void setId(long id)            { this.id = id; }
    public void setNombre(String nombre)  { this.nombre = nombre; }
    public void setCodigo(String codigo)  { this.codigo = codigo; }
}
