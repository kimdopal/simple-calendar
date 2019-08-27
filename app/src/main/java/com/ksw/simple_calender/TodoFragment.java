package com.ksw.simple_calender;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.TextView;

public class TodoFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private boolean mIsClicked;

    enum state{
        CLOSE,
        OPEN_START,
        OPEN_END
    }

    state mScollState;

    public TodoFragment() {
        // Required empty public constructor
    }

    public static TodoFragment newInstance(String param1, String param2) {
        TodoFragment fragment = new TodoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    ValueAnimator slideAnimator;
    ValueAnimator slideAnimator2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mScollState = state.CLOSE;

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_todo, container, false);
        mIsClicked = false;

        final Button startBtn  = v.findViewById(R.id.startBtn);
        final Button endBtn  = v.findViewById(R.id.endBtn);
        final SimpleDatePicker datePicker = v.findViewById(R.id.datePicker);

        datePicker.setListener(new SimpleDatePicker.ChangeDateListener() {
            @Override
            public void onChangeDate(DateAttr date) {
                if (mScollState == state.OPEN_START){
                    startBtn.setText(date.getYear() + "년 " + date.getMonth() + "월 " + date.getDay() + "일\n"
                            + date.getHour() + " : " + date.getMinute());
                    endBtn.setText(date.getYear() + "년 " + date.getMonth() + "월 " + date.getDay() + "일\n"
                            + date.getHour() + " : " + date.getMinute());
                }
                else if (mScollState == state.OPEN_END){
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

                if (!mIsClicked){
                    mScollState = state.CLOSE;
                    AnimatorSet set = new AnimatorSet();
                    set.play(slideAnimator);
                    set.setInterpolator(new AccelerateDecelerateInterpolator());
                    set.start();
                    mIsClicked = true;
                }
                else
                {
                    mScollState = state.OPEN_START;
                    AnimatorSet set = new AnimatorSet();
                    set.play(slideAnimator2);
                    set.setInterpolator(new AccelerateDecelerateInterpolator());
                    set.start();
                    mIsClicked = false;
                }
            }
        });

        endBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!mIsClicked){
                    mScollState = state.CLOSE;
                    AnimatorSet set = new AnimatorSet();
                    set.play(slideAnimator);
                    set.setInterpolator(new AccelerateDecelerateInterpolator());
                    set.start();
                    mIsClicked = true;
                }
                else
                {
                    mScollState = state.OPEN_END;
                    AnimatorSet set = new AnimatorSet();
                    set.play(slideAnimator2);
                    set.setInterpolator(new AccelerateDecelerateInterpolator());
                    set.start();
                    mIsClicked = false;
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
