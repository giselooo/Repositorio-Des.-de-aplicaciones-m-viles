package com.example.clase12;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;
import java.util.ArrayList;

public class AsistenciaFragment extends ListFragment {

    private AsistenciaDAO dao;
    private ArrayList<AsistenciaDTO> listaFiltrada;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inicializa la conexión a la base de datos
        dao = new AsistenciaDAO(getActivity());

        // Revisa si se recibieron los datos del fragmento anterior
        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.containsKey("claveCurso")) {
                // Desde la pantalla de Cursos
                String clave = bundle.getString("claveCurso");
                listaFiltrada = dao.obtenerAsistenciasFiltradas("claveCurso", clave);
            } else if (bundle.containsKey("nCuenta")) {
                //Desde la pantalla de Alumnos
                String cuenta = bundle.getString("nCuenta");
                listaFiltrada = dao.obtenerAsistenciasFiltradas("nCuenta", cuenta);
            }
        } else {
            listaFiltrada = dao.obtenerTodas();
        }

        // Creamos el adaptador para que se vea en el celular
        ArrayAdapter<AsistenciaDTO> adapter = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                listaFiltrada
        );

        // Poner la lista en pantalla
        setListAdapter(adapter);
    }


}