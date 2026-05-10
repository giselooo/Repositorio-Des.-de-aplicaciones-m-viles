package com.example.clase12;

public class AsistenciaDTO {
    private String nCuenta;
    private String claveCurso;
    private String estatus;


    // Constructor vacío para crear el objeto y luego llenarlo
    public AsistenciaDTO() {
    }

    // Getter y Setter
    public String getnCuenta() {
        return nCuenta;
    }

    public void setnCuenta(String nCuenta) {
        this.nCuenta = nCuenta;
    }

    public String getClaveCurso() {
        return claveCurso;
    }

    public void setClaveCurso(String claveCurso) {
        this.claveCurso = claveCurso;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    // Este es el que decide qué se va a ver escrito en el ListView
    @Override
    public String toString() {
        return "Cuenta: " + nCuenta + " | Curso: " + claveCurso + " | " + estatus;
    }
}