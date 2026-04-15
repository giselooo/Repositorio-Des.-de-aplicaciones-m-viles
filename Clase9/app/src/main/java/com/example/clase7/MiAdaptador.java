package com.example.clase7;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MiAdaptador extends RecyclerView.Adapter<MiViewHolder> {

    private ArrayList<Personaje> localDataSet;

    public MiAdaptador(ArrayList<Personaje> dataSet) {
        localDataSet = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MiViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.text_row_item, viewGroup, false);

        return new MiViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MiViewHolder viewHolder, final int position) {
        // 1. Obtener el personaje de la lista según la posición
        Personaje personaje = localDataSet.get(position);

        // 2. Llenar la vista (lo que ya tenías)
        viewHolder.getTextView().setText(personaje.getName());

        Glide.with(viewHolder.itemView.getContext())
                .load(personaje.getPhoto())
                .centerCrop()
                .into(viewHolder.getImageView());

        // --- AQUÍ EMPIEZA LO NUEVO (EL CLIC) ---
        viewHolder.itemView.setOnClickListener(v -> {
            // Creamos el Intent para ir a DetalleActivity
            android.content.Intent intent = new android.content.Intent(v.getContext(), DetalleActivity.class);

            // "Empacamos" los datos del personaje para que la otra pantalla los reciba
            intent.putExtra("nombre", personaje.getName());
            intent.putExtra("desc", personaje.getDesc());
            intent.putExtra("foto", personaje.getPhoto());
            intent.putExtra("ataque", personaje.getAtack());
            intent.putExtra("defensa", personaje.getDef());

            // ¡Lanzamos la actividad!
            v.getContext().startActivity(intent);
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

    public void addElemento(Personaje newElement) {
        localDataSet.add(newElement);
    }
}
