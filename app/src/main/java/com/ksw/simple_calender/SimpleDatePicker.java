package com.ksw.simple_calender;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class SimpleDatePicker extends LinearLayout {

    public SimpleDatePicker(Context context) {
        super(context);
        init(context);
    }

    public SimpleDatePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SimpleDatePicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        for (int i = 1; i <= 2; i++) {
            SimpleScrollView view = new SimpleScrollView(context);

            final int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 240, getResources().getDisplayMetrics());
            addView(view, 0, new ViewGroup.LayoutParams(width, ViewGroup.LayoutParams.MATCH_PARENT));
        }
    }
}
