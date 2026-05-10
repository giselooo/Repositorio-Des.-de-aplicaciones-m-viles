package com.example.clase12;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;

public class AlumnoDAO {
    private AdminDB admin;

    public AlumnoDAO(Context context) {
        admin = new AdminDB(context);
    }

    public ArrayList<AlumnoDTO> obtenerAlumnos() {
        ArrayList<AlumnoDTO> lista = new ArrayList<>();
        SQLiteDatabase db = admin.getReadableDatabase();

        Cursor fila = db.rawQuery("SELECT * FROM alumnos", null);

        if (fila.moveToFirst()) {
            do {

                AlumnoDTO alumno = new AlumnoDTO(
                        fila.getString(1), // nombre
                        fila.getString(2), // aPaterno
                        fila.getString(3), // aMaterno
                        fila.getString(0)  // nCuenta
                );
                lista.add(alumno);
            } while (fila.moveToNext());
        }
        fila.close();
        db.close();
        return lista;
    }
}