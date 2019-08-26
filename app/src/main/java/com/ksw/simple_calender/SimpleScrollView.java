package com.ksw.simple_calender;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.OverScroller;
import android.widget.Toast;

public class SimpleScrollView extends View {
    GestureDetector mGestureDetector;
    OverScroller mOverScroller;
    int mStartScroll;

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
        mOverScroller = new OverScroller(context, new LinearInterpolator());
        mGestureDetector = new GestureDetector(context, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent motionEvent) {
                mOverScroller.forceFinished(true);
                mStartScroll = (int)getScrollY();
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
                setScrollY((int)-(motionEvent1.getY() - motionEvent.getY()) + mStartScroll);
                invalidate();
                return true;
            }

            @Override
            public void onLongPress(MotionEvent motionEvent) {
            }

            @Override
            public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
                int currentY = getScrollY();
                int targetY = 0;
                if (v1 > 0) {
                    if (currentY <= 0) {
                        return false;
                    }

                    targetY = currentY / 1000 * 1000;
                }
                else {
                    if (currentY >= 1000 * (3 - 1)) {
                        return false;
                    }

                    targetY = 1000 * (currentY / 1000 + 1);
                }

                if (currentY == targetY) {
                    targetY -= 1000;
                }

                Log.d("fling","fling : " + currentY + "/" + v1 + "/" + targetY );
                mOverScroller.fling (0, currentY, 0, -(int) v1,
                        0, 0, Math.min (currentY, targetY), Math.max (currentY, targetY));
                invalidate();
                return true;
            }
        });
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mOverScroller.computeScrollOffset ()) {
            Log.d("computeScroll","computeScroll : " + mOverScroller.getCurrX() + "/" + mOverScroller.getCurrY());
            scrollTo (0, mOverScroller.getCurrY());
            invalidate();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint p = new Paint();
        canvas.drawRect(10, 10 ,30 , 30, p);
    }
}
