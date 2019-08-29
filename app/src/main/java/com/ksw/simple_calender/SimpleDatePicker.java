package com.ksw.simple_calender;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class SimpleDatePicker extends LinearLayout {
    ArrayList<String> mWeekStr;
    DateAttr mDate;
    private static ChangeDateListener listener;

    SimpleScrollView mView;
    SimpleScrollView mHourView;
    SimpleScrollView mMinuteView;

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
        if (v == mView){
            ArrayList<DateAttr> list = DateEventManager.getInstance().getmDateList();
            DateAttr date = list.get(index);
            changeDate(date.getYear(), date.getMonth(), date.getDay());
        }

        if (v == mHourView){
            mDate.setHour(index);
            listener.onChangeDate(mDate);
        }

        if (v == mMinuteView){
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
        int y = today.getYear() + 1900;
        int m = today.getMonth() + 1;
        int d = today.getDate();
        int h = today.getHours();
        int mn = today.getMinutes() % 5;

        mDate = new DateAttr(y,m,d,h,mn);
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
                index = i / 5;
            }
        }

        final int width3 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, getResources().getDisplayMetrics());
        mMinuteView = new SimpleScrollView(context, minuteStr);
        mMinuteView.setIndex(index);
        addView(mMinuteView, 0, new ViewGroup.LayoutParams(width3, ViewGroup.LayoutParams.MATCH_PARENT));

        ArrayList<String> hourStr = new ArrayList<>();
        for (int i = 0; i < 24 ; ++i) {
            if (i < 10){
                hourStr.add("0" + i);
            }else
            {
                hourStr.add(i + "");
            }

            if (mDate.getHour() == i){
                index = i;
            }
        }

        final int width2 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, getResources().getDisplayMetrics());
        mHourView = new SimpleScrollView(context, hourStr);
        mHourView.setIndex(index);
        addView(mHourView, 0, new ViewGroup.LayoutParams(width2, ViewGroup.LayoutParams.MATCH_PARENT));


        mWeekStr = new ArrayList<String> (Arrays.asList(
                "일", "월", "화", "수", "목", "금", "토"
        ));

        ArrayList<String> dateStrList = new ArrayList<>();
        ArrayList<DateAttr> dateList = DateEventManager.getInstance().getmDateList();
        for (DateAttr it: dateList){
            String month = it.getMonth() + "";
            if (month.length() == 1) {
                month = "0" + month;
            }

            String day = it.getDay() + "";
            if (day.length() == 1){
                day = "0" + day;
            }

            dateStrList.add(/*it.getYear() + "년" +*/ month + " 월" + day + " 일 " + mWeekStr.get(it.getWeek()));
        }

        mView = new SimpleScrollView(context, dateStrList);
        mView.setIndex(DateEventManager.getInstance().getDateIndex(mDate.getYear(), mDate.getMonth(), mDate.getDay()));
        final int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 240, getResources().getDisplayMetrics());
        addView(mView, 0, new ViewGroup.LayoutParams(width, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    public void setScrollIndex(DateAttr date){
        mDate.copyTo(date);
        mView.setIndex(DateEventManager.getInstance().getDateIndex(mDate.getYear(), mDate.getMonth(), mDate.getDay()));
        mHourView.setIndex(Math.min(mDate.getHour(), 23));
        mMinuteView.setIndex(Math.min(mDate.getMinute() / 5, 55));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }
}
