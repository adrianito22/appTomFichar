package com.tiburela.android.controlAsistencia.demo.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.tiburela.android.controlAsistencia.demo.R;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.concurrent.Executor;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class ActivityBiometrick extends AppCompatActivity {
    SecretKey secretKey = null;
    BiometricPrompt.CryptoObject cryptoObject = null;

    private static final String CHARSET_NAME = "UTF-8";

    Cipher cipher = null;

    private static final String TRANSFORMATION = KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/"
            + KeyProperties.ENCRYPTION_PADDING_PKCS7;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biometric);

        // Initialising msgtext and loginbutton
        TextView msgtex = findViewById(R.id.msgtext);
        final Button loginbutton = findViewById(R.id.login);

        // creating a variable for our BiometricManager
        // and lets check if our user can use biometric sensor or not
        BiometricManager biometricManager = androidx.biometric.BiometricManager.from(this);
        switch (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK)) {

            // this means we can use biometric sensor
            case BiometricManager.BIOMETRIC_SUCCESS:
                msgtex.setText("Puede utilizar el sensor de huellas dactilares para iniciar sesión");
              //  msgtex.setTextColor(Color.parseColor("#fafafa"));
                break;

            // this means that the device doesn't have fingerprint sensor
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                msgtex.setText("Este dispositivo no tiene un sensor de huellas dactilares.");
                loginbutton.setVisibility(View.GONE);
                break;

            // this means that biometric sensor is not available
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                msgtex.setText("El sensor biométrico no está disponible actualmente");
                loginbutton.setVisibility(View.GONE);
                break;

            // this means that the device doesn't contain your fingerprint
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                msgtex.setText("Su dispositivo no tiene huella digital guardada, verifique su configuración de seguridad");
                loginbutton.setVisibility(View.GONE);
                break;
        }
        // creating a variable for our Executor
        Executor executor = ContextCompat.getMainExecutor(this);
        // this will give us result of AUTHENTICATION



        final BiometricPrompt biometricPrompt = new BiometricPrompt(ActivityBiometrick.this, executor, new BiometricPrompt.AuthenticationCallback() {


            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);



            }

            // THIS METHOD IS CALLED WHEN AUTHENTICATION IS SUCCESS
            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(getApplicationContext(), "Acceso concedido", Toast.LENGTH_SHORT).show();
                loginbutton.setText("Acceso concedido");


            }
            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        });
        // creating a variable for our promptInfo
        // BIOMETRIC DIALOG





        final BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder().setTitle("PC peli")
                .setDescription("Use su huella digital para acceder ").setNegativeButtonText("Cancelar").build();





        final BiometricPrompt biometricPrompt2 = new BiometricPrompt(ActivityBiometrick.this, executor, new BiometricPrompt.AuthenticationCallback(){


            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);


                try {
                    secretKey = createKey();
                } catch (NoSuchProviderException e) {
                    throw new RuntimeException(e);
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                } catch (InvalidAlgorithmParameterException e) {
                    throw new RuntimeException(e);
                }
                try {
                    cipher = Cipher.getInstance(TRANSFORMATION);
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                } catch (NoSuchPaddingException e) {
                    throw new RuntimeException(e);
                }
                try {
                    cipher.init(Cipher.ENCRYPT_MODE, secretKey);
                } catch (InvalidKeyException e) {
                    throw new RuntimeException(e);
                }


            }

            // THIS METHOD IS CALLED WHEN AUTHENTICATION IS SUCCESS
            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
               // Toast.makeText(getApplicationContext(), "Login Exitoso", Toast.LENGTH_SHORT).show();
                loginbutton.setText("Login Exitoso");

                Intent intencion= new Intent(ActivityBiometrick.this,PaswordChange.class);
                intencion.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intencion);
                finish();

                // startActivity(new Intent(ActivityBiometrick.this,PaswordChange.class));



            }
            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        });


        try {
            cryptoObject = new BiometricPrompt.CryptoObject(getEncryptCipher(createKey()));
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        } catch (NoSuchProviderException e) {
            throw new RuntimeException(e);
        } catch (InvalidAlgorithmParameterException e) {
            throw new RuntimeException(e);
        }
     //   biometricPrompt2.authenticate(promptInfo, cryptoObject);




        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   biometricPrompt.authenticate(promptInfo);

                biometricPrompt2.authenticate(promptInfo, cryptoObject);


            }
        });
    }

    private SecretKey createKey() throws NoSuchProviderException, NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        String algorithm = KeyProperties.KEY_ALGORITHM_AES;
        String provider = "AndroidKeyStore";
        KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm, provider);
        KeyGenParameterSpec keyGenParameterSpec = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            keyGenParameterSpec = new KeyGenParameterSpec.Builder("MY_KEY", KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .setUserAuthenticationRequired(true)
                    .build();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            keyGenerator.init(keyGenParameterSpec);
        }
        return keyGenerator.generateKey();
    }

    private Cipher getEncryptCipher(Key key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        String algorithm = KeyProperties.KEY_ALGORITHM_AES;
        String blockMode = KeyProperties.BLOCK_MODE_CBC;
        String padding = KeyProperties.ENCRYPTION_PADDING_PKCS7;
        Cipher cipher = Cipher.getInstance(algorithm+"/"+blockMode+"/"+padding);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void saveUserPin(String pin) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException, BadPaddingException, IllegalBlockSizeException {
        // encrypt the password
        try {
             secretKey = createKey();
         //   Cipher cipher = Cipher.getInstance(TRANSFORMATION);
          //  cipher.init(Cipher.ENCRYPT_MODE, secretKey);


            byte[] encryptionIv = cipher.getIV();
            byte[] passwordBytes = pin.getBytes(CHARSET_NAME);
            byte[] encryptedPasswordBytes = cipher.doFinal(passwordBytes);
            String encryptedPassword = Base64.encodeToString(encryptedPasswordBytes, Base64.DEFAULT);
           Log.i("simenos","el cirpeter password es  "+encryptedPassword);


            // store the login data in the shared preferences
            // only the password is encrypted, IV used for the encryption is stored

          /*
            SharedPreferences.Editor editor = BaseActivity.prefs.edit();
            editor.putString("password", encryptedPassword);
            editor.putString("encryptionIv", Base64.encodeToString(encryptionIv, Base64.DEFAULT));
            editor.apply();
            */


        } catch (InvalidAlgorithmParameterException e) {
            throw new RuntimeException(e);
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        } catch (BadPaddingException e) {
            throw new RuntimeException(e);
        } catch (NoSuchProviderException e) {
            throw new RuntimeException(e);
        }
    }


}
