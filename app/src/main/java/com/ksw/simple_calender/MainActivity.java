package com.ksw.simple_calender;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DateEventManager mngr = DateEventManager.getInstance();
        mngr.init(this);

        // test
        DateAttr startAttr = new DateAttr(201908201430L);
        DateAttr endAttr = new DateAttr(201908231430L);
        DateEvent e = new DateEvent("dasd", "ㅎㅎ", false, startAttr, endAttr);
        mngr.addEvent(e);
        DateEvent e1 = new DateEvent("메롱", "ㅎㅎ", false, startAttr, endAttr);
        mngr.addEvent(e1);
        DateEvent e2 = new DateEvent("메롱", "ㅎㅎ", false, startAttr, endAttr);
        mngr.addEvent(e2);

        btn = (FloatingActionButton)findViewById(R.id.fab);
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                btn.setEnabled(false);
                getSupportFragmentManager().beginTransaction()
                        //.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.enter, R.anim.exit)
                        .addToBackStack(null)
                        .replace(R.id.contents, TodoFragment.newInstance())
                        .commit();
            }
        });
    }

    public void setEnableBtn(){
        btn.setEnabled(true);
        View vg = findViewById(R.id.simpleCalenderView);
        vg.invalidate();
    }
}
