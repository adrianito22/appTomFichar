package com.tiburela.android.controlAsistencia.demo.fragments;

import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import com.tiburela.android.controlAsistencia.demo.R;
import com.tiburela.android.controlAsistencia.demo.Utils.RealtimDatabase;
import com.tiburela.android.controlAsistencia.demo.Utils.SharePref;
import com.tiburela.android.controlAsistencia.demo.Utils.Utils;
import com.tiburela.android.controlAsistencia.demo.customClass.EventDecorator;
import com.tiburela.android.controlAsistencia.demo.models.Fichar;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentCalendar#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentCalendar extends Fragment {

    ArrayList<Fichar>milistFichejeCurrentUser= new ArrayList<>();
    long desdeDateMillisecond=0;
    long hastaDateMillisecond=0;

    View view;

    LocalDate initiLocalDate = LocalDate.now();

    MaterialCalendarView calendarVIew;
    private String idCurrentSelected="";

    private TextView textDiasTrabajados;
    private TextView txtHorasTrabajadas;
     private TextView txtPromedioEntrada;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentCalendar() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentCalendar.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentCalendar newInstance(String param1, String param2) {
        FragmentCalendar fragment = new FragmentCalendar();
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
            idCurrentSelected = getArguments().getString("KEY_USER_SELECTED");

            Log.i("mishdgf","el id current es "+idCurrentSelected);


        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        view = inflater.inflate(R.layout.fragment_calendar, container, false);

           calendarVIew= view.findViewById(R.id.calendarView);

          textDiasTrabajados=view.findViewById(R.id.textDiasTrabajados);
          txtHorasTrabajadas=view.findViewById(R.id.txtHorasTrabajadas);
          txtPromedioEntrada=view.findViewById(R.id.txtPromedioEntrada);



         Calendar  calendar = Calendar.getInstance();
        // calendar.setTimeInMillis(System.currentTimeMillis());



        initiLocalDate = LocalDate.of(calendar.get(Calendar.YEAR), Utils.indiceMesAxctual+1, 1);

        Date date=   convertToDateViaInstant(initiLocalDate);
        calendar.setTime(date);
        desdeDateMillisecond=calendar.getTimeInMillis();

        LocalDate end = initiLocalDate.with(lastDayOfMonth());
        date=   convertToDateViaInstant(end);
        calendar.setTime(date);
        hastaDateMillisecond=calendar.getTimeInMillis();
        eventoCalendarview();
        dowloadMarcacionesByDateRange(desdeDateMillisecond,hastaDateMillisecond, Utils.miEmpleadoGlobal.getIdEmpleado());




         return view;
    }


    private void getFichajesEspecificMesAndDecorateDiasATRABAJADOS(String idUserEmpleado, int mesSelecionadoNum, int yearSelecionado  ){

        HashMap<String, Fichar> hashMapAllRegistrosThisUser= SharePref.loadMapPreferencesFichaje(idUserEmpleado);

        ArrayList<Fichar>lisFichar= new ArrayList<>();

        GregorianCalendar c = new GregorianCalendar();

         int diasTrabajados=0;
         long millisecondsTimeTrabajados=0;
        long millisecondsTimeTrabajoDia=0;


        for(Fichar ficharObjec: hashMapAllRegistrosThisUser.values()){
            c.setTimeInMillis(ficharObjec.getEntradaMilliseconds());
            int month = c.get(Calendar.MONTH)+1;
            int year = c.get(Calendar.YEAR);

            Log.i("especificmes","el month by millisecond es "+month+" el mes selecionado num es "+mesSelecionadoNum);
            Log.i("especificmes","el year by millisecond es "+year+" el year selecioando es "+yearSelecionado);

            Log.i("mishdgf","mes es "+month+" y el otro mes es "+mesSelecionadoNum);

            if (month == mesSelecionadoNum && year == yearSelecionado) {
                lisFichar.add(ficharObjec);

                diasTrabajados++;

                millisecondsTimeTrabajoDia= ficharObjec.getHoraSalidaMilliseconds()- ficharObjec.getEntradaMilliseconds();

                millisecondsTimeTrabajados=millisecondsTimeTrabajados+millisecondsTimeTrabajoDia;



                Log.i("mishdgf","AGREGAMOS UN DATA ");

            }
        }




        DateFormat dateFormat = new SimpleDateFormat("hh:mm");

        Log.i("ssssd","los dias trabajados del mes son  "+diasTrabajados);
        Log.i("ssssd","las horas trabajadas del mes son "+dateFormat.format(millisecondsTimeTrabajados));

        HashSet<CalendarDay> setDays = new HashSet<>();
        Calendar calendar = Calendar.getInstance();

        int mYear ;
        int mMonth;
        int mDay ;

         /**recorremos la lista de lisfichar que tenemos y creamos objtos calendar day*/
        for(Fichar ficharObjec: lisFichar){



            calendar.setTimeInMillis(ficharObjec.getEntradaMilliseconds());
            mYear = calendar.get(Calendar.YEAR);
            mMonth = calendar.get(Calendar.MONTH)+1;
            mDay   = calendar.get(Calendar.DAY_OF_MONTH);
            CalendarDay calendarDay=CalendarDay.from(mYear,mMonth,mDay);
            setDays.add(calendarDay);

        }

        // CalendarDay calDay2 = CalendarDay.from(2023,2,16);

       // setDays.add(calDay2);

        // setDays.add(new CalendarDay(2,2,2));
        int myColor = R.color.colorAccent;
        calendarVIew.addDecorator(new EventDecorator(myColor, setDays,getActivity()));


    }

    private void getFichajesEspecificMesAndDecorateDiasATRABAJADOSOnline( ){



        GregorianCalendar c = new GregorianCalendar();

        int diasTrabajados=0;
        long millisecondsTimeTrabajados=0;
        long millisecondsTimeTrabajoDia=0;
         long promedioEntrada=0;

        for(Fichar ficharObjec: milistFichejeCurrentUser){
            c.setTimeInMillis(ficharObjec.getEntradaMilliseconds());
           // int month = c.get(Calendar.MONTH)+1;
           // int year = c.get(Calendar.YEAR);

            diasTrabajados++;
            millisecondsTimeTrabajoDia= ficharObjec.getHoraSalidaMilliseconds()- ficharObjec.getEntradaMilliseconds();
            millisecondsTimeTrabajados=millisecondsTimeTrabajados+millisecondsTimeTrabajoDia;
            promedioEntrada=promedioEntrada+ficharObjec.getEntradaMilliseconds();


        }



        DateFormat dateFormat = new SimpleDateFormat("hh:mm");

        Log.i("ssssd","los dias trabajados del mes son  "+diasTrabajados);
        Log.i("ssssd","las horas trabajadas del mes son "+dateFormat.format(millisecondsTimeTrabajados));




        HashSet<CalendarDay> setDays = new HashSet<>();
        Calendar calendar = Calendar.getInstance();

        int mYear ;
        int mMonth;
        int mDay ;

        /**recorremos la lista de lisfichar que tenemos y creamos objtos calendar day*/
        for(Fichar ficharObjec: milistFichejeCurrentUser){

            calendar.setTimeInMillis(ficharObjec.getEntradaMilliseconds());
            mYear = calendar.get(Calendar.YEAR);
            mMonth = calendar.get(Calendar.MONTH)+1;
            mDay   = calendar.get(Calendar.DAY_OF_MONTH);
            CalendarDay calendarDay=CalendarDay.from(mYear,mMonth,mDay);
            setDays.add(calendarDay);

        }

        // CalendarDay calDay2 = CalendarDay.from(2023,2,16);

        // setDays.add(calDay2);

        // setDays.add(new CalendarDay(2,2,2));
        int myColor = R.color.colorAccent;
        calendarVIew.addDecorator(new EventDecorator(myColor, setDays,getActivity()));



        if(diasTrabajados>0){
         //   Log.i("ssssd","las promedioEntrada del mes son "+dateFormat.format(promedioEntrada/diasTrabajados));
            textDiasTrabajados.setText(String.valueOf(diasTrabajados));
            txtHorasTrabajadas.setText(dateFormat.format(millisecondsTimeTrabajados));

        }else{
            textDiasTrabajados.setText("0");
            txtHorasTrabajadas.setText("0");
        }


        if(promedioEntrada>0 && diasTrabajados>0){
            Log.i("ssssd","las promedioEntrada del mes son "+dateFormat.format(promedioEntrada/diasTrabajados));
           // textDiasTrabajados.setText(String.valueOf(diasTrabajados));
            txtPromedioEntrada.setText(dateFormat.format(promedioEntrada/diasTrabajados));


        }else{
            txtPromedioEntrada.setText("0");

        }


        //vamos con horas trabajadas...



    }



    private void dowloadMarcacionesByDateRange(long desdeFecha, long hastFecha,String idCurrentMarcaciones){


        Query query = RealtimDatabase.rootDatabaseReference.child("marcaciones").child("allmarcaciones").
                orderByChild("entradaMilliseconds").startAt(desdeFecha).endAt(hastFecha);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                milistFichejeCurrentUser= new ArrayList<>();


                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    Fichar fichar=ds.getValue(Fichar.class);
                    //agregamos solo los que no esten en esta lista..
                    if(fichar!=null){//creamos un objet
                        if(fichar.getFicharUserId().equals(idCurrentMarcaciones)) {

                            milistFichejeCurrentUser.add(fichar);

                        }

                    }

                }


                getFichajesEspecificMesAndDecorateDiasATRABAJADOSOnline();





            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Log.i("sliexsa","el error es "+error.getMessage());

            }
        });



    }
    public Date convertToDateViaInstant(LocalDate dateToConvert) {
        return java.util.Date.from(dateToConvert.atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());
    }


    private void eventoCalendarview(){
        calendarVIew.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay datex) {



                Calendar  calendar = Calendar.getInstance();
                // calendar.setTimeInMillis(System.currentTimeMillis());



                initiLocalDate = LocalDate.of(calendar.get(Calendar.YEAR), datex.getMonth(), 1);

                Date date=   convertToDateViaInstant(initiLocalDate);
                calendar.setTime(date);
                desdeDateMillisecond=calendar.getTimeInMillis();

                LocalDate end = initiLocalDate.with(lastDayOfMonth());
                date=   convertToDateViaInstant(end);
                calendar.setTime(date);
                hastaDateMillisecond=calendar.getTimeInMillis();
                eventoCalendarview();
                dowloadMarcacionesByDateRange(desdeDateMillisecond,hastaDateMillisecond, Utils.miEmpleadoGlobal.getIdEmpleado());

                 /**hacer una maracion hoy para ver como va esto*/

                 Log.i("solert","el mes selcioando ahora es "+datex.getMonth());


            }
        });

    }


}