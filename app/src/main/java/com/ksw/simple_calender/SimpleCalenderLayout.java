package com.ksw.simple_calender;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.OverScroller;

public class SimpleCalenderLayout extends ViewGroup {

    private float prevX;
    private float prevY;

    private float startX;
    private float startY;
    private boolean bmoved;
    private boolean bdrawed;

    private SimpleCalenderView m_calendarView;
    private SimpleDayView m_dayView;

    public SimpleCalenderLayout(Context context) {
        super(context);
        init(context);
    }

    public SimpleCalenderLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SimpleCalenderLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        bmoved = false;
        bdrawed = false;
    }

    /**
     * Layout의 사이즈 지정하고 자식 뷰의 크기를 넘겨준다.
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        m_calendarView = (SimpleCalenderView) getChildAt(0);
        m_dayView = (SimpleDayView)getChildAt(1);

        int count = getChildCount();
        int maxHeight = 0;
        int maxWidth = 0;
        for (int i = 0; i < count; ++i) {
            View chlid = getChildAt(i);
            if (chlid.getVisibility() != GONE) {
                measureChild(chlid, widthMeasureSpec, heightMeasureSpec);
            }
        }

        maxWidth = getPaddingLeft() + getPaddingRight();
        maxHeight = getPaddingTop() + getPaddingBottom();
        Drawable drawable = getBackground();
        if (drawable != null) {
            maxHeight = Math.max(maxHeight, drawable.getMinimumHeight());
            maxWidth = Math.max(maxWidth, drawable.getMinimumWidth());
        }

        setMeasuredDimension(resolveSize(maxWidth, widthMeasureSpec),
                resolveSize(maxHeight, heightMeasureSpec));
    }

    @Override
    protected void onLayout(boolean bChanged, int l, int t, int r, int b) {
        int count = super.getChildCount();

        for (int i = 0; i < count; ++i) {
            View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                int offset = 0;
                if (i == 1) {
                    offset = getMeasuredHeight();
                }
                int left = getPaddingLeft();
                int top = getPaddingTop() + offset;
                child.layout(left, top,
                        left + child.getMeasuredWidth(),
                        top + child.getMeasuredHeight());
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float resize = getHeight() - (m_dayView.getY());

        m_calendarView.setCalendarSize(1.0f, Math.max(0.5f, 1.0f - resize / getHeight()));
        m_calendarView.invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        startX = getX();
        startY = getY();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        float x = event.getRawX();
        float y = event.getRawY();

        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                prevX = x;
                prevY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                float disX = event.getRawX() - prevX;
                float disY = event.getRawY() - prevY;

                if (! bmoved){
                    bmoved = true;
                    if (Math.abs(disX) < Math.abs(disY)){
                        bdrawed = true;
                    }
                }

                if (bdrawed){
                    if (0 <= m_dayView.getY() + disY && m_dayView.getY() + disY < m_dayView.getHeight()) {
                        m_dayView.offsetTopAndBottom((int) disY);
                        float resize = getHeight() - (m_dayView.getY());
                        m_calendarView.setCalendarSize(1.0f, Math.max(0.5f, 1.0f - resize / getHeight()));
                        m_calendarView.invalidate();
                    }

                    m_dayView.accumulateY(disY);
                }

                prevX = event.getRawX();
                prevY = event.getRawY();
                break;
            case MotionEvent.ACTION_UP:
                if (bdrawed){
                    m_dayView.startScroll();
                }
                bmoved = false;
                bdrawed = false;
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }

        if (bdrawed) {
            return true;
        }

        return super.dispatchTouchEvent(event);
    }
}
