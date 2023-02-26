package com.google.android.cameraview.demo.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import com.google.android.cameraview.demo.R;
import com.google.android.cameraview.demo.customClass.EventDecorator;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;

import sun.bob.mcalendarview.MCalendarView;
import sun.bob.mcalendarview.vo.DateData;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentCalendar#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentCalendar extends Fragment {

    View view;


    MaterialCalendarView calendarVIew;


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
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        view = inflater.inflate(R.layout.fragment_calendar, container, false);

        calendarVIew=    view.findViewById(R.id.calendarView);

        decorateSomeDates();

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

    private void decorateSomeDates(){

        HashSet<CalendarDay> setDays = new HashSet<>();

        CalendarDay calDay = CalendarDay.from(2023,2,15);
        CalendarDay calDay2 = CalendarDay.from(2023,2,16);

        setDays.add(calDay);
        setDays.add(calDay2);

        // setDays.add(new CalendarDay(2,2,2));
        int myColor = R.color.colorAccent;
        calendarVIew.addDecorator(new EventDecorator(myColor, setDays,getActivity()));
    }


}