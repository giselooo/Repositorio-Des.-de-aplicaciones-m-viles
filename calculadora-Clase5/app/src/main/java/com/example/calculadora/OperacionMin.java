package com.example.calculadora;

public class OperacionMin {

    OperationType operationType;

    Double x;

    public OperacionMin(OperationType operationType, Double x) {
        this.operationType = operationType;
        this.x = x;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }
}
