

package com.tiburela.android.controlAsistencia.demo.Activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tiburela.android.controlAsistencia.CameraView;
import com.tiburela.android.controlAsistencia.demo.R;
import com.tiburela.android.controlAsistencia.demo.Utils.FaceRecognizer;
import com.tiburela.android.controlAsistencia.demo.Utils.RealtimDatabase;
import com.tiburela.android.controlAsistencia.demo.Utils.SharePref;
import com.tiburela.android.controlAsistencia.demo.Utils.Utils;
import com.tiburela.android.controlAsistencia.demo.models.Empleado;
import com.tiburela.android.controlAsistencia.demo.models.Fichar;
import com.tzutalin.dlib.Constants;
import com.tzutalin.dlib.VisionDetRet;

import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;


public class ActivityReconocimientoF extends AppCompatActivity implements
        ActivityCompat.OnRequestPermissionsResultCallback {
         ImageView imageView6;
    File destination = null;


    private static final String TAG = "ActivityReconocimientoF";
    private static final int INPUT_SIZE = 500;


     String idEncontrado="";

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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_empty);

/*
        mCameraView = (CameraView) findViewById(R.id.camera);

       Button fab = (Button) findViewById(R.id.take_picture);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        checkPermissions();

        if (mCameraView != null) {
            mCameraView.addCallback(mCallback);
        }


        if (fab != null) {
            fab.setOnClickListener(mOnClickListener);
        }


        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }
*/

        takePickCamera();


    }


    private CameraView.Callback mCallback
            = new CameraView.Callback() {

        @Override
        public void onCameraOpened(CameraView cameraView) {
            Log.d(TAG, "onCameraOpened");
        }

        @Override
        public void onCameraClosed(CameraView cameraView) {
            Log.d(TAG, "onCameraClosed");
        }

        @Override
        public void onPictureTaken(CameraView cameraView, final byte[] data) {
          //  Log.d(TAG, "onPictureTaken " + data.length);

            Log.i("solapina","el value es startcc  : ");



            Bitmap bp = drawResizedBitmap(BitmapFactory.decodeByteArray(data, 0, data.length));
            new detectAsync().execute(bp);

        }

    };



    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.take_picture:
                    if (mCameraView != null) {
                        mCameraView.takePicture();
                    }
                    break;

            }
        }
    };






    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }

        if (!listPermissionsNeeded.isEmpty()) {

            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 100);
            return false;

        }


        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
           // mCameraView.start();
        }


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {

        }

    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause called");
      //  mCameraView.stop();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy called");
        super.onDestroy();


        if (mBackgroundHandler != null) {

            mBackgroundHandler.getLooper().quitSafely();
            mBackgroundHandler = null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            }
            return;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
/*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.switch_flash:
                if (mCameraView != null) {
                    mCurrentFlash = (mCurrentFlash + 1) % FLASH_OPTIONS.length;

                    item.setTitle(FLASH_TITLES[mCurrentFlash]);
                    item.setIcon(FLASH_ICONS[mCurrentFlash]);
                    mCameraView.setFlash(FLASH_OPTIONS[mCurrentFlash]);
                }
                return true;
            case R.id.switch_camera:
                if (mCameraView != null) {
                    int facing = mCameraView.getFacing();
                    ///int facing = 500;

                    mCameraView.setFacing(facing == CameraView.FACING_FRONT ?
                            CameraView.FACING_BACK : CameraView.FACING_FRONT);
                }

                return true;
        }
        return super.onOptionsItemSelected(item);
    }
*/
    private String getResultMessage(ArrayList<String> names) {
        String msg = "";
        if (names.isEmpty()) {
            msg = "No face detected or Unknown person";

        } else {
            for (int i = 0; i < names.size(); i++) {
                msg += names.get(i).split(Pattern.quote("."))[0];
                if (i != names.size() - 1) msg += ", ";
            }
            msg += " encontrado!";

            //encontramos alguna careta..///vamos a c

                 dowloadHorRIOoBJECIFexistAndFichajeoRupdate(idEncontrado);

            //    marcamosFichaje(idEncontrado);




        }







        return msg;
    }

    private void dowloadHorRIOoBJECIFexistAndFichajeoRupdate(String idFIchajeData) {

        DatabaseReference usersdRef = RealtimDatabase.rootDatabaseReference.child("marcaciones").child("allmarcaciones");

        Query query = usersdRef.orderByChild("keyficharDate").equalTo(idFIchajeData);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                Fichar informe = null;
                for (DataSnapshot ds : snapshot.getChildren()) {

                    informe = ds.getValue(Fichar.class);
                    Log.i("holerd", "aqui se encontro un cuadro muestreo......");

                    if (informe != null) {


                        break;
                    }


                }


                if (informe == null) {
                    fichaOnlineAndUpdate(informe, false);

                } else {
                    fichaOnlineAndUpdate(informe, true);

                }



            }

            @Override
            public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {
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

                    RealtimDatabase.updateMarcacion(ActivityReconocimientoF.this,fichar, fichar.getKeyWhereLocalizeObjec() );


                }else{

                    Toast.makeText(this, "Ya haz fichado anteriomente ", Toast.LENGTH_SHORT).show();
                }

            }

            else if(Fichar.tipoFichanSelecionadoCurrent==Fichar.FICHAJE_SALIDA){

                if(fichar.getHoraSalidaMilliseconds()==0){

                    fichar.setHoraSalidaMilliseconds(new Date().getTime());
                    showFichaje(time, "Salida", R.drawable.hora_salida);

                    RealtimDatabase.updateMarcacion(ActivityReconocimientoF.this,fichar, fichar.getKeyWhereLocalizeObjec() );



                }else{

                    Toast.makeText(this, "Ya haz fichado anteriomente ", Toast.LENGTH_SHORT).show();

                }

            }


        }


        else {

            fichar = new Fichar(idEncontrado, new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date().getTime()), Utils.maiLEmpleadorGlOBAL);


            if(Fichar.tipoFichanSelecionadoCurrent==Fichar.FICHAJE_ENTRADA){
                if (fichar.getEntradaMilliseconds() == 0) {

                    fichar.setEntradaMilliseconds(new Date().getTime());
                    showFichaje(time, "Entrada", R.drawable.hora_entrada);
                    RealtimDatabase.addMarcacion(ActivityReconocimientoF.this,fichar);


                }


            }

        }
    }

    void marcamosFichaje(String keyCurrentUser){

      //  Fichar fichar= Fichar.hashMapAllFicharRegistros

        //obtenomos el hasmap de fichaje de este empleado ,usando el id como key de prefrences...


        Fichar ficharObjec=null;

        HashMap<String, Fichar>hashMapFichajeRegistros= SharePref.loadMapPreferencesFichaje(keyCurrentUser);

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


                  //  Toast.makeText(this, "Hora de entrada Agregada", Toast.LENGTH_SHORT).show();


                }else{  //el user ya ficho

                    Log.i("fichnadodata","la hora de entrada es difrente de cero, ya hemos fichado antes ");

                    sheetBootomShowPorqueYaMarco(true);


                   // Toast.makeText(this, "Ya marcaste la hora de entrada", Toast.LENGTH_SHORT).show();


                }






        }

        else if(Fichar.tipoFichanSelecionadoCurrent==Fichar.FICHAJE_INCIO_COMIDA){
             if(ficharObjec.getHoraInicioComidaMilliseconds()==0){

                    ficharObjec.setHoraInicioComidaMilliseconds(new Date().getTime());

                    Log.i("fichnadodata","la hora de entrada es cero fichamos ahora");

                   // Toast.makeText(this, "fichamos hora de inicio comida", Toast.LENGTH_SHORT).show();
                 showFichaje(time,"Inicio Comida",R.drawable.hora_inicio_comida);


                }else{  //el user ya ficho

                    Log.i("fichnadodata","ya estaba fichada FICHAJE_INCIO_COMIDA");


                    Toast.makeText(this, "Ya marcaste la hora incio comida", Toast.LENGTH_SHORT).show();


                }


        }

        else if(Fichar.tipoFichanSelecionadoCurrent==Fichar.FICHAJE_FIN_COMIDA){

                if(ficharObjec.getHoraFinComidaMilliseconds()==0){

                    ficharObjec.setHoraFinComidaMilliseconds(new Date().getTime());

                    Log.i("fichnadodata","la hora de FICHAJE_FIN_COMIDA ahora la cambiamos");


                    showFichaje(time,"Fin de  Comida",R.drawable.fin_comida);

                    //Toast.makeText(this, "Hora de entrada Agregada", Toast.LENGTH_SHORT).show();


                }else{  //el user ya ficho
                    Log.i("fichnadodata","ya fichamos fin comida ");

                    Toast.makeText(this, "Ya marcaste fin de comida", Toast.LENGTH_SHORT).show();

            }
        }



        else if(Fichar.tipoFichanSelecionadoCurrent==Fichar.FICHAJE_SALIDA){
                if(ficharObjec.getHoraSalidaMilliseconds()==0){

                    ficharObjec.setHoraSalidaMilliseconds(new Date().getTime());

                    Log.i("fichnadodata","la hora de salida la ficahmos ahora");

                    showFichaje(time,"Salida",R.drawable.hora_salida);

                    //  Toast.makeText(this, "Hora de entrada Agregada", Toast.LENGTH_SHORT).show();

                }

                else{  //el user ya ficho

                    Log.i("fichnadodata","la hora de salida ya estaba agregada..");

                    sheetBootomShowPorqueYaMarco(false);


                  //  Toast.makeText(this, "Ya marcaste la hora de entrada", Toast.LENGTH_SHORT).show();


            }

        }


        //guardamos o remplzamos
        hashMapFichajeRegistros.put(new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date().getTime()),ficharObjec);


        SharePref.saveMapFichaje(hashMapFichajeRegistros,keyCurrentUser);


    }



    Bitmap drawResizedBitmap(final Bitmap src) {

        final Bitmap dst = Bitmap.createBitmap(INPUT_SIZE, INPUT_SIZE, Bitmap.Config.ARGB_8888);

        Display getOrient = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int orientation = Configuration.ORIENTATION_UNDEFINED;
        Point point = new Point();
        getOrient.getSize(point);
        int screen_width = point.x;
        int screen_height = point.y;
        Log.d(TAG, String.format("screen size (%d,%d)", screen_width, screen_height));

        if (screen_width < screen_height) {
            orientation = Configuration.ORIENTATION_PORTRAIT;
            // mScreenRotation = 0;
        } else {
            orientation = Configuration.ORIENTATION_LANDSCAPE;
            //   mScreenRotation = 0;
        }


        final float minDim = Math.min(src.getWidth(), src.getHeight());

        final Matrix matrix = new Matrix();


        final float translateX = -Math.max(0, (src.getWidth() - minDim) / 2);
        final float translateY = -Math.max(0, (src.getHeight() - minDim) / 2);
        matrix.preTranslate(translateX, translateY);

        final float scaleFactor = dst.getHeight() / minDim;
        matrix.postScale(scaleFactor, scaleFactor);


        final Canvas canvas = new Canvas(dst);
        canvas.drawBitmap(src, matrix, null);
        return dst;

    }

    private class detectAsync extends AsyncTask<Bitmap, Void, String> {
        ProgressDialog dialog = new ProgressDialog(ActivityReconocimientoF.this);

        Bitmap sourceBitmap;

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Detecting face...");
            dialog.setCancelable(false);
            dialog.show();
            super.onPreExecute();
        }

        protected String doInBackground(Bitmap... bp) {

            List<VisionDetRet> results;
            sourceBitmap = bp[0];

            startTimeLocally = System.currentTimeMillis();
            results = FaceRecognizer.getInstance().detect(sourceBitmap);



            String msg = null;
            if (results.size() == 0) {
                msg = "No face was detected or face was too small. Please select a different image";
            } else if (results.size() > 1) {
                msg = "More than one face was detected. Please select a different image";
            } else {
                return null;
            }
            return msg;
        }

        protected void onPostExecute(String result) {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
                if (result != null) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(ActivityReconocimientoF.this);
                    builder1.setMessage(result);
                    builder1.setCancelable(true);
                    AlertDialog alert11 = builder1.create();
                    alert11.show();

                } else {
                    if (sourceBitmap != null) {
                        new recognizeAsync().execute(sourceBitmap);
                    }

                }

            }

        }

    }

    private class recognizeAsync extends AsyncTask<Bitmap, Void, ArrayList<String>> {
        ProgressDialog dialog = new ProgressDialog(ActivityReconocimientoF.this);
        Handler handler = new Handler();
        private int mScreenRotation = 0;

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Reconociendo...");
            dialog.setCancelable(false);
            dialog.show();
            super.onPreExecute();
        }



        protected ArrayList<String> doInBackground(Bitmap... bp) {


            /**vamos a pasarle un bitmap para crear */


            //comparamos

          //  List <VisionDetRet>resultd=

            if (bp[0] != null) {


                List<VisionDetRet> results;

                results = FaceRecognizer.getInstance().recognize(bp[0]);

                final long endTime = System.currentTimeMillis();
                Log.d(TAG, "Time cost: " + (endTime - startTimeLocally) / 1000f + " sec");


                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        Toast.makeText(getApplicationContext(), "Time cost: " + (endTime - startTimeLocally) / 1000f + " sec", Toast.LENGTH_LONG).show();


                    }
                });



                ArrayList<String> names = new ArrayList<>();

                Log.i("solapina","el results size es : "+results.size());


                for (VisionDetRet n : results) {  //aqui buscamos

                    String getLabelStr = n.getLabel(); ///creo que es una conincidencia..
                    Log.i("solapina","el getLabelStr es : "+getLabelStr);
                  //  idEncontrado=getLabelStr;
                    //aqui buscamos este empleado id....

                    getLabelStr = getLabelStr.replaceAll("[0-9]", "");
                    names.add(getLabelStr);


                    ///si el label


                     if(!getLabelStr.equals("")){ //asi obtenemos solo la primera coincidencia
                         idEncontrado=getLabelStr;

                         break;
                     }
                }



                HashSet<String> hashSet = new HashSet<String>();
                hashSet.addAll(names);
                names.clear();
                names.addAll(hashSet);


                return names;
            } else {
                Toast.makeText(getApplicationContext(), "Bitmap is null", Toast.LENGTH_LONG).show();
                return null;
            }


        }


        protected void onPostExecute(ArrayList<String> names) {

            if (names != null) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(ActivityReconocimientoF.this);
                    builder1.setMessage(getResultMessage(names));
                    builder1.setCancelable(true);
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
            }


        }

    }


    private void showFichaje(String timeFichaje, String tipoFichaje, int drawable){


            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ActivityReconocimientoF.this);

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


    private void sheetBootomShowPorqueYaMarco(boolean esHoraEntrada){

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ActivityReconocimientoF.this);

        bottomSheetDialog.setContentView(R.layout.bottom_sheet_incorrect);

        TextView txtAdviserxxcc=bottomSheetDialog.findViewById(R.id.txtAdviserxxcc);
        ImageView imagSrc=bottomSheetDialog.findViewById(R.id.imagSrc);
        Button btnLoTengo=bottomSheetDialog.findViewById(R.id.btnLoTengo);
        bottomSheetDialog.setCancelable(false);
        btnLoTengo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
                bottomSheetDialog.dismiss();



            }
        });


        imagSrc.setImageResource(R.drawable.ic_baseline_timer_off_24);


        if(esHoraEntrada){
            txtAdviserxxcc.setText("Ya marcaste la hora de entrada del dia de hoy ");

        }else{
            txtAdviserxxcc.setText("Ya marcaste la hora de salida del dia de hoy ");

        }


        bottomSheetDialog.show();

    }


    void takePickCamera() {

        Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Start the activity with camera_intent, and request pic id
        startActivityForResult(camera_intent, 100);
        //startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_CODE); // OLD WAY

    }




    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Match the request 'pic id with requestCode
        if (requestCode == 100) {
            // BitMap is data structure of image file which store the image in memory
            Bitmap photo = (Bitmap) data.getExtras().get("data");

         //   Bitmap bp = drawResizedBitmap(BitmapFactory.decodeByteArray(data, 0, data.length));
            new detectAsync().execute(photo);

            // Set the image in imageview for display
            //click_image_id.setImageBitmap(photo);
        }
    }






}


