package com.google.android.cameraview.demo.Activities;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.cameraview.demo.R;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_center);

        Thread myThread = null;

        TextView  textView3=findViewById(R.id.textView3);


        String output =
                ZonedDateTime.now ( ZoneId.of ( "Europe/Madrid" ) )
                        .format (
                                DateTimeFormatter.ofLocalizedDate ( FormatStyle.FULL )
                                        .withLocale ( new Locale ( "es" , "ES" ) )
                        )
                ;

         textView3.setText(output);


        // Parse the given date-time string into OffsetDateTime
       // OffsetDateTime odt = OffsetDateTime.parse(date);

        // Define the formatter for output in a custom pattern and in Spanish Locale
        //  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd MM, uuuu hh:mm a", new Locale("es", "ES"));

       // DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy", new Locale("es", "ES"));

        // Print instant using the defined formatter
        //String formatted = formatter.format(odt);


        //Log.i("sanaz","el value es "+formatted);

       // textView3.setText(date);



        Runnable runnable = new CountDownRunner();
        myThread= new Thread(runnable);
        myThread.start();

    }



    public void doWork() {
        runOnUiThread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            public void run() {
                try{
                    TextView txtCurrentTime=findViewById(R.id.lbltime);


                    String time = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());



                    //int hours = dt.;
                    //int minutes = dt.getMinutes();
                   // int seconds = dt.getSeconds();
                   // String curTime = hours + ":" + minutes + ":" + seconds;
                    txtCurrentTime.setText(time);



                 //   String strDateTime = "2020-09-10T20:00:00.000Z";


                }catch (Exception e) {}
            }
        });
    }


    class CountDownRunner implements Runnable{
        // @Override
        public void run() {
            while(!Thread.currentThread().isInterrupted()){
                try {
                    doWork();
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }catch(Exception e){
                }
            }
        }
}}