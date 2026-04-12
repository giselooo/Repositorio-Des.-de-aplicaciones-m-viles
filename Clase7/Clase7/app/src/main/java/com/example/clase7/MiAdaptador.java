package com.example.clase7;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;

public class MiAdaptador extends RecyclerView.Adapter<MiViewHolder> {

    private ArrayList<String> localDataSet; /* cambie el string por un arraylist*/

    public MiAdaptador(ArrayList<String> dataSet) {
        this.localDataSet = dataSet; //hacer que la lista sea grande
    }


    /*Para el botón*/
    public void agregarNombre(String nombre) {
        localDataSet.add(nombre);
        notifyItemInserted(localDataSet.size() - 1);
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

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getTextView().setText(localDataSet.get(position)); //en vez de [position] puse el .get(position)
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size(); //hay que pasar de lenght a size
    }
}