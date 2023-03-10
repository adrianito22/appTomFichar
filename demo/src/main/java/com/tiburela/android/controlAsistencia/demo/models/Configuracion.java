package com.tiburela.android.controlAsistencia.demo.models;

public class Configuracion {

    public Configuracion(){

    }

    private String passWord;

    public String getKeyLocalice() {
        return keyLocalice;
    }

    public void setKeyLocalice(String keyLocalice) {
        this.keyLocalice = keyLocalice;
    }

    private String keyLocalice;


    public String getMailUser() {
        return mailUser;
    }


    private String mailUser;


    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    private String nameUser;



    public Configuracion(String passWord,String mailUser) {
        this.passWord = passWord;
        nameUser = "";
        this.mailUser=mailUser;
        keyLocalice="";
    }




}
