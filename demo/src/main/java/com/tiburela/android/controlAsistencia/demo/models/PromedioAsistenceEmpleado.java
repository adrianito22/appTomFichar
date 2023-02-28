package com.tiburela.android.controlAsistencia.demo.models;

public class PromedioAsistenceEmpleado {

    private String empleadoName;
    private String entradaPromedio;
    private String salidaPromedio;

    public String getIdUserEmpleado() {
        return idUserEmpleado;
    }

    private String idUserEmpleado;

    private int asistenciaPromedio; //el porcentaje de asistencia ...


    public PromedioAsistenceEmpleado(String empleadoName, String entradaPromedio, String salidaPromedio, int asistenciaPromedio,String idUserEmpleado) {
        this.empleadoName = empleadoName;
        this.entradaPromedio = entradaPromedio;
        this.salidaPromedio = salidaPromedio;
        this.asistenciaPromedio = asistenciaPromedio;
        this.idUserEmpleado=idUserEmpleado;
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
