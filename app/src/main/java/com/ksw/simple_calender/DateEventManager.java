package com.ksw.simple_calender;
import android.content.Context;
import java.util.ArrayList;
import java.util.Date;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class DateEventManager {
    ArrayList<DateEvent> eventList;
    ArrayList<DateAttr> dateList;
    private static DateEventManager inst = null;
    CalenderDBHelper dbHelper;

    private DateEventManager(){}

    public static DateEventManager getInstance(){
        if (inst == null) {
            inst = new DateEventManager();
        }

        return inst;
    }

    void init(Context context){
        dateList = new ArrayList<>();
        Date myDate = new Date(0, 0, 1);
        for (int i = 1900; i < 2100; ++i) {
            myDate.setYear(i - 1900);
            for (int j = 1; j <= 12 ; ++j){
                myDate.setMonth(j - 1);
                myDate.setDate(1);
                int day = myDate.getDay();
                myDate.setDate(32);
                int last =  32 - myDate.getDate();

                for (int k = 1; k <= last; ++k){
                    DateAttr newDate = new DateAttr(i, j, k, day);

                    day = (day + 1) % 7;
                    dateList.add(newDate);
                }
            }
        }

        eventList = new ArrayList<DateEvent>();
        dbHelper = new CalenderDBHelper(context, "calendar.db", null, 1);
    }

    // event list에 추가
    // TODO : DB에 추가
    void addEvent(DateEvent event){
        eventList.add(event);
    }

    void removeEvent(DateEvent event){
        eventList.remove(event);
    }

    void removeEvent(int index){
        eventList.remove(index);
    }

    void changeEvent(DateEvent fromEvent, DateEvent toEvent){
        fromEvent.setTitle(toEvent.getTitle());
        fromEvent.getStart().copyTo(toEvent.getStart());
        fromEvent.getEnd().copyTo(toEvent.getEnd());
    }

    public ArrayList<DateAttr> getDateList() {
        return dateList;
    }

    int getDateIndex(int year, int month, int date){
        int ret = -1;
        for (DateAttr it : dateList){
            ret++;
            if (it.getYear() != year) continue;
            if (it.getMonth() != month) continue;
            if (it.getDay() != date) continue;
            break;
        }

        return ret;
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

            long start = max(sdate, e.getStart().getDateTime());
            long end = min(edate, e.getEnd().getDateTime());

            DateEvent newEvent = new DateEvent(e.getTitle(),
                    e.getContent(),
                    e.isbRepeat(),
                    new DateAttr(start),
                    new DateAttr(end));
            newEvent.setParent(e);

            ret.add(newEvent);
        }

        return ret;
    }
}
