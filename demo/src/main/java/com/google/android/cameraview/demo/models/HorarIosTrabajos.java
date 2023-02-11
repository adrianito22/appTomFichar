package com.google.android.cameraview.demo.models;

import java.util.HashMap;

public class HorarIosTrabajos {


  private String horaraEntradaString;
  private String horaraSalidadaString;
  private HashMap<String, Integer>hasmpaDiasTrabajo;


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



  public HorarIosTrabajos(String horaraEntradaString, String horaraSalidadaString, HashMap<String, Integer> hasmpaDiasTrabajo) {
    this.horaraEntradaString = horaraEntradaString;
    this.horaraSalidadaString = horaraSalidadaString;
    this.hasmpaDiasTrabajo = hasmpaDiasTrabajo;
  }












}
