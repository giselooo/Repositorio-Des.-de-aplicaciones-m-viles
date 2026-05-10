package com.attendance.app.data.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.attendance.app.data.dao.AlumnoDao;
import com.attendance.app.data.dao.AsistenciaDao;
import com.attendance.app.data.dao.CursoDao;
import com.attendance.app.data.entities.Alumno;
import com.attendance.app.data.entities.AlumnoCursoCrossRef;
import com.attendance.app.data.entities.Asistencia;
import com.attendance.app.data.entities.Curso;

import java.time.LocalDate;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Singleton Room Database.
 * Incluye un Callback que pre-popula la BD con datos de muestra al crearla.
 */
@Database(
    entities = {
        Alumno.class,
        Curso.class,
        AlumnoCursoCrossRef.class,
        Asistencia.class
    },
    version = 1,
    exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {

    // ── DAOs ─────────────────────────────────────────────────────────────────

    public abstract AlumnoDao    alumnoDao();
    public abstract CursoDao     cursoDao();
    public abstract AsistenciaDao asistenciaDao();

    // ── Singleton ─────────────────────────────────────────────────────────────

    private static volatile AppDatabase INSTANCE;

    /** Executor de fondo compartido para operaciones de BD. */
    public static final ExecutorService DB_EXECUTOR =
            Executors.newFixedThreadPool(4);

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    "attendance_v2.db"
                            )
                            .addCallback(SEED_CALLBACK)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    // ── Seed de datos ─────────────────────────────────────────────────────────

    private static final RoomDatabase.Callback SEED_CALLBACK = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            DB_EXECUTOR.execute(() -> {
                AppDatabase database = INSTANCE;
                CursoDao  cursoDao  = database.cursoDao();
                AlumnoDao alumnoDao = database.alumnoDao();
                AsistenciaDao asDao = database.asistenciaDao();

                // Cursos
                long c1 = cursoDao.insert(new Curso("Matemáticas Avanzadas", "MAT-301"));
                long c2 = cursoDao.insert(new Curso("Programación en Java",  "DEV-210"));
                long c3 = cursoDao.insert(new Curso("Base de Datos",          "DB-405"));

                // Alumnos
                long a1 = alumnoDao.insert(new Alumno("Ana García",       "A001"));
                long a2 = alumnoDao.insert(new Alumno("Luis Martínez",    "A002"));
                long a3 = alumnoDao.insert(new Alumno("Sofía Hernández",  "A003"));
                long a4 = alumnoDao.insert(new Alumno("Carlos López",     "A004"));

                // Inscripciones (N:M)
                alumnoDao.inscribir(new AlumnoCursoCrossRef(a1, c1));
                alumnoDao.inscribir(new AlumnoCursoCrossRef(a1, c2));
                alumnoDao.inscribir(new AlumnoCursoCrossRef(a2, c1));
                alumnoDao.inscribir(new AlumnoCursoCrossRef(a2, c3));
                alumnoDao.inscribir(new AlumnoCursoCrossRef(a3, c2));
                alumnoDao.inscribir(new AlumnoCursoCrossRef(a3, c3));
                alumnoDao.inscribir(new AlumnoCursoCrossRef(a4, c1));
                alumnoDao.inscribir(new AlumnoCursoCrossRef(a4, c2));
                alumnoDao.inscribir(new AlumnoCursoCrossRef(a4, c3));

                // Asistencias de muestra
                String hoy      = LocalDate.now().toString();
                String ayer     = LocalDate.now().minusDays(1).toString();
                String antier   = LocalDate.now().minusDays(2).toString();

                asDao.insert(new Asistencia(a1, c1, hoy,    true));
                asDao.insert(new Asistencia(a1, c1, ayer,   false));
                asDao.insert(new Asistencia(a1, c1, antier, true));
                asDao.insert(new Asistencia(a2, c1, hoy,    true));
                asDao.insert(new Asistencia(a2, c1, ayer,   true));
                asDao.insert(new Asistencia(a1, c2, hoy,    true));
                asDao.insert(new Asistencia(a3, c2, hoy,    false));
                asDao.insert(new Asistencia(a4, c2, hoy,    true));
            });
        }
    };
}
