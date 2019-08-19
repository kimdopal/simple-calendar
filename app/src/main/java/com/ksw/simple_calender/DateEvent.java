package com.ksw.simple_calender;

import android.graphics.Color;

public class DateEvent {

    private String title;
    private String content;

    private boolean bRepeat;

    private DateAttr start;
    private DateAttr end;

    private Color color;

    DateEvent(String title, String content, boolean bRepeat, DateAttr s, DateAttr e) {
        this.title = title;
        this.content = content;
        this.bRepeat = bRepeat;
        this.start = s;
        this.end = e;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public boolean isbRepeat() {
        return bRepeat;
    }

    public DateAttr getStart() {
        return start;
    }

    public DateAttr getEnd() {
        return end;
    }

    public Color getColor() {
        return color;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setbRepeat(boolean bRepeat) {
        this.bRepeat = bRepeat;
    }

    public void setStart(DateAttr start) {
        this.start = start;
    }

    public void setEnd(DateAttr end) {
        this.end = end;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
