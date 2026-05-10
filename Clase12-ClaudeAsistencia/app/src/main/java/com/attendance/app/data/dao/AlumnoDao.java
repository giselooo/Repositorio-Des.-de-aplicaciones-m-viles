package com.attendance.app.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.attendance.app.data.entities.Alumno;
import com.attendance.app.data.entities.AlumnoCursoCrossRef;

import java.util.List;

/**
 * DAO para operaciones sobre `alumnos` e inscripciones.
 */
@Dao
public interface AlumnoDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(Alumno alumno);

    @Update
    void update(Alumno alumno);

    @Delete
    void delete(Alumno alumno);

    /** Inscribe a un alumno en un curso (inserta en la tabla cruzada). */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void inscribir(AlumnoCursoCrossRef crossRef);

    /** Elimina la inscripción de un alumno en un curso. */
    @Query("DELETE FROM alumno_curso_cross_ref WHERE alumnoId = :alumnoId AND cursoId = :cursoId")
    void desinscribir(long alumnoId, long cursoId);

    /** Alumnos inscritos en un curso dado, como LiveData. */
    @Query("""
        SELECT a.* FROM alumnos a
        INNER JOIN alumno_curso_cross_ref ref ON a.id = ref.alumnoId
        WHERE ref.cursoId = :cursoId
        ORDER BY a.nombre ASC
    """)
    LiveData<List<Alumno>> getAlumnosPorCurso(long cursoId);

    /** Versión síncrona para tests. */
    @Query("""
        SELECT a.* FROM alumnos a
        INNER JOIN alumno_curso_cross_ref ref ON a.id = ref.alumnoId
        WHERE ref.cursoId = :cursoId
        ORDER BY a.nombre ASC
    """)
    List<Alumno> getAlumnosPorCursoSync(long cursoId);

    @Query("SELECT COUNT(*) FROM alumno_curso_cross_ref WHERE alumnoId = :alumnoId AND cursoId = :cursoId")
    int isInscrito(long alumnoId, long cursoId);

    @Query("SELECT COUNT(*) FROM alumnos")
    int getCount();
}
