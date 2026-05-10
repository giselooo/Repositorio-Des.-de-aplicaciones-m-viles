package com.attendance.app.data.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

/**
 * Tabla intermedia para la relación Muchos-a-Muchos entre Alumno y Curso.
 * Un alumno puede estar inscrito en varios cursos y un curso puede tener varios alumnos.
 */
@Entity(
    tableName = "alumno_curso_cross_ref",
    primaryKeys = {"alumnoId", "cursoId"},
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
public class AlumnoCursoCrossRef {

    public long alumnoId;
    public long cursoId;

    public AlumnoCursoCrossRef(long alumnoId, long cursoId) {
        this.alumnoId = alumnoId;
        this.cursoId  = cursoId;
    }

    public long getAlumnoId() { return alumnoId; }
    public long getCursoId()  { return cursoId;  }
}
