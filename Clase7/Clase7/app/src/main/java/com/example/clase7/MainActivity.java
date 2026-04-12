package com.example.clase7;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

    // Cambiar el tipo para poder usar el agregar
    MiAdaptador adaptador;
    String miTexto = ""; // variable como en Fibonacci

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


        RecyclerView recyclerView = findViewById(R.id.my_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        MiCliente miCliente = new MiCliente();


     //mandarlo separado
        AsyncTask.execute(() -> {
                ArrayList<String> misDatos = miCliente.getElements();
                runOnUiThread(() -> {
                    adaptador = new MiAdaptador(misDatos);
                    recyclerView.setAdapter(adaptador);
                });
        });





    // Como en fibonacci jeje | mapeo de datos xml a codigo java
    EditText edtInput = findViewById(R.id.edtInput);
    Button btnLista = findViewById(R.id.btnLista);

        edtInput.addTextChangedListener(new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            miTexto = String.valueOf(s);
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override
        public void afterTextChanged(Editable s) {}
    });

        btnLista.setOnClickListener(v -> {
            if (!miTexto.isEmpty()) {
                String nombreNuevo = miTexto; // Guardamos el nombre

                AsyncTask.execute(() -> {
                    // 1. Lo mandamos a la nube (Railway)
                    miCliente.addCharacter(nombreNuevo);

                    // 2. IMPORTANTE: Regresamos al hilo principal para mover la interfaz
                    runOnUiThread(() -> {
                        // Esto es lo que hace que aparezca en tu celular:
                        adaptador.agregarNombre(nombreNuevo);

                        // Limpiamos el campo
                        edtInput.setText("");
                        miTexto = "";
                    });
                });
            }
        });

        }
    }