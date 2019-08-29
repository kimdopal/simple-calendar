package com.ksw.simple_calender;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.util.Date;

public class TodoFragment extends Fragment implements CompoundButton.OnCheckedChangeListener{
    private static DateEvent gEvent;
    private static DateEvent gprevEvent;

    enum state{
        CLOSE,
        OPEN_START,
        OPEN_END
    }
    state mScollState;

    final static int blueColor = 0xFF1E90FF;
    final static int yellowColor = 0xFFFFD700;
    final static int redColor = 0xFFFF6347;
    final static int greenColor = 0xFF32CD32;
    final static int orangeColor = 0xFFFFA500;

    private static boolean mIsAdd;

    CheckBox blueBtn;
    CheckBox yellowBtn;
    CheckBox redBtn;
    CheckBox greenBtn;
    CheckBox orangeBtn;


    public TodoFragment() {
    }

    static TodoFragment fragment;

    public static TodoFragment newInstance(DateEvent event) {
        fragment = new TodoFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);

        if (event == null){
            // 새로운 이벤트 생성
            Date today = new Date();
            int y = today.getYear() + 1900;
            int m = today.getMonth() + 1;
            int d = today.getDate();
            int h = today.getHours();
            int mn = today.getMinutes() / 5 * 5;

            DateAttr startDate = new DateAttr(y,m,d,h,mn);
            DateAttr endDate = new DateAttr(y,m,d,h,mn);
            gEvent = new DateEvent("", "", blueColor, startDate, endDate);
            mIsAdd = true;
        }
        else{
            // 이벤트 변경
            mIsAdd = false;
            gprevEvent = event;

            DateAttr startDate = new DateAttr(0);
            DateAttr endDate = new DateAttr(0);
            startDate.copyTo(gprevEvent.getStart());
            endDate.copyTo(gprevEvent.getEnd());
            gEvent = new DateEvent(gprevEvent.getTitle(), "", gprevEvent.getColor(), startDate, endDate);
        }

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    ValueAnimator slideAnimator;
    ValueAnimator slideAnimator2;
    private EditText edit;

    public String getTimeStr(int time){
        String timeStr = time + "";
        if (timeStr.length() == 1) {
            timeStr = "0" + timeStr;
        }

        return timeStr;
    }

    public String getDateAttrStr(DateAttr date){
        String month = getTimeStr(date.getMonth());
        String day = getTimeStr(date.getDay());
        String hour = getTimeStr(date.getHour());
        String minute = getTimeStr(date.getMinute());
        return date.getYear() + "년 " + month + "월 " + day + "일\n"
                    + hour + " : " + minute;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        mScollState = state.CLOSE;

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_todo, container, false);
        final Button startBtn  = v.findViewById(R.id.startBtn);
        final Button endBtn  = v.findViewById(R.id.endBtn);
        final SimpleDatePicker datePicker = v.findViewById(R.id.datePicker);
        edit = v.findViewById(R.id.editText);
        edit.setText(gEvent.getTitle());

        Button removeBtn = v.findViewById(R.id.remove);
        if (mIsAdd) {
            removeBtn.setEnabled(false);
        }
        else{
            removeBtn.setEnabled(true);
        }

        removeBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                DateEventManager mngr = DateEventManager.getInstance();
                mngr.removeEvent(gprevEvent);
                MainActivity act = (MainActivity)getActivity();
                act.disableFragment();
                getActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();

            }
        });

        Button allDayBtn  = v.findViewById(R.id.allday);
        allDayBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                DateAttr dateStart = gEvent.getStart();
                DateAttr dateEnd = gEvent.getEnd();

                dateStart.setHour(0);
                dateStart.setMinute(0);
                dateEnd.copyTo(dateStart);
                dateEnd.setHour(23);
                dateEnd.setMinute(59);
                startBtn.setText(getDateAttrStr(gEvent.getStart()));
                endBtn.setText(getDateAttrStr(gEvent.getEnd()));
            }
        });

        Button todayBtn  = v.findViewById(R.id.today);
        todayBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                DateAttr dateStart = gEvent.getStart();
                DateAttr dateEnd = gEvent.getEnd();

                Date today = new Date();
                int y = today.getYear() + 1900;
                int m = today.getMonth() + 1;
                int d = today.getDate();
                dateStart.setYear(y);
                dateStart.setMonth(m);
                dateStart.setDay(d);
                dateStart.setHour(0);
                dateStart.setMinute(0);
                dateEnd.copyTo(dateStart);
                dateEnd.setHour(23);
                dateEnd.setMinute(59);
                startBtn.setText(getDateAttrStr(gEvent.getStart()));
                endBtn.setText(getDateAttrStr(gEvent.getEnd()));
            }
        });
        startBtn.setText(getDateAttrStr(gEvent.getStart()));
        endBtn.setText(getDateAttrStr(gEvent.getEnd()));

        datePicker.setListener(new SimpleDatePicker.ChangeDateListener() {
            @Override
            public void onChangeDate(DateAttr date) {
                if (mScollState == state.OPEN_START){
                    gEvent.getStart().copyTo(date);
                    gEvent.getEnd().copyTo(date);
                    startBtn.setText(getDateAttrStr(date));
                    endBtn.setText(getDateAttrStr(date));
                }
                else if (mScollState == state.OPEN_END){
                    if (date.getDateTime() < gEvent.getStart().getDateTime()){
                        endBtn.setTextColor(Color.RED);
                        gEvent.getEnd().copyTo(gEvent.getStart());
                        endBtn.setText(getDateAttrStr(gEvent.getEnd()));
                    }
                    else{
                        endBtn.setTextColor(Color.BLACK);
                        gEvent.getEnd().copyTo(date);
                        endBtn.setText(getDateAttrStr(date));
                    }
                }
            }
        });

        slideAnimator = ValueAnimator
                .ofInt(500, 0)
                .setDuration(300);

        slideAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue();
                datePicker.getLayoutParams().height = value.intValue();
                datePicker.requestLayout();
            }
        });

        slideAnimator2 = ValueAnimator
                .ofInt(0, 500)
                .setDuration(300);

        slideAnimator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue();
                datePicker.getLayoutParams().height = value.intValue();
                datePicker.requestLayout();
            }
        });

        startBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                // 열려 있음
                if (mScollState == state.OPEN_START){
                    mScollState = state.CLOSE;
                    AnimatorSet set = new AnimatorSet();
                    set.play(slideAnimator);
                    set.setInterpolator(new AccelerateDecelerateInterpolator());
                    set.start();
                }
                // 닫혀 있음
                else if (mScollState == state.CLOSE){
                    mScollState = state.OPEN_START;
                    AnimatorSet set = new AnimatorSet();
                    set.play(slideAnimator2);
                    set.setInterpolator(new AccelerateDecelerateInterpolator());
                    set.start();
                    datePicker.setScrollIndex(gEvent.getStart());
                }
                else{
                    mScollState = state.OPEN_START;
                    datePicker.setScrollIndex(gEvent.getStart());
                }
            }
        });

        endBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mScollState == state.OPEN_END){
                    mScollState = state.CLOSE;
                    AnimatorSet set = new AnimatorSet();
                    set.play(slideAnimator);
                    set.setInterpolator(new AccelerateDecelerateInterpolator());
                    set.start();
                }
                else if (mScollState == state.CLOSE){
                    mScollState = state.OPEN_END;
                    AnimatorSet set = new AnimatorSet();
                    set.play(slideAnimator2);
                    set.setInterpolator(new AccelerateDecelerateInterpolator());
                    set.start();
                    datePicker.setScrollIndex(gEvent.getEnd());
                }
                else{
                    mScollState = state.OPEN_END;
                    datePicker.setScrollIndex(gEvent.getEnd());
                }
            }
        });

        setCallBackColorBtn(v);

        return v;
    }

    void setCallBackColorBtn(View v){
        blueBtn = v.findViewById(R.id.blueColor);
        yellowBtn = v.findViewById(R.id.yellowColor);
        redBtn = v.findViewById(R.id.redColor);
        greenBtn = v.findViewById(R.id.greenColor);
        orangeBtn = v.findViewById(R.id.orangeColor);

        blueBtn.setBackgroundColor(blueColor);
        yellowBtn.setBackgroundColor(yellowColor);
        redBtn.setBackgroundColor(redColor);
        greenBtn.setBackgroundColor(greenColor);
        orangeBtn.setBackgroundColor(orangeColor);

        blueBtn.setOnCheckedChangeListener(this);
        yellowBtn.setOnCheckedChangeListener(this);
        redBtn.setOnCheckedChangeListener(this);
        greenBtn.setOnCheckedChangeListener(this);
        orangeBtn.setOnCheckedChangeListener(this);

        switch (gEvent.getColor()) {
            case blueColor:
                blueBtn.setChecked(true);
                break;
            case yellowColor:
                yellowBtn.setChecked(true);
                break;
            case greenColor:
                greenBtn.setChecked(true);
                break;
            case redColor:
                redBtn.setChecked(true);
                break;
            case orangeColor:
                orangeBtn.setChecked(true);
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()){
            case R.id.blueColor:
                if (b) {
                    yellowBtn.setChecked(false);
                    redBtn.setChecked(false);
                    greenBtn.setChecked(false);
                    orangeBtn.setChecked(false);
                    gEvent.setColor(blueColor);
                }
                break;
            case R.id.yellowColor:
                if (b) {
                    blueBtn.setChecked(false);
                    redBtn.setChecked(false);
                    greenBtn.setChecked(false);
                    orangeBtn.setChecked(false);
                    gEvent.setColor(yellowColor);
                }
                break;
            case R.id.redColor:
                if (b) {
                    blueBtn.setChecked(false);
                    yellowBtn.setChecked(false);
                    greenBtn.setChecked(false);
                    orangeBtn.setChecked(false);
                    gEvent.setColor(redColor);
                }
                break;
            case R.id.greenColor:
                if (b) {
                    blueBtn.setChecked(false);
                    yellowBtn.setChecked(false);
                    redBtn.setChecked(false);
                    orangeBtn.setChecked(false);
                    gEvent.setColor(greenColor);
                }
                break;
            case R.id.orangeColor:
                if (b) {
                    blueBtn.setChecked(false);
                    yellowBtn.setChecked(false);
                    redBtn.setChecked(false);
                    greenBtn.setChecked(false);
                    gEvent.setColor(orangeColor);
                }
                break;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            MainActivity act = (MainActivity)getActivity();
            act.disableFragment();
            getActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();

            return true;
        }

        if (id == R.id.action_btn01){
            MainActivity act = (MainActivity)getActivity();
            act.disableFragment();
            DateEventManager mngr = DateEventManager.getInstance();
            if (mIsAdd){
                gEvent.setTitle(edit.getText().toString());
                mngr.addEvent(gEvent);
            }else{
                gEvent.setTitle(edit.getText().toString());
                mngr.changeEvent(gprevEvent, gEvent);
            }
            getActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }

        return super.onOptionsItemSelected(item);
    }
}
