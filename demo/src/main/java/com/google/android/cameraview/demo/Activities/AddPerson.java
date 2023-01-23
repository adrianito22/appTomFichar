

package com.google.android.cameraview.demo.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.developers.imagezipper.ImageZipper;
import com.google.android.cameraview.demo.R;
import com.google.android.cameraview.demo.Utils.FaceRecognizer;
import com.google.android.cameraview.demo.Utils.SharePref;
import com.google.android.cameraview.demo.Utils.Utils;
import com.google.android.cameraview.demo.models.Empleado;
import com.google.android.cameraview.demo.models.Fichar;
import com.tzutalin.dlib.Constants;
import com.tzutalin.dlib.VisionDetRet;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import in.myinnos.awesomeimagepicker.activities.AlbumSelectActivity;
import in.myinnos.awesomeimagepicker.helpers.ConstantsCustomGallery;
import in.myinnos.awesomeimagepicker.models.Image;


public class AddPerson extends AppCompatActivity {

    EditText et_name;
    TextView et_image;
    Button btn_select_image;
    int BITMAP_QUALITY = 100;
    File file;
    String TAG = "AddPerson";
    private Bitmap bitmap;
    private File destination = null;
    private String imgPath = null;
    private final int PICK_IMAGE_CAMERA = 1, PICK_IMAGE_GALLERY = 2;

    String[] permissions = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    ArrayList<File> fileArrayList = new ArrayList<>();
    ArrayList<Bitmap> bitmapArrayList = new ArrayList<>();

    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_person);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbarAddPeople);
        toolbar.setTitle("Agregar Persona");
        setSupportActionBar(toolbar);


        btn_select_image = (Button)findViewById(R.id.btn_select_image);

        et_name = (EditText)findViewById(R.id.et_name);
        et_image = (TextView) findViewById(R.id.et_image);

        btn_select_image.setOnClickListener(mOnClickListener);

        checkPermissions();


    }


    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_select_image:
                    selectImage();
                    break;

            }
        }
    };

    File capturedFile;
    Uri outPutfileUri;
    private void selectImage() {
        try {
            PackageManager pm = getPackageManager();
            int hasPerm = pm.checkPermission(Manifest.permission.CAMERA, getPackageName());


            if (hasPerm == PackageManager.PERMISSION_GRANTED)
            {

                if(!et_name.getText().toString().equals(""))
                {
                    final CharSequence[] options = {"Tomar Foto", "Elegir desde Galeria","Cancelar"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddPerson.this);
                    builder.setTitle("Selecione una opcion");
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int item) {
                            if (options[item].equals("Tomar Foto")) {
                                dialog.dismiss();

                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                capturedFile = new File(Environment.getExternalStorageDirectory(), "testing_photo.jpg");

                                outPutfileUri = FileProvider.getUriForFile(AddPerson.this,
                                        "com.google.android.cameraview.demo", //(use your app signature + ".provider" )
                                        capturedFile);

                                intent.putExtra  (MediaStore.EXTRA_OUTPUT, outPutfileUri) ;
                                startActivityForResult (intent, PICK_IMAGE_CAMERA) ;



                            }
                            else if (options[item].equals("Elegir desde Galeria"))
                            {
                                dialog.dismiss();
                                Intent intent = new Intent(AddPerson.this, AlbumSelectActivity.class);
                                intent.putExtra(ConstantsCustomGallery.INTENT_EXTRA_LIMIT, 5); // set limit for image selection
                                startActivityForResult(intent, ConstantsCustomGallery.REQUEST_CODE);

                            } else if (options[item].equals("Cancelar"))
                            {
                                dialog.dismiss();
                            }
                        }
                    });

                    builder.show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Por favor especifique un nombre",Toast.LENGTH_LONG).show();
                }

            }
            else
                Toast.makeText(this, "Error en permiso de camara", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Error en permiso de camara ", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_CAMERA && resultCode== RESULT_OK) {
            try {

              //  bitmap = (Bitmap) data.getExtras().get("data");

                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), outPutfileUri);


                Bitmap scaledBitmap = new ImageZipper(AddPerson.this).compressToBitmap(capturedFile);

                    if(scaledBitmap!=null)
                    {
                        new detectAsync().execute(scaledBitmap);
                    }

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),""+e.getMessage(),Toast.LENGTH_LONG).show();
            }
        }
        else if (requestCode == PICK_IMAGE_GALLERY && data!=null && resultCode== RESULT_OK) {
            Uri selectedImage = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);


                 file = new File(getRealPathFromURI(selectedImage));

                Bitmap scaledBitmap=new ImageZipper(AddPerson.this).compressToBitmap(file);

                if(scaledBitmap!=null)
                {
                    new detectAsync().execute(scaledBitmap);

                }



            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if (requestCode == ConstantsCustomGallery.REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            //The array list has the image paths of the selected images
            ArrayList<Image> images = data.getParcelableArrayListExtra(ConstantsCustomGallery.INTENT_EXTRA_IMAGES);



            // 5 selected

            fileArrayList.clear();

            for (int i = 0; i < images.size(); i++) {


                Uri selectedImage = Uri.fromFile(new File(images.get(i).path));
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);


                   Bitmap scaledBitmap=new ImageZipper(AddPerson.this).compressToBitmap(new File(images.get(i).path));

                    fileArrayList.add(new File(images.get(i).path));
                    bitmapArrayList.add(scaledBitmap);



                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            Toast.makeText(getApplicationContext(),bitmapArrayList.size()+" imagenes selecionadas",Toast.LENGTH_LONG).show();

           new detectAsyncMultipleImages().execute(bitmapArrayList);


        }
    }






    private String getRealPathFromURI(Uri contentURI) {
        String filePath;
        Cursor cursor = getApplicationContext().getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            filePath = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            filePath = cursor.getString(idx);
            cursor.close();
        }
        return filePath;
    }







    private class detectAsync extends AsyncTask<Bitmap, Void, String> {
        ProgressDialog dialog = new ProgressDialog(AddPerson.this);

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Detectando Rostro...");
            dialog.setCancelable(false);
            dialog.show();
            super.onPreExecute();
        }

        protected String doInBackground(Bitmap... bp) {

            List<VisionDetRet> results;
            results = FaceRecognizer.getInstance().detect(bp[0]);
            String msg = null;
            if (results.size()==0)
            {
               // msg = "No face was detected or face was too small. Please select a different image";
                msg = "Rostro no detectado o muy pequeño. Por favor selecione otra imagen";

            } else if (results.size() > 1) {
              //  msg = "More than one face was detected. Please select a different image";

                msg = "Se detectó más de un rostro. Seleccione una imagen diferente";

            } else {

                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bp[0].compress(Bitmap.CompressFormat.JPEG, BITMAP_QUALITY, bytes);
                FileOutputStream fo;
                try {
                    Long tsLong = System.currentTimeMillis() / 1000;
                    String ts = tsLong.toString();


                    destination = new File(Constants.getDLibImageDirectoryPath() +"/"+ et_name.getText().toString()+ts+".jpg");
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imgPath = destination.getAbsolutePath();
            }
            return msg;
        }

        protected void onPostExecute(String result) {
            if(dialog != null && dialog.isShowing()){
                dialog.dismiss();
                if (result!=null) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(AddPerson.this);
                    builder1.setMessage(result);
                    builder1.setCancelable(true);
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                    imgPath = null;
                    et_image.setText("");

                }

                else

                {
                    Log.i("simplerr", "se eejcuto else onpostexcejcute "+imgPath);

                    String [] array=imgPath.split("/");

                    String idunique=array[array.length-1];

                    Log.i("simplerr", "el id unico es "+idunique);

                    //OBTENEMOS LA LISTA DE EMPLEADOS Y SI ESTA VACIO SIGNIFCA QUE NO TENEMOS NINGUN EMPLEADO....
                    Fichar.hasMapAllEmpleados = SharePref.loadMapPreferencesEmpleados(SharePref.KEY_ALL_EMPLEADOS_Map);

                    //cremaos un objeto empleado //Adriano1674403660.jpg //ASI MAS O MNEOS ESTA
                  //  Empleado empleadoObject= new Empleado(et_name.getText().toString(),et_name.getText().toString()+ts+".jpg");
                    Empleado empleadoObject= new Empleado(et_name.getText().toString(),idunique);

                    Fichar.hasMapAllEmpleados.put(idunique,empleadoObject);

                    //gaurdamos en preferencias.
                    SharePref.saveMapEmpleados(Fichar.hasMapAllEmpleados,SharePref.KEY_ALL_EMPLEADOS_Map);

                    Toast.makeText(getApplicationContext(),"Usuario agregado exitosamente",Toast.LENGTH_LONG).show();

                    Utils.existeNewUserAdd=true;

                    finish();


                }


              //  enableSubmitIfReady();

            }

        }
    }



    private class detectAsyncMultipleImages extends AsyncTask<ArrayList<Bitmap>, Void, String> {
        ProgressDialog dialog = new ProgressDialog(AddPerson.this);

        Handler handler = new Handler();

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Detectando rostro...");
            dialog.setCancelable(false);
            dialog.show();
            super.onPreExecute();
        }

        protected String doInBackground(ArrayList<Bitmap>... bp) {
           // mFaceRec = new FaceRec(Constants.getDLibDirectoryPath());


            List<VisionDetRet> results;
            String msg = null;
           ArrayList<Bitmap> bitmapArr = bp[0];

            Log.i("simplerr", "se eejcuto doinbackgroudn ");


            for(int i=0;i<bitmapArr.size();i++)
            {
                results = FaceRecognizer.getInstance().detect(bitmapArr.get(i)); //aqui obtenemos el resutado y el mapa de bits

                if(results.size()==0)
                {
                  //  msg = "No face was detected or face was too small. Please select a different image";
                    msg = "Rostro no detectado o muy pequeño. Por favor selecione otra imagen";

                    Log.e("Face Detector", msg);
                }
                else if (results.size() > 1) {
                  //  msg = "More than one face was detected. Please select a different image";
                    msg = "Se detectó más de un rostro. Seleccione una imagen diferente";

                    Log.e("Face Detector", msg);
                }

                else
                {
                    // store it in new directory //lo guardamos en un nuevo directorio

                    Long tsLong = System.currentTimeMillis() / 1000;
                    String ts = tsLong.toString();


                    String targetPath = Constants.getDLibImageDirectoryPath() + "/" + et_name.getText().toString()+ts + ".jpg"; //target donde se guardara
                      //el target path es el id unico de cada usuario nuevo.....

                       /**aqui podemos agregar un nuevo empleado */

                    Log.i("simplerr", "se eejcuto este ");




                    //tambien podemos usar esa ruta para obtener la imagenn de cada user y mostrala en el recicler....
                    //eliminar users usando codigo secreto....
                    //...y asi...

                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();

                   bitmapArr.get(i).compress(Bitmap.CompressFormat.JPEG, BITMAP_QUALITY, bytes);
                    FileOutputStream fo;


                    try {
                        destination = new File(targetPath); //cremoas el destino

                        destination.createNewFile(); //creamos el file
                        fo = new FileOutputStream(destination); //exportamos aqui... el archivo
                        fo.write(bytes.toByteArray());
                        fo.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    catch (IOException e) {
                        e.printStackTrace();
                    }


                    imgPath = destination.getAbsolutePath();


                }
            }

            return msg;
        }

        protected void onPostExecute(String result) {
            if(dialog != null && dialog.isShowing()){
                dialog.dismiss();


                if (result!=null)
                {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(AddPerson.this);
                    builder1.setMessage(result);
                    builder1.setCancelable(true);
                    AlertDialog alert11 = builder1.create();
                    alert11.show();


                }

                else
                {


                   // Log.i("simplerr","se ejecuto else here "+);

                    Toast.makeText(getApplicationContext(),"Se agregaron exitosamente las imagenes",Toast.LENGTH_LONG).show();






                    finish();

                }

            }
            finish();
        }
    }



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

}
