package com.google.android.cameraview.demo.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.cameraview.demo.Activities.ActivityReportes;
import com.google.android.cameraview.demo.R;
import com.google.android.cameraview.demo.Utils.SharePref;
import com.google.android.cameraview.demo.Utils.Utils;
import com.google.android.cameraview.demo.adapters.AdapterAsistencePromedio;
import com.google.android.cameraview.demo.adapters.AdapterMarcacione;
import com.google.android.cameraview.demo.models.Fichar;
import com.google.android.cameraview.demo.models.PromedioAsistenceEmpleado;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentList extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

     private String idCurrentSelected="";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    Spinner spinnerMeses;
     View view;

    public FragmentList() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentList.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentList newInstance(String param1, String param2) {
        FragmentList fragment = new FragmentList();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

       /// return inflater.inflate(R.layout.fragment_list, container, false);

        view= inflater.inflate(R.layout.fragment_list,container,false);

         idCurrentSelected = getArguments().getString("KEY_USER_SELECTED");

        spinnerMeses=view.findViewById(R.id.spinnerMeses);


        //String[] datos = new String[] {"C#", "Java", "Python", "R", "Go"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.item_spinner, Utils.arrayMesSelecionado);

          spinnerMeses.setAdapter(adapter);
        spinnerMeses.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                 int year;

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

                     year = Year.now().getValue();
                }

                else{

                     year = Calendar.getInstance().get(Calendar.YEAR);
                }


                Log.i("spinnfg","el mes selected es "+(i+1)+" el id es "+idCurrentSelected+" el year es "+year);

                ArrayList<Fichar>currenListFichar=generaListMaracacionesEspecificMesAndUserID(idCurrentSelected,i+1,year);

                setDataRecyclerView(currenListFichar);



            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    return view;

    }





    private ArrayList<Fichar>generaListMaracacionesEspecificMesAndUserID(String idUserEmpleado, int mesSelecionadoNum, int yearSelecionado  ){

        HashMap<String, Fichar> hashMapAllRegistrosThisUser= SharePref.loadMapPreferencesFichaje(idUserEmpleado);

        ArrayList<Fichar>lisFichar= new ArrayList<>();
        GregorianCalendar c = new GregorianCalendar();


        for(Fichar ficharObjec: hashMapAllRegistrosThisUser.values()){
             c.setTimeInMillis(ficharObjec.getEntradaMilliseconds());
                int month = c.get(Calendar.MONTH)+1;
                int year = c.get(Calendar.YEAR);

                Log.i("especificmes","el month by millisecond es "+month+" el mes selecionado num es "+mesSelecionadoNum);
                Log.i("especificmes","el year by millisecond es "+year+" el year selecioando es "+yearSelecionado);

                if (month == mesSelecionadoNum && year == yearSelecionado) {
                    lisFichar.add(ficharObjec);
                }
        }


        return lisFichar;
    }



    private void setDataRecyclerView(ArrayList<Fichar> list){

        RecyclerView recylerVInformsAll=view.findViewById(R.id.recylerVInformsAll);

        Log.i("sizelists","el size de lista es "+list.size());
        TextView txtAdviserHere=view.findViewById(R.id.txtAdviserHereXX);

        if(list.size()==0){
            Log.i("sizelists","se ejecuto el if mostramos aviso");

            txtAdviserHere.setVisibility(TextView.VISIBLE);


        }


        else{
            Log.i("sizelists","else oluctamos aviso ");

            txtAdviserHere.setVisibility(TextView.GONE);


        }


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());

        AdapterMarcacione adapter = new AdapterMarcacione(getActivity(), list);
        recylerVInformsAll.setLayoutManager(layoutManager);
        recylerVInformsAll.setAdapter(adapter);



        adapter.setOnItemClickListener(new AdapterMarcacione.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {


                //   sheetBootomInforOptions(position);

                //  sheetBootomInforOptions(v.getTag(R.id.tagUniqueId1).toString(),v.getTag(R.id.tagUniqueId2).toString(),v.getTag(R.id.codigoProductor).toString());


                Log.i("elcickler","el click es llamado al secionar item fichaje");




            }
        });




    }




}