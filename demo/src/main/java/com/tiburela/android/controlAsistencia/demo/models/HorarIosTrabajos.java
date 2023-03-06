package com.tiburela.android.controlAsistencia.demo.models;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HorarIosTrabajos {


  public HorarIosTrabajos(){

  }

  private String horaraEntradaString;
  private String horaraSalidadaString;
  private HashMap<String, Integer>hasmpaDiasTrabajo;

  public String getMailEmppleador() {
    return mailEmppleador;
  }

  public void setMailEmppleador(String mailEmppleador) {
    this.mailEmppleador = mailEmppleador;
  }

  private String mailEmppleador;

  public String getIdHorarioHereKEYpreferences() {
    return idHorarioHereKEYpreferences;
  }

  public void setIdHorarioHereKEYpreferences(String idHorarioHereKEYpreferences) {
    this.idHorarioHereKEYpreferences = idHorarioHereKEYpreferences;
  }

  private String idHorarioHereKEYpreferences;

  public String getKeylocalizeMapHorario() {
    return keylocalizeMapHorario;
  }

  public void setKeylocalizeMapHorario(String keylocalizeMapHorario) {
    this.keylocalizeMapHorario = keylocalizeMapHorario;
  }

  private String keylocalizeMapHorario;

  public String getKeyWhereLocalizeObjec() {
    return keyWhereLocalizeObjec;
  }

  public void setKeyWhereLocalizeObjec(String keyWhereLocalizeObjec) {
    this.keyWhereLocalizeObjec = keyWhereLocalizeObjec;
  }

  private String keyWhereLocalizeObjec;

  public String getHorarioNombre() {
    return horarioNombre;
  }

  public void setHorarioNombre(String horarioNombre) {
    this.horarioNombre = horarioNombre;
  }

  private String horarioNombre;



  public String getHoraraEntradaString() {
    return horaraEntradaString;
  }

  public void setHoraraEntradaString(String horaraEntradaString) {
    this.horaraEntradaString = horaraEntradaString;
  }

  public String getHoraraSalidadaString() {
    return horaraSalidadaString;
  }

  public void setHoraraSalidadaString(String horaraSalidadaString) {
    this.horaraSalidadaString = horaraSalidadaString;
  }

  public HashMap<String, Integer> getHasmpaDiasTrabajo() {
    return hasmpaDiasTrabajo;
  }

  public void setHasmpaDiasTrabajo(HashMap<String, Integer> hasmpaDiasTrabajo) {
    this.hasmpaDiasTrabajo = hasmpaDiasTrabajo;
  }



  public HorarIosTrabajos(String horaraEntradaString, String horaraSalidadaString, HashMap<String, Integer> hasmpaDiasTrabajo,String horarioNombre) {
    this.horaraEntradaString = horaraEntradaString;
    this.horaraSalidadaString = horaraSalidadaString;
    this.hasmpaDiasTrabajo = hasmpaDiasTrabajo;
    this.horarioNombre=horarioNombre;
  }

  public HorarIosTrabajos(String horaraEntradaString, String horaraSalidadaString,String horarioNombre,String mailEmppleador) {
    this.horaraEntradaString = horaraEntradaString;
    this.horaraSalidadaString = horaraSalidadaString;
    this.horarioNombre=horarioNombre;
    idHorarioHereKEYpreferences= UUID.randomUUID().toString();
     keyWhereLocalizeObjec="";
    keylocalizeMapHorario="";
    this.mailEmppleador=mailEmppleador;


  }







  public Map<String, Object> toMap() {
    HashMap<String, Object> result = new HashMap<>();


    result.put("horaraEntradaString", horaraEntradaString);
    result.put("horaraSalidadaString", horaraSalidadaString);
    result.put("horarioNombre", horarioNombre);
    result.put("idHorarioHereKEYpreferences", idHorarioHereKEYpreferences);
    result.put("keyWhereLocalizeObjec", keyWhereLocalizeObjec);
    result.put("keylocalizeMapHorario", keylocalizeMapHorario);
    result.put("mailEmppleador", mailEmppleador);


    return result;

  }





}
