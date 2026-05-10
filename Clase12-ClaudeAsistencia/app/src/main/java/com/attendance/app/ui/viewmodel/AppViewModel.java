package com.attendance.app.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.attendance.app.data.entities.Alumno;
import com.attendance.app.data.entities.Asistencia;
import com.attendance.app.data.entities.Curso;
import com.attendance.app.data.repository.AppRepository;

import java.util.List;

/**
 * ViewModel único que expone LiveData para los tres fragmentos.
 * Usa Transformations.switchMap para que los datos de alumnos
 * y asistencias reaccionen al curso/alumno seleccionado.
 */
public class AppViewModel extends AndroidViewModel {

    private final AppRepository repository;

    // ── Cursos ────────────────────────────────────────────────────────────────

    private final LiveData<List<Curso>> allCursos;

    // ── Alumnos por curso ─────────────────────────────────────────────────────

    /** ID del curso actualmente seleccionado. */
    private final MutableLiveData<Long> cursoIdSeleccionado = new MutableLiveData<>();

    /** Lista de alumnos que reacciona al cursoId seleccionado. */
    private final LiveData<List<Alumno>> alumnosPorCurso;

    // ── Asistencias ───────────────────────────────────────────────────────────

    /** Clave compuesta: alumnoId + cursoId para buscar asistencias. */
    private final MutableLiveData<long[]> asistenciaKey = new MutableLiveData<>();

    /** Historial de asistencias que reacciona al par alumnoId/cursoId. */
    private final LiveData<List<Asistencia>> asistencias;

    // ── Constructor ───────────────────────────────────────────────────────────

    public AppViewModel(@NonNull Application application) {
        super(application);
        repository = new AppRepository(application);

        allCursos = repository.getAllCursos();

        alumnosPorCurso = Transformations.switchMap(
                cursoIdSeleccionado,
                cursoId -> repository.getAlumnosPorCurso(cursoId)
        );

        asistencias = Transformations.switchMap(
                asistenciaKey,
                key -> {
                    if (key == null || key.length < 2) return null;
                    return repository.getAsistencias(key[0], key[1]);
                }
        );
    }

    // ── API pública ───────────────────────────────────────────────────────────

    // Cursos
    public LiveData<List<Curso>> getAllCursos() {
        return allCursos;
    }

    // Alumnos
    public void seleccionarCurso(long cursoId) {
        cursoIdSeleccionado.setValue(cursoId);
    }

    public LiveData<List<Alumno>> getAlumnosPorCurso() {
        return alumnosPorCurso;
    }

    // Asistencias
    public void seleccionarAlumnoYCurso(long alumnoId, long cursoId) {
        asistenciaKey.setValue(new long[]{alumnoId, cursoId});
    }

    public LiveData<List<Asistencia>> getAsistencias() {
        return asistencias;
    }

    public void registrarAsistencia(long alumnoId, long cursoId,
                                    String fecha, boolean presente) {
        Asistencia a = new Asistencia(alumnoId, cursoId, fecha, presente);
        repository.insertAsistencia(a);
    }

    public void actualizarAsistencia(Asistencia asistencia) {
        repository.updateAsistencia(asistencia);
    }
}
