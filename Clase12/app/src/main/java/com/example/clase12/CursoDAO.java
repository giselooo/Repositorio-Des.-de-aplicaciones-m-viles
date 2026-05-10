package com.example.clase12;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;

public class CursoDAO {
    private AdminDB admin;

    public CursoDAO(Context context) {
        admin = new AdminDB(context);
    }

    public ArrayList<CursoDTO> obtenerCursos() {
        ArrayList<CursoDTO> lista = new ArrayList<>();
        SQLiteDatabase db = admin.getReadableDatabase();

        //consultar  los campos de cursos
        Cursor fila = db.rawQuery("SELECT * FROM cursos", null);

        if (fila.moveToFirst()) {
            do {
                CursoDTO curso = new CursoDTO(
                        fila.getString(0), // clave
                        fila.getString(1)  // nombre
                );
                lista.add(curso);
            } while (fila.moveToNext());
        }
        fila.close();
        db.close();
        return lista;
    }
}