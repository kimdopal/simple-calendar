package com.ksw.simple_calender;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.OverScroller;
import android.widget.Toast;

import androidx.core.view.ViewCompat;

import java.util.ArrayList;

public class SimpleScrollView extends View {
    GestureDetector gestureDetector;
    VelocityTracker m_velocityTracker;
    OverScroller overScroller;
    int AXIS_X_MIN = 0;
    int AXIS_Y_MIN = 0;
    int AXIS_X_MAX = 240;
    int AXIS_Y_MAX = 240;
    private RectF mScrollerStartViewport =
            new RectF(AXIS_X_MIN, AXIS_Y_MIN, AXIS_X_MAX, AXIS_Y_MAX);
    private RectF mCurrentViewport =
            new RectF(AXIS_X_MIN, AXIS_Y_MIN, AXIS_X_MAX, AXIS_Y_MAX);

    int startX;
    int startY;
    int index = 0;
    ArrayList<Integer> contents;

    public SimpleScrollView(Context context) {
        super(context);
        init(context);
    }

    public SimpleScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SimpleScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(final Context context) {
        contents = new ArrayList<Integer>(5);
        int number = 1;
        for (Integer i : contents){
            i = number;
            number++;
        }

        overScroller = new OverScroller(context, new LinearInterpolator());
        gestureDetector = new GestureDetector(context, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent motionEvent) {
                overScroller.forceFinished(true);
                mScrollerStartViewport.set(mCurrentViewport);
                startX = (int)motionEvent.getX();
                startY = (int)motionEvent.getY();
                //Toast.makeText(context, "on Dowm", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public void onShowPress(MotionEvent motionEvent) {
                Toast.makeText(context, "onShowPress", Toast.LENGTH_SHORT).show();
            }

            @Override
            public boolean onSingleTapUp(MotionEvent motionEvent) {
                Toast.makeText(context, "onSingleTapUp", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
                Log.d("onScroll","onScroll : " + motionEvent.getY() + "/" + motionEvent1.getY() + "/" + v +"/" + v1);
               //scroll(motionEvent, motionEvent1);
                offsetTopAndBottom((int)motionEvent1.getY() - (int)motionEvent.getY());
                return false;
            }

            @Override
            public void onLongPress(MotionEvent motionEvent) {
                Toast.makeText(context, "onLongPress", Toast.LENGTH_SHORT).show();

            }

            @Override
            public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {

                Log.d("onFling","onFling : " + startX + "/" + startY + "/" + v +"/" + v1);
                overScroller.startScroll(getScrollX(), getScrollY(), -getScrollX(), -getScrollY());
                return false;
            }
        });
    }

    @Override
    public void computeScroll() {
        Log.d("computeScroll","computeScroll : " + overScroller.getCurrX() + "/" + overScroller.getCurrY());
        super.computeScroll();
        //setY(overScroller.getCurrY());
        invalidate();
        //int currY = overScroller.getCurrY();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        /*
        if (m_velocityTracker == null) {
            m_velocityTracker = VelocityTracker.obtain();
        }

        m_velocityTracker.addMovement(event);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                m_velocityTracker.computeCurrentVelocity(1);
                float velocity = m_velocityTracker.getXVelocity();
                break;
            case MotionEvent.ACTION_UP:
                if (m_velocityTracker != null) {
                    m_velocityTracker.recycle();
                    m_velocityTracker = null;
                }

                break;
        }
        return true;
        */
        gestureDetector.onTouchEvent(event);
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint p = new Paint();
        canvas.drawRect(10, 10 ,30 , 30, p);
    }
}
