package com.google.android.cameraview.demo.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.cameraview.demo.R;
import com.google.android.cameraview.demo.Utils.SharePref;
import com.google.android.cameraview.demo.customClass.EventDecorator;
import com.google.android.cameraview.demo.models.Fichar;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentCalendar#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentCalendar extends Fragment {

    View view;


    MaterialCalendarView calendarVIew;
    private String idCurrentSelected="";


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

        calendarVIew=    view.findViewById(R.id.calendarView);

         Calendar  calendar = Calendar.getInstance();
         calendar.setTimeInMillis(System.currentTimeMillis());


        Log.i("mishdgf","el mes es  "+calendar.get(Calendar.MONTH)+" Y  el year es "+calendar.get(Calendar.YEAR));


        getFichajesEspecificMesAndDecorateDiasATRABAJADOS(idCurrentSelected,calendar.get(Calendar.MONTH)+1,calendar.get(Calendar.YEAR));



        /*
        ArrayList<DateData> dates=new ArrayList<>();

        dates.add(new DateData(2022,04,26));
        dates.add(new DateData(2022,04,27));

        for(int i=0;i<dates.size();i++) {
            //mark multiple dates with this code.
            calendarVIew.markDate(dates.get(i).getYear(),dates.get(i).getMonth(),dates.get(i).getDay());
        }
*/


         return view;
       // return inflater.inflate(R.layout.fragment_calendar, container, false);
    }

    private void decorateSomeDatesDiasTrabajadosAndGetMoreData(){



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



}