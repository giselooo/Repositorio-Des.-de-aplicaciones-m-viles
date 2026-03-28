package com.example.calculadora;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    ViewModel viewModel = new ViewModel();
    TextView txvOperation, txvResult;

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
        txvOperation = findViewById(R.id.txvOperation);
        txvResult = findViewById(R.id.txvResult);

        configurarBotones();
    }

    private void configurarBotones() {

        View.OnClickListener listener = v -> {
            Button b = (Button) v;
            String textoBoton = b.getText().toString();


            txvOperation.append(textoBoton);
            String operacionActual = txvOperation.getText().toString();


            String resultado = viewModel.processStringInput(operacionActual);
            txvResult.setText(resultado);
        };

        int[] ids = {R.id.btnOne, R.id.btnSecond, R.id.btnThird, R.id.btnFour,
                R.id.btnFive, R.id.btnSix, R.id.btnSeven, R.id.btnEight,
                R.id.btnNine, R.id.btnCero, R.id.btnPlus, R.id.btnMinus,
                R.id.btnMultiple, R.id.btnDivide, R.id.btnPoint};

        for (int id : ids) findViewById(id).setOnClickListener(listener);


        findViewById(R.id.btnAc).setOnClickListener(v -> {
            txvOperation.setText("");
            txvResult.setText("0");
        });

        findViewById(R.id.btnDelete).setOnClickListener(v -> {
            String current = txvOperation.getText().toString();
            if (!current.isEmpty()) {
                txvOperation.setText(current.substring(0, current.length() - 1));

                txvResult.setText(viewModel.processStringInput(txvOperation.getText().toString()));
            }
        });
    }
}