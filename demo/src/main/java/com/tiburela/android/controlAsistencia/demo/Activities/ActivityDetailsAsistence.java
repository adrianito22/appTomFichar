package com.tiburela.android.controlAsistencia.demo.Activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tiburela.android.controlAsistencia.demo.R;
import com.tiburela.android.controlAsistencia.demo.Utils.Utils;
import com.tiburela.android.controlAsistencia.demo.fragments.FragmentCalendar;
import com.tiburela.android.controlAsistencia.demo.fragments.FragmentList;

public class ActivityDetailsAsistence extends AppCompatActivity {


     ImageView btnBackButton;
     TextView txtNameHere;

    LinearLayout layoutCalendar;
    LinearLayout layoutLista;

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





        btnBackButton=findViewById(R.id.btnBackButton);
         btnBackButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {

                 finish();
             }

         });


         txtNameHere=findViewById(R.id.txtNameHere);
        txtNameHere.setText(Utils.nameCurrentEmpleado);


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