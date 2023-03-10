package com.tiburela.android.controlAsistencia.demo.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.tiburela.android.controlAsistencia.demo.R;
import com.tiburela.android.controlAsistencia.demo.Utils.RealtimDatabase;
import com.tiburela.android.controlAsistencia.demo.Utils.Utils;
import com.tiburela.android.controlAsistencia.demo.models.Configuracion;

public class PaswordChange extends AppCompatActivity {
    ImageView imgVback;
    Button btnGuardar;
    EditText ediPassWord1;
    EditText ediPassWord2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pasword_change);
        imgVback=findViewById(R.id.imgVback);
        btnGuardar=findViewById(R.id.btnGuardar);
        ediPassWord1=findViewById(R.id.ediPassWord1);
        ediPassWord2=findViewById(R.id.ediPassWord2);

        eventos();


    }

    private void eventos(){
        imgVback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });


        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(ediPassWord1.getText().toString().equals("")){  //primero no puede estar vacio
                    ediPassWord1.requestFocus();
                    ediPassWord1.setError("no puede estar vacio");

                    return;
                }

                if(ediPassWord2.getText().toString().equals("")){  //primero no puede estar vacio
                    ediPassWord2.requestFocus();
                    ediPassWord2.setError("no puede estar vacio");

                    return;
                }


                if(! ediPassWord1.getText().toString().equals(ediPassWord2.getText().toString())){  //si no son iguales
                    ediPassWord1.requestFocus();
                    ediPassWord2.requestFocus();
                    Toast.makeText(PaswordChange.this, "Contraseñas no son iguales", Toast.LENGTH_SHORT).show();

                    return;
                }


                if( ediPassWord1.getText().toString().length()<4   ){  //si no son iguales
                    ediPassWord1.requestFocus();
                    ediPassWord1.setError("Ingrese al menos 4 digitos ");
                    ediPassWord2.requestFocus();
                    Toast.makeText(PaswordChange.this, "Contraseña muy corta ", Toast.LENGTH_SHORT).show();

                    return;
                }



                //aqui si todo sale bien vamos a actualizar contrasena
                Utils.configuracionGlobalObject.setPassWord(ediPassWord1.getText().toString());
                RealtimDatabase .updateConfiguracionAndPasword(PaswordChange.this, Utils.configuracionGlobalObject);

                finish();


            }
        });

    }

}