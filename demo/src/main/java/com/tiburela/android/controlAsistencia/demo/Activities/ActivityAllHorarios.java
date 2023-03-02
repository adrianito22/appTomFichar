package com.tiburela.android.controlAsistencia.demo.Activities;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.tiburela.android.controlAsistencia.demo.R;
import com.tiburela.android.controlAsistencia.demo.Utils.RealtimDatabase;
import com.tiburela.android.controlAsistencia.demo.Utils.SharePref;
import com.tiburela.android.controlAsistencia.demo.Utils.Utils;
import com.tiburela.android.controlAsistencia.demo.adapters.AdapterHorario;
import com.tiburela.android.controlAsistencia.demo.models.Empleado;
import com.tiburela.android.controlAsistencia.demo.models.HorarIosTrabajos;

import java.util.ArrayList;
import java.util.HashMap;

public class ActivityAllHorarios extends AppCompatActivity {
RecyclerView recyclerViewHorarios;
    //    public static  ArrayList<HorarIosTrabajos> getListHorarios ( String KeyOfItem) {
Button btnCrearHorario;

    ArrayList<HorarIosTrabajos> listAllhorarios;

    HashMap<String,HorarIosTrabajos>miMap= new HashMap<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //aqui tendremos dos horarios

        setContentView(R.layout.activity_all_horarios);

        recyclerViewHorarios=findViewById(R.id.recyclerViewHorarios);

        btnCrearHorario=findViewById(R.id.btnCrearHorario);

        btnCrearHorario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Utils.isEditHorario=false;

                startActivity(new Intent(ActivityAllHorarios.this, ActivityHorario.class));


            }
        });





    }

    @Override
    protected void onStart() {
        super.onStart();



     //   miMap= SharePref.getListHorarios(SharePref.KEY_ALL_HORARIOS);
     //   listAllhorarios= new ArrayList<>();
     //   listAllhorarios.addAll(miMap.values());
       // gethasmpasAll(); //obtenemos asi todos los maps de los horarios....

        dowloadAllHorarios();

       // setDataRecyclerView(listAllhorarios);


    }

    private void setDataRecyclerView(ArrayList<HorarIosTrabajos> listx){
          if(listx.size()==0){
              TextView txtHorarioAdviser=findViewById(R.id.txtHorarioAdviser);
              txtHorarioAdviser.setVisibility(View.VISIBLE);
          }



        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ActivityAllHorarios.this);

        AdapterHorario adapter = new AdapterHorario(ActivityAllHorarios.this, listx);
        recyclerViewHorarios.setLayoutManager(layoutManager);
        recyclerViewHorarios.setAdapter(adapter);


        adapter.setOnItemClickListener(new AdapterHorario.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {

                Utils.currentHorarioSelectedUid =listx.get(position).getIdHorarioHereKEYpreferences();

                Log.i("smaornrnr","el uid selected es  "+Utils.currentHorarioSelectedUid);

                Utils.isEditHorario=true;

                Intent intencion= new Intent(ActivityAllHorarios.this, ActivityHorario.class);
                startActivity(intencion);


            }
        });




    }



    void gethasmpasAll(){


        for(HorarIosTrabajos objec: listAllhorarios){

            HashMap<String,String>mapHorario =  SharePref.loadMapPreferencesHorario(objec.getIdHorarioHereKEYpreferences());

            if(mapHorario.size()>0){
                Utils.mihasWhitMhorariosAll.put(objec.getIdHorarioHereKEYpreferences(),mapHorario);

            }
        }


    }

    private void dowloadAllHorarios(  ){

        ValueEventListener seenListener  = RealtimDatabase.rootDatabaseReference.child("horarios").child("horariostrabajos")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        listAllhorarios= new ArrayList<>();


                        for (DataSnapshot dss : dataSnapshot.getChildren()) {
                            HorarIosTrabajos horario = dss.getValue(HorarIosTrabajos.class);

                            if (horario != null) {
                                listAllhorarios.add(horario);

                            }
                        }


                        callsolwoadHorarios(listAllhorarios);




                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.i("libiadod", "el error es " + databaseError.getMessage());

                    }
                });

    }


    private void callsolwoadHorarios(ArrayList<HorarIosTrabajos>list){

        int contador=0;
        boolean descrgamosTodos=false;
        for(HorarIosTrabajos objec: list){  //size 1
            contador++;

            if(contador==list.size()){
                descrgamosTodos=true;
            }


            dowloadHoriosMap(objec.getKeylocalizeMapHorario(),objec.getIdHorarioHereKEYpreferences(),descrgamosTodos);

        }
    }


    private void dowloadHoriosMap(String keylocalizemap ,String keyMAP,boolean yaDescargamosTodos ){

        ValueEventListener seenListener  = RealtimDatabase.rootDatabaseReference.child("mapsHorariosData").child("maps").child(keylocalizemap)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //   Utils.mihasWhitMhorariosAll = new HashMap<>();

                         HashMap<String,String>miMap= new HashMap<>();


                        for (DataSnapshot dss : dataSnapshot.getChildren()) {
                            String key = dss.getKey();
                            String fieldData = dss.getValue(String.class);

                            if (fieldData != null) {///
                                miMap.put(key, fieldData);
                            }
                        }

                   Utils.mihasWhitMhorariosAll.put(keyMAP,miMap);

                         if(yaDescargamosTodos){ //cuando descarguemos todos..

                             setDataRecyclerView(listAllhorarios);

                         }




                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.i("libiadod", "el error es " + databaseError.getMessage());


                    }
                });

    }



}