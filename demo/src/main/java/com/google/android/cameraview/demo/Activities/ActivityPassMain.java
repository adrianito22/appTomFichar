package com.google.android.cameraview.demo.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.bumptech.glide.util.Util;
import com.google.android.cameraview.demo.R;
import com.google.android.cameraview.demo.Utils.Utils;
import com.hanks.passcodeview.PasscodeView;

public class ActivityPassMain extends AppCompatActivity {

    PasscodeView passcodeView;
    int activityDondeVamos=0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_main);

        Bundle extras = getIntent().getExtras();


        if (extras != null) {
            activityDondeVamos = extras.getInt("key");
            //The key argument here must match that used in the other activity
        }



        passcodeView = findViewById(R.id.passcodeview);
        passcodeView.setCorrectInputTip("Correcto");

        // to set length of password as here
        // we have set the length as 5 digits
        passcodeView.setPasscodeLength(4)
                // to set pincode or passcode
                .setLocalPasscode(Utils.PASSWORD_ADMIN)

                // to set listener to it to check whether
                // passwords has matched or failed
                .setListener(new PasscodeView.PasscodeViewListener() {
                    @Override
                    public void onFail() {
                        Log.i("sucnmerr","el pass es incorrecto ");

                        // to show message when Password is incorrect
                        Toast.makeText(ActivityPassMain.this, "Contraseña Incorrecta!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(String number) {


                        if(activityDondeVamos==Utils.GO_ActivityHorario){
                            startActivity(new Intent(ActivityPassMain.this, ActivityAllHorarios.class));

                        }
                        else if(activityDondeVamos==Utils.GO_AddPerson){
                            startActivity(new Intent(ActivityPassMain.this, AddPerson.class));


                        }

                        finish();

                        Log.i("sucnmerr","el pass es correcto hurra");

                        // here is used so that when password
                        // is correct user will be
                        // directly navigated to next activity
                        //Intent intent_passcode = new Intent(MainActivity.this, passcode_activity.class);
                        //startActivity(intent_passcode);
                    }
                });
    }
}