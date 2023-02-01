package com.google.android.cameraview.demo.Activities;

import android.content.Intent;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.cameraview.demo.R;
import com.google.android.cameraview.demo.Utils.SharePref;
import com.google.android.cameraview.demo.adapters.AdapterEmpleado;
import com.google.android.cameraview.demo.models.Empleado;

import java.util.ArrayList;
import java.util.HashMap;

public class ActivityEmpleadosAll extends AppCompatActivity {

    RecyclerView recylerVInformsAll;
     ArrayList<Empleado>listAllEmpleados= new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empleados_all);

        recylerVInformsAll=findViewById(R.id.recylerVInformsAll);


        HashMap<String, Empleado> mhasMap = SharePref.loadMapPreferencesEmpleados(SharePref.KEY_ALL_EMPLEADOS_Map);



        if(mhasMap.size()>0){ //la lista tiene data

            listAllEmpleados.addAll(mhasMap.values());

            setDataRecyclerView(listAllEmpleados);

        }



        else{ //no hay ningun empleado registrado//mostramos txt no hay texto
            TextView txtAdviser= findViewById(R.id.txtAdviser);
            txtAdviser.setVisibility(View.VISIBLE);

        }





    }





    private void setDataRecyclerView(ArrayList<Empleado> list){


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ActivityEmpleadosAll.this);

        AdapterEmpleado adapter = new AdapterEmpleado(ActivityEmpleadosAll.this, list);
        recylerVInformsAll.setLayoutManager(layoutManager);
        recylerVInformsAll.setAdapter(adapter);


        adapter.setOnItemClickListener(new AdapterEmpleado.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {


                sheetBootomInforOptions(position);

              //  sheetBootomInforOptions(v.getTag(R.id.tagUniqueId1).toString(),v.getTag(R.id.tagUniqueId2).toString(),v.getTag(R.id.codigoProductor).toString());


                Log.i("elcickler","el click es llamado");




            }
        });




    }


    private void sheetBootomInforOptions(int positionSelected){


        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ActivityEmpleadosAll.this);

        bottomSheetDialog.setContentView(R.layout.bottom_sheet_options_infromsx);


        Button btnAbrir=bottomSheetDialog.findViewById(R.id.btnAbrir);
        Button btnEliminar=bottomSheetDialog.findViewById(R.id.btnEliminar);

        //ImageView imgClose=bottomSheetDialog.findViewById(R.id.imgClose);
        //8  bottomSheetDialog.setCancelable(false);


        btnAbrir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /**abrimos el informe dpeendiendo del tipo de informe */

                //aqui recuperamos el id ...del informe...
                //intent...//aqui abrimos dialog.... y le pasamos un intent...

                Log.i("elcickler","el click es llamadoen sheet");



                bottomSheetDialog.dismiss();


            }
        });



        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                sheetBootomSureDeleteQuestion(positionSelected);

                bottomSheetDialog.dismiss();


            }
        });




        bottomSheetDialog.show();

    }


    private void sheetBootomSureDeleteQuestion(int positionSelected){

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ActivityEmpleadosAll.this);

        bottomSheetDialog.setContentView(R.layout.bottom_sheet_sure_delete);

        Button btnSiElimnar=bottomSheetDialog.findViewById(R.id.btnSiElimnar);
        Button btnNoElimnar=bottomSheetDialog.findViewById(R.id.btnNoElimnar);

        //ImageView imgClose=bottomSheetDialog.findViewById(R.id.imgClose);
        //8  bottomSheetDialog.setCancelable(false);


        btnSiElimnar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                bottomSheetDialog.dismiss();


            }
        });



        btnNoElimnar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bottomSheetDialog.dismiss();


            }
        });




        bottomSheetDialog.show();

    }




}