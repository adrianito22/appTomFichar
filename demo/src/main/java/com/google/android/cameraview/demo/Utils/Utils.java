package com.google.android.cameraview.demo.Utils;

import android.util.Log;

import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class Utils {

    public static boolean existeNewUserAdd=false;


    public static void formatTime(Date time, Locale locale){

    }



    public static  int generateNumRadom4igits() {

        Random r = new Random( System.currentTimeMillis() );
        Log.i("elnumber","el numero generado es "+((1 + r.nextInt(2)) * 10000 + r.nextInt(10000)));

        return ((1 + r.nextInt(2)) * 1000 + r.nextInt(1000)); //estab en 5 ceros

    }
}
