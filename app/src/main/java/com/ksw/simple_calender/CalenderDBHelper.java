package com.ksw.simple_calender;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/*
 TODO : 캘린더 이벤트 정보를 넣고 뺼 수 있도록 함
*/
public class CalenderDBHelper extends SQLiteOpenHelper {

    public CalenderDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                            int version){
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
