package com.example.calculadora;

public class Calculadora implements ICalculadora {

    @Override
    public double sumar(double x, double y) {
        return x + y;
    }

    @Override
    public double resta(double x, double y) {
        return x - y;
    }

    @Override
    public double multi(double x, double y) {
        return x * y;
    }

    @Override
    public double div(double x, double y) {
        return x / y;
    }
}
