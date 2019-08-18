package com.ksw.simple_calender;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public class SimpleCalenderLayout extends ViewGroup {
    public SimpleCalenderLayout(Context context) {
        super(context);
    }

    public SimpleCalenderLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SimpleCalenderLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                int left = getPaddingLeft();
                int top = getPaddingTop();
                child.layout(left,
                        top,
                            left + child.getMeasuredWidth(),
                            top + child.getMeasuredHeight());
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }
}
