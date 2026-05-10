package com.example.clase12;

public class CursoDTO {
    private String clave;
    private String nombreCurso;

    public CursoDTO(String clave, String nombreCurso) {
        this.clave = clave;
        this.nombreCurso = nombreCurso;
    }

    public String getClave() { return clave; }
    public String getNombreCurso() { return nombreCurso; }

    @Override
    public String toString() {
        return clave + " - " + nombreCurso;
    }
}
