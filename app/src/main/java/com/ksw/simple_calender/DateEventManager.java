package com.ksw.simple_calender;
import android.content.Context;
import java.util.ArrayList;
import java.util.Date;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class DateEventManager {
    ArrayList<DateEvent> eventList;
    private static DateEventManager inst = null;
    CalenderDBHelper dbHelper;

    private DateEventManager(){}

    DateEventManager getInstance(){
        if (inst == null) {
            inst = new DateEventManager();
        }

        return inst;
    }

    void init(Context context){
        dbHelper = new CalenderDBHelper(context, "calendar.db", null, 1);
    }

    void addEvent(DateEvent event){
        // event list에 추가
        // DB에 추가
        eventList.add(event);
    }

    ArrayList<DateEvent> rangeEvent(long sdate, long edate) {
        ArrayList ret = new ArrayList<DateEvent>();

        for (DateEvent e: eventList) {
            if (e.getEnd().getDateTime() < sdate)continue;
            if (e.getStart().getDateTime() > edate) continue;

            ret.add(e);
        }

        return ret;
    }

    ArrayList<DateEvent> rangeCutEvent(long sdate, long edate) {
        ArrayList ret = new ArrayList<DateEvent>();

        for (DateEvent e: eventList) {
            if (e.getEnd().getDateTime() < sdate)continue;
            if (e.getStart().getDateTime() > edate) continue;
            /*
            int start = max(sday, e.getStart().getDay());
            int end = min(eday, e.getEnd().getDay());

            DateEvent newEvent = new DateEvent(e.getTitle(),
                    e.getContent(),
                    e.isbRepeat(),
                    start, end);

            ret.add(newEvent);
            */
        }

        return ret;
    }
}
