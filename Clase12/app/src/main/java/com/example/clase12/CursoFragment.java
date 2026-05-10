package com.example.clase12;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;
import java.util.ArrayList;

public class CursoFragment extends ListFragment {

    // Lista global para que el clic la pueda reconocer
    private ArrayList<CursoDTO> listaCursos;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Conecta con el DAO de Cursos
        CursoDAO dao = new CursoDAO(getActivity());

        //Obtiene los datos de la base de datos
        listaCursos = dao.obtenerCursos();

        //Configura el adaptador para mostrar la lista en el cel
        ArrayAdapter<CursoDTO> adapter = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                listaCursos
        );
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(@NonNull ListView l, @NonNull View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        // Obtenemos el curso
        CursoDTO curso = listaCursos.get(position);

        // Bundle con la clave del curso
        Bundle bundle = new Bundle();
        bundle.putString("claveCurso", curso.getClave());

        // Define el destino (AsistenciaFragment)
        AsistenciaFragment destino = new AsistenciaFragment();
        destino.setArguments(bundle);

        // Hace el cambio de pantalla
        getParentFragmentManager().beginTransaction()
                .replace(R.id.contenedor_fragmentos, destino)
                .addToBackStack(null)
                .commit();
    }
}