package com.example.clase12;

import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnCursos = findViewById(R.id.btnCursos);
        Button btnAlumnos = findViewById(R.id.btnAlumnos);
        Button btnAsistencias = findViewById(R.id.btnAsistencias);


        btnCursos.setOnClickListener(v -> cargarFragmento(new CursoFragment()));


        btnAlumnos.setOnClickListener(v -> cargarFragmento(new AlumnoFragment()));


        btnAsistencias.setOnClickListener(v -> cargarFragmento(new AsistenciaFragment()));
    }


    private void cargarFragmento(Fragment fragmento) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.contenedor_fragmentos, fragmento);
        ft.commit();
    }
}