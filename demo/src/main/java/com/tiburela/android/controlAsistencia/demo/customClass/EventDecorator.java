package com.tiburela.android.controlAsistencia.demo.customClass;


import android.content.Context;
import android.graphics.Color;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Collection;
import java.util.HashSet;

public class EventDecorator implements DayViewDecorator {
    private final int color;
    private final HashSet<CalendarDay> dates;
    private Context context;

    public EventDecorator(int color, Collection<CalendarDay> dates,Context context) {
        this.color = color;
        this.dates = new HashSet<>(dates);
        this.context=context;
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
       // view.addSpan(new DotSpan(20, Color.GREEN));

        view.addSpan(new ForegroundColorSpan(Color.BLACK));
        view.addSpan(new BackgroundColorSpan(Color.GREEN));

      // view.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.name, null));
        //view.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.name));
       // view.setBackgroundDrawable(Objects.requireNonNull(ContextCompat.getDrawable(getContext(), R.drawable.back_drawcc)));
       // view.setBackgroundDrawable(ContextCompat.getDrawable(context,R.drawable.back_draw));

    }
}
