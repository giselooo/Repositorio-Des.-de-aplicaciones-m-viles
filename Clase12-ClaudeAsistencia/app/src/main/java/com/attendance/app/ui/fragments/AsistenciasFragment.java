package com.attendance.app.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.attendance.app.databinding.FragmentAsistenciasBinding;
import com.attendance.app.navigation.NavigationHost;
import com.attendance.app.ui.adapters.AsistenciaAdapter;
import com.attendance.app.ui.viewmodel.AppViewModel;

import java.time.LocalDate;

/**
 * Muestra el historial de asistencias de un alumno en un curso específico.
 * Permite registrar la asistencia del día y alternar presencia/ausencia
 * mediante el checkbox de cada ítem.
 */
public class AsistenciasFragment extends Fragment {

    public static final String TAG = "AsistenciasFragment";

    private static final String ARG_ALUMNO_ID     = "alumnoId";
    private static final String ARG_CURSO_ID      = "cursoId";
    private static final String ARG_ALUMNO_NOMBRE = "alumnoNombre";

    private FragmentAsistenciasBinding binding;
    private AppViewModel viewModel;
    private NavigationHost navigationHost;

    // ── Factory method ────────────────────────────────────────────────────────

    public static AsistenciasFragment newInstance(long alumnoId, long cursoId,
                                                   String alumnoNombre) {
        AsistenciasFragment f = new AsistenciasFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_ALUMNO_ID, alumnoId);
        args.putLong(ARG_CURSO_ID, cursoId);
        args.putString(ARG_ALUMNO_NOMBRE, alumnoNombre);
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
        binding = FragmentAsistenciasBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        long alumnoId     = requireArguments().getLong(ARG_ALUMNO_ID);
        long cursoId      = requireArguments().getLong(ARG_CURSO_ID);
        String alumnoNombre = requireArguments().getString(ARG_ALUMNO_NOMBRE, "Alumno");

        binding.tvAsistenciasTitulo.setText("Asistencias: " + alumnoNombre);
        binding.btnAsistenciasBack.setOnClickListener(v -> navigationHost.navegarAtras());

        viewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);
        viewModel.seleccionarAlumnoYCurso(alumnoId, cursoId);

        // Adaptador con toggle de presencia
        AsistenciaAdapter adapter = new AsistenciaAdapter((asistencia, presente) -> {
            asistencia.setPresente(presente);
            viewModel.actualizarAsistencia(asistencia);
        });

        binding.rvAsistencias.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvAsistencias.setAdapter(adapter);

        viewModel.getAsistencias().observe(getViewLifecycleOwner(), lista -> {
            adapter.submitList(lista);
            binding.tvVacioAsistencias.setVisibility(
                    lista == null || lista.isEmpty() ? View.VISIBLE : View.GONE
            );
        });

        // Registrar asistencia de hoy
        binding.btnRegistrarHoy.setOnClickListener(v -> {
            String hoy = LocalDate.now().toString();
            viewModel.registrarAsistencia(alumnoId, cursoId, hoy, true);
            Toast.makeText(requireContext(),
                    "Asistencia de hoy registrada ✓", Toast.LENGTH_SHORT).show();
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
