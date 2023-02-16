package com.google.android.cameraview.demo.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.cameraview.demo.models.Empleado;
import com.google.android.cameraview.demo.models.Fichar;
import com.google.android.cameraview.demo.models.HorarIosTrabajos;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class SharePref {


 public static  final String KEY_FICHAR_MAP="KEY_FICHAR_MAP";
    public static  final String KEY_ALL_EMPLEADOS_Map ="KEYALLEMPLEADOSxxx";

    public static  final String KEY_ALL_HORARIOS ="horarios";

    private static SharedPreferences mSharedPrefUniqueObjc;




    public static void init(Context context)
    {
        if(mSharedPrefUniqueObjc == null)
            mSharedPrefUniqueObjc = context.getSharedPreferences(context.getPackageName(), AppCompatActivity.MODE_PRIVATE);


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



    public static  void saveMapHorario(HashMap<String, String> inputMap, String keySharePref) {
        if (mSharedPrefUniqueObjc != null){
            // JSONObject jsonObject = new JSONObject(inputMap);
            //   String jsonString = jsonObject.toString();
            mSharedPrefUniqueObjc.edit()
                    //  .remove("My_map")
                    .putString(keySharePref, new Gson().toJson(inputMap))


                    // .putString(keySharePref, jsonString)
                    .apply();

            Log.i("norooas","el size de map aqui es "+inputMap.size());

        }
    }

    public static  HashMap<String,String> loadMapPreferencesHorario(String keyShare) {

        Gson   gson = new Gson();
        String response=mSharedPrefUniqueObjc.getString(keyShare , "");

        Type type = new TypeToken<HashMap<String,String>>(){}.getType();
        HashMap<String,String> map;
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





    public static void saveListHorarios (HashMap<String, HorarIosTrabajos> hasMapHorarios, String KeyTOsAVE) {
        Gson   gson = new Gson();
        String jsonListString = gson.toJson(hasMapHorarios);

      //  .edit()
                //.remove("My_map")
              //  .putString(KeyTOsAVE, jsonListString).

                mSharedPrefUniqueObjc.edit()
                //  .remove("My_map")
                .putString(KeyTOsAVE,jsonListString )

                        .putString(KeyTOsAVE, jsonListString)

                // .putString(keySharePref, jsonString)
                .apply();

                // .putString(keySharePref, jsonString)

        Log.i("sumare","el size es "+jsonListString.length()+" y el key es "+KeyTOsAVE);

    }

    public static  HashMap<String, HorarIosTrabajos> getListHorarios ( String KeyOfItem) {
        Gson   gson = new Gson();
        String response=mSharedPrefUniqueObjc.getString(KeyOfItem , "");

        //Type type = new TypeToken<ArrayList<HorarIosTrabajos>>(){}.getType();

        Type listType = new TypeToken<HashMap<String, HorarIosTrabajos>>(){}.getType();


        HashMap<String, HorarIosTrabajos> listHorarios = gson.fromJson(response, listType);

        assert response != null;
        if(response.equals("")) {
            Log.i("lashareperf","no hay data en share");

            listHorarios= new HashMap();
            return listHorarios;
        }

        else{
            Log.i("lashareperf","Si hay data en share y el length es "+listHorarios.size());

            return listHorarios;
        }

    }



}
