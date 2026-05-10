package com.example.clase12;

public class AlumnoDTO {
    private String nombre;
    private String aPaterno;
    private String aMaterno;
    private String nCuenta;

    public AlumnoDTO(String nombre, String aPaterno, String aMaterno, String nCuenta){
        this.nombre = nombre;
        this.aPaterno = aPaterno;
        this.aMaterno = aMaterno;
        this.nCuenta = nCuenta;
    }


    public String getNombre() { return nombre; }
    public String getaPaterno() { return aPaterno; }
    public String getaMaterno() { return aMaterno; }
    public String getnCuenta() { return nCuenta; }

    @Override
    public String toString() {

        return nCuenta + " - " + aPaterno + " " + aMaterno + " " + nombre;
    }
}