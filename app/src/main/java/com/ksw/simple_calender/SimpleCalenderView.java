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
    private OverScroller m_scroller;
    Paint m_paint;

    private float startX;
    private float startY;

    float m_widthRatio;
    float m_heightRatio;

    private float prevX;
    private float prevY;

    static final int ITEM_SIZE = 7;
    static final int WEEK = 7;

    boolean[][] weekList = null;

    // scroll val
    PointF m_scrolloffset;
    float m_offsetX;

    int m_year;
    int m_month;
    int m_date;

    int m_yearToday;
    int m_monthToday;
    int m_dateToday;

    // draw Size Val
    private float m_width;
    private float m_height;
    private float m_weekHeight;
    private float m_eventGap;
    private float m_eventBoxSize;
    private float m_circleSize;
    private float m_testTextSize;
    private float m_weekTextSize;
    private float m_dayWidth;
    private float m_dayHeight;

    private ArrayList<String> weekStr;

    private static CalendarTouchListener listener;
    public interface CalendarTouchListener {
        public void onDayClick(DateAttr dateClicked);
    }

    static void setListener(CalendarTouchListener l) {
        listener = l;
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
        m_scroller = new OverScroller(context, new LinearInterpolator());
        m_scrolloffset = new PointF();
        m_widthRatio = 1.0f;
        m_heightRatio = 1.0f;

        Date today = new Date();
        m_yearToday = m_year = today.getYear();
        m_monthToday = m_month = today.getMonth();
        m_dateToday = m_date = today.getDate();

        weekStr = new ArrayList<String> (Arrays.asList(
                "일", "월", "화", "수", "목", "금", "토"
        ));


    }

    @Override
    public void computeScroll() {
        if (m_scroller.computeScrollOffset()) {
            m_scrolloffset.x = m_scroller.getCurrX();
            Log.d("좌표 : ", m_scrolloffset.x + "");
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
                prevX = x;
                prevY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                // 이동량
                float disX = event.getRawX() - prevX;
                float disY = event.getRawY() - prevY;

                if (Math.abs(disX) > Math.abs(disY)){
                    m_scrolloffset.x += disX;
                    invalidate();

                    if (Math.abs(m_scrolloffset.x) > getWidth() / 3) {
                        if (m_scrolloffset.x > 0){
                            startX = getWidth();
                        }
                        else{
                            startX = -getWidth();
                        }
                    }
                    else {
                        startX = 0;
                    }

                    Log.d("좌표 : ", m_scrolloffset.x + "");
                }

                prevX = event.getRawX();
                prevY = event.getRawY();
                break;
            case MotionEvent.ACTION_UP:
                if (m_scrolloffset.x == 0) {
                    selectCalender(event.getX(), event.getY());
                }
                else {
                    if (startX > 0){
                        m_scrolloffset.x -= getWidth();
                        DateAttr date = new DateAttr(m_year, m_month,m_date,0,0);
                        DateAttr date2 = date.getPrevMonth();
                        m_year = date2.getYear();
                        m_month = date2.getMonth();
                        m_date = 1;
                    }
                    else if (startX < 0) {
                        m_scrolloffset.x += getWidth();
                        DateAttr date = new DateAttr(m_year, m_month,m_date,0,0);
                        DateAttr date2 = date.getNextMonth();
                        m_year = date2.getYear();
                        m_month = date2.getMonth();
                        m_date = 1;
                    }

                    int ex = (int)m_scrolloffset.x;
                    int ey = (int)getY();
                    int wx = (int)ex;
                    int wy = (int)ey;
                    m_scroller.startScroll(ex, ey, -wx, -wy);
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
        float dayWidth = m_width / 7;
        float dayHeight = (m_height - m_weekHeight) / 5;
        Date myDate = new Date(m_year, m_month, 1);
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

                if (w <= x && x <= nw && h + m_weekHeight <= y && y <= nh + m_weekHeight) {
                    // num;
                    listener.onDayClick(new DateAttr(m_year, m_month, num, 0, 0));
                    m_date = num;
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
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (m_paint == null) {
            m_paint = new Paint();
        }

        if (weekList == null) {
            weekList = new boolean[WEEK][ITEM_SIZE];
        }

        m_width = getWidth();
        m_height = getHeight() * m_heightRatio;
        m_weekHeight = getHeight() / 20 * m_heightRatio;

        m_weekTextSize = m_weekHeight / 2 * (2 - m_heightRatio);

        m_dayWidth = m_width / 7;
        m_dayHeight = (m_height - m_weekHeight) / 5;
        m_testTextSize = m_dayHeight / 10 * (2 - m_heightRatio); // 12.5
        m_circleSize = m_dayHeight / 8; //
        m_eventGap = m_dayHeight / 40;
        m_eventBoxSize = m_dayHeight / 8;

        drawWeekText(canvas);
        drawCalenderAttr(canvas);
    }

    private void drawWeekText(Canvas canvas) {
        m_paint.setTextAlign(Paint.Align.CENTER);
        m_paint.setTextSize(m_weekTextSize);
        float dayWidth = m_width / 7;
        float tempX = dayWidth / 2;

        m_paint.setColor(Color.RED);
        canvas.drawText(weekStr.get(0), tempX,  m_weekHeight * 9 / 10, m_paint);

        m_paint.setColor(Color.BLACK);
        for (int i = 1; i < 7; ++i)
        {
            tempX += dayWidth;
            canvas.drawText(weekStr.get(i), tempX, m_weekHeight * 9 / 10, m_paint);
        }
    }

    private void drawCalenderAttr(Canvas canvas) {
        DateAttr d = new DateAttr(m_year, m_month, m_date, 0,0);
        DateAttr pd = d.getPrevMonth();
        DateAttr nd = d.getNextMonth();

        m_offsetX = m_scrolloffset.x - getWidth();
        drawMonth(canvas, pd.getYear(), pd.getMonth(), pd.getDay());
        m_offsetX = m_scrolloffset.x;
        drawMonth(canvas, m_year, m_month, m_date);
        m_offsetX = m_scrolloffset.x + getWidth();
        drawMonth(canvas, nd.getYear(), nd.getMonth(), nd.getDay());
    }

    private void drawMonth(Canvas canvas, int year, int month, int date) {
        Date myDate = new Date(year, month, 1);
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
                if (year == m_yearToday && month == m_monthToday && num == m_dateToday) {
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
                Arrays.fill(weekList[j], false);
            }

            DateAttr sWeek = new DateAttr(year + 1900, month + 1, startWeek, 0, 0);
            DateAttr eWeek = new DateAttr(year + 1900, month + 1, endWeek, 23, 59);

            ArrayList<DateEvent> ret = mngr.rangeCutEvent(sWeek.getDateTime(), eWeek.getDateTime());

            for (DateEvent e : ret) {
                int startEventWeek;
                int endEventWeek;
                if (i == 0) {
                    startEventWeek = day + e.getStart().getDay();
                    endEventWeek = day + e.getEnd().getDay();
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
            for (int j = startEventWeek; j < endEventWeek; ++j) {
                if (weekList[j][i]) {
                    isPos = true;
                    break;
                }
            }

            if (! isPos) {
                for (int j = startEventWeek; j < endEventWeek; ++j) {
                    weekList[j][i] = true;
                }

                return i;
            }
        }

        return -1;
    }

    private void drawScheduleLine(Canvas canvas, int weekIndex, int pos, int startEventWeek, int endEventWeek, DateEvent e) {
        float tempStartX = m_dayWidth * startEventWeek + m_offsetX;
        float tempEndX = m_dayWidth * (endEventWeek + 1) + m_offsetX;
        float tempY = m_dayHeight * weekIndex + (m_eventGap + m_eventBoxSize) * (pos + 1);

        m_paint.setColor(Color.BLUE);
        RectF rect = new RectF(tempStartX, tempY + m_weekHeight, tempEndX, tempY + m_weekHeight + m_eventBoxSize);
        canvas.drawRoundRect(rect, 10, 10, m_paint);
        m_paint.setTextAlign(Paint.Align.LEFT);
        m_paint.setTextSize(m_testTextSize - m_eventGap);
        m_paint.setColor(Color.WHITE);
        canvas.drawText(e.getTitle(), tempStartX + 3, tempY + m_weekHeight + m_eventBoxSize - m_eventGap, m_paint);
    }

    private void drawMarkNumber(Canvas canvas, int y, int x, int c) {
        m_paint.setColor(c);
        m_paint.setAlpha(100);
        float tempX = m_dayWidth / 2 + m_dayWidth * x;
        float tempY = m_circleSize / 2 + m_weekHeight + m_dayHeight * y;

        canvas.drawCircle( tempX + m_offsetX,  tempY, m_circleSize, m_paint);
    }

    private void drawDayNumber(Canvas canvas, int y, int x, int num) {
        if (x == 0) {
            m_paint.setColor(Color.RED);
        }
        else {
            m_paint.setColor(Color.BLACK);
        }

        m_paint.setTextAlign(Paint.Align.CENTER);
        m_paint.setTextSize(m_testTextSize);
        float tempX = m_dayWidth * x;
        float tempY = m_dayHeight * y;

        canvas.drawText(num +"", tempX + m_dayWidth / 2 + m_offsetX,
                tempY + m_weekHeight + m_testTextSize, m_paint);
    }

    private void drawCalenderBackLine(Canvas canvas) {
        m_paint.setColor(Color.GRAY);
        float tempX = m_dayWidth;
        float tempY = m_weekHeight;

        for (int i = 0; i < 6; ++i) {
            canvas.drawLine(tempX + m_offsetX, 0, tempX + m_offsetX, m_height,  m_paint);
            tempX += m_dayWidth;
        }

        for (int i = 0; i < 5; ++i) {
            canvas.drawLine(m_offsetX, tempY, m_width, tempY,  m_paint);
            tempY += m_dayHeight;
        }
    }

    /**
     * 캘린더 크기 비율
     * @param widthRatio
     * @param heightRatio
     */
    public void setCalendarSize(float widthRatio, float heightRatio) {
        m_heightRatio = heightRatio;
        m_widthRatio = widthRatio;
    }
}
