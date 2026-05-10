package com.example.clase12;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;
import java.util.ArrayList;

public class AlumnoFragment extends ListFragment {

    // declarar lista de forma global
    private ArrayList<AlumnoDTO> listaAlumnos;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AlumnoDAO dao = new AlumnoDAO(getActivity());

        // llena la lista global
        listaAlumnos = dao.obtenerAlumnos();

        ArrayAdapter<AlumnoDTO> adapter = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                listaAlumnos
        );
        setListAdapter(adapter);
    }

    // clic específicamente para los alumnos
    @Override
    public void onListItemClick(@NonNull ListView l, @NonNull View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        // Obtenemos el alumno seleccionado
        AlumnoDTO alumno = listaAlumnos.get(position);

        // Crear bundle con el número de cuenta
        Bundle bundle = new Bundle();
        bundle.putString("nCuenta", alumno.getnCuenta()); // Usamos nCuenta para filtrar

        // Ir fragmento de Asistencias
        AsistenciaFragment destino = new AsistenciaFragment();
        destino.setArguments(bundle);

        // Hace el cambio de pantalla
        getParentFragmentManager().beginTransaction()
                .replace(R.id.contenedor_fragmentos, destino)
                .addToBackStack(null)
                .commit();
    }
}