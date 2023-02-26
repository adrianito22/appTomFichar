package com.google.android.cameraview.demo.Activities;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.cameraview.demo.R;
import com.google.android.cameraview.demo.fragments.FragmentCalendar;
import com.google.android.cameraview.demo.fragments.FragmentList;

public class ActivityDetailsAsistence extends AppCompatActivity {

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




        FragmentList fragment= new FragmentList();
        loadFragment(fragment);



        layoutCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                FragmentCalendar fragment= new FragmentCalendar();
                loadFragment(fragment);

            }
        });


        layoutLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                FragmentList fragment= new FragmentList();
                loadFragment(fragment);

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