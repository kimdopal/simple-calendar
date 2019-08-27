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
    private GestureDetector mGestureDetector;
    private OverScroller mOverScroller;
    private int mStartScroll;
    private int mMoveScroll;

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
        mMoveScroll = 0;
        mOverScroller = new OverScroller(context, new LinearInterpolator());
        mGestureDetector = new GestureDetector(context, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent motionEvent) {
                mOverScroller.forceFinished(true);
                mStartScroll = mMoveScroll;
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
                mMoveScroll = (int)-(motionEvent1.getY() - motionEvent.getY()) + mStartScroll;

                Log.d("onScroll","스크롤 위치 : " + mMoveScroll
                        + "/ 손까락 위치 : "  + motionEvent.getY() + "/" + motionEvent1.getY() + "/속도 : " + v +"/" + v1);

                invalidate();
                return true;
            }

            @Override
            public void onLongPress(MotionEvent motionEvent) {
            }

            @Override
            public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
                int currentY = getScrollY();
                int maxY = 10000;
                if (v1 < 0){

                }
                else {

                }

                Log.d("fling","fling : " + mMoveScroll + "/" + motionEvent1.getY() + "/" + v1);
                mOverScroller.fling (0, mMoveScroll, 0, -(int) v1,
                        0, 0, 0, 10000);
                invalidate();
                return true;
            }
        });
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mOverScroller.computeScrollOffset ()) {
            //Log.d("computeScroll","computeScroll : " + mOverScroller.getCurrX() + "/" + mOverScroller.getCurrY());
            mMoveScroll = mOverScroller.getCurrY();
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
        p.setTextSize(40);
        canvas.drawText(mMoveScroll+"", 10, 50, p);

        int pos = mMoveScroll % 40;
        for (int i = 0;i < 5; ++i) {
            canvas.drawText((mMoveScroll / 40) + i +"", 10, 100 + 40 * i + pos, p);
        }
    }
}
