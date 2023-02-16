package com.google.android.cameraview.demo.models;

import java.util.HashMap;
import java.util.UUID;

public class HorarIosTrabajos {


  private String horaraEntradaString;
  private String horaraSalidadaString;
  private HashMap<String, Integer>hasmpaDiasTrabajo;

  public String getIdHorarioHereKEYpreferences() {
    return idHorarioHereKEYpreferences;
  }

  public void setIdHorarioHereKEYpreferences(String idHorarioHereKEYpreferences) {
    this.idHorarioHereKEYpreferences = idHorarioHereKEYpreferences;
  }

  private String idHorarioHereKEYpreferences;


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

  public HorarIosTrabajos(String horaraEntradaString, String horaraSalidadaString,String horarioNombre) {
    this.horaraEntradaString = horaraEntradaString;
    this.horaraSalidadaString = horaraSalidadaString;
    this.horarioNombre=horarioNombre;
    idHorarioHereKEYpreferences= UUID.randomUUID().toString();

  }













}
