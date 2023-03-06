package com.tiburela.android.controlAsistencia.demo.Activities;

import androidx.annotation.NonNull;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tiburela.android.controlAsistencia.demo.R;
import com.tiburela.android.controlAsistencia.demo.Utils.RealtimDatabase;
import com.tiburela.android.controlAsistencia.demo.Utils.SharePref;
import com.tiburela.android.controlAsistencia.demo.Utils.Utils;
import com.tiburela.android.controlAsistencia.demo.customClass.OtpEditText;
import com.tiburela.android.controlAsistencia.demo.models.Empleado;
import com.tiburela.android.controlAsistencia.demo.models.Fichar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class ActivityCodigoFichar extends AppCompatActivity {
    Button btnFicharCode;
    OtpEditText et_otpCode;
    HashMap<String, Empleado> mhasMap;
    private String idCurrentUser = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_codigo_fichar);

        btnFicharCode = findViewById(R.id.btnFicharCode);
        et_otpCode = findViewById(R.id.et_otpCode);

        SharePref.init(ActivityCodigoFichar.this);
        mhasMap = SharePref.loadMapPreferencesEmpleados(SharePref.KEY_ALL_EMPLEADOS_Map);


        btnFicharCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (et_otpCode.getText().toString().trim().isEmpty()) {
                    et_otpCode.requestFocus();
                    et_otpCode.setError("No puede estar vacio");
                    return;
                }


                if (et_otpCode.getText().toString().trim().length() < 4) {
                    et_otpCode.requestFocus();
                    et_otpCode.setError("Faltan digitos");
                    return;
                }


                /***cheekemos si codigo existe , si existe llamamos funcion marcar si no mostramos sheet (no existe)**/
                checkifExisteCodigoficharEmpleadoAndCallFichaje(et_otpCode.getText().toString(),idCurrentUser);



                //entonces aqui fichamos...
                //obtenmos la lista de empleados..


            }
        });
    }


    private boolean checkIFcodeExisteEnAlgunEmpleadoAndGetId(String codigo) {

        if (mhasMap.size() == 0) {

            return false;
        }


        for (Empleado empleado : mhasMap.values()) {
            if (empleado.getCodigoPaFichar().equals(codigo)) {
                idCurrentUser = empleado.getIdEmpleado();

                return true;
            }


        }


        return false;
    }


    void marcamosFichaje(String keyCurrentUser) {

        //  Fichar fichar= Fichar.hashMapAllFicharRegistros

        //obtenomos el hasmap de fichaje de este empleado ,usando el id como key de prefrences...


        Fichar ficharObjec = null;

        HashMap<String, Fichar> hashMapFichajeRegistros = SharePref.loadMapPreferencesFichaje(keyCurrentUser);

        if (hashMapFichajeRegistros.size() > 0) { //hay data...chekeamos si tenemos el registro del dia de hoy usando un la fecha actual como key..
            if (hashMapFichajeRegistros.containsKey(new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date().getTime()))) {
                //SI EXISTE OBTENEMOS EL OBJETO FICHAR  usando la fecha como key
                ficharObjec = hashMapFichajeRegistros.get(new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date().getTime()));


            }
        }


        if (ficharObjec == null) { //si fichar objet es nulo cremoa sun nirvo

            ficharObjec = new Fichar(keyCurrentUser, new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date().getTime()),Utils.maiLEmpleadorGlOBAL);

        }
        String time = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());


        if (Fichar.tipoFichanSelecionadoCurrent == Fichar.FICHAJE_ENTRADA) {
            if (ficharObjec.getEntradaMilliseconds() == 0) {

                ficharObjec.setEntradaMilliseconds(new Date().getTime());


                Log.i("fichnadodata", "la hora de entrada es cero fichamos ahora");

                showFichaje(time, "Entrada", R.drawable.hora_entrada);


            } else {  //el user ya ficho

                Log.i("fichnadodata", "la hora de entrada es difrente de cero, ya hemos fichado antes ");

                Toast.makeText(this, "Ya marcaste la hora de entrada", Toast.LENGTH_SHORT).show();

            }


        } else if (Fichar.tipoFichanSelecionadoCurrent == Fichar.FICHAJE_SALIDA) {

            ///si no tenemos el fichacje de entrada no podremos marcar mas


            if (ficharObjec.getEntradaMilliseconds() == 0) {

                Toast.makeText(this, "Antes tienes que marcar Hora de entrada", Toast.LENGTH_LONG).show();

                return;
            }


            if (ficharObjec.getHoraSalidaMilliseconds() == 0) {
                ficharObjec.setHoraSalidaMilliseconds(new Date().getTime());
                Log.i("fichnadodata", "la hora de salida la ficahmos ahora");
                showFichaje(time, "Salida", R.drawable.hora_salida);

                //  Toast.makeText(this, "Hora de entrada Agregada", Toast.LENGTH_SHORT).show();

            } else {  //el user ya ficho

                Log.i("fichnadodata", "la hora de salida ya estaba agregada..");

                Toast.makeText(this, "Ya marcaste la hora de entrada", Toast.LENGTH_SHORT).show();


            }

        }


        //guardamos o remplzamos
        hashMapFichajeRegistros.put(new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date().getTime()), ficharObjec);


        SharePref.saveMapFichaje(hashMapFichajeRegistros, keyCurrentUser);
        //RealtimDatabase.(ActivityCodigoFichar.this,);


    }


    private void dowloadHorRIOoBJECIFexistAndFichajeoRupdate(String idUserId) {

        DatabaseReference usersdRef = RealtimDatabase.rootDatabaseReference.child("marcaciones").child("allmarcaciones");

        Query query = usersdRef.orderByChild("ficharUserId").equalTo(idUserId);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Fichar informe = null;
                 boolean existeFichajeMes=false;

                for (DataSnapshot ds : snapshot.getChildren()) {

                    informe = ds.getValue(Fichar.class);

                    if (informe != null ) {

                        SimpleDateFormat format=new SimpleDateFormat("dd-MM-yyyy");

                        String fechaFichada= format.format(informe.getEntradaMilliseconds());

                        if(fechaFichada.equalsIgnoreCase(new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date().getTime()))){
                            existeFichajeMes=true;

                            Log.i("solamerra","existe fichaje ");
                            break;
                        }

                    }


                }


                if (!existeFichajeMes) {
                    Log.i("solamerra","no existe fichaje ");

                    fichaOnlineAndUpdate(informe, false);

                } else {
                    fichaOnlineAndUpdate(informe, true);

                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("misdata", "el error es  " + error.getMessage());

            }
        });

    }



    private void checkifExisteCodigoficharEmpleadoAndCallFichaje(String codifoFichar, String idEmpleado) {

        DatabaseReference usersdRef = RealtimDatabase.rootDatabaseReference.child("empleados").child("allEmpleados");

        Query query = usersdRef.orderByChild("codigoPaFichar").equalTo(codifoFichar);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Empleado empleado = null;

                for (DataSnapshot ds : snapshot.getChildren()) {

                    empleado = ds.getValue(Empleado.class);

                    if (empleado != null ) {

                        break;

                    }


                }


                if (empleado!=null) { //signifca que exxiste este codigo en algun suario

                    dowloadHorRIOoBJECIFexistAndFichajeoRupdate(idEmpleado);

                } else {
                    sheetBootomShowINcorrect();

                    //mostramos advertencia

                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("misdata", "el error es  " + error.getMessage());

            }
        });

    }



    private void fichaOnlineAndUpdate(Fichar fichar, boolean existeFIchaje) {

        String time = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());


        if (existeFIchaje ) {//editamos el fichaje

            if(Fichar.tipoFichanSelecionadoCurrent==Fichar.FICHAJE_ENTRADA){

                if(fichar.getEntradaMilliseconds()==0){

                    fichar.setEntradaMilliseconds(new Date().getTime());

                    showFichaje(time, "Entrada", R.drawable.hora_entrada);

                     RealtimDatabase.updateMarcacion(ActivityCodigoFichar.this,fichar, fichar.getKeyWhereLocalizeObjec() );


                }else{


                    Toast.makeText(this, "Ya haz fichado anteriomente ", Toast.LENGTH_SHORT).show();
                }

            }

           else if(Fichar.tipoFichanSelecionadoCurrent==Fichar.FICHAJE_SALIDA){

                if(fichar.getHoraSalidaMilliseconds()==0){

                    fichar.setHoraSalidaMilliseconds(new Date().getTime());
                    showFichaje(time, "Salida", R.drawable.hora_salida);

                    RealtimDatabase.updateMarcacion(ActivityCodigoFichar.this,fichar, fichar.getKeyWhereLocalizeObjec() );



                }else{

                    Toast.makeText(this, "Ya haz fichado anteriomente ", Toast.LENGTH_SHORT).show();

                }

            }


        }


        else {

            fichar = new Fichar(idCurrentUser, new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date().getTime()), Utils.maiLEmpleadorGlOBAL);



            if(Fichar.tipoFichanSelecionadoCurrent==Fichar.FICHAJE_ENTRADA){
                if (fichar.getEntradaMilliseconds() == 0) {

                    fichar.setEntradaMilliseconds(new Date().getTime());
                    showFichaje(time, "Entrada", R.drawable.hora_entrada);
                    RealtimDatabase.addMarcacion(ActivityCodigoFichar.this,fichar);


                }


            }

        }
    }






    void marcamosFichajeONline(String keyCurrentUser){

        //  Fichar fichar= Fichar.hashMapAllFicharRegistros

        //obtenomos el hasmap de fichaje de este empleado ,usando el id como key de prefrences...


        Fichar ficharObjec=null;

        HashMap<String, Fichar>hashMapFichajeRegistros=SharePref.loadMapPreferencesFichaje(keyCurrentUser);

        if(hashMapFichajeRegistros.size()>0){ //hay data...chekeamos si tenemos el registro del dia de hoy usando un la fecha actual como key..
            if(hashMapFichajeRegistros.containsKey(new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date().getTime()))){
                //SI EXISTE OBTENEMOS EL OBJETO FICHAR  usando la fecha como key
                ficharObjec= hashMapFichajeRegistros.get(new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date().getTime()));


            }
        }



        if(ficharObjec==null){ //si fichar objet es nulo cremoa sun nirvo

            ficharObjec= new Fichar(keyCurrentUser,new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date().getTime()),Utils.maiLEmpleadorGlOBAL);

        }
        String time = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());


        if(Fichar.tipoFichanSelecionadoCurrent==Fichar.FICHAJE_ENTRADA){
            if(ficharObjec.getEntradaMilliseconds()==0){

                ficharObjec.setEntradaMilliseconds(new Date().getTime());





                Log.i("fichnadodata","la hora de entrada es cero fichamos ahora");

                showFichaje(time,"Entrada",R.drawable.hora_entrada);



            }

            else

            {  //el user ya ficho

                Log.i("fichnadodata","la hora de entrada es difrente de cero, ya hemos fichado antes ");

                Toast.makeText(this, "Ya marcaste la hora de entrada", Toast.LENGTH_SHORT).show();

            }






        }



        else if(Fichar.tipoFichanSelecionadoCurrent==Fichar.FICHAJE_SALIDA){

            ///si no tenemos el fichacje de entrada no podremos marcar mas


            if(ficharObjec.getEntradaMilliseconds()==0){

                Toast.makeText(this, "Antes tienes que marcar Hora de entrada", Toast.LENGTH_LONG).show();

                return;
            }



            if(ficharObjec.getHoraSalidaMilliseconds()==0){
                ficharObjec.setHoraSalidaMilliseconds(new Date().getTime());
                Log.i("fichnadodata","la hora de salida la ficahmos ahora");
                showFichaje(time,"Salida",R.drawable.hora_salida);

                //  Toast.makeText(this, "Hora de entrada Agregada", Toast.LENGTH_SHORT).show();

            }

            else{  //el user ya ficho

                Log.i("fichnadodata","la hora de salida ya estaba agregada..");

                Toast.makeText(this, "Ya marcaste la hora de entrada", Toast.LENGTH_SHORT).show();


            }

        }


        //guardamos o remplzamos
        hashMapFichajeRegistros.put(new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date().getTime()),ficharObjec);


        SharePref.saveMapFichaje(hashMapFichajeRegistros,keyCurrentUser);
        //RealtimDatabase.(ActivityCodigoFichar.this,);



    }


    private void showFichaje(String timeFichaje, String tipoFichaje, int drawable){


        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ActivityCodigoFichar.this);

        bottomSheetDialog.setContentView(R.layout.bottom_sheet_reconocimiento);

        Button btnOk=bottomSheetDialog.findViewById(R.id.btnOk);
        TextView txtFichajeCurrent =bottomSheetDialog.findViewById(R.id.txtFichajeCurrent);
        TextView txtTime =bottomSheetDialog.findViewById(R.id.txtTime);
        ImageView imgView=bottomSheetDialog.findViewById(R.id.imgView);
        bottomSheetDialog.setCancelable(false);

        txtFichajeCurrent.setText("Hora de "+tipoFichaje+", fichada Correctamente");
        txtTime.setText(timeFichaje);
        imgView.setImageResource(drawable);



        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                finish();

                bottomSheetDialog.dismiss();




            }
        });






        bottomSheetDialog.show();





    }

    private void sheetBootomShowINcorrect(){

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ActivityCodigoFichar.this);

        bottomSheetDialog.setContentView(R.layout.bottom_sheet_incorrect);

        Button btnLoTengo=bottomSheetDialog.findViewById(R.id.btnLoTengo);
        btnLoTengo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bottomSheetDialog.dismiss();
            }
        });

        //ImageView imgClose=bottomSheetDialog.findViewById(R.id.imgClose);
        //8  bottomSheetDialog.setCancelable(false);




        bottomSheetDialog.show();

    }



}