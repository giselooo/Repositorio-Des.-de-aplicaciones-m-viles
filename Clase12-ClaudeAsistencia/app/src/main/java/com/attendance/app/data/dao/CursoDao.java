package com.attendance.app.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.attendance.app.data.entities.Curso;

import java.util.List;

/**
 * DAO para operaciones CRUD sobre la tabla `cursos`.
 */
@Dao
public interface CursoDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(Curso curso);

    @Update
    void update(Curso curso);

    @Delete
    void delete(Curso curso);

    /** Todos los cursos, observables en tiempo real. */
    @Query("SELECT * FROM cursos ORDER BY nombre ASC")
    LiveData<List<Curso>> getAllCursos();

    /** Versión síncrona para tests o seed. */
    @Query("SELECT * FROM cursos ORDER BY nombre ASC")
    List<Curso> getAllCursosSync();

    @Query("SELECT COUNT(*) FROM cursos")
    int getCount();
}
