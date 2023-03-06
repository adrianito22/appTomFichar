package com.tiburela.android.controlAsistencia.demo.Activities;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.tiburela.android.controlAsistencia.demo.R;
import com.tiburela.android.controlAsistencia.demo.Utils.Utils;

public class ActivityLogin extends AppCompatActivity {
    private  FirebaseUser userGoogle;
    private FirebaseAuth mAuth;
   GoogleSignInClient mGoogleSignInClient;

    private final int RC_SIGN_IN=500;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        ImageView ivImage= findViewById(R.id.imagevAnim);
       // GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(ivImage);
        Glide.with(this)
                .load(R.drawable.timexx)
                .into(ivImage);





        FirebaseApp.initializeApp(ActivityLogin.this);

        inigoogleSigni();//iniciamos google account autentificacion


        LinearLayout layIniciarSesion=findViewById(R.id.layIniciarSesion);
        layIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                signInGoogle();

            }
        });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);

                Log.i("defugero", "firebaseAuthWithGoogle:" + account.getId());
                Log.i("defugero", "firebaseAuthWithGoogle:" + account.getDisplayName());


                if(task.isSuccessful()){  ///vamohaber tareaspendientes

                }


                firebaseAuthWithGoogle(account.getIdToken());
                Log.i("defugero","se jecuito el try");

            } catch (ApiException e) {

                Log.i("defugero","se produjo un error ");
                Log.i("defugero","se produjo un error es "+e);
                // Google Sign In failed, update UI appropriately
                Log.w("defugero", "Google sign in failed", e);
            }
        }
    }



    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Log.i("dataLogin","se ejecuto firebaseAuthWithGoogle()  succes");

                          userGoogle = mAuth.getCurrentUser();

                          Utils.maiLEmpleadorGlOBAL=userGoogle.getEmail();

                            startActivity(new Intent(ActivityLogin.this,MainActivity.class ));


                        }
                        else {
                            Log.i("dataLogin","se ejecuto firebaseAuthWithGoogle() else failure ");

                            Log.i("logingoogle","ocurrio un errro "+task.getException());

                        }
                    }
                });
    }


    private void inigoogleSigni(){

        mAuth = FirebaseAuth.getInstance();


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("809244264013-9f5u7cii0ds4ljau2d67cjodbpar3me5.apps.googleusercontent.com")

                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);




    }

    @Override
    protected void onStart() {
        super.onStart();

         userGoogle = mAuth.getCurrentUser();

        if(userGoogle!=null){

           Utils. maiLEmpleadorGlOBAL=userGoogle.getEmail();

            startActivity(new Intent(ActivityLogin.this,MainActivity.class ));

        }


    }



    private void signInGoogle() {



        Intent signInIntent =  mGoogleSignInClient.getSignInIntent();


        startActivityForResult(signInIntent, RC_SIGN_IN);
        Log.i("logingoogle","se puslo singin metodo2");



    }


}