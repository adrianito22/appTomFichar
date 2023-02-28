package com.tiburela.android.controlAsistencia.demo.models;

import java.util.HashMap;

public class Fichar {

    public static int FICHAJE_INCIO_COMIDA=100;
    public static int FICHAJE_FIN_COMIDA=101;
    public static int FICHAJE_ENTRADA=102;
    public static int FICHAJE_SALIDA=103;

    public static int tipoFichanSelecionadoCurrent;

    public static HashMap<String , Fichar>hashMapAllFicharRegistros= new HashMap<>();

    public static HashMap<String ,Empleado> hasMapAllEmpleados = new HashMap<>();


    public Fichar() {
        entradaMilliseconds= 0;
        horaInicioComidaMilliseconds= 0;
        horaFinComidaMilliseconds= 0;
        horaSalidaMilliseconds= 0;
        horasTrabajadas=0;
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




}
