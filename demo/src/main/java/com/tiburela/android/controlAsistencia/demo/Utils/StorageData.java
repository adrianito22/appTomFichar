package com.tiburela.android.controlAsistencia.demo.Utils;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.tiburela.android.controlAsistencia.demo.Activities.AddPerson;
import com.tiburela.android.controlAsistencia.demo.models.Empleado;

import java.io.File;

public class StorageData {
    public static StorageReference rootStorageReference;


    public static void initStorageReference()  {

        FirebaseStorage rootFirebaseStorage = FirebaseStorage.getInstance();


        rootStorageReference = rootFirebaseStorage.getReference();

    }

    public static void uploadObjecAndImagen( String nameImagen, Empleado empleado, Context context,File file) {

        /**SI HAY PROBELASM DE URI PERMISOS ASEGURARSE QUE EL URI CONTENGA UNA PROPIEDAD QUE HACER QUE LE DE PERMISOS DE
         * LECTURA ALGO AS..ESO EN INTENT AL SELECIONAR IMAGENES*/

        final StorageReference ImageFolder =  FirebaseStorage.getInstance().getReference().child("imagenes_empleados");

           // Uri uriImage  = Uri.parse(pathImageuri);

             /**interesnate aqui ya le pasmaos el file y ese file contiene ya permisos o algo asi....*/

            Uri uriImage=  Uri.fromFile(file);


            final StorageReference imagename = ImageFolder.child(nameImagen);

            imagename.putFile(uriImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imagename.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            Log.i("latypeimage","es succces bien");

                            String iconPathFirebase = uri.toString();

                            empleado.setUrlPickEmpleado(iconPathFirebase);

                            Log.i("latypeimage","info es on success  y path es  "+iconPathFirebase);

                            RealtimDatabase.addEmpleado(context,empleado);



                        }
                    });

                }


            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e)
                {

                    Log.i("latypeimage","la expecion es "+e.getMessage());

                    // Error, Image not uploaded
                    Toast.makeText(context, "Error " + e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });






    }

}
