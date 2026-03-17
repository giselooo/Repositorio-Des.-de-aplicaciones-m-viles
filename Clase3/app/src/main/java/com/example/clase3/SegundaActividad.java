package com.example.clase3;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SegundaActividad extends AppCompatActivity {


    TextView txvTitulo;
    Button btnIncrementa;
    int contador = 0;
    String MI_LLAVE = "actividad.segunda.millave";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segunda);
        txvTitulo = findViewById(R.id.txvTitulo);
        btnIncrementa = findViewById(R.id.btnIncrementa);

        if(savedInstanceState != null && savedInstanceState.containsKey(MI_LLAVE)){
            contador=savedInstanceState.getInt(MI_LLAVE);
            muestraTitulo();
        }

        btnIncrementa.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                contador += 1;

                muestraTitulo();

            }
        });

        Log.i("FCA", "OnCreate() executed");

    }

    private void muestraTitulo(){
        txvTitulo.setText("" + contador);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("FCA", "OnStart() executed");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("FCA", "OnResume() executed");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("FCA", "OnPause() executed");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("FCA", "onStop() executed");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("FCA", "OnDestroy() executed");
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(MI_LLAVE, contador);
    }
}