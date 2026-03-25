package com.example.calculadora;

import java.util.ArrayList;

public class CalculadoraBuilder {

    private Operacion operacionInicial = null;

    private ArrayList<OperacionMin> operaciones = new ArrayList<>();
    public  CalculadoraBuilder() {}

    public CalculadoraBuilder addOperationInitial(Operacion operacionInicial) {
        this.operacionInicial = operacionInicial;
        return this;
    }
    public CalculadoraBuilder addOperation(OperacionMin operacion) {
        operaciones.add(operacion);
        return this;
    }

    public CalculadoraInput build() {
        return new CalculadoraInput(operacionInicial, operaciones);
    }

}
