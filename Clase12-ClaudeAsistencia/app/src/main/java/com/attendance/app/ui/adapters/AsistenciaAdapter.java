package com.attendance.app.ui.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.attendance.app.data.entities.Asistencia;
import com.attendance.app.databinding.ItemAsistenciaBinding;

/**
 * Adaptador para el historial de asistencias.
 * Cada ítem muestra fecha + checkbox de presencia (estilo checklist).
 */
public class AsistenciaAdapter extends BaseAdapter<Asistencia, ItemAsistenciaBinding> {

    public interface OnTogglePresencia {
        void onToggle(Asistencia asistencia, boolean presente);
    }

    private final OnTogglePresencia listener;

    private static final DiffUtil.ItemCallback<Asistencia> DIFF =
            new DiffUtil.ItemCallback<>() {
                @Override
                public boolean areItemsTheSame(@NonNull Asistencia o, @NonNull Asistencia n) {
                    return o.id == n.id;
                }
                @Override
                public boolean areContentsTheSame(@NonNull Asistencia o, @NonNull Asistencia n) {
                    return o.presente == n.presente && o.fecha.equals(n.fecha);
                }
            };

    public AsistenciaAdapter(@NonNull OnTogglePresencia listener) {
        super(DIFF);
        this.listener = listener;
    }

    @Override
    protected ItemAsistenciaBinding inflate(@NonNull LayoutInflater inflater,
                                            @NonNull ViewGroup parent) {
        return ItemAsistenciaBinding.inflate(inflater, parent, false);
    }

    @Override
    protected void bind(@NonNull ItemAsistenciaBinding b,
                        @NonNull Asistencia asistencia, int pos) {
        b.tvAsistenciaFecha.setText(asistencia.fecha);

        // Evita bucles al setear el estado del checkbox
        b.cbPresente.setOnCheckedChangeListener(null);
        b.cbPresente.setChecked(asistencia.presente);
        b.cbPresente.setOnCheckedChangeListener((btn, isChecked) ->
                listener.onToggle(asistencia, isChecked));
    }
}
