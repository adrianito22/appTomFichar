package com.tiburela.android.controlAsistencia.demo.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.tiburela.android.controlAsistencia.demo.R;


public class SplashScreen extends AppCompatActivity {
    ImageView imageView ;
    Animation fadeInAnimation;
 //   private IntroPref introPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // imageView = findViewById(R.id.imageView);


         //fadeInAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animation);
        // imageView.startAnimation(fadeInAnimation); //





        new Handler() .postDelayed(() -> {

            // Intent intent=new Intent (SplashScreen.this, Home_Tab.class);

           Intent intento=new Intent (SplashScreen.this, ActivityLogin.class);
           startActivity(intento);

        //    chekea();

            finish();
        },1500);





    }




}