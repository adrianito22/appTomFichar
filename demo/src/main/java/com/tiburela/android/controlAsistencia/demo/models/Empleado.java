package com.tiburela.android.controlAsistencia.demo.models;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Empleado {
    public String getMailEmppleador() {
        return mailEmppleador;
    }

    public void setMailEmppleador(String mailEmppleador) {
        this.mailEmppleador = mailEmppleador;
    }

    private String mailEmppleador;

    public  Empleado(){


    }

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

    public String getUrlPickEmpleado() {
        return urlPickEmpleado;
    }

    public void setUrlPickEmpleado(String urlPickEmpleado) {
        this.urlPickEmpleado = urlPickEmpleado;
    }

    private String  urlPickEmpleado;


    public String getCodigoPaFichar() {
        return codigoPaFichar;
    }

    public void setCodigoPaFichar(String codigoPaFichar) {
        this.codigoPaFichar = codigoPaFichar;
    }

    private String  codigoPaFichar;

    public String getKeyWhereLocalizeObjec() {
        return keyWhereLocalizeObjec;
    }

    public void setKeyWhereLocalizeObjec(String keyWhereLocalizeObjec) {
        this.keyWhereLocalizeObjec = keyWhereLocalizeObjec;
    }

    private String keyWhereLocalizeObjec;



    public Empleado( String nombreYapellidoEmpleado, String idEmpleado,String codigoPaFichar,String mailEmppleador) {

        fechaRegistro = new Date().getTime();
        fechaRegistroString = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(fechaRegistro);
        this.nombreYapellidoEmpleado = nombreYapellidoEmpleado;
        this.idEmpleado = idEmpleado;
        this.codigoPaFichar=codigoPaFichar;
        keyWhereLocalizeObjec="";
        urlPickEmpleado="";
        this.mailEmppleador=mailEmppleador;

    }



    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("fechaRegistro", fechaRegistro);
        result.put("fechaRegistroString", fechaRegistroString);
        result.put("nombreYapellidoEmpleado", nombreYapellidoEmpleado);

        result.put("idEmpleado", idEmpleado);
        result.put("codigoPaFichar", codigoPaFichar);
        result.put("keyWhereLocalizeObjec",keyWhereLocalizeObjec);
        result.put("urlPickEmpleado",urlPickEmpleado);
        result.put("mailEmppleador", mailEmppleador);



        return result;

    }

}
