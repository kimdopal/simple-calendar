package com.ksw.simple_calender;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.OverScroller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

public class SimpleDayView extends View {
    private OverScroller m_scroller;
    private float startX;
    private float startY;
    private float accumulatedX;
    private float accumulatedY;

    final static int HORIZONTAL_BOTTOM = 0;
    final static int HORIZONTAL_CENTER = 1;
    final static int HORIZONTAL_TOP = 2;

    private int mState;

    private float bottomY;
    private float centerY;
    private float topY;

    int m_year;
    int m_month;
    int m_date;

    ArrayList<String> weekStr;

    private float m_BlockGap;
    private float m_width;
    private float m_height;
    private float m_textSize;
    private float m_BlockSize;
    private float m_TitleGap;
    Paint  m_paint;

    ArrayList<DateEvent> events;

    private OnItemClickListener listener;
    public interface OnItemClickListener {
        public void onClickedItem(DateEvent event);
    }

    public void setListener(OnItemClickListener l) {
        listener = l;
    }

    public SimpleDayView(Context context) {
        super(context);
        init(context);
    }

    public SimpleDayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SimpleDayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context) {
        m_scroller = new OverScroller(context, new LinearInterpolator(), 0, 0);
        accumulatedX = 0;
        accumulatedY = 0;

        mState = HORIZONTAL_BOTTOM;
        Date today = new Date();
        m_year = today.getYear() + 1900;
        m_month = today.getMonth() + 1;
        m_date = today.getDate();

        weekStr = new ArrayList<String>(Arrays.asList(
                "일", "월", "화", "수", "목", "금", "토"
        ));

        SimpleCalenderView.setListener(new SimpleCalenderView.CalendarTouchListener() {
            @Override
            public void onDayClick(DateAttr dateClicked) {
                m_year = dateClicked.getYear();
                m_month = dateClicked.getMonth();
                m_date = dateClicked.getDay();

                mState = HORIZONTAL_CENTER;
                startY = centerY;
                startScroll();
            }
        });
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        startX = getX();
        startY = getY();

        DateEventManager mngr = DateEventManager.getInstance();
        DateAttr dayStart = new DateAttr(m_year, m_month, m_date, 0, 0);
        DateAttr dayEnd = new DateAttr(m_year, m_month, m_date, 23, 59);
        events = mngr.rangeCutEvent(dayStart.getDateTime(), dayEnd.getDateTime());

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        bottomY = getMeasuredHeight();
        centerY = getMeasuredHeight() / 2;
        topY = 0;
    }

    public void accumulateX(float x) {
        accumulatedX += x;
        if (Math.abs(accumulatedX) > getWidth() / 2){
            if (accumulatedX < 0){
            }
            else{
            }
        }
    }

    public void accumulateY(float y) {
        accumulatedY += y;
        if (Math.abs(accumulatedY) > getHeight() / 5)
        {
            if (accumulatedY > 0) {
                if (mState == HORIZONTAL_BOTTOM) {
                    startY = bottomY;
                }
                else if (mState == HORIZONTAL_CENTER) {
                    mState = HORIZONTAL_BOTTOM;
                    startY = bottomY;
                }
                else if (mState == HORIZONTAL_TOP) {
                    mState = HORIZONTAL_CENTER;
                    startY = centerY;
                }
            }
            else {
                if (mState == HORIZONTAL_BOTTOM) {
                    mState = HORIZONTAL_CENTER;
                    startY = centerY;
                }
                else if (mState == HORIZONTAL_CENTER) {
                    mState = HORIZONTAL_TOP;
                    startY = topY;
                }
                else if (mState == HORIZONTAL_TOP) {
                    mState = HORIZONTAL_TOP;
                    startY = topY;
                }
            }

            accumulatedY = 0.0f;
        }
    }

    public void setStartPos(float x, float y) {
        startX = x;
        startY = y;
    }

    public void startScroll() {
        int x = (int) getX();
        int y = (int) getY();
        int wx = (int) (x - startX);
        int wy = (int) (y - startY);
        m_scroller.startScroll(x, y, -wx, -wy);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (m_scroller.computeScrollOffset()) {
            //setX(m_scroller.getCurrX());
            setY(m_scroller.getCurrY());
            Log.d("좌표 : ", m_scroller.getCurrX() + " " + m_scroller.getCurrY());
            invalidate();
            ViewGroup viewGroup = (ViewGroup)getParent();
            if (viewGroup != null) {
                float resize = getHeight() - (getY());
                SimpleCalenderView view = (SimpleCalenderView)viewGroup.getChildAt(0);
                if (view != null) {
                    view.setCalendarSize(1.0f, Math.max(0.5f, 1.0f - resize / getHeight()));
                    view.invalidate();
                }
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        m_paint = new Paint();
        m_width = getWidth();
        m_height = getHeight();
        m_textSize = m_height / 30;
        m_TitleGap = m_textSize * 2;
        m_BlockGap = m_height / 50;
        m_BlockSize = m_height / 12;

        DateEventManager mngr = DateEventManager.getInstance();
        DateAttr dayStart = new DateAttr(m_year, m_month, m_date, 0, 0);
        DateAttr dayEnd = new DateAttr(m_year, m_month, m_date, 23, 59);
        events = mngr.rangeCutEvent(dayStart.getDateTime(), dayEnd.getDateTime());

        drawDay(canvas);
        drawTodoBlock(canvas);
    }

    private void drawTodoBlock(Canvas canvas) {
        m_BlockSize = Math.min(m_width / 12, m_width / events.size());

        Collections.sort(events);

        int idx = 0;
        for (DateEvent e : events) {
            drawBlock(canvas, e, idx);
            idx++;
        }
    }

    private void drawBlock(Canvas canvas, DateEvent e, int idx) {

        m_paint.setColor(Color.BLUE);
        RectF rect = new RectF(30,m_TitleGap + idx * (m_BlockGap + m_BlockSize), m_width - 30,
                m_TitleGap + (idx * (m_BlockGap + m_BlockSize) + m_BlockSize));
        canvas.drawRoundRect(rect, 10, 10, m_paint);
        m_paint.setColor(Color.WHITE);
        m_paint.setTextSize(m_textSize);
        canvas.drawText(e.getDateStr() + "    " + e.getTitle() , 100 , m_TitleGap + idx * (m_BlockSize + m_BlockGap) + m_BlockSize * 2 / 3, m_paint);
    }

    private void drawDay(Canvas canvas) {
        Date myDate = new Date(m_year - 1900, m_month - 1, 1);
        int day = myDate.getDay();
        int week = (day - 1 + m_date) % 7;
        m_paint.setTextSize(m_textSize);

        String month = m_month + "";
        if (month.length() == 1) {
            month = "0" + month;
        }

        String date = m_date + "";
        if (date.length() == 1){
            date = "0" + date;
        }

        canvas.drawText((m_year) + "-" + (month) + "-" + date
                + "(" + weekStr.get(week) + ")", m_textSize, m_textSize, m_paint);
    }

    DateEvent getPosItem(float x , float y){
        m_BlockSize = Math.min(m_width / 12, m_width / events.size());

        int idx = 0;
        for (DateEvent e : events) {
            float gap1 = m_TitleGap + idx * (m_BlockGap + m_BlockSize);
            float gap2 = m_TitleGap + (idx * (m_BlockGap + m_BlockSize) + m_BlockSize);
            if (gap1 < y && y < gap2){
                return e;
            }
            idx++;
        }

        return null;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                DateEvent dateEvent = getPosItem(event.getX(), event.getY());
                if (dateEvent != null){
                    listener.onClickedItem(dateEvent.getParent());
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }

        return true;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return super.dispatchTouchEvent(event);
    }

}
