package com.example.clase12;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;

public class AsistenciaDAO {
    private AdminDB admin;

    public AsistenciaDAO(Context context) {
        // Se inicializa con el contexto para conectar a Escuela.db
        this.admin = new AdminDB(context);
    }

    // Obtener todas las asistencias
    public ArrayList<AsistenciaDTO> obtenerTodas() {
        ArrayList<AsistenciaDTO> lista = new ArrayList<>();
        SQLiteDatabase db = admin.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM asistencias", null);

        if (cursor.moveToFirst()) {
            do {
                AsistenciaDTO obj = new AsistenciaDTO();
                obj.setnCuenta(cursor.getString(2));    // Columna nCuenta
                obj.setClaveCurso(cursor.getString(3)); // Columna claveCurso
                obj.setEstatus(cursor.getString(4));    // Columna estatus
                lista.add(obj);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return lista;
    }
    //Asistencias pero filtradas
    public ArrayList<AsistenciaDTO> obtenerAsistenciasFiltradas(String columna, String valor) {
        ArrayList<AsistenciaDTO> lista = new ArrayList<>();
        SQLiteDatabase db = admin.getReadableDatabase();

        // Usar la columna que nos pasen (claveCurso o nCuenta)
        String query = "SELECT * FROM asistencias WHERE " + columna + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{valor});

        if (cursor.moveToFirst()) {
            do {
                AsistenciaDTO obj = new AsistenciaDTO();
                obj.setnCuenta(cursor.getString(2));
                obj.setClaveCurso(cursor.getString(3));
                obj.setEstatus(cursor.getString(4));
                lista.add(obj);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return lista;
    }
}