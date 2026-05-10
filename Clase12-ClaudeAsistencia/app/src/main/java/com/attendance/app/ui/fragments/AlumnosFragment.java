package com.attendance.app.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.attendance.app.databinding.FragmentAlumnosBinding;
import com.attendance.app.navigation.NavigationHost;
import com.attendance.app.ui.adapters.AlumnoAdapter;
import com.attendance.app.ui.viewmodel.AppViewModel;

/**
 * Muestra los alumnos inscritos en el curso indicado por {@code ARG_CURSO_ID}.
 * Al seleccionar un alumno navega a su historial de asistencias.
 */
public class AlumnosFragment extends Fragment {

    public static final String TAG = "AlumnosFragment";

    // Argumentos que recibe este Fragment
    private static final String ARG_CURSO_ID     = "cursoId";
    private static final String ARG_CURSO_NOMBRE = "cursoNombre";

    private FragmentAlumnosBinding binding;
    private AppViewModel viewModel;
    private NavigationHost navigationHost;

    // ── Factory method ────────────────────────────────────────────────────────

    public static AlumnosFragment newInstance(long cursoId, String cursoNombre) {
        AlumnosFragment f = new AlumnosFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_CURSO_ID, cursoId);
        args.putString(ARG_CURSO_NOMBRE, cursoNombre);
        f.setArguments(args);
        return f;
    }

    // ── Lifecycle ─────────────────────────────────────────────────────────────

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof NavigationHost) {
            navigationHost = (NavigationHost) context;
        } else {
            throw new RuntimeException(context + " debe implementar NavigationHost");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentAlumnosBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        long cursoId     = requireArguments().getLong(ARG_CURSO_ID);
        String cursoNombre = requireArguments().getString(ARG_CURSO_NOMBRE, "Curso");

        binding.tvAlumnosTitulo.setText("Curso: " + cursoNombre);
        binding.btnAlumnosBack.setOnClickListener(v -> navigationHost.navegarAtras());

        viewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);
        viewModel.seleccionarCurso(cursoId);   // dispara el switchMap

        AlumnoAdapter adapter = new AlumnoAdapter(alumno -> {
            viewModel.seleccionarAlumnoYCurso(alumno.id, cursoId);
            navigationHost.navegarAAsistencias(alumno.id, cursoId, alumno.nombre);
        });

        binding.rvAlumnos.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvAlumnos.setAdapter(adapter);

        viewModel.getAlumnosPorCurso().observe(getViewLifecycleOwner(), alumnos -> {
            adapter.submitList(alumnos);
            binding.tvVacioAlumnos.setVisibility(
                    alumnos == null || alumnos.isEmpty() ? View.VISIBLE : View.GONE
            );
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        navigationHost = null;
    }
}
