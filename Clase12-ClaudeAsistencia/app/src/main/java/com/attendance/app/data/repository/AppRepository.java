package com.attendance.app.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.attendance.app.data.dao.AlumnoDao;
import com.attendance.app.data.dao.AsistenciaDao;
import com.attendance.app.data.dao.CursoDao;
import com.attendance.app.data.db.AppDatabase;
import com.attendance.app.data.entities.Alumno;
import com.attendance.app.data.entities.AlumnoCursoCrossRef;
import com.attendance.app.data.entities.Asistencia;
import com.attendance.app.data.entities.Curso;

import java.util.List;

/**
 * Repositorio único que abstrae el acceso a datos y expone
 * las operaciones necesarias para el ViewModel.
 */
public class AppRepository {

    private final AlumnoDao    alumnoDao;
    private final CursoDao     cursoDao;
    private final AsistenciaDao asistenciaDao;

    public AppRepository(Application application) {
        AppDatabase db  = AppDatabase.getInstance(application);
        alumnoDao       = db.alumnoDao();
        cursoDao        = db.cursoDao();
        asistenciaDao   = db.asistenciaDao();
    }

    // ── Cursos ────────────────────────────────────────────────────────────────

    public LiveData<List<Curso>> getAllCursos() {
        return cursoDao.getAllCursos();
    }

    public void insertCurso(Curso curso) {
        AppDatabase.DB_EXECUTOR.execute(() -> cursoDao.insert(curso));
    }

    // ── Alumnos ───────────────────────────────────────────────────────────────

    public LiveData<List<Alumno>> getAlumnosPorCurso(long cursoId) {
        return alumnoDao.getAlumnosPorCurso(cursoId);
    }

    public void insertAlumno(Alumno alumno) {
        AppDatabase.DB_EXECUTOR.execute(() -> alumnoDao.insert(alumno));
    }

    public void inscribir(long alumnoId, long cursoId) {
        AppDatabase.DB_EXECUTOR.execute(() ->
                alumnoDao.inscribir(new AlumnoCursoCrossRef(alumnoId, cursoId)));
    }

    // ── Asistencias ───────────────────────────────────────────────────────────

    public LiveData<List<Asistencia>> getAsistencias(long alumnoId, long cursoId) {
        return asistenciaDao.getAsistencias(alumnoId, cursoId);
    }

    public void insertAsistencia(Asistencia asistencia) {
        AppDatabase.DB_EXECUTOR.execute(() -> asistenciaDao.insert(asistencia));
    }

    public void updateAsistencia(Asistencia asistencia) {
        AppDatabase.DB_EXECUTOR.execute(() -> asistenciaDao.update(asistencia));
    }
}
