package com.example.calculadora;

import java.util.ArrayList;

public class CalculadoraInput {

    Operacion operacionInicial;

    ArrayList<OperacionMin> operaciones;


    public CalculadoraInput(Operacion operacionInicial, ArrayList<OperacionMin> operaciones) {
        this.operacionInicial = operacionInicial;
        this.operaciones = operaciones;
    }
}
