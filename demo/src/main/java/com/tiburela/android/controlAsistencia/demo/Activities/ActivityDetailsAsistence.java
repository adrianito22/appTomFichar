package com.tiburela.android.controlAsistencia.demo.Activities;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.tiburela.android.controlAsistencia.demo.R;
import com.tiburela.android.controlAsistencia.demo.Utils.Utils;
import com.tiburela.android.controlAsistencia.demo.fragments.FragmentCalendar;
import com.tiburela.android.controlAsistencia.demo.fragments.FragmentList;

public class ActivityDetailsAsistence extends AppCompatActivity {


     ImageView btnBackButton;
     TextView txtNameHere;

    LinearLayout layoutCalendar;
    LinearLayout layoutLista;
    ImageView imgPickEmpleado;
    String keyUserSelected="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            keyUserSelected = extras.getString("KEY_USER_SELECTED");
            //The key argument here must match that used in the other activity
        }


        setContentView(R.layout.activity_details_asistence);


        layoutCalendar=findViewById(R.id.layoutCalendar);
        layoutLista=findViewById(R.id.layoutLista);
        imgPickEmpleado=findViewById(R.id.imgPickEmpleado);



        Glide.with(ActivityDetailsAsistence.this)
                .load(Utils.miEmpleadoGlobal.getUrlPickEmpleado())

                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.DATA)  //ESTABA EN ALL         //ALL or NONE as your requirementDiskCacheStrategy.DATA
                //.thumbnail(Glide.with(OfertsAdminActivity.context).load(R.drawable.enviado_icon))
                //.error(R.drawable.)
                //aqi cargamos una version lower
                .apply(RequestOptions.circleCropTransform())

                .circleCrop()
                .into(imgPickEmpleado);



        btnBackButton=findViewById(R.id.btnBackButton);
         btnBackButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {

                 finish();
             }

         });


         txtNameHere=findViewById(R.id.txtNameHere);
        txtNameHere.setText(Utils.miEmpleadoGlobal.getNombreYapellidoEmpleado());


        FragmentList fragment= new FragmentList();
        loadFragment(fragment);



        layoutCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentCalendar fragment= new FragmentCalendar();
                loadFragment(fragment);

                ///modificcomo el color de este layout
                layoutCalendar.setBackgroundResource(R.drawable.back_selector);
                layoutLista.setBackgroundResource(R.drawable.back_selector_no);







            }
        });


        layoutLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                FragmentList fragment= new FragmentList();
                loadFragment(fragment);

                ///modificcomo el color de este layout
                layoutCalendar.setBackgroundResource(R.drawable.back_selector_no);
                layoutLista.setBackgroundResource(R.drawable.back_selector);




            }
        });



    }



    private void loadFragment(Fragment fragment) {

        Bundle bundle = new Bundle();
        bundle.putString("KEY_USER_SELECTED", keyUserSelected);
        fragment.setArguments(bundle);


// create a FragmentManager
        FragmentManager fm = getSupportFragmentManager();
// create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
// replace the FrameLayout with new Fragment
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit(); // save the changes
    }



}