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

import com.attendance.app.databinding.FragmentCursosBinding;
import com.attendance.app.navigation.NavigationHost;
import com.attendance.app.ui.adapters.CursoAdapter;
import com.attendance.app.ui.viewmodel.AppViewModel;

/**
 * Pantalla principal: muestra todos los cursos disponibles.
 * Al seleccionar un curso navega al listado de alumnos.
 */
public class CursosFragment extends Fragment {

    public static final String TAG = "CursosFragment";

    private FragmentCursosBinding binding;
    private AppViewModel viewModel;
    private NavigationHost navigationHost;

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
        binding = FragmentCursosBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);

        // Configurar RecyclerView
        CursoAdapter adapter = new CursoAdapter(curso -> {
            viewModel.seleccionarCurso(curso.id);
            navigationHost.navegarAAlumnos(curso.id, curso.nombre);
        });

        binding.rvCursos.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvCursos.setAdapter(adapter);

        // Observar datos
        viewModel.getAllCursos().observe(getViewLifecycleOwner(), cursos -> {
            adapter.submitList(cursos);
            binding.tvVacioCursos.setVisibility(
                    cursos == null || cursos.isEmpty() ? View.VISIBLE : View.GONE
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
