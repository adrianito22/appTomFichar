package com.tiburela.android.controlAsistencia.demo.Activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Handler;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.tiburela.android.controlAsistencia.demo.R;
import com.tiburela.android.controlAsistencia.demo.Utils.FaceRecognizer;
import com.tiburela.android.controlAsistencia.demo.Utils.FileUtils;
import com.tzutalin.dlib.Constants;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ChooserActivity extends AppCompatActivity {


    Handler handler = new Handler();

    String[] permissions = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    String[] array = {"Recognize","Train images","Add Persons"};
    CardView cardViewTrainFaces,cardViewRecognize,cardViewAddPersons;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chooser);
        checkPermissions();

        Toolbar toolbar = (Toolbar)findViewById(R.id.chooserToolbar);
        cardViewTrainFaces = (CardView)findViewById(R.id.trainImagesCardView);
        cardViewRecognize = (CardView)findViewById(R.id.cardViewRecognize);
        cardViewAddPersons = (CardView)findViewById(R.id.cardViewAddPersons);


        cardViewTrainFaces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new initRecAsync().execute();
            }
        });

        cardViewRecognize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChooserActivity.this, ActivityReconocimientoF.class);
                startActivity(intent);
            }
        });

        cardViewAddPersons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChooserActivity.this, AddPerson.class);
                startActivity(intent);
            }
        });

        toolbar.setTitle("Face Recognition Project");
        setSupportActionBar(toolbar);


        File folder = new File(Constants.getDLibDirectoryPath());
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdirs();
        }
        if (success) {
            File image_folder = new File(Constants.getDLibImageDirectoryPath());
            image_folder.mkdirs();
            if (!new File(Constants.getFaceShapeModelPath()).exists()) {
                FileUtils.copyFileFromRawToOthers(ChooserActivity.this, R.raw.shape_predictor_5_face_landmarks, Constants.getFaceShapeModelPath());
            }
            if (!new File(Constants.getFaceDescriptorModelPath()).exists()) {
                FileUtils.copyFileFromRawToOthers(ChooserActivity.this, R.raw.dlib_face_recognition_resnet_model_v1, Constants.getFaceDescriptorModelPath());
            }
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





    private class initRecAsync extends AsyncTask<Void, Void, Void>
    {

        ProgressDialog dialog = new ProgressDialog(ChooserActivity.this);

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
                    FileUtils.copyFileFromRawToOthers(ChooserActivity.this, R.raw.shape_predictor_5_face_landmarks, Constants.getFaceShapeModelPath());
                }
                if (!new File(Constants.getFaceDescriptorModelPath()).exists()) {
                    FileUtils.copyFileFromRawToOthers(ChooserActivity.this, R.raw.dlib_face_recognition_resnet_model_v1, Constants.getFaceDescriptorModelPath());
                }
            }



            final long startTime = System.currentTimeMillis();

            changeProgressDialogMessage(dialog, "Training people...");
            FaceRecognizer.getInstance().train();

            final long endTime = System.currentTimeMillis();

            Log.d("TimeCost", "Time cost: " + (endTime - startTime) / 1000f + " sec");

            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),"Time cost: "+ (endTime - startTime) / 1000f + " sec",Toast.LENGTH_LONG).show();
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


}
