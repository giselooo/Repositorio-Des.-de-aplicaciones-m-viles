package com.example.clase7;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

public class MiViewHolder extends RecyclerView.ViewHolder {
    private final TextView textView;
    private ImageView imageView;

    public MiViewHolder(View view) {
        super(view);
        // Define click listener for the ViewHolder's View
        textView = (TextView) view.findViewById(R.id.textView);
        imageView = view.findViewById(R.id.imageView);
    }

    public TextView getTextView() {
        return textView;
    }

    public ImageView getImageView() {
        return imageView;
    }
}
