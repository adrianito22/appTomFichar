package com.tiburela.android.controlAsistencia.demo.Utils;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tiburela.android.controlAsistencia.demo.models.Empleado;
import com.tiburela.android.controlAsistencia.demo.models.Fichar;
import com.tiburela.android.controlAsistencia.demo.models.HorarIosTrabajos;

import java.util.HashMap;
import java.util.Map;

public class RealtimDatabase {

    static public DatabaseReference rootDatabaseReference;

    public static void initDatabasesRootOnly() {

        rootDatabaseReference = FirebaseDatabase.getInstance().getReference(); //anterior

        // mibasedataPathImages = rootDatabaseReference.child("Informes").child("ImagesData");


    }


    /*****ADD NUEVO ***/

    public static void addMarcacion(Context context, Fichar ficharObjec) {

        DatabaseReference mibasedata = rootDatabaseReference.child("marcaciones").child("allmarcaciones");
        String keyThisLoactionForm = mibasedata.push().getKey();
        ficharObjec.setKeyWhereLocalizeObjec(keyThisLoactionForm);
        Map<String, Object> mapValues = ficharObjec.toMap(); //lo convertimos en mapa

        mibasedata.child(keyThisLoactionForm).setValue(mapValues).addOnCompleteListener(new OnCompleteListener<Void>() {


            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {


                    Toast.makeText(context, "Se subio Correctamente", Toast.LENGTH_LONG).show();
                    // Toast.makeText(context, "Se subio", Toast.LENGTH_SHORT).show();

                    //callback aqui...

                } else {

                    Toast.makeText(context, "Ocurrio un Error, revisa tu conexion Internet", Toast.LENGTH_LONG).show();


                }
            }
        });


    }

    public static void addHorariosTrabajo(Context context, HorarIosTrabajos horarIosTrabajosObj) {

        DatabaseReference mibasedata = rootDatabaseReference.child("horarios").child("horariostrabajos");
          String keyThisLoactionForm = mibasedata.push().getKey();
        horarIosTrabajosObj.setKeyWhereLocalizeObjec(keyThisLoactionForm);

        Map<String, Object> mapValues = horarIosTrabajosObj.toMap(); //lo convertimos en mapa

        mibasedata.child(keyThisLoactionForm).setValue(mapValues).addOnCompleteListener(new OnCompleteListener<Void>() {


            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {


                    Toast.makeText(context, "Se subio Correctamente", Toast.LENGTH_LONG).show();
                    // Toast.makeText(context, "Se subio", Toast.LENGTH_SHORT).show();

                    //callback aqui...

                } else {

                    Toast.makeText(context, "Ocurrio un Error, revisa tu conexion Internet", Toast.LENGTH_LONG).show();


                }
            }
        });


    }


    public static void addEmpleado(Context context, Empleado empleado) {

        DatabaseReference mibasedata = rootDatabaseReference.child("empleados").child("allEmpleados");
        String keyThisLoactionForm = mibasedata.push().getKey();
        empleado.setKeyWhereLocalizeObjec(keyThisLoactionForm);

        Map<String, Object> mapValues = empleado.toMap(); //lo convertimos en mapa

        mibasedata.child(keyThisLoactionForm).setValue(mapValues).addOnCompleteListener(new OnCompleteListener<Void>() {


            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {


                    Toast.makeText(context, "Se subio Correctamente", Toast.LENGTH_LONG).show();
                    // Toast.makeText(context, "Se subio", Toast.LENGTH_SHORT).show();

                    //callback aqui...

                } else {

                    Toast.makeText(context, "Ocurrio un Error, revisa tu conexion Internet", Toast.LENGTH_LONG).show();


                }
            }
        });


    }


    public static void addHorarioDataMap(Context context,   HashMap<String,String> MImap,String keyDondeAddMAP) {

        DatabaseReference mibasedata = rootDatabaseReference.child("mapsHorariosData").child("maps");
        mibasedata.child(keyDondeAddMAP).setValue(MImap).addOnCompleteListener(new OnCompleteListener<Void>() {

            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {


                    Toast.makeText(context, "Se subio Correctamente", Toast.LENGTH_LONG).show();
                    // Toast.makeText(context, "Se subio", Toast.LENGTH_SHORT).show();

                    //callback aqui...

                } else {

                    Toast.makeText(context, "Ocurrio un Error, revisa tu conexion Internet", Toast.LENGTH_LONG).show();


                }
            }
        });


    }





    /*****UPDATE ***/

    public static void updateMarcacion(Context context, Fichar ficharObjec,String keyUpdateNode) {

        DatabaseReference mibasedata = rootDatabaseReference.child("marcaciones").child("allmarcaciones");
        ficharObjec.setKeyWhereLocalizeObjec(keyUpdateNode);
        Map<String, Object> mapValues = ficharObjec.toMap(); //lo convertimos en mapa

        mibasedata.child(keyUpdateNode).setValue(mapValues).addOnCompleteListener(new OnCompleteListener<Void>() {


            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {


                    Toast.makeText(context, "Se subio Correctamente", Toast.LENGTH_LONG).show();
                    // Toast.makeText(context, "Se subio", Toast.LENGTH_SHORT).show();

                    //callback aqui...

                } else {

                    Toast.makeText(context, "Ocurrio un Error, revisa tu conexion Internet", Toast.LENGTH_LONG).show();


                }
            }
        });


    }

    public static void updateHorariosTrabajo(Context context, HorarIosTrabajos horarIosTrabajosObj,String keyUpdateNode) {

        DatabaseReference mibasedata = rootDatabaseReference.child("horarios").child("horariostrabajos");
        horarIosTrabajosObj.setKeyWhereLocalizeObjec(keyUpdateNode);

        Map<String, Object> mapValues = horarIosTrabajosObj.toMap(); //lo convertimos en mapa

        mibasedata.child(keyUpdateNode).setValue(mapValues).addOnCompleteListener(new OnCompleteListener<Void>() {


            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {


                    Toast.makeText(context, "Se subio Correctamente", Toast.LENGTH_LONG).show();
                    // Toast.makeText(context, "Se subio", Toast.LENGTH_SHORT).show();

                    //callback aqui...

                } else {

                    Toast.makeText(context, "Ocurrio un Error, revisa tu conexion Internet", Toast.LENGTH_LONG).show();


                }
            }
        });


    }


    public static void updateEmpleado(Context context, Empleado empleado,String keyUpdateNode) {

        DatabaseReference mibasedata = rootDatabaseReference.child("empleados").child("allEmpleados");
        empleado.setKeyWhereLocalizeObjec(keyUpdateNode);

        Map<String, Object> mapValues = empleado.toMap(); //lo convertimos en mapa

        mibasedata.child(keyUpdateNode).setValue(mapValues).addOnCompleteListener(new OnCompleteListener<Void>() {


            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {


                    Toast.makeText(context, "Se subio Correctamente", Toast.LENGTH_LONG).show();
                    // Toast.makeText(context, "Se subio", Toast.LENGTH_SHORT).show();

                    //callback aqui...

                } else {

                    Toast.makeText(context, "Ocurrio un Error, revisa tu conexion Internet", Toast.LENGTH_LONG).show();


                }
            }
        });


    }



    public static void updateHorarioTrabajoMap(Context context,   HashMap<String,String> MImap,String keyUpdateNode) {

        DatabaseReference mibasedata = rootDatabaseReference.child("mapsHorariosData").child("maps");

        mibasedata.child(keyUpdateNode).setValue(MImap).addOnCompleteListener(new OnCompleteListener<Void>() {


            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {


                    Toast.makeText(context, "Se subio Correctamente", Toast.LENGTH_LONG).show();
                    // Toast.makeText(context, "Se subio", Toast.LENGTH_SHORT).show();

                    //callback aqui...

                } else {

                    Toast.makeText(context, "Ocurrio un Error, revisa tu conexion Internet", Toast.LENGTH_LONG).show();


                }
            }
        });


    }



}