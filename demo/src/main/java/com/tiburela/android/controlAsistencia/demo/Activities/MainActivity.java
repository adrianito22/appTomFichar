package com.tiburela.android.controlAsistencia.demo.Activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.tiburela.android.controlAsistencia.CameraView;
import com.tiburela.android.controlAsistencia.demo.R;
import com.tiburela.android.controlAsistencia.demo.Utils.FaceRecognizer;
import com.tiburela.android.controlAsistencia.demo.Utils.FileUtils;
import com.tiburela.android.controlAsistencia.demo.Utils.RealtimDatabase;
import com.tiburela.android.controlAsistencia.demo.Utils.SharePref;
import com.tiburela.android.controlAsistencia.demo.Utils.StorageData;
import com.tiburela.android.controlAsistencia.demo.Utils.Utils;
import com.tiburela.android.controlAsistencia.demo.models.Empleado;
import com.tiburela.android.controlAsistencia.demo.models.Fichar;
import com.tzutalin.dlib.Constants;

import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    File destination = null;
    int contador=0;

    CardView cardViewEntrada;
    CardView cardViewInformes;
    CardView cardViewHorario;
    CardView cardViewSalida;
    LinearLayout layoutAddEmpleado;
   LinearLayout layoutAllEmployers;



    Handler handler = new Handler();


    private boolean isFirstUsage=true;

    private static final String TAG = "ActivityReconocimientoF";
    private static final int INPUT_SIZE = 500;


    private static final int[] FLASH_OPTIONS = {
            CameraView.FLASH_OFF,
            CameraView.FLASH_AUTO,
            CameraView.FLASH_ON,
    };




    private static final int[] FLASH_ICONS = {
            R.drawable.ic_flash_off,
            R.drawable.ic_flash_auto,
            R.drawable.ic_flash_on,
    };


    private static final int[] FLASH_TITLES = {
            R.string.flash_off,
            R.string.flash_auto,
            R.string.flash_on,
    };

    String[] permissions = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    long startTimeLocally;

    private int mCurrentFlash;
    private CameraView mCameraView;
    private Handler mBackgroundHandler;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_center);

        RealtimDatabase.initDatabasesRootOnly();
        StorageData.initStorageReference();

        Log.i("solapina","oncreate   : ");


        layoutAllEmployers=findViewById(R.id.layoutAllEmployers);
        cardViewEntrada=findViewById(R.id.cardViewEntrada);
        cardViewInformes =findViewById(R.id.cardViewInformes);
        cardViewHorario =findViewById(R.id.cardViewHorario);
        cardViewSalida=findViewById(R.id.cardViewSalida);
        layoutAddEmpleado=findViewById(R.id.layoutAddEmpleado);


        SharePref.init(MainActivity.this);


        listennersEventos();



        Thread myThread = null;

        TextView  textView3=findViewById(R.id.textView3);


        String output =
                ZonedDateTime.now ( ZoneId.of ( "Europe/Madrid" ) )
                        .format (
                                DateTimeFormatter.ofLocalizedDate ( FormatStyle.FULL )
                                        .withLocale ( new Locale ( "es" , "ES" ) )
                        )
                ;

         textView3.setText(output);


        // Parse the given date-time string into OffsetDateTime
       // OffsetDateTime odt = OffsetDateTime.parse(date);

        // Define the formatter for output in a custom pattern and in Spanish Locale
        //  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd MM, uuuu hh:mm a", new Locale("es", "ES"));

       // DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy", new Locale("es", "ES"));

        // Print instant using the defined formatter
        //String formatted = formatter.format(odt);


        //Log.i("sanaz","el value es "+formatted);

       // textView3.setText(date);



        Runnable runnable = new CountDownRunner();
        myThread= new Thread(runnable);
        myThread.start();
    }

    @Override
    protected void onStart() {
        super.onStart();

         //descragamos la lista de empleados....

        dowloadAllEmpleados();


    }


    private void dowloadAllEmpleados(  ){

        ValueEventListener seenListener  = RealtimDatabase.rootDatabaseReference.child("empleados").child("allEmpleados")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Utils.listEmpleadosAll= new ArrayList<>();

                        for (DataSnapshot dss : dataSnapshot.getChildren()) {
                            Empleado empleado = dss.getValue(Empleado.class);

                            if (empleado != null) {
                                if(empleado.getMailEmppleador().equals(Utils.maiLEmpleadorGlOBAL)){
                                    Utils.listEmpleadosAll.add(empleado);

                                }
                            }
                        }

                         if( Utils.listEmpleadosAll.size()>0){
                             dowloadImagenAndBitmap(Utils.listEmpleadosAll);

                         }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.i("libiadod", "el error es " + databaseError.getMessage());

                    }
                });

    }

    private void dowloadImagenAndBitmap(ArrayList<Empleado> listEmpleados){
         contador=0;

        int BITMAP_QUALITY = 100;


        /**gaurdamos las imageens esn espcific directorio*/
        for(Empleado empleado: listEmpleados){

            Glide.with(MainActivity.this)
                    .asBitmap()
                    .load(empleado.getUrlPickEmpleado())
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {

                            Log.i("solapina","on resoruce ready");

                            contador++;


                            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                            resource.compress(Bitmap.CompressFormat.JPEG, BITMAP_QUALITY, bytes);
                            FileOutputStream fo;

                            Long tsLong = System.currentTimeMillis() / 1000;
                            String ts = tsLong.toString();


                           //

                            File file = new File(empleado.getPathImageEmpleado());
                            File file2 = new File(Constants.getDLibImageDirectoryPath() +"/"+ empleado.getNombreYapellidoEmpleado()+ts+".jpg");

                            if(file.exists()){

                                Log.i("solapina","el file existe ");


                            }

                            else



                            {

                                Log.i("solapina","el file no existe ");
                                try {

                                    destination =file2;
                                    destination.createNewFile();
                                    fo = new FileOutputStream(destination);
                                    fo.write(bytes.toByteArray());
                                    fo.close();
                                } catch (FileNotFoundException e) {

                                    e.printStackTrace();
                                    Log.i("solapina","el exepcion 1 es "+e.getMessage());

                                } catch (IOException e) {

                                    Log.i("solapina","el exepcion 2 es "+e.getMessage());
                                    e.printStackTrace();

                                }

                            }





                            Log.i("solapina","es igual a list contador es "+contador+" y size list es "+listEmpleados.size());

                            if(contador==listEmpleados.size() &&  Utils.addPersonaNuevaOrISfirst){

                                Log.i("solapina","es igual a list contador es "+contador+" y size list es "+listEmpleados.size());
                                Utils.addPersonaNuevaOrISfirst =false;

                                new initRecAsync().execute();


                            }


                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                        }
                    });






        }





    }

    public void doWork() {
        runOnUiThread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            public void run() {
                try{
                    TextView txtCurrentTime=findViewById(R.id.lbltime);


                    String time = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());



                    //int hours = dt.;
                    //int minutes = dt.getMinutes();
                   // int seconds = dt.getSeconds();
                   // String curTime = hours + ":" + minutes + ":" + seconds;
                    txtCurrentTime.setText(time);



                 //   String strDateTime = "2020-09-10T20:00:00.000Z";


                }catch (Exception e) {}
            }
        });
    }


    class CountDownRunner implements Runnable{
        // @Override
        public void run() {
            while(!Thread.currentThread().isInterrupted()){
                try {
                    doWork();
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }catch(Exception e){
                }
            }
        }
}



void listennersEventos(){





     cardViewEntrada.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {

             Fichar.tipoFichanSelecionadoCurrent=Fichar.FICHAJE_ENTRADA;


             sheetBootomFicharOptions();




         }
     });
     cardViewInformes.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {

            //    Fichar.tipoFichanSelecionadoCurrent=Fichar.FICHAJE_INCIO_COMIDA;

            Intent intent = new Intent(MainActivity.this, ActivityReportes.class);
             startActivity(intent);

         }
     });
    cardViewHorario.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {


             Intent intent = new Intent(MainActivity.this, ActivityPassMain.class);
             intent.putExtra("key",Utils.GO_ActivityHorario);
             startActivity(intent);

         }
     });


     cardViewSalida.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             Fichar.tipoFichanSelecionadoCurrent=Fichar.FICHAJE_SALIDA;

             sheetBootomFicharOptions();

           //  Intent intent = new Intent(MainActivity.this, ActivityReconocimientoF.class);
          //   startActivity(intent);

         }
     });


    layoutAddEmpleado.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Intent intent = new Intent(MainActivity.this, ActivityPassMain.class);
            intent.putExtra("key",Utils.GO_AddPerson);
            startActivity(intent);


        }
    });


    layoutAllEmployers.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {


            startActivity(new Intent(MainActivity.this, ActivityEmpleadosAll.class));

            //aqui vamos a activit all employes
        }
    });


}





    private class initRecAsync extends AsyncTask<Void, Void, Void> {

        ProgressDialog dialog = new ProgressDialog(MainActivity.this);

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Initializing...");
            dialog.setCancelable(false);
            dialog.show();
            super.onPreExecute();
        }



        protected Void doInBackground(Void... args) {

            File folder = new File(Constants.getDLibDirectoryPath());
            boolean success = true;
            if (!folder.exists()) {
                success = folder.mkdirs();

                Log.i("solapina","primer if  "+success);

            }

            Log.i("solapina","2  if  "+success);

            if (success) {
                File image_folder = new File(Constants.getDLibImageDirectoryPath());
                image_folder.mkdirs();
                if (!new File(Constants.getFaceShapeModelPath()).exists()) {
                    FileUtils.copyFileFromRawToOthers(MainActivity.this, R.raw.shape_predictor_5_face_landmarks, Constants.getFaceShapeModelPath());
                }
                if (!new File(Constants.getFaceDescriptorModelPath()).exists()) {
                    FileUtils.copyFileFromRawToOthers(MainActivity.this, R.raw.dlib_face_recognition_resnet_model_v1, Constants.getFaceDescriptorModelPath());
                }
            }



           final long startTime = System.currentTimeMillis();

            changeProgressDialogMessage(dialog, "Entrenando Imagenes...");
            FaceRecognizer.getInstance().train();

            final long endTime = System.currentTimeMillis();
            Log.d("TimeCost", "Time cost: " + (endTime - startTime) / 1000f + " sec");

            handler.post(new Runnable() {
                @Override
                public void run() {


                    Toast.makeText(getApplicationContext(),"Tiempo: "+ (endTime - startTime) / 1000f + " sec",Toast.LENGTH_LONG).show();


                }
            });

            return null;
        }

        protected void onPostExecute(Void result) {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }




    private void changeProgressDialogMessage(final ProgressDialog pd, final String msg) {
        Runnable changeMessage = new Runnable() {
            @Override
            public void run() {
                pd.setMessage(msg);
            }
        };
        runOnUiThread(changeMessage);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        FaceRecognizer.getInstance().release();
    }

    private void sheetBootomFicharOptions(){

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(MainActivity.this);

        bottomSheetDialog.setContentView(R.layout.bottom_sheet_option_fichar);

        Button btnRecocimientoFacial=bottomSheetDialog.findViewById(R.id.btnRecocimientoFacial);
        Button btnCodigo=bottomSheetDialog.findViewById(R.id.btnCodigo);

        //ImageView imgClose=bottomSheetDialog.findViewById(R.id.imgClose);
        //8  bottomSheetDialog.setCancelable(false);


        btnRecocimientoFacial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, ActivityReconocimientoF.class);
                startActivity(intent);

                bottomSheetDialog.dismiss();


            }
        });



        btnCodigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ActivityCodigoFichar.class);
                startActivity(intent);
                bottomSheetDialog.dismiss();


            }
        });




        bottomSheetDialog.show();

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.option:

                startActivity(new Intent(MainActivity.this,ActivityBiometrick.class));
                // archive(item);
                Log.i("gdher","se oulso option1");
                return true;

            case R.id.option2:

                FirebaseAuth.getInstance().signOut();


                GoogleSignIn.getClient(
                        MainActivity.this,
                        new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
                ).signOut();

                //cerramos sesion.. y vamos a la activity  login

                finish();

                // delete(item);
                Log.i("gdher","se oulso option2");

                return true;
            default:
                return false;
        }
    }

    public void showMenu(View v) {

        Log.i("gdher","se oulso show here");


        PopupMenu popup = new PopupMenu(this, v);
        // This activity implements OnMenuItemClickListener
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.menu2);
        popup.show();
    }


private void cerrarSesion(){


}




}