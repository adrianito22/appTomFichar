package com.google.android.cameraview.demo.models;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Empleado {

    private long   fechaRegistro;
    private String    fechaRegistroString;
    private String  nombreYapellidoEmpleado;
    private String  idEmpleado;


    public Empleado( String nombreYapellidoEmpleado, String idEmpleado) {


        fechaRegistro = new Date().getTime();
        fechaRegistroString = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(fechaRegistro);
        this.nombreYapellidoEmpleado = nombreYapellidoEmpleado;
        this.idEmpleado = idEmpleado;
    }





}
