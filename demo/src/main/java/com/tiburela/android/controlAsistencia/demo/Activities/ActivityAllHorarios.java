package com.tiburela.android.controlAsistencia.demo.Activities;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tiburela.android.controlAsistencia.demo.R;
import com.tiburela.android.controlAsistencia.demo.Utils.SharePref;
import com.tiburela.android.controlAsistencia.demo.Utils.Utils;
import com.tiburela.android.controlAsistencia.demo.adapters.AdapterHorario;
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



        miMap= SharePref.getListHorarios(SharePref.KEY_ALL_HORARIOS);
        listAllhorarios= new ArrayList<>();
        listAllhorarios.addAll(miMap.values());
        gethasmpasAll(); //obtenemos asi todos los maps de los horarios....
        setDataRecyclerView(listAllhorarios);


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

}