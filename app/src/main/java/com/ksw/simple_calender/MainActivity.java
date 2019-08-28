package com.ksw.simple_calender;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private FloatingActionButton mBtn;
    private DateAttr mDate;
    private MenuItem mItem;
    SimpleCalenderLayout mLayout;
    SimpleCalenderView mSimpleView;

    enum state{
        CALENDAER,
        ADDDATE
    }

    state mState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDate = new DateAttr(0);
        DateEventManager mngr = DateEventManager.getInstance();
        mngr.init(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        mState = state.CALENDAER;
        // test
        DateAttr startAttr = new DateAttr(201908201430L);
        DateAttr endAttr = new DateAttr(201908231430L);
        DateEvent e = new DateEvent("dasd", "ㅎㅎ", false, startAttr, endAttr);
        mngr.addEvent(e);
        DateEvent e1 = new DateEvent("메롱", "ㅎㅎ", false, startAttr, endAttr);
        mngr.addEvent(e1);
        DateEvent e2 = new DateEvent("메롱", "ㅎㅎ", false, startAttr, endAttr);
        mngr.addEvent(e2);

        mLayout = findViewById(R.id.simpleCalenderLayout);
        mSimpleView = findViewById(R.id.simpleCalenderView);

        mBtn = (FloatingActionButton)findViewById(R.id.fab);
        mBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                mLayout.setEnabled(false);
                mState = state.ADDDATE;
                mBtn.setEnabled(false);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setTitle("일정 추가");
                getSupportFragmentManager().beginTransaction()
                        //.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.enter, R.anim.exit)
                        .addToBackStack(null)
                        .replace(R.id.contents, TodoFragment.newInstance())
                        .commit();
            }
        });

        SimpleCalenderView view = (SimpleCalenderView)findViewById(R.id.simpleCalenderView);
        view.setListener(new SimpleCalenderView.CalendarMonthListener() {
            @Override
            public void changeMonth(DateAttr date) {
                mDate.copyTo(date);
                getSupportActionBar().setTitle(date.getYear() + 1900 + "-" + (date.getMonth() + 1));
            }
        });
    }

    public void disableFragment(){
        mState = state.CALENDAER;
        mLayout.setEnabled(true);
        mBtn.setEnabled(true);
        mItem.setTitle("오늘");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle(mDate.getYear() + 1900 + "-" + (mDate.getMonth() + 1));
        mSimpleView.invalidate();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        mItem = menu.findItem(R.id.action_btn01);
        if(mState == state.CALENDAER){
            mItem.setTitle("오늘");
        }
        else{
            mItem.setTitle("추가");
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (mState == state.CALENDAER){

            if (id == R.id.action_btn01){
                Date today = new Date();
                int year = today.getYear();
                int month = today.getMonth();
                int date = today.getDate();
                mSimpleView.setDate(new DateAttr(year, month, date, 0 , 0));
                return true;
            }
        }

        return false;
    }
}
