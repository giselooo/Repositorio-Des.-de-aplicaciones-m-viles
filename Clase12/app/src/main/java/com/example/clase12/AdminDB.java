package com.example.clase12;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
public class AdminDB extends SQLiteOpenHelper {

    // Nombre y versión de la base de datos
    private static final String DATABASE_NAME = "Escuela.db";
    private static final int DATABASE_VERSION = 1;

    public AdminDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE alumnos (nCuenta TEXT PRIMARY KEY, nombre TEXT, aPaterno TEXT, aMaterno TEXT)");
        db.execSQL("CREATE TABLE cursos (clave TEXT PRIMARY KEY, nombre TEXT)");
        db.execSQL("CREATE TABLE asistencias (id INTEGER PRIMARY KEY AUTOINCREMENT, fecha TEXT, nCuenta TEXT, claveCurso TEXT, estatus TEXT)");


        db.execSQL("INSERT INTO alumnos VALUES ('321277189', 'Gisel', 'Reyes', 'López')");
        db.execSQL("INSERT INTO alumnos VALUES ('2111111', 'Rodrigo', 'Hernández', 'Silva')");


        db.execSQL("INSERT INTO cursos VALUES ('111111', 'Desarrollo de Aplicaciones Móviles')");
        db.execSQL("INSERT INTO cursos VALUES ('222222', 'Investigación de Operaciones')");


        db.execSQL("INSERT INTO asistencias (fecha, nCuenta, claveCurso, estatus) VALUES ('28/04/2026', '321277189', '111111', 'Asistencia')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS alumnos");
        db.execSQL("DROP TABLE IF EXISTS cursos");
        db.execSQL("DROP TABLE IF EXISTS asistencias");
        onCreate(db);
    }
}