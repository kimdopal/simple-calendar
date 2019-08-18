package com.ksw.simple_calender;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class SimpleCalenderView extends View {
    Paint m_paint;
    float m_width;
    float m_height;
    float m_weekHeight;

    public SimpleCalenderView(Context context) {
        super(context);
    }

    public SimpleCalenderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SimpleCalenderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (m_paint == null) {
            m_paint = new Paint();
        }


        m_width = getWidth();
        m_height = getHeight();
        m_weekHeight = getHeight() / 20;


        drawCalenderBackLine(canvas);
        drawWeekText(canvas);
        drawCalenderAttr(canvas);
    }

    private void drawWeekText(Canvas canvas) {
        final float testTextSize = m_weekHeight / 2;
        m_paint.setTextAlign(Paint.Align.CENTER);
        m_paint.setTextSize(testTextSize);
        float dayWidth = m_width / 7;
        float tempX = dayWidth / 2;

        ArrayList<String> weekStr = new ArrayList<String> (Arrays.asList(
               "일", "월", "화", "수", "목", "금", "토"
        ));

        m_paint.setColor(Color.RED);
        canvas.drawText(weekStr.get(0), tempX,  m_weekHeight * 9 / 10, m_paint);

        m_paint.setColor(Color.BLACK);
        for (int i = 1; i < 7; ++i)
        {
            tempX+= dayWidth;
            canvas.drawText(weekStr.get(i), tempX, m_weekHeight * 9 / 10, m_paint);
        }
    }

    private void drawCalenderAttr(Canvas canvas) {
        Date today = new Date();

        int year = today.getYear();
        int month = today.getMonth();
        int date = today.getDate();

        Date myDate = new Date(year, month, 1);
        int day = myDate.getDay();
        myDate.setDate(32);
        int last =  32- myDate.getDate();
        int num = 1;

        for (int i = 0; i < 5 ; ++i) {
            for (int j = 0; j < 7; ++j) {
                if (i == 0 && j < day) {
                    continue;
                }

                if (num == date) {
                    drawMarkNumber(canvas, i, j);
                }

                if (num <= last) {
                    drawDayNumber(canvas, i, j, num);
                    num++;
                }
            }
        }

        drawSchedule(canvas);
    }

    private void drawSchedule(Canvas canvas) {
        m_paint.setColor(Color.BLUE);
        RectF rect = new RectF(40, 40, 110, 110);
        canvas.drawRoundRect(rect, 10, 10, m_paint);

        // 나름의 규칙이 필요함
        //drawScheduleLine(Canvas canvas, );
    }

    private void drawMarkNumber(Canvas canvas, int y, int x) {
        m_paint.setColor(Color.GREEN);
        m_paint.setAlpha(100);
        float dayWidth = m_width / 7;
        float dayHeight = (m_height - m_weekHeight) / 5;
        final float circleSize = 24f;
        float tempX = dayWidth / 2 + dayWidth * x;
        float tempY = circleSize / 2 + m_weekHeight + dayHeight * y;

        canvas.drawCircle( tempX,  tempY, circleSize, m_paint);
    }

    private void drawDayNumber(Canvas canvas, int y, int x, int num) {
        if (x == 0) {
            m_paint.setColor(Color.RED);
        }
        else {
            m_paint.setColor(Color.BLACK);
        }

        final float testTextSize = 24f;
        m_paint.setTextAlign(Paint.Align.CENTER);
        m_paint.setTextSize(testTextSize);
        float dayWidth = m_width / 7;
        float dayHeight = (m_height - m_weekHeight) / 5;
        float tempX = dayWidth * x;
        float tempY = dayHeight * y;

        canvas.drawText(num +"", tempX + dayWidth / 2, tempY + m_weekHeight + testTextSize, m_paint);
    }

    private void drawCalenderBackLine(Canvas canvas) {
        m_paint.setColor(Color.GRAY);
        float dayWidth = m_width / 7;
        float dayHeight = (m_height - m_weekHeight) / 5;

        float tempX = dayWidth;
        float tempY = m_weekHeight;

        for (int i = 0; i < 6; ++i)
        {
            canvas.drawLine(tempX, 0, tempX, m_height,  m_paint);
            tempX += dayWidth;
        }

        for (int i = 0; i < 5; ++i)
        {
            canvas.drawLine(0, tempY, m_width, tempY,  m_paint);
            tempY += dayHeight;
        }
    }
}
