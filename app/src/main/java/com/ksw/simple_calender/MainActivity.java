package com.ksw.simple_calender;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DateEventManager mngr = DateEventManager.getInstance();
        mngr.init(this);

        // test
        DateAttr startAttr = new DateAttr(201908201430L);
        DateAttr endAttr = new DateAttr(201908231430L);
        DateEvent e = new DateEvent("메롱", "ㅎㅎ", false, startAttr, endAttr);
        mngr.addEvent(e);
    }
}
