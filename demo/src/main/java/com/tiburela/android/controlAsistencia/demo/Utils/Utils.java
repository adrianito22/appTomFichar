package com.tiburela.android.controlAsistencia.demo.Utils;

import android.util.Log;

import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;

public class Utils {

    /**CONTRASENA QUE NECESITAMOS PARA ENTRAR A ADMIN**/
    public final static String PASSWORD_ADMIN="1111";
    public static boolean isEditHorario=false;

    public static final  boolean modoOnlineActivate=true;



    public static String nameCurrentEmpleado="";

    public  static HashMap<String,HashMap<String,String>> mihasWhitMhorariosAll= new HashMap();
    public static String currentHorarioSelectedUid;

    public static final int GO_ActivityHorario=200;
    public static final int GO_AddPerson=201;


    public static String [] arrayMesSelecionado={"Enero","Febrero","Marzo","Abril","Mayo","Junio","Julio",
            "Agosto", "Septiembre","Octubre","Noviembre","Diciembre"};


    public static  int tipodeDatoMostrar=Utils.ITEM_MARACIONES_MODO;

    public static final int ITEM_MARACIONES_MODO=121;
    public static final int ITEM_ASISTENCIA_MODO=120;


    public static final int MES_ESPECIFICO=4;
    public static final int DIA_ESPECIFICO=5;



    public final static int NUMS_DIAS_MINIMO=2;


    /**dias semna*/
    public static int LUNES=1;
    public static int MARTES=2;
    public static int MIERCOLES=3;
    public static int JUEVES=4;
    public static int VIERNES=5;
    public static int SABADO=6;
    public static int DOMINGO=7;


    public static boolean existeNewUserAdd=false;


    public static void formatTime(Date time, Locale locale){

    }



    public static  int generateNumRadom4igits() {

        Random r = new Random( System.currentTimeMillis() );
        Log.i("elnumber","el numero generado es "+((1 + r.nextInt(2)) * 10000 + r.nextInt(10000)));

        return ((1 + r.nextInt(2)) * 1000 + r.nextInt(1000)); //estab en 5 ceros


    }






}
