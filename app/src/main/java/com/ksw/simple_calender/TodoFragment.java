package com.ksw.simple_calender;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Date;

public class TodoFragment extends Fragment {

    private DateAttr startDate;
    private DateAttr endDate;

    enum state{
        CLOSE,
        OPEN_START,
        OPEN_END
    }

    state mScollState;

    public TodoFragment() {
        // Required empty public constructor
    }
    static TodoFragment fragment;

    public static TodoFragment newInstance() {
        fragment = new TodoFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mScollState = state.CLOSE;
        Date today = new Date();
        int y = today.getYear();
        int m = today.getMonth();
        int d = today.getDate();
        int h = today.getHours();
        int mn = today.getMinutes() % 5;

        startDate = new DateAttr(y + 1900,m + 1,d,h,mn);
        endDate = new DateAttr(y + 1900,m + 1,d,h,mn);

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_todo, container, false);
        final Button startBtn  = v.findViewById(R.id.startBtn);
        final Button endBtn  = v.findViewById(R.id.endBtn);
        final SimpleDatePicker datePicker = v.findViewById(R.id.datePicker);
        final EditText edit = v.findViewById(R.id.editText);
        final Button allDayBtn  = v.findViewById(R.id.allday);
        allDayBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity act = (MainActivity)getActivity();
                act.setEnableBtn();
                DateEventManager mngr = DateEventManager.getInstance();
                DateEvent e = new DateEvent(edit.getText().toString(), "ㅎㅎ", false, startDate, endDate);
                mngr.addEvent(e);
                getActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            }
        });

        datePicker.setListener(new SimpleDatePicker.ChangeDateListener() {
            @Override
            public void onChangeDate(DateAttr date) {
                if (mScollState == state.OPEN_START){
                    startDate.copyTo(date);
                    endDate.copyTo(date);
                    startBtn.setText(date.getYear() + "년 " + date.getMonth() + "월 " + date.getDay() + "일\n"
                            + date.getHour() + " : " + date.getMinute());
                    endBtn.setText(date.getYear() + "년 " + date.getMonth() + "월 " + date.getDay() + "일\n"
                            + date.getHour() + " : " + date.getMinute());
                }
                else if (mScollState == state.OPEN_END){
                    endDate.copyTo(date);
                    endBtn.setText(date.getYear() + "년 " + date.getMonth() + "월 " + date.getDay() + "일\n"
                            + date.getHour() + " : " + date.getMinute());
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
                    datePicker.setScrollIndex(startDate);
                }
                else{
                    mScollState = state.OPEN_START;
                    datePicker.setScrollIndex(startDate);
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
                    datePicker.setScrollIndex(endDate);
                }
                else{
                    mScollState = state.OPEN_END;
                    datePicker.setScrollIndex(endDate);
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
}
