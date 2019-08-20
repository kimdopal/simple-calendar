package com.ksw.simple_calender;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class SimpleDayView extends View {
    public SimpleDayView(Context context) {
        super(context);
    }

    public SimpleDayView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SimpleDayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
}
