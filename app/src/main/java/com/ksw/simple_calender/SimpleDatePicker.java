package com.ksw.simple_calender;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class SimpleDatePicker extends LinearLayout {
    ArrayList<String> weekStr;
    DateAttr mDate;
    private static ChangeDateListener listener;

    SimpleScrollView view;
    SimpleScrollView hourView;
    SimpleScrollView minuteView;

    public interface ChangeDateListener {
        public void onChangeDate(DateAttr date);
    }

    static void setListener(ChangeDateListener l) {
        listener = l;
    }

    public void changeDate(int year, int month, int day){
        if (listener == null) return;
        mDate.setYear(year);
        mDate.setMonth(month);
        mDate.setDay(day);
        listener.onChangeDate(mDate);
    }

    public void changeIndex(SimpleScrollView v, int index){
        if (listener == null) return;
        if (v == view){
            ArrayList<DateAttr> list = DateEventManager.getInstance().getDateList();
            DateAttr date = list.get(index);
            changeDate(date.getYear(), date.getMonth(), date.getDay());
        }

        if (v == hourView){
            mDate.setHour(index);
            listener.onChangeDate(mDate);
        }

        if (v == minuteView){
            mDate.setMinute(index * 5);
            listener.onChangeDate(mDate);
        }
    }

    public SimpleDatePicker(Context context) {
        super(context);
        init(context);
    }

    public SimpleDatePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SimpleDatePicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        Date today = new Date();
        int y = today.getYear();
        int m = today.getMonth();
        int d = today.getDate();
        int h = today.getHours();
        int mn = today.getMinutes() % 5;

        mDate = new DateAttr(y + 1900,m + 1,d,h,mn);
        listener = null;

        ArrayList<String> minuteStr = new ArrayList<>();
        int index = 0;
        for (int i = 0; i < 60 ; i = i + 5) {
            if (i < 10){
                minuteStr.add("0" + i);
            }else
            {
                minuteStr.add(i+"");
            }
            if (mDate.getMinute() == i){
                index = i /5;
            }
        }

        final int width3 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, getResources().getDisplayMetrics());
        minuteView = new SimpleScrollView(context, minuteStr);
        minuteView.setIndex(index);
        addView(minuteView, 0, new ViewGroup.LayoutParams(width3, ViewGroup.LayoutParams.MATCH_PARENT));

        ArrayList<String> hourStr = new ArrayList<>();
        for (int i = 0; i < 24 ; ++i) {
            if (i < 10){
                hourStr.add("0" + i);
            }else
            {
                hourStr.add(i+"");
            }

            if (mDate.getHour() == i){
                index = i;
            }
        }

        final int width2 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, getResources().getDisplayMetrics());
        hourView = new SimpleScrollView(context, hourStr);
        hourView.setIndex(index);
        addView(hourView, 0, new ViewGroup.LayoutParams(width2, ViewGroup.LayoutParams.MATCH_PARENT));


        weekStr = new ArrayList<String> (Arrays.asList(
                "일", "월", "화", "수", "목", "금", "토"
        ));

        ArrayList<String> dateStrList = new ArrayList<>();
        ArrayList<DateAttr> dateList = DateEventManager.getInstance().getDateList();
        for (DateAttr it: dateList){
            String month = it.getMonth() + "";
            if (month.length() == 1) {
                month = "0" + month;
            }

            String day = it.getDay() + "";
            if (day.length() == 1){
                day = "0" + day;
            }

            dateStrList.add(/*it.getYear() + "년" +*/ month + " 월" + day + " 일 " + weekStr.get(it.getWeek()));
        }

        view = new SimpleScrollView(context, dateStrList);
        view.setIndex(DateEventManager.getInstance().getDateIndex(mDate.getYear(), mDate.getMonth(), mDate.getDay()));
        final int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 240, getResources().getDisplayMetrics());
        addView(view, 0, new ViewGroup.LayoutParams(width, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    public void setScrollIndex(DateAttr date){
        mDate.copyTo(date);
        view.setIndex(DateEventManager.getInstance().getDateIndex(mDate.getYear(), mDate.getMonth(), mDate.getDay()));
        hourView.setIndex(Math.min(mDate.getHour(), 23));
        minuteView.setIndex(Math.min(mDate.getMinute() / 5, 55));
    }
}
