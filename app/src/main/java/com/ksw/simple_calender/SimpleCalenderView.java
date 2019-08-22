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
    float m_width;
    float m_height;
    float m_weekHeight;

    private float startX;
    private float startY;

    float m_widthRatio;
    float m_heightRatio;

    private float prevX;
    private float prevY;

    private boolean bmoved;

    static final int WEEK = 7;
    static final float EVENT_GAP = 30.0f;
    static final int ITEM_SIZE = 7;
    boolean[][] weekList = null;

    PointF m_scrolloffset;
    float m_offsetX;

    int m_year;
    int m_month;
    int m_date;

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
        m_year = today.getYear();
        m_month = today.getMonth();
        m_date = today.getDate();
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
        m_height = getHeight();
        m_weekHeight = getHeight() / 20;

        drawWeekText(canvas);
        drawCalenderAttr(canvas);
    }

    @Override
    public void computeScroll() {
        if (m_scroller.computeScrollOffset()) {
            m_scrolloffset.x = m_scroller.getCurrX();
            Log.d("좌표 : ", m_scrolloffset.x + "");
            // m_scroller.getCurrY());
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
                float disX = event.getRawX() - prevX;
                float disY = event.getRawY() - prevY;

                if (Math.abs(disX) > Math.abs(disY)){
                    //offsetLeftAndRight((int) disX);
                    m_scrolloffset.x += disX;
                    invalidate();

                    if (Math.abs(m_scrolloffset.x) > getWidth() / 2) {
                        if (m_scrolloffset.x > 0){
                            startX = getWidth();
                        }
                        else{
                            startX = -getWidth();
                        }
                    }
                    Log.d("좌표 : ", m_scrolloffset.x + "");
                }

                prevX = event.getRawX();
                prevY = event.getRawY();
                break;
            case MotionEvent.ACTION_UP:
                int ex = (int) getX();
                int ey = (int) getY();
                int wx = (int) (ex - startX);
                int wy = (int) (ey - startY);
                m_scroller.startScroll(ex, ey, -wx, -wy);
                invalidate();
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }

        return true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        startX = getX();
        startY = getY();
    }

    private void drawWeekText(Canvas canvas) {
        final float testTextSize = m_weekHeight / 2;
        m_paint.setTextAlign(Paint.Align.CENTER);
        m_paint.setTextSize(testTextSize);
        float dayWidth = m_width / 7;
        float tempX = dayWidth / 2;

        ArrayList<String> weekStr = new ArrayList<String> (Arrays.asList(
               "일", "월", "화", "수", "목", "금", "토"
        ));

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
                if (i == 0 && j < day) {
                    continue;
                }

                if (num == date) {
                    drawMarkNumber(canvas, i, j);
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

            if (!isPos) {
                for (int j = startEventWeek; j < endEventWeek; ++j) {
                    weekList[j][i] = true;
                }

                return i;
            }
        }

        return -1;
    }

    private void drawScheduleLine(Canvas canvas, int weekIndex, int pos, int startEventWeek, int endEventWeek, DateEvent e) {
        float dayWidth = m_width / 7;
        float dayHeight = (m_height - m_weekHeight) / 5;

        float tempStartX = dayWidth * startEventWeek + m_offsetX;
        float tempEndX = dayWidth * (endEventWeek + 1) + m_offsetX;
        float tempY = dayHeight * weekIndex + EVENT_GAP * (pos + 1);

        m_paint.setColor(Color.BLUE);
        RectF rect = new RectF(tempStartX, tempY + m_weekHeight, tempEndX, tempY + m_weekHeight + EVENT_GAP);
        canvas.drawRoundRect(rect, 10, 10, m_paint);
        m_paint.setTextAlign(Paint.Align.LEFT);
        m_paint.setColor(Color.WHITE);
        canvas.drawText(e.getTitle(), tempStartX, tempY + m_weekHeight + EVENT_GAP, m_paint);
    }

    private void drawMarkNumber(Canvas canvas, int y, int x) {
        m_paint.setColor(Color.GREEN);
        m_paint.setAlpha(100);
        float dayWidth = m_width / 7;
        float dayHeight = (m_height - m_weekHeight) / 5;
        final float circleSize = 24f;
        float tempX = dayWidth / 2 + dayWidth * x;
        float tempY = circleSize / 2 + m_weekHeight + dayHeight * y;

        canvas.drawCircle( tempX + m_offsetX,  tempY, circleSize, m_paint);
    }

    private void drawDayNumber(Canvas canvas, int y, int x, int num) {
        if (x == 0) {
            m_paint.setColor(Color.RED);
        }
        else {
            m_paint.setColor(Color.BLACK);
        }

        final float testTextSize = 24f;
        m_paint.setTextAlign(Paint.Align.CENTER);
        m_paint.setTextSize(testTextSize);
        float dayWidth = m_width / 7;
        float dayHeight = (m_height - m_weekHeight) / 5;
        float tempX = dayWidth * x;
        float tempY = dayHeight * y;

        canvas.drawText(num +"", tempX + dayWidth / 2 + m_offsetX,
                tempY + m_weekHeight + testTextSize, m_paint);
    }

    private void drawCalenderBackLine(Canvas canvas) {
        m_paint.setColor(Color.GRAY);
        float dayWidth = m_width / 7;
        float dayHeight = (m_height - m_weekHeight) / 5;

        float tempX = dayWidth;
        float tempY = m_weekHeight;

        for (int i = 0; i < 6; ++i)
        {
            canvas.drawLine(tempX + m_offsetX, 0, tempX + m_offsetX, m_height,  m_paint);
            tempX += dayWidth;
        }

        for (int i = 0; i < 5; ++i)
        {
            canvas.drawLine(m_offsetX, tempY, m_width, tempY,  m_paint);
            tempY += dayHeight;
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
