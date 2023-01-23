package com.google.android.cameraview.demo.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;


import com.google.android.cameraview.demo.models.Empleado;
import com.google.android.cameraview.demo.models.Fichar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SharePref {


 public static  final String KEY_FICHAR_MAP="KEY_FICHAR_MAP";
    public static  final String KEY_ALL_EMPLEADOS_Map ="KEYALLEMPLEADOSxxx";

    private static SharedPreferences mSharedPrefUniqueObjc;




    public static void init(Context context)
    {
        if(mSharedPrefUniqueObjc == null)
            mSharedPrefUniqueObjc = context.getSharedPreferences(context.getPackageName(), Activity.MODE_PRIVATE);


    }





    public static void deleteMap(Context context,String keyTOremove) {
       // SharedPreferences pSharedPref = context.getSharedPreferences("MyVariables", Context.MODE_PRIVATE);
        if (mSharedPrefUniqueObjc != null) {
            mSharedPrefUniqueObjc.edit()
                    .remove(keyTOremove)
                    //.putString("My_map", jsonString)
                    .apply();
        }
    }








    public static  void saveMapFichaje(HashMap<String, Fichar> inputMap, String keySharePref) {
        if (mSharedPrefUniqueObjc != null){
            // JSONObject jsonObject = new JSONObject(inputMap);
            //   String jsonString = jsonObject.toString();
            mSharedPrefUniqueObjc.edit()
                    //  .remove("My_map")
                    .putString(keySharePref, new Gson().toJson(inputMap))


                    // .putString(keySharePref, jsonString)
                    .apply();
        }
    }

    public static  HashMap<String,Fichar> loadMapPreferencesFichaje(String keyShare) {

        Gson   gson = new Gson();
        String response=mSharedPrefUniqueObjc.getString(keyShare , "");

        Type type = new TypeToken<HashMap<String,Fichar>>(){}.getType();
        HashMap<String,Fichar> map;
        map = gson.fromJson(response, type);


        if(response.equals("")) {
            Log.i("lashareperf","no hay data en share");

            map= new HashMap<>();
            return map;

        }else{
            Log.i("lashareperf","Si hay data en share y data es length es "+response);
            return map;
        }


    }






    public static  void saveMapEmpleados(HashMap<String, Empleado> inputMap, String keySharePref) {
        if (mSharedPrefUniqueObjc != null){
            // JSONObject jsonObject = new JSONObject(inputMap);
            //   String jsonString = jsonObject.toString();
            mSharedPrefUniqueObjc.edit()
                    //  .remove("My_map")
                    .putString(keySharePref, new Gson().toJson(inputMap))


                    // .putString(keySharePref, jsonString)
                    .apply();
        }
    }

    public static  HashMap<String,Empleado> loadMapPreferencesEmpleados(String keyShare) {

        Gson   gson = new Gson();
        String response=mSharedPrefUniqueObjc.getString(keyShare , "");

        Type type = new TypeToken<HashMap<String,Empleado>>(){}.getType();
        HashMap<String,Empleado> map;
        map = gson.fromJson(response, type);


        if(response.equals("")) {
            Log.i("lashareperf","no hay data en share");

            map= new HashMap<>();
            return map;

        }else{
            Log.i("lashareperf","Si hay data en share y data es length es "+response);
            return map;
        }

    }






    public static void saveListEmpleados (ArrayList<Empleado> listEmpleados, String KeyTOsAVE) {
        Gson   gson = new Gson();
        String jsonListString = gson.toJson(listEmpleados);

        mSharedPrefUniqueObjc.edit()
                //.remove("My_map")
                .putString(KeyTOsAVE, jsonListString);

    }


    public static  ArrayList<Empleado> getListEmpleado ( String KeyOfItem) {
        Gson   gson = new Gson();
        String response=mSharedPrefUniqueObjc.getString(KeyOfItem , "");

        Type type = new TypeToken<ArrayList<Empleado>>(){}.getType();
        ArrayList<Empleado> listProductores;
        listProductores = gson.fromJson(response, type);


        if(response.equals("")) {
            Log.i("lashareperf","no hay data en share");

            listProductores= new ArrayList<>();
            return listProductores;
        }

        else{
            Log.i("lashareperf","Si hay data en share y el length es "+listProductores.size());

            return listProductores;
        }

    }


}
