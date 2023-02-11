package com.google.android.cameraview.demo.Activities;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.cameraview.demo.R;
import com.google.android.cameraview.demo.Utils.SharePref;
import com.google.android.cameraview.demo.Utils.Utils;
import com.google.android.cameraview.demo.adapters.AdapterAsistencePromedio;
import com.google.android.cameraview.demo.adapters.AdapterEmpleado;
import com.google.android.cameraview.demo.models.Empleado;
import com.google.android.cameraview.demo.models.Fichar;
import com.google.android.cameraview.demo.models.PromedioAsistenceEmpleado;
import com.whiteelephant.monthpicker.MonthPickerDialog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.TimeZone;

public class ActivityReportes extends AppCompatActivity {
   RecyclerView recylerVInformsAll;
   int mesSelecionado=0;
    int yearSelecionado=0;
    String fechaSelecionadaCalendar ="";

    LinearLayout linLaySelectRageDate;


    ArrayList<Empleado>miLisEmpleados= new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportes);

        linLaySelectRageDate =findViewById(R.id.imgSelectRageDate);
        recylerVInformsAll=findViewById(R.id.recylerVInformsAll);

        linLaySelectRageDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sheetBootomInforOptions();
            }
        });




        //obtenmos toda la lista de empleados

        HashMap<String,Empleado> miMapEmpleados =SharePref.loadMapPreferencesEmpleados(SharePref.KEY_ALL_EMPLEADOS_Map);

        //creamos una lista con todos los empleados


        if(miMapEmpleados.size()>0){
            miLisEmpleados.addAll(miMapEmpleados.values());
            //mostramos por defecto la asitencia del di de hoy//

            //                 String fechaOfMilliseconds=mDay+"/"+mMonth+"/"+mYear;  en este formaro

            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date date = new Date();
            fechaSelecionadaCalendar=dateFormat.format(date);
            generateAsistenciaArrayListByFilter(miLisEmpleados,Utils.DIA_ESPECIFICO);


        }
        else

        {
            TextView txtAdviserHere=findViewById(R.id.txtAdviserHere);
            txtAdviserHere.setVisibility(View.VISIBLE);

        }



    }

    private void setDataRecyclerView(ArrayList<PromedioAsistenceEmpleado> list){

        if(list.size()==0){
            TextView txtAdviserHere=findViewById(R.id.txtAdviserHere);
            txtAdviserHere.setVisibility(View.VISIBLE);

        }

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ActivityReportes.this);

        AdapterAsistencePromedio adapter = new AdapterAsistencePromedio(ActivityReportes.this, list);
        recylerVInformsAll.setLayoutManager(layoutManager);
        recylerVInformsAll.setAdapter(adapter);


        adapter.setOnItemClickListener(new AdapterAsistencePromedio.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {


             //   sheetBootomInforOptions(position);

                //  sheetBootomInforOptions(v.getTag(R.id.tagUniqueId1).toString(),v.getTag(R.id.tagUniqueId2).toString(),v.getTag(R.id.codigoProductor).toString());


                Log.i("elcickler","el click es llamado al secionar item fichaje");




            }
        });




    }





    private void sheetBootomInforOptions() {


        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ActivityReportes.this);

        bottomSheetDialog.setContentView(R.layout.bottom_sheet_option_filter_range);


        Button btnSelecDataEspecific = bottomSheetDialog.findViewById(R.id.btnSelecDataEspecific);
        Button btnSelecMonthYear = bottomSheetDialog.findViewById(R.id.btnSelecMonthYear);


        btnSelecDataEspecific.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /**abrimos el informe dpeendiendo del tipo de informe */

                //aqui recuperamos el id ...del informe...
                //intent...//aqui abrimos dialog.... y le pasamos un intent...

                selecionaFecha();

                Log.i("elcickler", "el click es llamadoen sheet");


                bottomSheetDialog.dismiss();


            }
        });
        btnSelecMonthYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /**abrimos el informe dpeendiendo del tipo de informe */

                //aqui recuperamos el id ...del informe...
                //intent...//aqui abrimos dialog.... y le pasamos un intent...

                Log.i("elcickler", "el click es llamadoen sheet");

                showMont();
                bottomSheetDialog.dismiss();


            }
        });


        bottomSheetDialog.show();

    }

    private void showMont() {


        //    long seconds =10000000;

        Calendar ca = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

        MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(ActivityReportes.this,
                new MonthPickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(int selectedMonth, int selectedYear) { // on date set }


                    }

                }, ca.get(Calendar.YEAR), ca.get(Calendar.MONTH));

        builder.setActivatedMonth(Calendar.JULY)
                .setMinYear(2023)
                .setActivatedYear(ca.get(Calendar.YEAR))
                .setMaxYear(ca.get(Calendar.YEAR))
                .setMinMonth(Calendar.FEBRUARY)
                .setTitle("Selecione El rango")
                .setMonthRange(Calendar.FEBRUARY, Calendar.NOVEMBER)
                // .setMaxMonth(Calendar.OCTOBER)
                // .setYearRange(1890, 1890)
                // .setMonthAndYearRange(Calendar.FEBRUARY, Calendar.OCTOBER, 1890, 1890)
                //.showMonthOnly()
                // .showYearOnly()



                .setOnMonthChangedListener(new MonthPickerDialog.OnMonthChangedListener() {
                    @Override
                    public void onMonthChanged(int i) {

                        mesSelecionado=i;
                        Log.i("misafama","mes es "+i);


                    }
                });

        builder.setOnYearChangedListener(new MonthPickerDialog.OnYearChangedListener() {
            @Override
            public void onYearChanged(int selectedYear) { // on year selected } })


                yearSelecionado=selectedYear;
                   Log.i("misafama","solaper year es "+selectedYear);
            }
        });

                builder .build()
                .show();
    }


    //

    //aqui ya le pasamos la lista de fichaje con el que vamos a contruir una lista de promedios.
    private void generateAsistenciaArrayListByFilter(ArrayList<Empleado>listUsersToGetFichajePromedio,int modo){

        ArrayList<PromedioAsistenceEmpleado>arrayList= new ArrayList<>();


        for(Empleado ficharObjec: listUsersToGetFichajePromedio){ //biscamos

            HashMap<String, Fichar> hashMapAllRegistrosThisUser= SharePref.loadMapPreferencesFichaje(ficharObjec.getIdEmpleado());

            Log.i("misdta","el size de map fichaje de este user es "+hashMapAllRegistrosThisUser.size());

            //debug imprimos los datos de este fichaje


            for(Fichar objec: hashMapAllRegistrosThisUser.values()){

                Log.i("misdta","el getEntradaMilliseconds es "+objec.getEntradaMilliseconds());
                Log.i("misdta","el getHoraSalidaMilliseconds es "+objec.getHoraSalidaMilliseconds());


            }


            //ahora necesitamos solo obtener los de este mes...

              if(modo== Utils.MES_ESPECIFICO){
                 // mesSelecionado
                ArrayList<Fichar>lisFichajeCurrentUser=generaListMaracacionesEspecifiDateOrRange(hashMapAllRegistrosThisUser,Utils.MES_ESPECIFICO);

                PromedioAsistenceEmpleado promedioObjec=  generatePromedioObjectThisUserByArrayList(lisFichajeCurrentUser,ficharObjec.getNombreYapellidoEmpleado());

                arrayList.add(promedioObjec);


              }

              else if(modo==Utils.DIA_ESPECIFICO){

                  REVISAR AQUI LOS DOS STRING QUE SE COMPARAN.. Y VER SI EL METODO DE ABAJO NOS GENEROA UN VA;OR./

                  ArrayList<Fichar>lisFichajeCurrentUser=generaListMaracacionesEspecifiDateOrRange(hashMapAllRegistrosThisUser,Utils.DIA_ESPECIFICO);

                  Log.i("misdta","el getEntradaMilliseconds es "+objec.getEntradaMilliseconds());


                  PromedioAsistenceEmpleado promedioObjec=  generatePromedioObjectThisUserByArrayList(lisFichajeCurrentUser,ficharObjec.getNombreYapellidoEmpleado());

                  arrayList.add(promedioObjec);


              }




        }


        setDataRecyclerView(arrayList);


    }



    private ArrayList<Fichar>generaListMaracacionesEspecifiDateOrRange(HashMap<String, Fichar> hashMapAllRegistrosThisUser,int modo){

        ArrayList<Fichar>lisFichar= new ArrayList<>();

        GregorianCalendar c = new GregorianCalendar();
        Calendar calendar = Calendar.getInstance();


        for(Fichar ficharObjec: hashMapAllRegistrosThisUser.values()){

             if(modo==Utils.MES_ESPECIFICO) {
                 c.setTimeInMillis(ficharObjec.getEntradaMilliseconds());
                 int month = c.get(Calendar.MONTH);
                 int year = c.get(Calendar.YEAR);
                 if (month == mesSelecionado && year == yearSelecionado) {
                     lisFichar.add(ficharObjec);

                 }

             }


             else if(modo==Utils.DIA_ESPECIFICO){

                 calendar.setTimeInMillis(ficharObjec.getEntradaMilliseconds());
                 int mYear = calendar.get(Calendar.YEAR);
                 int mMonth = calendar.get(Calendar.MONTH);
                 int mDay = calendar.get(Calendar.DAY_OF_MONTH);

                 String fechaOfMilliseconds=mDay+"/"+mMonth+"/"+mYear;

                 if (fechaOfMilliseconds.equals(fechaSelecionadaCalendar)) {
                     lisFichar.add(ficharObjec);

                 }

                 ///cpnvertimos milisegunod en una fecha,, y si esta fecha coincide con la selecioanda en Slector Clendar



             }

        }


        return lisFichar;
    }


    void selecionaFecha(){

        final Calendar cldr = Calendar.getInstance();
        int year = cldr.get(Calendar.YEAR);
        int daySemana = cldr.get(Calendar.DAY_OF_WEEK);
        int mes = cldr.get(Calendar.MONTH);

        // time picker dialog
        DatePickerDialog picker = new DatePickerDialog(ActivityReportes.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                         fechaSelecionadaCalendar =i2+"/"+(i1+1)+"/"+i;


                      //  generateAsistenciaArrayListByFilter(Utils.DIA_ESPECIFICO);

                       // ediFecha.setText(dateSelec);

                        // ediFecha.setText(daySemana+"/"+mes+"/"+year);

                    }
                }, year,  mes, daySemana);

        picker.setButton(DialogInterface.BUTTON_POSITIVE, "OK", picker);
        picker.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancelar", picker);


        picker.show();
    }



    PromedioAsistenceEmpleado generatePromedioObjectThisUserByArrayList(ArrayList<Fichar>list,String nombre){

        Log.i("sistemasx","el size de lista es  "+list.size());


        long millisecondsPromedioEntrada=0;
        long millisecondsPromedioSalida=0;
        int diasAsistencia=0;
         //tambien debemos buscar los dias de asistencia..

        int minutosEntrada = 0;
        int horaEntrada   =0;

        int minutosSalida = 0;
        int horaSalida   =0;


        int contadorItems=0;

        for(Fichar ficharObject: list){

            Log.i("sistemasx","el iterando  millisecondsPromedioEntrada "+ficharObject.getEntradaMilliseconds());
            Log.i("sistemasx","el iterando  millisecondsPromedioSalida "+ficharObject.getHoraSalidaMilliseconds());

            millisecondsPromedioEntrada=millisecondsPromedioEntrada+ficharObject.getEntradaMilliseconds();
            millisecondsPromedioSalida=millisecondsPromedioSalida+ficharObject.getHoraSalidaMilliseconds();

            if(ficharObject.getEntradaMilliseconds()>0){
                contadorItems++;

            }

        }

        Log.i("sistemasx","el millisecondsPromedioEntrada es "+millisecondsPromedioEntrada);
        Log.i("sistemasx","el millisecondsPromedioSalida es "+millisecondsPromedioSalida);


        if(millisecondsPromedioEntrada>0){
            millisecondsPromedioEntrada=millisecondsPromedioEntrada/contadorItems;
             minutosEntrada = (int) ((millisecondsPromedioEntrada / (1000*60)) % 60);
             horaEntrada   = (int) ((millisecondsPromedioEntrada / (1000*60*60)) % 24);
        }


        if(millisecondsPromedioSalida>0){
            millisecondsPromedioSalida=millisecondsPromedioSalida/contadorItems;
             minutosSalida = (int) ((millisecondsPromedioSalida / (1000*60)) % 60);
             horaSalida   = (int) ((millisecondsPromedioSalida / (1000*60*60)) % 24);
        }






        return new PromedioAsistenceEmpleado(nombre,horaEntrada+":"+minutosEntrada,
                horaSalida+":"+minutosSalida,diasAsistencia);


    }


}