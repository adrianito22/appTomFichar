package com.google.android.cameraview.demo.Activities;

import android.annotation.SuppressLint;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.cameraview.demo.R;

public class ActivityHorario extends AppCompatActivity implements View.OnClickListener {


    TextInputEditText txtImputEntrada;
    TextInputEditText txtImputSalida;


   //DIAS SEMANA
   TextView txtLunes;
    TextView txtMartes;
    TextView txtMiercoles;
    TextView txtJueves;
    TextView txtViernes;
    TextView txtSabado;
    TextView txtDomingo;

    TextView txtNumsEmpleados;
    ImageView imgAddEmpleado;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horario);

        findviewids();

        txtImputEntrada.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                Log.i("misdatas","se presiono el click ");

                return false;

            }
        });


    }


    void findviewids(){
         txtImputEntrada=findViewById(R.id.txtImputEntrada);
         txtImputSalida=findViewById(R.id.txtImputEntrada);


        //DIAS SEMANA
         txtLunes=findViewById(R.id.txtImputEntrada);
         txtMartes=findViewById(R.id.txtImputEntrada);
         txtMiercoles=findViewById(R.id.txtImputEntrada);
         txtJueves=findViewById(R.id.txtImputEntrada);
         txtViernes=findViewById(R.id.txtImputEntrada);
         txtSabado=findViewById(R.id.txtImputEntrada);
         txtDomingo=findViewById(R.id.txtImputEntrada);

         txtNumsEmpleados=findViewById(R.id.txtImputEntrada);
         imgAddEmpleado=findViewById(R.id.txtImputEntrada);

    }


    @Override
    public void onClick(View view) {

    }
}