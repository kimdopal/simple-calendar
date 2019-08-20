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
    private OverScroller m_scroller;
    private VelocityTracker m_velocityTracker;

    private int m_scaledMinimumFlingVelocity;
    private int m_scaledMaximumFlingVelocity;

    private float prevX;
    private float prevY;

    private float startX;
    private float startY;


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
        m_scroller = new OverScroller(context, new BounceInterpolator());

        ViewConfiguration mViewConfig = ViewConfiguration.get(getContext());
        m_scaledMinimumFlingVelocity = mViewConfig.getScaledMinimumFlingVelocity();
        m_scaledMaximumFlingVelocity = mViewConfig.getScaledMaximumFlingVelocity();

    }

    /**
     * Layout의 사이즈 지정하고 자식 뷰의 크기를 넘겨준다.
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
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
                int left = getPaddingLeft();
                int top = getPaddingTop();
                child.layout(left, top,
                        left + child.getMeasuredWidth(),
                        top + child.getMeasuredHeight());
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint p =  new Paint();
        p.setColor(Color.GREEN);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(3);
        canvas.drawRect(10, 10, 100, 100, p);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (null == m_velocityTracker) {
            m_velocityTracker = VelocityTracker.obtain();
        }

        m_velocityTracker.addMovement(event);

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

                offsetLeftAndRight((int) disX);
                offsetTopAndBottom((int) disY);
                prevX = event.getRawX();
                prevY = event.getRawY();
                break;
            case MotionEvent.ACTION_UP:
                m_velocityTracker.computeCurrentVelocity(1000, m_scaledMaximumFlingVelocity);
                int velocityX = (int)m_velocityTracker.getXVelocity();
                int velocityY = (int)m_velocityTracker.getYVelocity();

                int ex = (int) getX();
                int ey = (int) getY();
                int wx = (int) (ex - startX);
                int wy = (int) (ey - startY);
                m_scroller.startScroll(ex, ey, -wx, -wy);
                invalidate();
                break;
            case MotionEvent.ACTION_CANCEL:
                if(null != m_velocityTracker) {
                    m_velocityTracker.clear();
                    m_velocityTracker.recycle();
                    m_velocityTracker = null;
                }
                break;
        }

        return true;
    }

    @Override
    public void computeScroll() {
        if (m_scroller.computeScrollOffset()) {
            setX(m_scroller.getCurrX());
            setY(m_scroller.getCurrY());
            invalidate();
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        startX = getX();
        startY = getY();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }
}
