package com.tiburela.android.controlAsistencia.demo.Activities;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.internal.Util;
import com.tiburela.android.controlAsistencia.demo.R;
import com.tiburela.android.controlAsistencia.demo.Utils.RealtimDatabase;
import com.tiburela.android.controlAsistencia.demo.Utils.SharePref;
import com.tiburela.android.controlAsistencia.demo.Utils.Utils;
import com.tiburela.android.controlAsistencia.demo.adapters.AdapterAsistencePromedio;
import com.tiburela.android.controlAsistencia.demo.models.Empleado;
import com.tiburela.android.controlAsistencia.demo.models.Fichar;
import com.tiburela.android.controlAsistencia.demo.models.PromedioAsistenceEmpleado;
import com.whiteelephant.monthpicker.MonthPickerDialog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class ActivityReportes extends AppCompatActivity {

    HashMap<String,ArrayList<Fichar>> mapOfMapAllFichajeUserByRange = new HashMap<>();


    long desdeDateMillisecond=0;
    long hastaDateMillisecond=0;

    int modoMostrarDataRecicler=Utils.ITEM_MARACIONES_MODO;



    RecyclerView recylerVInformsAll;
   int mesSelecionado=0;
    int yearSelecionado=0;
    String fechaSelecionadaCalendar ="";
    boolean isFirstShowReciclerData =true;
    LinearLayout linLaySelectRageDate;
     TextView txtDateSelected;
     Spinner spinnerTipoReporte;
     int modoDateRangeSearch= Utils.DIA_ESPECIFICO;
    ArrayList<Empleado> miLisAllEmpleadosRegistrados = new ArrayList<>();
    TextView txt1;
    TextView txt2;
    TextView txt3;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportes);

        linLaySelectRageDate =findViewById(R.id.imgSelectRageDate);
        recylerVInformsAll=findViewById(R.id.recylerVInformsAll);
        txtDateSelected=findViewById(R.id.txtDateSelected);
        spinnerTipoReporte =findViewById(R.id.spinTipoReporte);


          SharePref.init(ActivityReportes.this);

         txt1=findViewById(R.id.txt1);
         txt2=findViewById(R.id.txt2);
         txt3=findViewById(R.id.txt3);





        //obtenmos toda la lista de empleados

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        fechaSelecionadaCalendar=dateFormat.format(date);



        linLaySelectRageDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sheetBootomInforOptions();
            }
        });





        //creamos una lista con todos los empleados




        spinnerTipoReporte.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if(spinnerTipoReporte.getSelectedItem().toString().equalsIgnoreCase("Marcación")){
                    Utils.tipodeDatoMostrar=Utils.ITEM_MARACIONES_MODO;

                          ///mostramos prefefinido una sola vez
                    if(isFirstShowReciclerData){
                        showFisrtReciclerData();
                        isFirstShowReciclerData=false;
                    }

                    else

                    {

                        dowloadAllEmpleadosAndGetPromedio(desdeDateMillisecond,hastaDateMillisecond,modoDateRangeSearch);

                        //  dowloadAllEmpleadosAndGetPromedio(modoDateRangeSearch);
                       // generateAsistenciaArrayListByFilterXOnline(miLisAllEmpleadosRegistrados,modoDateRangeSearch);
                       // generateAsistenciaArrayListByFilter(miLisAllEmpleadosRegistrados,modoDateRangeSearch);
                        //aqui mostramos normalemente
                    }


                }else {

                    Utils.tipodeDatoMostrar=Utils.ITEM_ASISTENCIA_MODO;

                    //aqui debemos tener el modo
                    dowloadAllEmpleadosAndGetPromedio(desdeDateMillisecond,hastaDateMillisecond,modoDateRangeSearch);

                  //  generateAsistenciaArrayListByFilter(miLisAllEmpleadosRegistrados,modoDateRangeSearch);


                    //showFisrtReciclerData();


                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });




    }




    private void showFisrtReciclerData(){
        //obtenmos el dia de hoy

        Calendar calendar= Calendar.getInstance();
        // calendar.set(Calendar.DAY_OF_MONTH,7);
        calendar.add(Calendar.DATE, -1);
        desdeDateMillisecond=calendar.getTimeInMillis();

        calendar.add(Calendar.DATE, +2); //estaba en  +1
         hastaDateMillisecond=calendar.getTimeInMillis();

//                        SimpleDateFormat format=new SimpleDateFormat("dd-MM-yyyy");
         SimpleDateFormat forma=new SimpleDateFormat("dd/MM/yyyy");

         Log.i("comenzardata","el dia de ayer "+forma.format(desdeDateMillisecond)+" el dia de manana es "+forma.format(hastaDateMillisecond));


        dowloadAllEmpleadosAndGetPromedio(desdeDateMillisecond,hastaDateMillisecond,Utils.DIA_ESPECIFICO);



/*
        HashMap<String,Empleado> miMapEmpleados =SharePref.loadMapPreferencesEmpleados(SharePref.KEY_ALL_EMPLEADOS_Map);

        if(miMapEmpleados.size()>0){

          //  miLisAllEmpleadosRegistrados.addAll(miMapEmpleados.values());
            generateAsistenciaArrayListByFilter(miLisAllEmpleadosRegistrados,Utils.DIA_ESPECIFICO);


        }
        else

        {
            TextView txtAdviserHere=findViewById(R.id.txtAdviserHere);
            txtAdviserHere.setVisibility(View.VISIBLE);

        }

        */


    }

    private void dowloadAllEmpleadosAndGetPromedio( long desde,long hasta,int modoRangeDate ){

        ValueEventListener seenListener  = RealtimDatabase.rootDatabaseReference.child("empleados").child("allEmpleados")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        miLisAllEmpleadosRegistrados= new ArrayList<>();


                        for (DataSnapshot dss : dataSnapshot.getChildren()) {
                            Empleado empleado = dss.getValue(Empleado.class);

                            if (empleado != null) {
                                miLisAllEmpleadosRegistrados.add(empleado);

                            }

                        }



                        if(miLisAllEmpleadosRegistrados.size()>0){

                            Log.i("comenzardata","es mayor a cero");

                            if(modoRangeDate==Utils.DIA_ESPECIFICO){

                                Log.i("comenzardata","es un dia especifico");

                                dowloadMarcacionesByDateRange(desde,hasta,modoRangeDate);

                                //    dowloadMaraciones_DIA_ESPECIFICO(0);
                                //generateAsistenciaArrayListByFilter(miLisAllEmpleadosRegistrados,Utils.DIA_ESPECIFICO);

                            }else{
                                Log.i("comenzardata","es un mes especifico");

                                dowloadMarcacionesByDateRange(desde,hasta,modoRangeDate);
                               // generateAsistenciaArrayListByFilter(miLisAllEmpleadosRegistrados,Utils.MES_ESPECIFICO);

                            }


                        }else{
                            TextView txtAdviserHere=findViewById(R.id.txtAdviserHere);
                            txtAdviserHere.setVisibility(View.VISIBLE);

                        }





                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.i("libiadod", "el error es " + databaseError.getMessage());

                    }
                });

    }


    private void setDataRecyclerView(ArrayList<PromedioAsistenceEmpleado> list){

        if(Utils.tipodeDatoMostrar== Utils.ITEM_MARACIONES_MODO) {

            txt2.setVisibility(View.VISIBLE);
            txt2.setText("Hora entrada");
            txt3.setText("Hora salida");

        }

        else {

            txt2.setVisibility(View.GONE);
            txt3.setText("Dias asistencia");

        }


        Log.i("sizelists","el size de lista es "+list.size());
        TextView txtAdviserHere=findViewById(R.id.txtAdviserHere);

        if(list.size()==0){

            txtAdviserHere.setVisibility(View.VISIBLE);


        }else{

            txtAdviserHere.setVisibility(View.GONE);


        }


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ActivityReportes.this);

        AdapterAsistencePromedio adapter = new AdapterAsistencePromedio(ActivityReportes.this, list);
        recylerVInformsAll.setLayoutManager(layoutManager);
        recylerVInformsAll.setAdapter(adapter);

        adapter.setOnItemClickListener(new AdapterAsistencePromedio.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {

                Utils.nameCurrentEmpleado=list.get(position).getEmpleadoName();

                Intent intencion= new Intent(ActivityReportes.this, ActivityDetailsAsistence.class);
                intencion.putExtra("KEY_USER_SELECTED",list.get(position).getIdUserEmpleado());


                startActivity(intencion);


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

        Calendar ca = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

        MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(ActivityReportes.this,
                new MonthPickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(int selectedMonth, int selectedYear) { // on date set }

                        Log.i("misafama","ondatset mes es "+selectedMonth+" y year es "+selectedYear);

                        mesSelecionado=selectedMonth+1;
                        yearSelecionado=selectedYear;
                        txtDateSelected.setText(Utils.arrayMesSelecionado[selectedMonth]);
                        modoDateRangeSearch=Utils.MES_ESPECIFICO;
                        //llamos  generar by mes...

                        Calendar calendarx =  Calendar.getInstance();
                        calendarx.set(Calendar.DAY_OF_MONTH, Calendar.getInstance().getActualMinimum(Calendar.DAY_OF_MONTH));

                        long calendaerioStart=calendarx.getTimeInMillis();

                        calendarx.set(Calendar.DAY_OF_MONTH, Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH));
                        SimpleDateFormat ddd=new SimpleDateFormat("dd-MM-yyyy");
                        String date= ddd.format(calendaerioStart);

                        Log.i("superman","el dia desde "+date+" hasta el dia"+ddd.format(calendarx.getTimeInMillis()));




                        dowloadAllEmpleadosAndGetPromedio(calendaerioStart,calendarx.getTimeInMillis(),Utils.MES_ESPECIFICO);


                    }

                }, ca.get(Calendar.YEAR), ca.get(Calendar.MONTH));

        builder.setActivatedMonth(Calendar.JULY)
                .setMinYear(2023)
                .setActivatedYear(ca.get(Calendar.YEAR))
                .setMaxYear(ca.get(Calendar.YEAR))
                .setMinMonth(Calendar.JANUARY)
                .setTitle("Selecione El rango")
                .setMonthRange(Calendar.JANUARY, Calendar.DECEMBER)
                // .setMaxMonth(Calendar.OCTOBER)
                // .setYearRange(1890, 1890)
                // .setMonthAndYearRange(Calendar.FEBRUARY, Calendar.OCTOBER, 1890, 1890)
                //.showMonthOnly()
                // .showYearOnly()



                .setOnMonthChangedListener(new MonthPickerDialog.OnMonthChangedListener() {
                    @Override
                    public void onMonthChanged(int i) {

                      //  mesSelecionado=i;
                        Log.i("especificmes","mes es "+i);


                    }
                });

        builder.setOnYearChangedListener(new MonthPickerDialog.OnYearChangedListener() {
            @Override
            public void onYearChanged(int selectedYear) { // on year selected } })


              //  yearSelecionado=selectedYear;
                   Log.i("misafama","solaper year es "+selectedYear);
            }
        });

                builder .build()
                .show();
    }


    //

    //aqui ya le pasamos la lista de fichaje con el que vamos a contruir una lista de promedios.


    private void generateAsistenciaArrayListByFilterXOnline(ArrayList<Empleado>listUsersToGetFichajePromedio,int modoDateRangeSearch){

       ArrayList<PromedioAsistenceEmpleado>arrayList= new ArrayList<>();

      //  Log.i("hakunama","DL SIZE DE ARRAU LIST start es  "+arrayList.size());


        ///

        for(Empleado empleadoObject: listUsersToGetFichajePromedio){ //biscamos

            //Log.i("comenzardata","el key es  "+key);


            //aqui podemos obtener
         //   HashMap<String, Fichar> hashMapAllRegistrosThisUser= SharePref.loadMapPreferencesFichaje(empleadoObject.getIdEmpleado());
           // Log.i("misdta","el size de map fichaje de este user es "+hashMapAllRegistrosThisUser.size());


            if(modoDateRangeSearch== Utils.MES_ESPECIFICO){

                Log.i("comenzardata","seleciono mes especifico ");

                // mesSelecionado
                ArrayList<Fichar>lisFichajeCurrentUser= mapOfMapAllFichajeUserByRange.get(empleadoObject.getIdEmpleado());


                if(lisFichajeCurrentUser.size()>0){
                    PromedioAsistenceEmpleado promedioObjec=  generatePromedioObjectThisUserByArrayList(lisFichajeCurrentUser,
                            empleadoObject.getNombreYapellidoEmpleado(),empleadoObject.getIdEmpleado());
                    arrayList.add(promedioObjec);
                }

            }

            else if(modoDateRangeSearch==Utils.DIA_ESPECIFICO){

                Log.i("comenzardata","selecionado un dia espcifico y el map size es "+mapOfMapAllFichajeUserByRange.size());

/*
                for (HashMap.Entry<String, ArrayList<Fichar>> entry : mapOfMapAllFichajeUserByRange.entrySet()) {
                    String key = entry.getKey();
                    ArrayList<Fichar> value = entry.getValue();

                    Log.i("comenzardata","el key es  "+key);


                    // ...
                }

*/


                if(mapOfMapAllFichajeUserByRange.containsKey(empleadoObject.getIdEmpleado())){

                    Log.i("comenzardata","si contiene este key y es "+empleadoObject.getCodigoPaFichar());
                    ArrayList<Fichar>lisFichajeCurrentUser=mapOfMapAllFichajeUserByRange.get(empleadoObject.getIdEmpleado());
                    Log.i("hakunama","el size de la lista es  "+lisFichajeCurrentUser.size());


                    if(lisFichajeCurrentUser.size()>0){

                        PromedioAsistenceEmpleado promedioObjec=  generatePromedioObjectThisUserByArrayList(lisFichajeCurrentUser,empleadoObject.getNombreYapellidoEmpleado()
                                ,empleadoObject.getIdEmpleado());
                        arrayList.add(promedioObjec);
                    }


                    Log.i("hakunama","el size de la ahora es "+arrayList.size());

                }



            }


        }

        Log.i("hakunama","DL SIZE DE ARRAU LIST vv  ES "+arrayList.size());

        setDataRecyclerView(arrayList);




    }

   // private void getmaracionesallUsersEspecificRange
    private void generateAsistenciaArrayListByFilter(ArrayList<Empleado>listUsersToGetFichajePromedio,int modoDateRangeSearch){

        ArrayList<PromedioAsistenceEmpleado>arrayList= new ArrayList<>();

        Log.i("hakunama","DL SIZE DE ARRAU LIST start es  "+arrayList.size());


         ///

        for(Empleado empleadoObject: listUsersToGetFichajePromedio){ //biscamos

            //aqui podemos obtener

            HashMap<String, Fichar> hashMapAllRegistrosThisUser= SharePref.loadMapPreferencesFichaje(empleadoObject.getIdEmpleado());

            Log.i("misdta","el size de map fichaje de este user es "+hashMapAllRegistrosThisUser.size());

              if(modoDateRangeSearch== Utils.MES_ESPECIFICO){

                  Log.i("hakunama","seleciono mes especifico ");

                  // mesSelecionado
                ArrayList<Fichar>lisFichajeCurrentUser=generaListMaracacionesEspecifiDateOrRange(hashMapAllRegistrosThisUser,Utils.MES_ESPECIFICO);


                if(lisFichajeCurrentUser.size()>0){
                        PromedioAsistenceEmpleado promedioObjec=  generatePromedioObjectThisUserByArrayList(lisFichajeCurrentUser,
                                empleadoObject.getNombreYapellidoEmpleado(),empleadoObject.getIdEmpleado());
                        arrayList.add(promedioObjec);
                    }

              }

              else if(modoDateRangeSearch==Utils.DIA_ESPECIFICO){


                 // REVISAR AQUI LOS DOS STRING QUE SE COMPARAN.. Y VER SI EL METODO DE ABAJO NOS GENEROA UN VA;OR./
                  ArrayList<Fichar>lisFichajeCurrentUser=generaListMaracacionesEspecifiDateOrRange(hashMapAllRegistrosThisUser,Utils.DIA_ESPECIFICO);


                  Log.i("hakunama","el size de la lista es  "+lisFichajeCurrentUser.size());

                  if(lisFichajeCurrentUser.size()>0){


                      PromedioAsistenceEmpleado promedioObjec=  generatePromedioObjectThisUserByArrayList(lisFichajeCurrentUser,empleadoObject.getNombreYapellidoEmpleado()
                      ,empleadoObject.getIdEmpleado());
                      arrayList.add(promedioObjec);
                  }


                  Log.i("hakunama","el size de la ahora es "+arrayList.size());

              }


        }

        Log.i("hakunama","DL SIZE DE ARRAU LIST vv  ES "+arrayList.size());


        setDataRecyclerView(arrayList);


    }


    //lisfichajes de un mes especifico....
    private void dowloadMarcacionesByDateRange(long desdeFecha, long hastFecha, int modoDateRangeSearch){

        Query query = RealtimDatabase.rootDatabaseReference.child("marcaciones").child("allmarcaciones").
                orderByChild("entradaMilliseconds").startAt(desdeFecha).endAt(hastFecha);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mapOfMapAllFichajeUserByRange= new HashMap<>();

                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    Fichar fichar=ds.getValue(Fichar.class);
                    //agregamos solo los que no esten en esta lista..
                    if(fichar!=null){  //creamos un objet

                        String keyActual=fichar.getFicharUserId();

                        Log.i("comenzardata","el key actual para addd es "+keyActual);

                        if(mapOfMapAllFichajeUserByRange.containsKey(keyActual)){ //existe este mapa

                         ArrayList <Fichar>list = mapOfMapAllFichajeUserByRange.get(keyActual);

                            list.add(fichar);
                            mapOfMapAllFichajeUserByRange.put(keyActual,list);

                        }

                        else

                        {
                            ArrayList <Fichar>list= new ArrayList<>();

                            list.add(fichar);

                            mapOfMapAllFichajeUserByRange.put(keyActual,list);

                        }



                    }

                }


                Log.i("comenzardata","el size de mapaofnap es "+mapOfMapAllFichajeUserByRange.size());

                generateAsistenciaArrayListByFilterXOnline(miLisAllEmpleadosRegistrados,modoDateRangeSearch);


              ///  setAdapaterDataAndShow(listReport);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Log.i("sliexsa","el error es "+error.getMessage());

            }
        });



    }


    private void dowloadMaraciones_DIA_ESPECIFICO(Long diaEspecifico) {

        DatabaseReference usersdRef = RealtimDatabase.rootDatabaseReference.child("marcaciones").child("allmarcaciones");

        Query query = usersdRef.orderByChild("entradaMilliseconds").equalTo(diaEspecifico);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Fichar informe = null;

                    mapOfMapAllFichajeUserByRange= new HashMap<>();

                    for(DataSnapshot ds : snapshot.getChildren()) {
                        Fichar fichar=ds.getValue(Fichar.class);
                        //agregamos solo los que no esten en esta lista..
                        if(fichar!=null){  //creamos un objet

                            String keyActual=fichar.getKeyficharDate();

                            if(mapOfMapAllFichajeUserByRange.containsKey(keyActual)){ //existe este mapa

                                ArrayList <Fichar>list = mapOfMapAllFichajeUserByRange.get(keyActual);

                                list.add(fichar);
                                mapOfMapAllFichajeUserByRange.put(keyActual,list);

                            }

                            else

                            {
                                ArrayList <Fichar>list= new ArrayList<>();

                                list.add(fichar);

                                mapOfMapAllFichajeUserByRange.put(keyActual,list);

                            }



                        }

                    }

                    Log.i("debeber","el size de mapaofnap es "+mapOfMapAllFichajeUserByRange.size());

                    generateAsistenciaArrayListByFilterXOnline(miLisAllEmpleadosRegistrados,modoDateRangeSearch);





            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("misdata", "el error es  " + error.getMessage());

            }
        });

    }



    private ArrayList<Fichar>generaListMaracacionesEspecifiDateOrRange(HashMap<String, Fichar> hashMapAllRegistrosThisUser,int modo){

        ArrayList<Fichar>lisFichar= new ArrayList<>();
        GregorianCalendar c = new GregorianCalendar();


        for(Fichar ficharObjec: hashMapAllRegistrosThisUser.values()){

             if(modo==Utils.MES_ESPECIFICO) {
                 c.setTimeInMillis(ficharObjec.getEntradaMilliseconds());
                 int month = c.get(Calendar.MONTH)+1;
                 int year = c.get(Calendar.YEAR);

                 Log.i("especificmes","el month by millisecond es "+month+" el mes selecionado num es "+mesSelecionado);

                 Log.i("especificmes","el year by millisecond es "+year+" el year selecioando es "+yearSelecionado);

                 if (month == mesSelecionado && year == yearSelecionado) {
                     lisFichar.add(ficharObjec);

                 }

             }


             else if(modo==Utils.DIA_ESPECIFICO){


                 DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                 String currentDate = dateFormat.format(ficharObjec.getEntradaMilliseconds());

                 Log.i("hakunama","el fecha milisegundoses "+currentDate);
                 Log.i("hakunama","el fecha selecionado calendario es "+fechaSelecionadaCalendar);


                 if (currentDate.equals(fechaSelecionadaCalendar)) {
                     lisFichar.add(ficharObjec);
                     Log.i("hakunama","el fecha entarda es  "+ficharObjec.getEntradaMilliseconds());


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

                              String dia;
                              String mes;
                         fechaSelecionadaCalendar =i2+"/"+(i1+1)+"/"+i;  //dia mes .year
                        txtDateSelected.setText(fechaSelecionadaCalendar);

                        dia=String.valueOf(i2);
                        mes=String.valueOf(i1+1);


                        if(i2<10){
                            dia="0"+dia;
                         }

                        if(i1+1<10){
                            mes="0"+mes;
                        }


                        fechaSelecionadaCalendar=dia+"/"+mes+"/"+i;
                        modoDateRangeSearch=Utils.DIA_ESPECIFICO;


                        Calendar calendar= Calendar.getInstance();
                        calendar.set(i,i1,i2);

                        // calendar.set(Calendar.DAY_OF_MONTH,7);
                        calendar.add(Calendar.DATE, -1);
                        desdeDateMillisecond=calendar.getTimeInMillis();
                        calendar.add(Calendar.DATE, +2); //estaba en  +1
                        hastaDateMillisecond=calendar.getTimeInMillis();



//                        SimpleDateFormat format=new SimpleDateFormat("dd-MM-yyyy");
                        SimpleDateFormat forma=new SimpleDateFormat("dd/MM/yyyy");

                        Log.i("comenzardata"," seleciono dia del calendario fargemnt y el dia pasado es  "+forma.format(desdeDateMillisecond)+" el dia que le sigue es  es "+forma.format(hastaDateMillisecond));

                       //*****  valor del medio es el selecioando y ese debe ser el dia que selcionamos

                        dowloadAllEmpleadosAndGetPromedio(desdeDateMillisecond,hastaDateMillisecond,Utils.DIA_ESPECIFICO);

                            /**si no encontramos data, oucltamos o mostramos un araray list vacio oewro no null y kle decimos que no hay data ,,cehekear */

                     //   generateAsistenciaArrayListByFilter(miLisAllEmpleadosRegistrados,Utils.DIA_ESPECIFICO);

                       // ediFecha.setText(dateSelec);

                        // ediFecha.setText(daySemana+"/"+mes+"/"+year);

                    }
                }, year,  mes, daySemana);

        picker.setButton(DialogInterface.BUTTON_POSITIVE, "OK", picker);
        picker.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancelar", picker);


        picker.show();
    }



    PromedioAsistenceEmpleado generatePromedioObjectThisUserByArrayList(ArrayList<Fichar>list,String nombre,String idUserEmpleado){

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
                diasAsistencia++;

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
                horaSalida+":"+minutosSalida,diasAsistencia,idUserEmpleado);


    }


}