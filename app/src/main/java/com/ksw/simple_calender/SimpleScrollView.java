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
import java.util.ArrayList;

public class SimpleScrollView extends View {
    private GestureDetector mGestureDetector;
    private OverScroller mOverScroller;
    private int mStartScroll;
    private int mMoveScroll;
    private int mNumGap = 100;
    private int mCenterLineStart = mNumGap * 2;
    private int mCenterLineEnd = mNumGap * 3;

    private boolean mIsDown;
    private boolean mIsAdjust;
    private boolean mIsFiling;

    private int mMinScrollValue;
    private int mMaxScrollValue;

    ArrayList<String> mOutStrList;

    public SimpleScrollView(Context context) {
        super(context);
        init(context);
    }

    public SimpleScrollView(Context context, ArrayList<String> arr) {
        super(context);
        mOutStrList = arr;
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

    public void setIndex(int idx){
        mMoveScroll = mNumGap * idx;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        mMaxScrollValue = mNumGap * (mOutStrList.size() - 1);

    }

    private void init(final Context context) {
        mMinScrollValue = 0;
        mMaxScrollValue = 10000;

        mIsDown = false;
        mIsAdjust = false;
        mIsFiling = false;

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
            }

            @Override
            public boolean onSingleTapUp(MotionEvent motionEvent) {
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
                Log.d("fling","fling : " + mMoveScroll + "/" + motionEvent1.getY() + "/" + v1);
                mOverScroller.fling (0, mMoveScroll, 0, -(int) v1,
                        0, 0, 0, mMaxScrollValue);
                mIsFiling = true;
                invalidate();
                return true;
            }
        });
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mOverScroller.computeScrollOffset ()) {
            mMoveScroll = mOverScroller.getCurrY();

            Log.d("onScrollChanged","onScrollChanged : " + mMoveScroll);
            changeIndex();
            invalidate();
        }
        else {
            if (! mIsDown && !mIsAdjust){
                mMoveScroll = Math.max(0, mMoveScroll);
                mMoveScroll = Math.min(mMaxScrollValue, mMoveScroll);
                mOverScroller.startScroll(0, mMoveScroll, 0,
                        (mMoveScroll + mNumGap/2)/mNumGap * mNumGap - mMoveScroll);
                mIsAdjust = true;
                mIsFiling = false;
                invalidate();
            }
        }
    }

    private void changeIndex() {

        if (getParent() instanceof SimpleDatePicker){
            SimpleDatePicker picker = (SimpleDatePicker)getParent();
            picker.changeIndex(this, (int)mMoveScroll / mNumGap);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);

        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                mIsDown = true;
                mIsAdjust = false;
                mIsFiling = false;
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                if (! mIsFiling) {
                    mMoveScroll = Math.max(mMinScrollValue, mMoveScroll);
                    mMoveScroll = Math.min(mMaxScrollValue, mMoveScroll);
                    mOverScroller.startScroll(0, mMoveScroll, 0,
                            (mMoveScroll + mNumGap/2) / mNumGap * mNumGap - mMoveScroll);
                    changeIndex();
                    invalidate();
                    mIsAdjust = true;
                }

                mIsDown = false;
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }

        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint p = new Paint();
        p.setTextSize(mNumGap);
        int pos = mMoveScroll % mNumGap;
        int idx = (mMoveScroll / mNumGap);

        for (int i = -2;i < 4; ++i) {
            if (i + idx < 0)continue;
            if (i + idx < mOutStrList.size())
                canvas.drawText(mOutStrList.get(idx + i), 10, mCenterLineEnd + mNumGap * i - pos, p);
        }

        canvas.drawLine(0, mCenterLineStart, getWidth(), mCenterLineStart, p);
        canvas.drawLine(0, mCenterLineEnd, getWidth(), mCenterLineEnd, p);
    }
}
