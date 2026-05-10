package com.attendance.app.ui.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import java.util.function.Function;

/**
 * Adaptador genérico y abstracto para RecyclerView.
 *
 * <p>Utiliza {@link ListAdapter} + {@link DiffUtil.ItemCallback} para
 * actualizaciones eficientes de la lista.</p>
 *
 * <p>Subclases solo necesitan implementar:
 * <ul>
 *   <li>{@link #inflate(LayoutInflater, ViewGroup)} — inflar el ViewBinding concreto.</li>
 *   <li>{@link #bind(ViewBinding, Object, int)} — enlazar datos al ViewHolder.</li>
 * </ul>
 * </p>
 *
 * @param <T>  Tipo del modelo de datos.
 * @param <VB> Tipo del ViewBinding generado para el item layout.
 */
public abstract class BaseAdapter<T, VB extends ViewBinding>
        extends ListAdapter<T, BaseAdapter.BindingViewHolder<VB>> {

    protected BaseAdapter(@NonNull DiffUtil.ItemCallback<T> diffCallback) {
        super(diffCallback);
    }

    // ── Métodos abstractos que deben implementar las subclases ────────────────

    /**
     * Infla y devuelve el ViewBinding correspondiente al item.
     */
    protected abstract VB inflate(@NonNull LayoutInflater inflater,
                                  @NonNull ViewGroup parent);

    /**
     * Enlaza los datos del ítem {@code item} al ViewHolder {@code binding}.
     *
     * @param binding  El ViewBinding del ítem.
     * @param item     El objeto de datos en la posición {@code position}.
     * @param position Posición en la lista.
     */
    protected abstract void bind(@NonNull VB binding, @NonNull T item, int position);

    // ── Implementación de RecyclerView.Adapter ────────────────────────────────

    @NonNull
    @Override
    public BindingViewHolder<VB> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        VB binding = inflate(inflater, parent);
        return new BindingViewHolder<>(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BindingViewHolder<VB> holder, int position) {
        bind(holder.binding, getItem(position), position);
    }

    // ── ViewHolder genérico ───────────────────────────────────────────────────

    public static class BindingViewHolder<VB extends ViewBinding>
            extends RecyclerView.ViewHolder {

        public final VB binding;

        public BindingViewHolder(@NonNull VB binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
