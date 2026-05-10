package com.attendance.app.data.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * Entidad que representa un registro de asistencia de un alumno en un curso.
 * Un alumno inscrito en un curso puede tener múltiples registros (uno por sesión/fecha).
 */
@Entity(
    tableName = "asistencias",
    foreignKeys = {
        @ForeignKey(
            entity = Alumno.class,
            parentColumns = "id",
            childColumns = "alumnoId",
            onDelete = ForeignKey.CASCADE
        ),
        @ForeignKey(
            entity = Curso.class,
            parentColumns = "id",
            childColumns = "cursoId",
            onDelete = ForeignKey.CASCADE
        )
    },
    indices = {
        @Index("alumnoId"),
        @Index("cursoId")
    }
)
public class Asistencia {

    @PrimaryKey(autoGenerate = true)
    public long id;

    public long alumnoId;
    public long cursoId;
    public String fecha;      // formato ISO-8601: "2025-05-10"
    public boolean presente;

    public Asistencia() {}

    public Asistencia(long alumnoId, long cursoId, String fecha, boolean presente) {
        this.alumnoId = alumnoId;
        this.cursoId  = cursoId;
        this.fecha    = fecha;
        this.presente = presente;
    }

    // ── Getters ──────────────────────────────────────────────────────────────

    public long getId()        { return id; }
    public long getAlumnoId()  { return alumnoId; }
    public long getCursoId()   { return cursoId; }
    public String getFecha()   { return fecha; }
    public boolean isPresente(){ return presente; }

    // ── Setters ──────────────────────────────────────────────────────────────

    public void setId(long id)              { this.id = id; }
    public void setAlumnoId(long alumnoId)  { this.alumnoId = alumnoId; }
    public void setCursoId(long cursoId)    { this.cursoId = cursoId; }
    public void setFecha(String fecha)      { this.fecha = fecha; }
    public void setPresente(boolean p)      { this.presente = p; }
}
