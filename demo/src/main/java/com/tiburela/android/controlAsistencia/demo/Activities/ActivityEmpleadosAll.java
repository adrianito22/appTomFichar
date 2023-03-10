package com.tiburela.android.controlAsistencia.demo.Activities;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.jetbrains.annotations.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.tiburela.android.controlAsistencia.demo.R;
import com.tiburela.android.controlAsistencia.demo.Utils.RealtimDatabase;
import com.tiburela.android.controlAsistencia.demo.Utils.Utils;
import com.tiburela.android.controlAsistencia.demo.adapters.AdapterEmpleado;
import com.tiburela.android.controlAsistencia.demo.models.Empleado;
import com.tzutalin.dlib.Constants;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ActivityEmpleadosAll extends AppCompatActivity {
    File destination = null;

    RecyclerView recylerVInformsAll;
     ArrayList<Empleado>listAllEmpleados= new ArrayList<>();

    ArrayList<Empleado>listFilteredEmpleados= new ArrayList<>();

    EditText ediNombreEmpleado;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empleados_all);

        ediNombreEmpleado=findViewById(R.id.ediNombreEmpleado);
        recylerVInformsAll=findViewById(R.id.recylerVInformsAll);


        //iniicializamos text watcher
        textWatcher();


        dowloadAllEmpleados();


        /*
       HashMap<String, Empleado> mhasMap = SharePref.loadMapPreferencesEmpleados(SharePref.KEY_ALL_EMPLEADOS_Map);
        if(mhasMap.size()>0){

            listAllEmpleados.addAll(mhasMap.values());

            setDataRecyclerView(listAllEmpleados);

        }



        else{ //no hay ningun empleado registrado//mostramos txt no hay texto
            TextView txtAdviser= findViewById(R.id.txtAdviser);
            txtAdviser.setVisibility(View.VISIBLE);

        }

*/



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

    private void dowloadAllEmpleados(  ){

        ValueEventListener seenListener  = RealtimDatabase.rootDatabaseReference.child("empleados").child("allEmpleados")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                         listAllEmpleados= new ArrayList<>();


                        for (DataSnapshot dss : dataSnapshot.getChildren()) {
                            Empleado empleado = dss.getValue(Empleado.class);

                            if (empleado != null) {

                                if(empleado.getMailEmppleador().equals(Utils.maiLEmpleadorGlOBAL)){

                                    listAllEmpleados.add(empleado);


                                }


                            }

                        }


                        setDataRecyclerView(listAllEmpleados);

                        dowloadImagenAndBitmap(listAllEmpleados);



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.i("libiadod", "el error es " + databaseError.getMessage());

                    }
                });

    }

    private void dowloadImagenAndBitmap(ArrayList<Empleado> listEmpleados){

        int BITMAP_QUALITY = 100;


        /**gaurdamos las imageens esn espcific directorio*/
        for(Empleado empleado: listEmpleados){
            Glide.with(ActivityEmpleadosAll.this)
                    .asBitmap()
                    .load(empleado.getUrlPickEmpleado())
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {

                            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                            resource.compress(Bitmap.CompressFormat.JPEG, BITMAP_QUALITY, bytes);
                            FileOutputStream fo;
                            try {
                                Long tsLong = System.currentTimeMillis() / 1000;
                                String ts = tsLong.toString();


                                destination = new File(Constants.getDLibImageDirectoryPath() +"/"+ empleado.getNombreYapellidoEmpleado()+ts+".jpg");
                                destination.createNewFile();
                                fo = new FileOutputStream(destination);
                                fo.write(bytes.toByteArray());
                                fo.close();
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();

                                Log.i("eltagddd","el exepcion 1 es "+e.getMessage());

                            } catch (IOException e) {
                                Log.i("eltagddd","el exepcion 2 es "+e.getMessage());

                                e.printStackTrace();

                            }

                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                        }
                    });

        }










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



    private void textWatcher(  ) {

        ediNombreEmpleado.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Log.i("comisaria","el size en before text es "+ i2);
                // Log.i("comisaria","el size  en ontexttext es "+edt_search.getText().toString().length() );

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                // Log.i("comisaria","el size  en ontexttext es "+edt_search.getText().toString().length() );
                Log.i("comisaria","llamamos ontextchange " );



                listFilteredEmpleados.clear();
                listFilteredEmpleados= new ArrayList<>();

                if(listAllEmpleados==null){
                    return;
                }

                for(int index = 0; index<  listAllEmpleados.size(); index++) {

                    if( !charSequence.toString().isEmpty() &&  listAllEmpleados.get(index).getNombreYapellidoEmpleado().toUpperCase().contains(charSequence.toString().toUpperCase()))
                    {
                        listFilteredEmpleados.add(listAllEmpleados.get(index));
                        Log.i("comisaria","se ejecuto el if aqui agregamos este nombre  "+listAllEmpleados.get(index).getNombreYapellidoEmpleado());
                    }
                    //llaamos a crear recilcer nuevamente todos

                }
                recylerVInformsAll.removeAllViews();;

                addDataReciclerAndShowOptions(listFilteredEmpleados);


                //adap.notifyDataSetChanged();

                Log.i("comisaria","bien el size es  "+listFilteredEmpleados.size());





                if(listFilteredEmpleados.size()==0 && charSequence.toString().isEmpty()){
                    addDataReciclerAndShowOptions(listAllEmpleados);

                }else{
                    addDataReciclerAndShowOptions(listFilteredEmpleados);


                }




            }

            @Override
            public void afterTextChanged(Editable s) {
                //  Log.i("comisaria","el size  en affter es "+edt_search.getText().toString().length() );


            }

        });

    }

    private void addDataReciclerAndShowOptions(ArrayList<Empleado> listEmpleados) {

        recylerVInformsAll.setVisibility(View.VISIBLE);

        recylerVInformsAll.setHasFixedSize(true);

        AdapterEmpleado adapter = new AdapterEmpleado(ActivityEmpleadosAll.this, listEmpleados);

        recylerVInformsAll.setLayoutManager(new LinearLayoutManager(ActivityEmpleadosAll.this,
                LinearLayoutManager.VERTICAL, false));

        recylerVInformsAll.setAdapter(adapter);

        adapter.setOnItemClickListener(new AdapterEmpleado.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {

                /***aqui obtenemos el objeto global*/

                Log.i("elcickler","hemos clickeado");



            }
        });


    }


}