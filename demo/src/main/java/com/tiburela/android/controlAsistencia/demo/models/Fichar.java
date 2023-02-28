package com.tiburela.android.controlAsistencia.demo.models;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Fichar {

    public static int FICHAJE_INCIO_COMIDA=100;
    public static int FICHAJE_FIN_COMIDA=101;
    public static int FICHAJE_ENTRADA=102;
    public static int FICHAJE_SALIDA=103;

    public static int tipoFichanSelecionadoCurrent;

    public static HashMap<String , Fichar>hashMapAllFicharRegistros= new HashMap<>();



public Fichar(){

}

    public static HashMap<String ,Empleado> hasMapAllEmpleados = new HashMap<>();

    private String ficharUserId;

    public String getKeyficharDate() {
        return keyficharDate;
    }

    public void setKeyficharDate(String keyficharDate) {
        this.keyficharDate = keyficharDate;
    }

    private String keyficharDate;

    public String getKeyWhereLocalizeObjec() {
        return keyWhereLocalizeObjec;
    }

    public void setKeyWhereLocalizeObjec(String keyWhereLocalizeObjec) {
        this.keyWhereLocalizeObjec = keyWhereLocalizeObjec;
    }

    private String keyWhereLocalizeObjec;


    public Fichar(String ficharUserId,String keyficharDate) {
        entradaMilliseconds= 0;
        horaInicioComidaMilliseconds= 0;
        horaFinComidaMilliseconds= 0;
        horaSalidaMilliseconds= 0;
        horasTrabajadas=0;
        this.ficharUserId=ficharUserId;
        this.keyficharDate=keyficharDate;
        keyWhereLocalizeObjec="";

    }


    public long getHoraInicioComidaMilliseconds() {
        return horaInicioComidaMilliseconds;
    }

    public void setHoraInicioComidaMilliseconds(long horaInicioComidaMilliseconds) {
        this.horaInicioComidaMilliseconds = horaInicioComidaMilliseconds;
    }

    public long getHoraFinComidaMilliseconds() {
        return horaFinComidaMilliseconds;
    }

    public void setHoraFinComidaMilliseconds(long horaFinComidaMilliseconds) {
        this.horaFinComidaMilliseconds = horaFinComidaMilliseconds;
    }

    public long getHoraSalidaMilliseconds() {
        return horaSalidaMilliseconds;
    }

    public void setHoraSalidaMilliseconds(long horaSalidaMilliseconds) {
        this.horaSalidaMilliseconds = horaSalidaMilliseconds;
    }

    public long getEntradaMilliseconds() {
        return entradaMilliseconds;
    }

    public void setEntradaMilliseconds(long entradaMilliseconds) {
        this.entradaMilliseconds = entradaMilliseconds;
    }

    private long horaFinComidaMilliseconds;
    private long horaSalidaMilliseconds;
    private long entradaMilliseconds; //fecha y hora de entrada
    private long horaInicioComidaMilliseconds;

    public long getHorasTrabajadas() {
        return horasTrabajadas;
    }

    public void setHorasTrabajadas(long horasTrabajadas) {
        this.horasTrabajadas = horasTrabajadas;
    }

    private long horasTrabajadas;

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("entradaMilliseconds", entradaMilliseconds);
        result.put("horaInicioComidaMilliseconds", horaInicioComidaMilliseconds);
        result.put("horaFinComidaMilliseconds", horaFinComidaMilliseconds);

        result.put("horaSalidaMilliseconds", horaSalidaMilliseconds);
        result.put("horasTrabajadas", horasTrabajadas);
        result.put("ficharUserId", ficharUserId);

        result.put("keyficharDate", keyficharDate);
        result.put("keyWhereLocalizeObjec", keyWhereLocalizeObjec);

        return result;

    }


}
