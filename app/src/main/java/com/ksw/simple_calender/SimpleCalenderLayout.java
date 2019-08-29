package com.ksw.simple_calender;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public class SimpleCalenderLayout extends ViewGroup {

    private float mPrevX;
    private float mPrevY;

    private float startX;
    private float startY;
    private boolean mIsMoved;
    private boolean mIsDrawed;

    private boolean mIsEnable;

    private SimpleCalenderView mCalendarView;
    private SimpleDayView mDayView;

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
        mIsMoved = false;
        mIsDrawed = false;
        mIsEnable = true;
    }

    /**
     * Layout의 사이즈 지정하고 자식 뷰의 크기를 넘겨준다.
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mCalendarView = (SimpleCalenderView) getChildAt(0);
        mDayView = (SimpleDayView)getChildAt(1);

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
        float resize = getHeight() - (mDayView.getY());

        mCalendarView.setCalendarSize(1.0f, Math.max(0.5f, 1.0f - resize / getHeight()));
        mCalendarView.invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        startX = getX();
        startY = getY();
    }

    @Override
    public void setEnabled(boolean enabled) {
        mIsEnable = enabled;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (! mIsEnable) return true;
        float x = event.getRawX();
        float y = event.getRawY();
        boolean bup = false;

        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                mPrevX = x;
                mPrevY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                float disX = event.getRawX() - mPrevX;
                float disY = event.getRawY() - mPrevY;

                if (!mIsMoved){
                    mIsMoved = true;
                    if (Math.abs(disX) < Math.abs(disY)){
                        mIsDrawed = true;
                    }
                }

                if (mIsDrawed){
                    if (0 <= mDayView.getY() + disY && mDayView.getY() + disY < mDayView.getHeight()) {
                        mDayView.offsetTopAndBottom((int) disY);
                        float resize = getHeight() - (mDayView.getY());
                        mCalendarView.setCalendarSize(1.0f, Math.max(0.5f, 1.0f - resize / getHeight()));
                        mCalendarView.invalidate();
                    }

                    mDayView.accumulateY(disY);
                }

                mPrevX = event.getRawX();
                mPrevY = event.getRawY();
                break;
            case MotionEvent.ACTION_UP:
                if (mIsDrawed){
                    mDayView.startScroll();
                }
                mIsMoved = false;
                bup = true;
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }

        if (mIsDrawed) {
            if (bup){
                mIsDrawed = false;
            }
            return true;
        }

        return super.dispatchTouchEvent(event);
    }
}
