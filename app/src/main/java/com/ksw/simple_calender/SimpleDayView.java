package com.ksw.simple_calender;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.OverScroller;

public class SimpleDayView extends View {
    private OverScroller m_scroller;
    private float startX;
    private float startY;
    private float accumulatedX;
    private float accumulatedY;

    final static int HORIZONTAL_BOTTOM = 0;
    final static int HORIZONTAL_CENTER = 1;
    final static int HORIZONTAL_TOP = 2;

    int m_state;

    private float bottomY;
    private float centerY;
    private float topY;

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

        m_state = HORIZONTAL_BOTTOM;
        SimpleCalenderView.setListener(new SimpleCalenderView.CalendarTouchListener() {
            @Override
            public void onDayClick(DateAttr dateClicked) {
                dateClicked.getYear();
                dateClicked.getMonth();
                dateClicked.getDay();

                m_state = HORIZONTAL_CENTER;
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
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
            return true;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return super.dispatchTouchEvent(event);
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
                if (m_state == HORIZONTAL_BOTTOM) {
                    startY = bottomY;
                }
                else if (m_state == HORIZONTAL_CENTER) {
                    m_state = HORIZONTAL_BOTTOM;
                    startY = bottomY;
                }
                else if (m_state == HORIZONTAL_TOP) {
                    m_state = HORIZONTAL_CENTER;
                    startY = centerY;
                }
            }
            else {
                if (m_state == HORIZONTAL_BOTTOM) {
                    m_state = HORIZONTAL_CENTER;
                    startY = centerY;
                }
                else if (m_state == HORIZONTAL_CENTER) {
                    m_state = HORIZONTAL_TOP;
                    startY = topY;
                }
                else if (m_state == HORIZONTAL_TOP) {
                    m_state = HORIZONTAL_TOP;
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
        Paint  m_paint = new Paint();
        canvas.drawRect(10, 10, 30, 30, m_paint);

        
    }
}
