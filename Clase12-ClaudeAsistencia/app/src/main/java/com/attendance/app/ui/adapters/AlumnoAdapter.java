package com.attendance.app.ui.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.attendance.app.data.entities.Alumno;
import com.attendance.app.databinding.ItemAlumnoBinding;

/**
 * Adaptador para la lista de alumnos inscritos en un curso.
 */
public class AlumnoAdapter extends BaseAdapter<Alumno, ItemAlumnoBinding> {

    public interface OnAlumnoClick {
        void onClick(Alumno alumno);
    }

    private final OnAlumnoClick listener;

    private static final DiffUtil.ItemCallback<Alumno> DIFF =
            new DiffUtil.ItemCallback<>() {
                @Override
                public boolean areItemsTheSame(@NonNull Alumno o, @NonNull Alumno n) {
                    return o.id == n.id;
                }
                @Override
                public boolean areContentsTheSame(@NonNull Alumno o, @NonNull Alumno n) {
                    return o.nombre.equals(n.nombre) && o.matricula.equals(n.matricula);
                }
            };

    public AlumnoAdapter(@NonNull OnAlumnoClick listener) {
        super(DIFF);
        this.listener = listener;
    }

    @Override
    protected ItemAlumnoBinding inflate(@NonNull LayoutInflater inflater,
                                        @NonNull ViewGroup parent) {
        return ItemAlumnoBinding.inflate(inflater, parent, false);
    }

    @Override
    protected void bind(@NonNull ItemAlumnoBinding b, @NonNull Alumno alumno, int pos) {
        b.tvAlumnoNombre.setText(alumno.nombre);
        b.tvAlumnoMatricula.setText("Matrícula: " + alumno.matricula);
        b.getRoot().setOnClickListener(v -> listener.onClick(alumno));
    }
}
