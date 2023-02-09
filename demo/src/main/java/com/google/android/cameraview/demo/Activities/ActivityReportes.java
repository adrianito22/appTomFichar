package com.google.android.cameraview.demo.Activities;

import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.google.android.cameraview.demo.R;
import com.google.android.cameraview.demo.adapters.AdapterEmpleado;
import com.google.android.cameraview.demo.models.Empleado;
import com.whiteelephant.monthpicker.MonthPickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

public class ActivityReportes extends AppCompatActivity {
    Spinner spinnerDateRange;
   RecyclerView recylerVInformsAll;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportes);

        spinnerDateRange = findViewById(R.id.spinnerDateRange);
        recylerVInformsAll=findViewById(R.id.recylerVInformsAll);


        setAdapaterSpinner("Hoy");


        spinnerDateRange.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                sheetBootomInforOptions();

                return false;
            }
        });







    }

    private void setDataRecyclerView(ArrayList<Empleado> list){


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ActivityReportes.this);

        AdapterEmpleado adapter = new AdapterEmpleado(ActivityReportes.this, list);
        recylerVInformsAll.setLayoutManager(layoutManager);
        recylerVInformsAll.setAdapter(adapter);


        adapter.setOnItemClickListener(new AdapterEmpleado.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {


             //   sheetBootomInforOptions(position);

                //  sheetBootomInforOptions(v.getTag(R.id.tagUniqueId1).toString(),v.getTag(R.id.tagUniqueId2).toString(),v.getTag(R.id.codigoProductor).toString());


                Log.i("elcickler","el click es llamado");




            }
        });




    }



    void setAdapaterSpinner(String fraseSpinner){

        String[] arraySpinner = new String[] {
                fraseSpinner
        };



        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDateRange.setAdapter(adapter);

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
                        Log.i("misafama","mes es "+i);


                    }
                });

        builder.setOnYearChangedListener(new MonthPickerDialog.OnYearChangedListener() {
            @Override
            public void onYearChanged(int selectedYear) { // on year selected } })

                   Log.i("misafama","solaper year es "+selectedYear);
            }
        });

                builder .build()
                .show();
    }

}