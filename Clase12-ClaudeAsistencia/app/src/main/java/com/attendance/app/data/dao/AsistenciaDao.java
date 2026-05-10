package com.attendance.app.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.attendance.app.data.entities.Asistencia;

import java.util.List;

/**
 * DAO para el historial de asistencias.
 */
@Dao
public interface AsistenciaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Asistencia asistencia);

    @Update
    void update(Asistencia asistencia);

    /**
     * Historial completo de asistencias de un alumno en un curso específico,
     * ordenado por fecha descendente.
     */
    @Query("""
        SELECT * FROM asistencias
        WHERE alumnoId = :alumnoId AND cursoId = :cursoId
        ORDER BY fecha DESC
    """)
    LiveData<List<Asistencia>> getAsistencias(long alumnoId, long cursoId);

    /** Versión síncrona para tests. */
    @Query("""
        SELECT * FROM asistencias
        WHERE alumnoId = :alumnoId AND cursoId = :cursoId
        ORDER BY fecha DESC
    """)
    List<Asistencia> getAsistenciasSync(long alumnoId, long cursoId);

    @Query("SELECT COUNT(*) FROM asistencias")
    int getCount();

    /** Comprueba si ya existe una asistencia para alumno/curso/fecha. */
    @Query("""
        SELECT COUNT(*) FROM asistencias
        WHERE alumnoId = :alumnoId AND cursoId = :cursoId AND fecha = :fecha
    """)
    int existeRegistro(long alumnoId, long cursoId, String fecha);
}
