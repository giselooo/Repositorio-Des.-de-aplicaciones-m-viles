package com.attendance.app;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import com.attendance.app.data.dao.AlumnoDao;
import com.attendance.app.data.dao.AsistenciaDao;
import com.attendance.app.data.dao.CursoDao;
import com.attendance.app.data.db.AppDatabase;
import com.attendance.app.data.entities.Alumno;
import com.attendance.app.data.entities.AlumnoCursoCrossRef;
import com.attendance.app.data.entities.Asistencia;
import com.attendance.app.data.entities.Curso;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;

/**
 * Pruebas unitarias para los DAOs usando una base de datos Room en memoria.
 *
 * <p>Robolectric provee el contexto Android sin necesidad de emulador.
 * {@link InstantTaskExecutorRule} hace que LiveData se resuelva síncronamente.</p>
 *
 * Casos cubiertos:
 * <ol>
 *   <li>Inserción y recuperación de un Curso.</li>
 *   <li>Inserción y recuperación de un Alumno.</li>
 *   <li>Vinculación (inscripción) de un alumno a un curso.</li>
 *   <li>Consulta de alumnos por curso.</li>
 *   <li>Un alumno inscrito en múltiples cursos.</li>
 *   <li>Un curso con múltiples alumnos.</li>
 *   <li>Inserción y recuperación de una asistencia.</li>
 *   <li>Verificar que no se duplican asistencias del mismo día.</li>
 * </ol>
 */
@RunWith(RobolectricTestRunner.class)
@Config(sdk = 33)
public class DaoTest {

    // Hace que LiveData.observe() ejecute en el hilo actual (tests síncronos)
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private AppDatabase db;
    private CursoDao    cursoDao;
    private AlumnoDao   alumnoDao;
    private AsistenciaDao asistenciaDao;

    // ── Setup / Teardown ──────────────────────────────────────────────────────

    @Before
    public void setUp() {
        Context ctx = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(ctx, AppDatabase.class)
                .allowMainThreadQueries()   // Solo para tests
                .build();
        cursoDao      = db.cursoDao();
        alumnoDao     = db.alumnoDao();
        asistenciaDao = db.asistenciaDao();
    }

    @After
    public void tearDown() {
        if (db != null && db.isOpen()) {
            db.close();
        }
    }

    // ── Test 1: Inserción de un Curso ─────────────────────────────────────────

    @Test
    public void insertCurso_retornaIdPositivo() {
        Curso curso = new Curso("Matemáticas", "MAT-101");
        long id = cursoDao.insert(curso);

        assertNotEquals("El ID generado debe ser > 0", -1L, id);
        assertTrue("El ID debe ser positivo", id > 0);
    }

    @Test
    public void insertCurso_seRecuperaCorrectamente() {
        Curso curso = new Curso("Física Cuántica", "FIS-402");
        cursoDao.insert(curso);

        List<Curso> cursos = cursoDao.getAllCursosSync();

        assertNotNull(cursos);
        assertEquals("Debe existir exactamente 1 curso", 1, cursos.size());
        assertEquals("Física Cuántica", cursos.get(0).nombre);
        assertEquals("FIS-402", cursos.get(0).codigo);
    }

    @Test
    public void insertMultiplesCursos_conteoEsCorrecto() {
        cursoDao.insert(new Curso("Curso A", "CA-01"));
        cursoDao.insert(new Curso("Curso B", "CB-02"));
        cursoDao.insert(new Curso("Curso C", "CC-03"));

        assertEquals(3, cursoDao.getCount());
    }

    // ── Test 2: Inserción de un Alumno ────────────────────────────────────────

    @Test
    public void insertAlumno_retornaIdPositivo() {
        Alumno alumno = new Alumno("Ana García", "A001");
        long id = alumnoDao.insert(alumno);

        assertTrue("El ID debe ser positivo", id > 0);
    }

    @Test
    public void insertAlumno_datosSeConservan() {
        // Insertamos un curso y al alumno
        long cursoId = cursoDao.insert(new Curso("Java", "DEV-210"));
        long alumnoId = alumnoDao.insert(new Alumno("Luis Martínez", "B002"));

        // Inscribimos para poder consultar
        alumnoDao.inscribir(new AlumnoCursoCrossRef(alumnoId, cursoId));

        List<Alumno> alumnos = alumnoDao.getAlumnosPorCursoSync(cursoId);

        assertNotNull(alumnos);
        assertEquals(1, alumnos.size());
        assertEquals("Luis Martínez", alumnos.get(0).nombre);
        assertEquals("B002", alumnos.get(0).matricula);
    }

    // ── Test 3: Vinculación alumno ↔ curso ────────────────────────────────────

    @Test
    public void inscripcion_alumnoEsVinculadoAlCurso() {
        long cursoId  = cursoDao.insert(new Curso("Base de Datos", "DB-301"));
        long alumnoId = alumnoDao.insert(new Alumno("Sofía López", "C003"));

        alumnoDao.inscribir(new AlumnoCursoCrossRef(alumnoId, cursoId));

        int inscrito = alumnoDao.isInscrito(alumnoId, cursoId);
        assertEquals("isInscrito debe devolver 1 (true)", 1, inscrito);
    }

    @Test
    public void desinscripcion_eliminaVinculo() {
        long cursoId  = cursoDao.insert(new Curso("Algoritmos", "ALG-205"));
        long alumnoId = alumnoDao.insert(new Alumno("Pedro Ruiz", "D004"));

        alumnoDao.inscribir(new AlumnoCursoCrossRef(alumnoId, cursoId));
        assertEquals(1, alumnoDao.isInscrito(alumnoId, cursoId));

        alumnoDao.desinscribir(alumnoId, cursoId);
        assertEquals("Tras desinscribir debe ser 0", 0, alumnoDao.isInscrito(alumnoId, cursoId));
    }

    // ── Test 4: Consulta de alumnos por curso ─────────────────────────────────

    @Test
    public void getAlumnosPorCurso_retornaAlumnosDelCurso() {
        long cursoId  = cursoDao.insert(new Curso("Redes", "NET-110"));
        long alumnoId1 = alumnoDao.insert(new Alumno("María Torres", "E005"));
        long alumnoId2 = alumnoDao.insert(new Alumno("Carlos Vega",  "E006"));

        alumnoDao.inscribir(new AlumnoCursoCrossRef(alumnoId1, cursoId));
        alumnoDao.inscribir(new AlumnoCursoCrossRef(alumnoId2, cursoId));

        List<Alumno> alumnos = alumnoDao.getAlumnosPorCursoSync(cursoId);

        assertEquals("Deben retornarse 2 alumnos", 2, alumnos.size());
    }

    @Test
    public void getAlumnosPorCurso_noRetornaAlumnosDeOtroCurso() {
        long cursoA   = cursoDao.insert(new Curso("Curso A", "CA"));
        long cursoB   = cursoDao.insert(new Curso("Curso B", "CB"));
        long alumnoId = alumnoDao.insert(new Alumno("Alumno Solo", "F007"));

        // Inscribe solo en cursoA
        alumnoDao.inscribir(new AlumnoCursoCrossRef(alumnoId, cursoA));

        List<Alumno> enCursoB = alumnoDao.getAlumnosPorCursoSync(cursoB);
        assertEquals("No debe haber alumnos en cursoB", 0, enCursoB.size());
    }

    // ── Test 5: Alumno en múltiples cursos ────────────────────────────────────

    @Test
    public void alumnoInscritoEnMultiplesCursos_apareceEnCadaUno() {
        long c1 = cursoDao.insert(new Curso("Historia", "HIS-1"));
        long c2 = cursoDao.insert(new Curso("Geografía", "GEO-2"));
        long c3 = cursoDao.insert(new Curso("Arte", "ART-3"));

        long alumnoId = alumnoDao.insert(new Alumno("Valentina Cruz", "G008"));

        alumnoDao.inscribir(new AlumnoCursoCrossRef(alumnoId, c1));
        alumnoDao.inscribir(new AlumnoCursoCrossRef(alumnoId, c2));
        alumnoDao.inscribir(new AlumnoCursoCrossRef(alumnoId, c3));

        assertEquals(1, alumnoDao.getAlumnosPorCursoSync(c1).size());
        assertEquals(1, alumnoDao.getAlumnosPorCursoSync(c2).size());
        assertEquals(1, alumnoDao.getAlumnosPorCursoSync(c3).size());
    }

    // ── Test 6: Curso con múltiples alumnos ───────────────────────────────────

    @Test
    public void cursoConMultiplesAlumnos_conteoEsCorrecto() {
        long cursoId = cursoDao.insert(new Curso("Química", "QUI-101"));

        for (int i = 1; i <= 5; i++) {
            long aId = alumnoDao.insert(new Alumno("Alumno " + i, "MAT-00" + i));
            alumnoDao.inscribir(new AlumnoCursoCrossRef(aId, cursoId));
        }

        List<Alumno> alumnos = alumnoDao.getAlumnosPorCursoSync(cursoId);
        assertEquals("Deben haber 5 alumnos en el curso", 5, alumnos.size());
    }

    // ── Test 7: Inserción de asistencia ───────────────────────────────────────

    @Test
    public void insertAsistencia_seRegistraCorrectamente() {
        long cursoId  = cursoDao.insert(new Curso("Biología", "BIO-201"));
        long alumnoId = alumnoDao.insert(new Alumno("Roberto Díaz", "H009"));
        alumnoDao.inscribir(new AlumnoCursoCrossRef(alumnoId, cursoId));

        Asistencia a = new Asistencia(alumnoId, cursoId, "2025-05-10", true);
        long asistenciaId = asistenciaDao.insert(a);

        assertTrue("ID de asistencia debe ser > 0", asistenciaId > 0);

        List<Asistencia> registros = asistenciaDao.getAsistenciasSync(alumnoId, cursoId);
        assertEquals(1, registros.size());
        assertEquals("2025-05-10", registros.get(0).fecha);
        assertTrue(registros.get(0).presente);
    }

    // ── Test 8: Verificación de registro existente ────────────────────────────

    @Test
    public void existeRegistro_detectaAsistenciaDuplicada() {
        long cursoId  = cursoDao.insert(new Curso("Filosofía", "FIL-301"));
        long alumnoId = alumnoDao.insert(new Alumno("Isabel Mora", "I010"));

        asistenciaDao.insert(new Asistencia(alumnoId, cursoId, "2025-06-01", true));

        int existe = asistenciaDao.existeRegistro(alumnoId, cursoId, "2025-06-01");
        assertEquals("Debe detectar el registro existente", 1, existe);

        int noExiste = asistenciaDao.existeRegistro(alumnoId, cursoId, "2025-06-02");
        assertEquals("No debe encontrar registro del día siguiente", 0, noExiste);
    }
}
