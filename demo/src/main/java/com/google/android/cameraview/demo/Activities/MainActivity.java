package com.google.android.cameraview.demo.Activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.cameraview.CameraView;
import com.google.android.cameraview.demo.R;
import com.google.android.cameraview.demo.Utils.FaceRecognizer;
import com.google.android.cameraview.demo.Utils.FileUtils;
import com.google.android.cameraview.demo.Utils.SharePref;
import com.google.android.cameraview.demo.Utils.Utils;
import com.google.android.cameraview.demo.models.Empleado;
import com.google.android.cameraview.demo.models.Fichar;
import com.tzutalin.dlib.Constants;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

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

        try {
            HashMap<String, Empleado> mhasMap = SharePref.loadMapPreferencesEmpleados(SharePref.KEY_ALL_EMPLEADOS_Map);

         //  new initRecAsync().execute();

            if(isFirstUsage || Utils.existeNewUserAdd){

                if(mhasMap.size()>0){

                    new initRecAsync().execute();


                }

                Utils.existeNewUserAdd=false;

            }





        } catch (Exception e) {
            e.printStackTrace();
        }


        isFirstUsage=false;
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

         //    Fichar.tipoFichanSelecionadoCurrent=Fichar.FICHAJE_FIN_COMIDA;


             Intent intent = new Intent(MainActivity.this, ActivityHorario.class);
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

            Intent intent = new Intent(MainActivity.this, AddPerson.class);
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
            }
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


}