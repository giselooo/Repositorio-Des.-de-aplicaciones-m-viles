package com.example.clase7;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    MiAdaptador adaptador;
    Button miButton;
    EditText miInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        MiCliente miCliente = new MiCliente();
        AsyncTask.execute(() -> {
            ArrayList<Personaje> misDatos = miCliente.getElements();
            runOnUiThread(() -> {
                adaptador = new MiAdaptador(misDatos);
                recyclerView.setAdapter(adaptador);
            });
        });
        miButton = findViewById(R.id.button);
        miInput = findViewById(R.id.textInputEditText);
        recyclerView = findViewById(R.id.my_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        miButton.setOnClickListener(v -> {
            String nameNuevo = miInput.getText().toString();
            if (!nameNuevo.isBlank()){
                // Creamos el objeto Personaje
                Personaje nuevo = new Personaje(
                        nameNuevo,
                        "Descripción generada localmente",
                        "https://via.placeholder.com/150", // Imagen temporal
                        10,
                        10
                );

                // Lo agregamos a la lista del adaptador
                adaptador.addElemento(nuevo);

                //Avisarle al RecyclerView que hay uno nuevo para que lo dibuje
                adaptador.notifyItemInserted(adaptador.getItemCount() - 1);

                miInput.setText(""); // Limpiamos el cuadro
            }
        });

    }
}