package com.tiburela.android.controlAsistencia.demo.Activities;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import com.google.android.material.textfield.TextInputEditText;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tiburela.android.controlAsistencia.demo.R;
import com.tiburela.android.controlAsistencia.demo.Utils.RealtimDatabase;
import com.tiburela.android.controlAsistencia.demo.Utils.SharePref;
import com.tiburela.android.controlAsistencia.demo.Utils.Utils;
import com.tiburela.android.controlAsistencia.demo.models.HorarIosTrabajos;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class ActivityHorario extends AppCompatActivity implements View.OnClickListener {


    HashMap<String,String>MImap= new HashMap<>();

    TextInputEditText txtImputEntrada;
    TextInputEditText txtImputSalida;

    String keylocalizeMapHorario="";


   //DIAS SEMANA
   TextView txtLunes;
    TextView txtMartes;
    TextView txtMiercoles;
    TextView txtJueves;
    TextView txtViernes;
    TextView txtSabado;
    TextView txtDomingo;

     TextInputEditText ediNombreHorario;

    TextView txtNumsEmpleados;
    ImageView imgAddEmpleado;

   Button btnGuardar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horario);

       // SharePref.init(ActivityHorario.this);

        findviewids();

        //AD CLICK LSITENER
         txtLunes.setOnClickListener(this);
         txtMartes.setOnClickListener(this);
         txtMiercoles.setOnClickListener(this);
         txtJueves.setOnClickListener(this);
         txtViernes.setOnClickListener(this);
         txtSabado.setOnClickListener(this);
         txtDomingo.setOnClickListener(this);


         //confirguramos editext
        txtImputEntrada.setCursorVisible(false);
        txtImputEntrada.setKeyListener(null);


        txtImputSalida.setCursorVisible(false);
        txtImputSalida.setKeyListener(null);



        if(Utils.isEditHorario){


            dowloadHorRIOoBJEC(Utils.currentHorarioSelectedUid);

            //obtenemos preferencias y si hay data la seteamos
/*
            MImap= SharePref.loadMapPreferencesHorario(Utils.currentHorarioSelectedUid);

            if(MImap.size()>0){

                Log.i("sumare","el size es mayor a 0");
                seteamosHorarioDePrefrencias();

            }
*/
        }




       // Log.i("sumare","el size lis here es t"+lis.size());



        imgAddEmpleado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intencion = new Intent(ActivityHorario.this, AddPerson.class);
                startActivity(intencion);

            }
        });


        txtImputEntrada.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {




                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN){
                    Log.i("misdatas`","se presiono el click ");
                    showingTimePicker(view);
                }


                return false;

            }
        });
        txtImputSalida.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {




                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN){
                    Log.i("misdatas`","se presiono el click ");
                    showingTimePicker(view);
                }


                return false;

            }
        });

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(!checkExistDiaSelectAlMenos()){
                    return;
                }


                if(txtImputEntrada.getText().toString().equals("")){
                    Toast.makeText(ActivityHorario.this, "Selecione una hora de entrada", Toast.LENGTH_SHORT).show();
                    return;
                }


                if(txtImputSalida.getText().toString().equals("")){
                    Toast.makeText(ActivityHorario.this, "Selecione una hora de Salida", Toast.LENGTH_SHORT).show();
                    return;
                }


                if(ediNombreHorario.getText().toString().equals("")){
                    Toast.makeText(ActivityHorario.this, " Agrege un nombre al Horario", Toast.LENGTH_SHORT).show();
                    return;
                }



                if(Utils.modoOnlineActivate){

                    saveDataInDataBase();

                }else {  //agudramso en prefencioas

                    saveDataInPreferences();

                }




            }
        });


    }



    private void saveDataInPreferences(){

        ///vamos a guardar el horario...

        MImap.put(String.valueOf(txtImputEntrada.getId()),txtImputEntrada.getText().toString());

        MImap.put(String.valueOf(txtImputSalida.getId()),txtImputSalida.getText().toString());

        MImap.put(String.valueOf(ediNombreHorario.getId()),ediNombreHorario.getText().toString());


        TextView [] txtArray={txtLunes,txtMartes,txtMiercoles,txtJueves,txtViernes,txtSabado,txtDomingo};

        for(TextView txtCurrent: txtArray){//si contiene el bacground

            if(txtCurrent.getBackground()==ContextCompat.getDrawable(  ActivityHorario.this, R.drawable.back_dia_selected)){
                MImap.put(String.valueOf(txtCurrent.getTag()),txtCurrent.getTag().toString());
            }
        }


        HorarIosTrabajos horarioObjec= new HorarIosTrabajos(txtImputEntrada.getText().toString(),txtImputSalida.getText().toString(),
                ediNombreHorario.getText().toString(),Utils.maiLEmpleadorGlOBAL);

        HashMap<String, HorarIosTrabajos>mapHorariosTrabajos=SharePref.getListHorarios(SharePref.KEY_ALL_HORARIOS);



        if(Utils.isEditHorario){ //estamos editando
            horarioObjec.setIdHorarioHereKEYpreferences(Utils.currentHorarioSelectedUid);
            mapHorariosTrabajos.put(Utils.currentHorarioSelectedUid,horarioObjec);

        }

        else{ //es crear un nuevo horario
            mapHorariosTrabajos.put(horarioObjec.getIdHorarioHereKEYpreferences(),horarioObjec);
            RealtimDatabase.addHorariosTrabajo(ActivityHorario.this,horarioObjec);


        }


        Log.i("smaornrnr","el uid de horario object es "+horarioObjec.getIdHorarioHereKEYpreferences());


        SharePref.saveListHorarios(mapHorariosTrabajos,SharePref.KEY_ALL_HORARIOS);
        SharePref.saveMapHorario(MImap,horarioObjec.getIdHorarioHereKEYpreferences());


        Toast.makeText(ActivityHorario.this, "Se guarad?? Horario", Toast.LENGTH_SHORT).show();

        finish();

        Log.i("sumare","hemos guardado aqui hurra");



    }


    private void saveDataInDataBase(){

        ///vamos a guardar el horario...

        MImap.put(String.valueOf(txtImputEntrada.getId()),txtImputEntrada.getText().toString());

        MImap.put(String.valueOf(txtImputSalida.getId()),txtImputSalida.getText().toString());

        MImap.put(String.valueOf(ediNombreHorario.getId()),ediNombreHorario.getText().toString());


        TextView [] txtArray={txtLunes,txtMartes,txtMiercoles,txtJueves,txtViernes,txtSabado,txtDomingo};

        for(TextView txtCurrent: txtArray){//si contiene el bacground

            if(txtCurrent.getBackground()==ContextCompat.getDrawable(  ActivityHorario.this, R.drawable.back_dia_selected)){
                MImap.put(String.valueOf(txtCurrent.getTag()),txtCurrent.getTag().toString());
            }

        }


        HorarIosTrabajos horarioObjec= new HorarIosTrabajos(txtImputEntrada.getText().toString(),txtImputSalida.getText().toString(),
                ediNombreHorario.getText().toString(),Utils.maiLEmpleadorGlOBAL);



        if(Utils.isEditHorario){ //estamos editando

            horarioObjec.setIdHorarioHereKEYpreferences(Utils.currentHorarioSelectedUid);
          //  String keydondeGudaremos= RealtimDatabase.rootDatabaseReference.push().getKey();
            RealtimDatabase.updateHorarioTrabajoMap(ActivityHorario.this,MImap, Utils.horarioTrabajoGlobalSelected.getKeylocalizeMapHorario() );
            horarioObjec.setKeylocalizeMapHorario(keylocalizeMapHorario);
            RealtimDatabase.updateHorariosTrabajo(ActivityHorario.this,horarioObjec,Utils.horarioTrabajoGlobalSelected.getKeyWhereLocalizeObjec());


        }

        else{ //es crear un nuevo horario

            String keydondeGudaremos= RealtimDatabase.rootDatabaseReference.push().getKey();
            horarioObjec.setKeylocalizeMapHorario(keydondeGudaremos);

            RealtimDatabase.addHorarioTrabajoMap(ActivityHorario.this,MImap, keydondeGudaremos );
            RealtimDatabase.addHorariosTrabajo(ActivityHorario.this,horarioObjec);



        }



        finish();

        Log.i("sumare","hemos guardado aqui hurra");



    }







    private int  getPosicionBYid(ArrayList<HorarIosTrabajos>list){
        int position=0;
        for(HorarIosTrabajos objec: list){

            if(objec.getIdHorarioHereKEYpreferences().equals(Utils.currentHorarioSelectedUid)){

             return position;
            }

        }
        return 10000;
    }


    void showingTimePicker( View vista){

        final Calendar cldr = Calendar.getInstance();
        int hour = cldr.get(Calendar.HOUR_OF_DAY);
        int minutes = cldr.get(Calendar.MINUTE);

        // time picker dialog
        TimePickerDialog picker = new TimePickerDialog(ActivityHorario.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker tp, int sHour, int sMinute) {



                        if(vista.getId()==R.id.txtImputEntrada) {

                            txtImputEntrada.setText(sHour + ":" + minutes);


                        }


                        else if (vista.getId()== R.id.txtImputSalida) {

                            txtImputSalida.setText(sHour + ":" + minutes);



                        }



                    }
                }, hour, minutes, true);

        picker.setButton(DialogInterface.BUTTON_POSITIVE, "OK", picker);
        picker.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancelar", picker);

        picker.show();
    }

    private void dowloadHorRIOoBJEC(String IDuNIQUEHORARIO ){

        DatabaseReference usersdRef = RealtimDatabase.rootDatabaseReference.child("horarios").child("horariostrabajos");

        Query query = usersdRef.orderByChild("idHorarioHereKEYpreferences").equalTo(IDuNIQUEHORARIO);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot ds : snapshot.getChildren()) {

                    HorarIosTrabajos informe=ds.getValue(HorarIosTrabajos.class);
                    Log.i("holerd","aqui se encontro un cuadro muestreo......");

                    if(informe!=null){

                        keylocalizeMapHorario=informe.getKeylocalizeMapHorario();
                        dowloadHoriosMap(informe.getKeylocalizeMapHorario());

                        break;
                    }


                }





            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("misdata","el error es  "+ error.getMessage());

            }
        } );

    }


    private void dowloadHoriosMap(String keylocalizemap  ){

        ValueEventListener seenListener  = RealtimDatabase.rootDatabaseReference.child("mapsHorariosData").child("maps").child(keylocalizemap)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                              MImap = new HashMap<>();


                        for (DataSnapshot dss : dataSnapshot.getChildren()) {
                            String key = dss.getKey();
                            String fieldData = dss.getValue(String.class);

                            if (fieldData != null) {///
                                MImap.put(key, fieldData);
                            }
                        }


                          if(MImap!=null){
                              seteamosHorarioDePrefrencias();

                          }



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.i("libiadod", "el error es " + databaseError.getMessage());


                    }
                });

    }


    private void getunavez(){

        DatabaseReference mDatabase = RealtimDatabase.rootDatabaseReference.child("mapsHorariosData").child("maps");

        mDatabase.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));




                }
            }
        });

}


    void findviewids(){
         txtImputEntrada=findViewById(R.id.txtImputEntrada);
         txtImputSalida=findViewById(R.id.txtImputSalida);
        btnGuardar=findViewById(R.id.btnGuardar);
        ediNombreHorario=findViewById(R.id.ediNombreHorario);
        //DIAS SEMANA
         txtLunes=findViewById(R.id.txtLunes);
         txtMartes=findViewById(R.id.txtMartes);
         txtMiercoles=findViewById(R.id.txtMiercoles);
         txtJueves=findViewById(R.id.txtJueves);
         txtViernes=findViewById(R.id.txtViernes);
         txtSabado=findViewById(R.id.txtSabado);
         txtDomingo=findViewById(R.id.txtDomingo);

         txtNumsEmpleados=findViewById(R.id.txtNumsEmpleados);
         imgAddEmpleado=findViewById(R.id.imgAddEmpleado);

    }


    @Override
    public void onClick(View view) {
           int idCurrent=view.getId();


        if(idCurrent==R.id.txtLunes|| idCurrent==R.id.txtMartes || idCurrent==R.id.txtMiercoles ||
                idCurrent==R.id.txtJueves||idCurrent==R.id.txtViernes||idCurrent==R.id.txtSabado
                ||idCurrent==R.id.txtDomingo)
        {

            TextView currentTextview=(TextView) view;

            String tag=view.getTag().toString();
            if(MImap.containsKey(tag)){ //deselcionamos
                MImap.remove(tag);
                currentTextview.setTextColor(Color.parseColor("#A4525151"));
                currentTextview.setBackground(ContextCompat.getDrawable(  ActivityHorario.this, R.drawable.back_dia_no_selected));

            }

            else
            { //slecionamos
                MImap.put(tag,tag);
                currentTextview.setTextColor(Color.parseColor("#A4FFFFFF"));
                currentTextview.setBackground(ContextCompat.getDrawable(  ActivityHorario.this, R.drawable.back_dia_selected));

            }


        }


    }


    private void seteamosHorarioDePrefrencias( ){

        //por hoara solotendremos un horario ,un solo item en la lista
       // HorarIosTrabajos objectHorario= list.get(0);

        txtImputEntrada.setText(MImap.get(String.valueOf(R.id.txtImputEntrada)));
        txtImputSalida.setText(MImap.get(String.valueOf(R.id.txtImputSalida)));
        ediNombreHorario.setText(MImap.get(String.valueOf(R.id.ediNombreHorario)));



        TextView [] arraTextView={

                txtLunes,txtMartes,txtMiercoles,txtJueves,txtViernes,txtSabado,txtDomingo

        };

        for(TextView txtb: arraTextView){

            if(MImap.containsKey(String.valueOf(txtb.getTag()))){
                txtb.setTextColor(Color.parseColor("#A4FFFFFF"));
                txtb.setBackground(ContextCompat.getDrawable(  ActivityHorario.this, R.drawable.back_dia_selected));

            }
        }



        //usamos un bucle for... para cambiarle el nbackground..


    }


    boolean checkExistDiaSelectAlMenos(){


        Log.i("diasmin","el size de map es "+MImap.size());


        int [] arraySlected={1,2,3,4,5,6,7

        };

           int contadorClavesExist=0;

        for(int idKey: arraySlected){

            if(MImap.containsKey(String.valueOf(idKey))){

                contadorClavesExist++;

                Log.i("diasmin","ecl contador es "+contadorClavesExist);

                if(contadorClavesExist>= Utils.NUMS_DIAS_MINIMO){
                    return true;

                }

            }

        }



        Toast.makeText(this, "El numero de dias minimo que debes selecionar es "+Utils.NUMS_DIAS_MINIMO, Toast.LENGTH_LONG).show();

        return false;

    }


}