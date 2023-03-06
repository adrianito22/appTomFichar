package com.tiburela.android.controlAsistencia.demo.fragments;

import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tiburela.android.controlAsistencia.demo.R;
import com.tiburela.android.controlAsistencia.demo.Utils.RealtimDatabase;
import com.tiburela.android.controlAsistencia.demo.Utils.SharePref;
import com.tiburela.android.controlAsistencia.demo.Utils.Utils;
import com.tiburela.android.controlAsistencia.demo.adapters.AdapterMarcacione;
import com.tiburela.android.controlAsistencia.demo.models.Fichar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
    long desdeDateMillisecond=0;
    long hastaDateMillisecond=0;

    String idCurrentSelected;

    // TODO: Rename and change types of parameters

    Calendar calendar= new GregorianCalendar();
    LocalDate initiLocalDate = LocalDate.now();


    Spinner spinnerMeses;
     View view;

    public FragmentList() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
      //  spinnerMeses.setSelection(Utils.indiceMesAxctual);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.item_spinner, Utils.arrayMesSelecionado);

        spinnerMeses.setAdapter(adapter);
        spinnerMeses.setSelection(Utils.indiceMesAxctual);
        Log.i("sopresa","el indice slecioando onstart es  "+Utils.indiceMesAxctual);

        spinnerMeses.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");


            //    Utils. indiceMesAxctual =i+1;

                  // int indice=i+;


                initiLocalDate =LocalDate.of(calendar.get(Calendar.YEAR), i+1, 1);

                Date date=   convertToDateViaInstant(initiLocalDate);
                calendar.setTime(date);
                desdeDateMillisecond=calendar.getTimeInMillis();

                LocalDate end = initiLocalDate.with(lastDayOfMonth());
                date=   convertToDateViaInstant(end);
                calendar.setTime(date);
                hastaDateMillisecond=calendar.getTimeInMillis();



                Log.i("spinnerss","in first show list va desde "+dateFormat.format(desdeDateMillisecond)+" hasta "+dateFormat.format(hastaDateMillisecond));
                dowloadMarcacionesByDateRange(desdeDateMillisecond,hastaDateMillisecond, Utils.miEmpleadoGlobal.getIdEmpleado());


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


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



        // Utils.indiceMesAxctual =calendar.get(Calendar.MONTH);


        //String[] datos = new String[] {"C#", "Java", "Python", "R", "Go"};



    return view;

    }

    private void dowloadMarcacionesByDateRange(long desdeFecha, long hastFecha,String userID){

        Query query = RealtimDatabase.rootDatabaseReference.child("marcaciones").child("allmarcaciones").
                orderByChild("entradaMilliseconds").startAt(desdeFecha).endAt(hastFecha);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ArrayList<Fichar> list= new ArrayList<>();

                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    Fichar fichar=ds.getValue(Fichar.class);
                    //agregamos solo los que no esten en esta lista..
                    if(fichar!=null){  //creamos un objet
                        Log.i("spinnerss","el ficha user id es "+fichar.getFicharUserId()+" comparado con "+userID);


                        if(fichar.getFicharUserId().equals(userID)){
                            list.add(fichar);


                        }


                    }

                }


                Log.i("spinnerss","el size de lista es  "+list.size());


                setDataRecyclerView(list);



                ///  setAdapaterDataAndShow(listReport);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Log.i("sliexsa","el error es "+error.getMessage());

            }
        });



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



    public Date convertToDateViaInstant(LocalDate dateToConvert) {
        return java.util.Date.from(dateToConvert.atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());
    }
}