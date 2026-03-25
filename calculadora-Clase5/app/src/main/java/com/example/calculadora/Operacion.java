package com.example.calculadora;

public class Operacion {

    double x;
    double y;

    OperationType operationType;

    public Operacion(double x, double y, OperationType operationType) {
        this.x = x;
        this.y = y;
        this.operationType = operationType;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }
}
