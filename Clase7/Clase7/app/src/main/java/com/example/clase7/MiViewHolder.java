package com.example.clase7;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class MiViewHolder extends RecyclerView.ViewHolder {
    private final TextView textView;

    public MiViewHolder(View view) {
        super(view);
        // Define click listener for the ViewHolder's View

        textView = (TextView) view.findViewById(R.id.id_valor_lista);
    }

    public TextView getTextView() {
        return textView;
    }
}