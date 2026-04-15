package com.example.clase7;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;

public class DetalleActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);

        // Declara las vistas
        ImageView img = findViewById(R.id.imgDetalle);
        TextView txtName = findViewById(R.id.txtNameDetalle);
        TextView txtDesc = findViewById(R.id.txtDescDetalle);
        TextView txtAtaque = findViewById(R.id.txtAtaqueDetalle);
        TextView txtDefensa = findViewById(R.id.txtDefensaDetalle);

        // Recupera los datos del Intent
        String nombre = getIntent().getStringExtra("nombre");
        String desc = getIntent().getStringExtra("desc");
        String foto = getIntent().getStringExtra("foto");
        int ataque = getIntent().getIntExtra("ataque", 0);
        int defensa = getIntent().getIntExtra("defensa", 0);

        //  cada cosa en su lugar
        txtName.setText(nombre);
        txtDesc.setText(desc);

        // Formatea el texto para que se vea más pro
        txtAtaque.setText("Ataque: " + ataque);
        txtDefensa.setText("Defensa: " + defensa);

        // Carga la imagen con Glide
        Glide.with(this)
                .load(foto)
                .placeholder(R.mipmap.ic_launcher) // Por si tarda en cargar
                .into(img);
    }
}