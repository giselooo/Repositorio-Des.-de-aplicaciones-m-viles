package com.attendance.app.ui.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.attendance.app.data.entities.Curso;
import com.attendance.app.databinding.ItemCursoBinding;

/**
 * Adaptador para la lista de cursos.
 * Extiende {@link BaseAdapter} especificando el modelo {@link Curso}
 * y el ViewBinding {@link ItemCursoBinding}.
 */
public class CursoAdapter extends BaseAdapter<Curso, ItemCursoBinding> {

    /** Callback que la Activity/Fragmento implementa para manejar el click. */
    public interface OnCursoClick {
        void onClick(Curso curso);
    }

    private final OnCursoClick listener;

    private static final DiffUtil.ItemCallback<Curso> DIFF =
            new DiffUtil.ItemCallback<>() {
                @Override
                public boolean areItemsTheSame(@NonNull Curso o, @NonNull Curso n) {
                    return o.id == n.id;
                }
                @Override
                public boolean areContentsTheSame(@NonNull Curso o, @NonNull Curso n) {
                    return o.nombre.equals(n.nombre) && o.codigo.equals(n.codigo);
                }
            };

    public CursoAdapter(@NonNull OnCursoClick listener) {
        super(DIFF);
        this.listener = listener;
    }

    @Override
    protected ItemCursoBinding inflate(@NonNull LayoutInflater inflater,
                                       @NonNull ViewGroup parent) {
        return ItemCursoBinding.inflate(inflater, parent, false);
    }

    @Override
    protected void bind(@NonNull ItemCursoBinding b, @NonNull Curso curso, int pos) {
        b.tvCursoNombre.setText(curso.nombre);
        b.tvCursoCodigo.setText(curso.codigo);
        b.getRoot().setOnClickListener(v -> listener.onClick(curso));
    }
}
