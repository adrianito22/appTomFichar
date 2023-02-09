package com.google.android.cameraview.demo.models;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PromedioAsistenceEmpleado {

    private String empleadoName;
    private String entradaPromedio;
    private String salidaPromedio;
    private int asistenciaPromedio; //el porcentaje de asistencia ...


    public PromedioAsistenceEmpleado(String empleadoName, String entradaPromedio, String salidaPromedio, int asistenciaPromedio) {
        this.empleadoName = empleadoName;
        this.entradaPromedio = entradaPromedio;
        this.salidaPromedio = salidaPromedio;
        this.asistenciaPromedio = asistenciaPromedio;
    }


    public String getEmpleadoName() {
        return empleadoName;
    }

    public void setEmpleadoName(String empleadoName) {
        this.empleadoName = empleadoName;
    }

    public String getEntradaPromedio() {
        return entradaPromedio;
    }

    public void setEntradaPromedio(String entradaPromedio) {
        this.entradaPromedio = entradaPromedio;
    }

    public String getSalidaPromedio() {
        return salidaPromedio;
    }

    public void setSalidaPromedio(String salidaPromedio) {
        this.salidaPromedio = salidaPromedio;
    }

    public int getAsistenciaPromedio() {
        return asistenciaPromedio;
    }

    public void setAsistenciaPromedio(int asistenciaPromedio) {
        this.asistenciaPromedio = asistenciaPromedio;
    }





}
