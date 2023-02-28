package com.tiburela.android.controlAsistencia.demo.models;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Empleado {

    public long getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(long fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getFechaRegistroString() {
        return fechaRegistroString;
    }

    public void setFechaRegistroString(String fechaRegistroString) {
        this.fechaRegistroString = fechaRegistroString;
    }

    public String getNombreYapellidoEmpleado() {
        return nombreYapellidoEmpleado;
    }

    public void setNombreYapellidoEmpleado(String nombreYapellidoEmpleado) {
        this.nombreYapellidoEmpleado = nombreYapellidoEmpleado;
    }

    public String getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(String idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    private long   fechaRegistro;
    private String    fechaRegistroString;
    private String  nombreYapellidoEmpleado;
    private String  idEmpleado;

    public String getCodigoPaFichar() {
        return codigoPaFichar;
    }

    public void setCodigoPaFichar(String codigoPaFichar) {
        this.codigoPaFichar = codigoPaFichar;
    }

    private String  codigoPaFichar;


    public Empleado(){

    }

    public Empleado( String nombreYapellidoEmpleado, String idEmpleado,String codigoPaFichar) {

        fechaRegistro = new Date().getTime();
        fechaRegistroString = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(fechaRegistro);
        this.nombreYapellidoEmpleado = nombreYapellidoEmpleado;
        this.idEmpleado = idEmpleado;
        this.codigoPaFichar=codigoPaFichar;

    }





}
