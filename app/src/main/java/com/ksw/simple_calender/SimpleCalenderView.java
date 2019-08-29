package com.ksw.simple_calender;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.OverScroller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import static java.lang.Math.min;

public class SimpleCalenderView extends View {
    private OverScroller mScroller;
    Paint mPaint;

    private float startX;
    private float startY;

    float mWidthRatio;
    float mHeightRatio;

    private float mPrevX;
    private float mPrevY;

    static final int ITEM_SIZE = 7;
    static final int WEEK = 7;

    boolean[][] mWeekList = null;

    // scroll val
    PointF mScrolloffset;
    float mOffsetX;

    int mYear;
    int mMonth;
    int mDate;

    int mYearToday;
    int mMonthToday;
    int mDateToday;

    // draw Size Val
    private float mWidth;
    private float mHeight;
    private float mWeekHeight;
    private float mEventGap;
    private float mEventBoxSize;
    private float mCircleSize;
    private float mTestTextSize;
    private float mWeekTextSize;
    private float mDayWidth;
    private float mDayHeight;

    private ArrayList<String> mWeekStr;

    private static CalendarTouchListener listener;
    public interface CalendarTouchListener {
        public void onDayClick(DateAttr dateClicked);
    }

    static void setListener(CalendarTouchListener l) {
        listener = l;
    }

    private static CalendarMonthListener monthListener;
    public interface CalendarMonthListener {
        public void changeMonth(DateAttr date);
    }

    static void setListener(CalendarMonthListener l) {
        monthListener = l;
    }


    public SimpleCalenderView(Context context) {
        super(context);
        init(context);
    }

    public SimpleCalenderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SimpleCalenderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mScroller = new OverScroller(context, new LinearInterpolator());
        mScrolloffset = new PointF();
        mWidthRatio = 1.0f;
        mHeightRatio = 1.0f;

        Date today = new Date();
        mYearToday = mYear = today.getYear() + 1900;
        mMonthToday = mMonth = today.getMonth() + 1;
        mDateToday = mDate = today.getDate();
        mWeekStr = new ArrayList<String> (Arrays.asList(
                "일", "월", "화", "수", "목", "금", "토"
        ));
    }

    public void setDate(DateAttr date){
        mYear = date.getYear();
        mMonth = date.getMonth();
        mDate = date.getDay();
        if(monthListener != null){
            monthListener.changeMonth(date);
        }

        invalidate();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            mScrolloffset.x = mScroller.getCurrX();
            Log.d("좌표 : ", mScrolloffset.x + "");
            invalidate();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getRawX();
        float y = event.getRawY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPrevX = x;
                mPrevY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                // 이동량
                float disX = event.getRawX() - mPrevX;
                float disY = event.getRawY() - mPrevY;

                if (Math.abs(disX) > Math.abs(disY)){
                    mScrolloffset.x += disX;
                    invalidate();

                    if (Math.abs(mScrolloffset.x) > getWidth() / 3) {
                        if (mScrolloffset.x > 0){
                            startX = getWidth();
                        }
                        else{
                            startX = -getWidth();
                        }
                    }
                    else {
                        startX = 0;
                    }

                    Log.d("좌표 : ", mScrolloffset.x + "");
                }

                mPrevX = event.getRawX();
                mPrevY = event.getRawY();

                break;
            case MotionEvent.ACTION_UP:
                if (mScrolloffset.x == 0) {
                    selectCalender(event.getX(), event.getY());
                }
                else {
                    if (startX > 0){
                        mScrolloffset.x -= getWidth();
                        DateAttr date = new DateAttr(mYear, mMonth, mDate,0,0);
                        DateAttr prevMonth = date.getPrevMonth();
                        mYear = prevMonth.getYear();
                        mMonth = prevMonth.getMonth();
                        mDate = 1;

                        if(monthListener != null) monthListener.changeMonth(prevMonth);
                    }
                    else if (startX < 0) {
                        mScrolloffset.x += getWidth();
                        DateAttr date = new DateAttr(mYear, mMonth, mDate,0,0);
                        DateAttr nextMonth = date.getNextMonth();
                        mYear = nextMonth.getYear();
                        mMonth = nextMonth.getMonth();
                        mDate = 1;

                        if(monthListener != null){
                            monthListener.changeMonth(nextMonth);
                        }
                    }

                    int ex = (int) mScrolloffset.x;
                    int ey = (int)getY();
                    int wx = (int)ex;
                    int wy = (int)ey;
                    mScroller.startScroll(ex, ey, -wx, -wy);
                    Log.d("슬라이드 : ", ex+ " " + ey+ " " + wx+ " " + wy );
                    invalidate();
                }

                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }

        return true;
    }

    private void selectCalender(float x, float y) {
        float dayWidth = mWidth / 7;
        float dayHeight = (mHeight - mWeekHeight) / 5;
        Date myDate = new Date(mYear - 1900, mMonth - 1, 1);
        int day = myDate.getDay();
        myDate.setDate(32);
        int last =  32 - myDate.getDate();
        int num = 1;
        for (int i = 0; i < 5 ; ++i) {
            for (int j = 0; j < 7; ++j) {
                if (i == 0 && j < day)continue;
                float w = dayWidth * j;
                float nw = dayWidth * (j + 1);
                float h = dayHeight * i;
                float nh = dayHeight * (i + 1);

                if (w <= x && x <= nw && h + mWeekHeight <= y && y <= nh + mWeekHeight) {
                    listener.onDayClick(new DateAttr(mYear, mMonth, num, 0, 0));
                    mDate = num;
                    invalidate();

                    return;
                }

                if (num <= last) {
                    num++;
                }
                else{
                    return;
                }
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        startX = getX();
        startY = getY();

        DateAttr date = new DateAttr(mYear, mMonth, mDate,0,0);
        if (monthListener != null){
            monthListener.changeMonth(date);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mPaint == null) {
            mPaint = new Paint();
        }

        if (mWeekList == null) {
            mWeekList = new boolean[WEEK][ITEM_SIZE];
        }

        mWidth = getWidth();
        mHeight = getHeight() * mHeightRatio;
        mWeekHeight = getHeight() / 20 * mHeightRatio;

        mWeekTextSize = mWeekHeight / 2 * (2 - mHeightRatio);

        mDayWidth = mWidth / 7;
        mDayHeight = (mHeight - mWeekHeight) / 5;
        mTestTextSize = mDayHeight / 10 * (2 - mHeightRatio); // 12.5
        mCircleSize = mDayHeight / 8; //
        mEventGap = mDayHeight / 40;
        mEventBoxSize = mDayHeight / 8;

        drawWeekText(canvas);
        drawCalenderAttr(canvas);
    }

    private void drawWeekText(Canvas canvas) {
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTextSize(mWeekTextSize);
        float dayWidth = mWidth / 7;
        float tempX = dayWidth / 2;

        mPaint.setColor(Color.RED);
        canvas.drawText(mWeekStr.get(0), tempX,  mWeekHeight * 9 / 10, mPaint);

        mPaint.setColor(Color.BLACK);
        for (int i = 1; i < 7; ++i)
        {
            tempX += dayWidth;
            canvas.drawText(mWeekStr.get(i), tempX, mWeekHeight * 9 / 10, mPaint);
        }
    }

    private void drawCalenderAttr(Canvas canvas) {
        DateAttr d = new DateAttr(mYear, mMonth, mDate, 0,0);
        DateAttr pd = d.getPrevMonth();
        DateAttr nd = d.getNextMonth();

        mOffsetX = mScrolloffset.x - getWidth();
        drawMonth(canvas, pd.getYear(), pd.getMonth(), pd.getDay());
        mOffsetX = mScrolloffset.x;
        drawMonth(canvas, mYear, mMonth, mDate);
        mOffsetX = mScrolloffset.x + getWidth();
        drawMonth(canvas, nd.getYear(), nd.getMonth(), nd.getDay());
    }

    private void drawMonth(Canvas canvas, int year, int month, int date) {
        Date myDate = new Date(year - 1900, month - 1, 1);
        int day = myDate.getDay();
        myDate.setDate(32);
        int last =  32- myDate.getDate();
        int num = 1;

        drawCalenderBackLine(canvas);

        for (int i = 0; i < 5 ; ++i) {
            for (int j = 0; j < 7; ++j) {
                if (i == 0 && j < day) continue;

                if (num == date) {
                    drawMarkNumber(canvas, i, j, Color.GRAY);
                }
                if (year == mYearToday && month == mMonthToday && num == mDateToday) {
                    drawMarkNumber(canvas, i, j, Color.GREEN);
                }
                if (num <= last) {
                    drawDayNumber(canvas, i, j, num);
                    num++;
                }
            }
        }

        drawSchedule(canvas, year, month, day, last);
    }

    private void drawSchedule(Canvas canvas, int year, int month, int day, int last) {
        DateEventManager mngr = DateEventManager.getInstance();

        // week
        int startWeek = 1;
        int endWeek = WEEK - day;
        for (int i= 0; i < 5; ++i) {
            for (int j = 0; j < 7; ++j) {
                Arrays.fill(mWeekList[j], false);
            }

            DateAttr sWeek = new DateAttr(year, month, startWeek, 0, 0);
            DateAttr eWeek = new DateAttr(year, month, endWeek, 23, 59);

            ArrayList<DateEvent> ret = mngr.rangeCutEvent(sWeek.getDateTime(), eWeek.getDateTime());

            for (DateEvent e : ret) {
                int startEventWeek;
                int endEventWeek;
                if (i == 0) {
                    startEventWeek = day - 1 + e.getStart().getDay();
                    endEventWeek = day - 1 + e.getEnd().getDay();
                }
                else{
                    startEventWeek = e.getStart().getDay() - startWeek;
                    endEventWeek = e.getEnd().getDay() - startWeek;
                }

                int pos = getWeekPos(startEventWeek, endEventWeek);
                drawScheduleLine(canvas, i, pos, startEventWeek, endEventWeek, e);
            }

            startWeek = endWeek + 1;
            if (startWeek > last) break;
            endWeek += 7;
            endWeek = min(endWeek, last);
        }
    }

    private int getWeekPos(int startEventWeek, int endEventWeek) {
        for (int i = 0; i < ITEM_SIZE; ++i) {
            boolean isPos = false;
            for (int j = startEventWeek; j <= endEventWeek; ++j) {
                if (mWeekList[j][i]) {
                    isPos = true;
                    break;
                }
            }

            if (! isPos) {
                for (int j = startEventWeek; j <= endEventWeek; ++j) {
                    mWeekList[j][i] = true;
                }

                return i;
            }
        }

        return -1;
    }

    private void drawScheduleLine(Canvas canvas, int weekIndex, int pos, int startEventWeek, int endEventWeek, DateEvent e) {
        float tempStartX = mDayWidth * startEventWeek + mOffsetX;
        float tempEndX = mDayWidth * (endEventWeek + 1) + mOffsetX;
        float tempY = mDayHeight * weekIndex + (mEventGap + mEventBoxSize) * (pos + 1);

        mPaint.setColor(e.getColor());
        RectF rect = new RectF(tempStartX, tempY + mWeekHeight, tempEndX, tempY + mWeekHeight + mEventBoxSize);
        canvas.drawRoundRect(rect, 10, 10, mPaint);
        mPaint.setTextAlign(Paint.Align.LEFT);
        mPaint.setTextSize(mTestTextSize - mEventGap);
        mPaint.setColor(Color.WHITE);
        canvas.drawText(e.getTitle(), tempStartX + 3, tempY + mWeekHeight + mEventBoxSize - mEventGap, mPaint);
    }

    private void drawMarkNumber(Canvas canvas, int y, int x, int c) {
        mPaint.setColor(c);
        mPaint.setAlpha(100);
        float tempX = mDayWidth / 2 + mDayWidth * x;
        float tempY = mCircleSize / 2 + mWeekHeight + mDayHeight * y;

        canvas.drawCircle( tempX + mOffsetX,  tempY, mCircleSize, mPaint);
    }

    private void drawDayNumber(Canvas canvas, int y, int x, int num) {
        if (x == 0) {
            mPaint.setColor(Color.RED);
        }
        else {
            mPaint.setColor(Color.BLACK);
        }

        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTextSize(mTestTextSize);
        float tempX = mDayWidth * x;
        float tempY = mDayHeight * y;

        canvas.drawText(num +"", tempX + mDayWidth / 2 + mOffsetX,
                tempY + mWeekHeight + mTestTextSize, mPaint);
    }

    private void drawCalenderBackLine(Canvas canvas) {
        mPaint.setColor(Color.GRAY);
        float tempX = mDayWidth;
        float tempY = mWeekHeight;

        for (int i = 0; i < 6; ++i) {
            canvas.drawLine(tempX + mOffsetX, 0, tempX + mOffsetX, mHeight, mPaint);
            tempX += mDayWidth;
        }

        for (int i = 0; i < 5; ++i) {
            canvas.drawLine(mOffsetX, tempY, mWidth, tempY, mPaint);
            tempY += mDayHeight;
        }
    }

    /**
     * 캘린더 크기 비율
     * @param widthRatio
     * @param heightRatio
     */
    public void setCalendarSize(float widthRatio, float heightRatio) {
        mHeightRatio = heightRatio;
        mWidthRatio = widthRatio;
    }
}
