package com.ksw.simple_calender;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
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
import android.widget.EditText;

import java.util.Date;

public class TodoFragment extends Fragment {
    private static DateEvent gEvent;
    private static DateEvent gprevEvent;

    enum state{
        CLOSE,
        OPEN_START,
        OPEN_END
    }
    state mScollState;

    private static boolean mIsAdd;

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
            int mn = today.getMinutes() % 5;

            DateAttr startDate = new DateAttr(y,m,d,h,mn);
            DateAttr endDate = new DateAttr(y,m,d,h,mn);
            gEvent = new DateEvent("", "", false, startDate, endDate);
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
            gEvent = new DateEvent(gprevEvent.getTitle(), "", false, startDate, endDate);
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

        final Button allDayBtn  = v.findViewById(R.id.allday);
        allDayBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        datePicker.setListener(new SimpleDatePicker.ChangeDateListener() {
            @Override
            public void onChangeDate(DateAttr date) {
                String month = date.getMonth() + "";
                if (month.length() == 1) {
                    month = "0" + month;
                }

                String day = date.getDay() + "";
                if (day.length() == 1){
                    day = "0" + day;
                }


                String hour = date.getHour() + "";
                if (hour.length() == 1){
                    hour = "0" + hour;
                }


                String minute = date.getMinute() + "";
                if (minute.length() == 1){
                    minute = "0" + minute;
                }

                if (mScollState == state.OPEN_START){
                    gEvent.getStart().copyTo(date);
                    gEvent.getEnd().copyTo(date);
                    startBtn.setText(date.getYear() + "년 " + month + "월 " + day + "일\n"
                            + hour + " : " + minute);
                    endBtn.setText(date.getYear() + "년 " + month + "월 " + day + "일\n"
                            + hour + " : " + minute);
                }
                else if (mScollState == state.OPEN_END){
                    gEvent.getEnd().copyTo(date);
                    endBtn.setText(date.getYear() + "년 " + month + "월 " + day + "일\n"
                            + hour + " : " + minute);
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

        return v;
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
