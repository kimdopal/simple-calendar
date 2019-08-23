package com.ksw.simple_calender;

import android.graphics.Color;

public class DateEvent implements Comparable<DateEvent>{

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

    public String getDateStr(){
        return start.getHour() + ":" + start.getMinute() +"~" + end.getHour() + ":" + end.getMinute();
    }

    @Override
    public int compareTo(DateEvent dateEvent) {
        if (isSameRangeEvent(dateEvent)) return 0;
        if (this.isAllDay())return 1;
        if (dateEvent.isAllDay()) return -1;

        if (start.getHour() < dateEvent.start.getHour()){
            return 1;
        }
        else if(start.getHour() > dateEvent.start.getHour()){
            return -1;
        }
        else {
            if (start.getMinute() < dateEvent.start.getMinute()){
                return 1;
            }
            else if(start.getMinute() > dateEvent.start.getMinute()){
                return -1;
            }
        }

        return 0;
    }

    private boolean isAllDay(){
        if (start.isSameDay(end)){
            if (start.getHour() == 0 && start.getMinute() == 0 && end.getHour() == 23 && end.getMinute() == 59)
            {
                return true;
            }
        }

        return false;
    }

    private boolean isSameRangeEvent(DateEvent event){
        if (start.isSameDate(event.start) && end.isSameDay(event.end)){
            return true;
        }

        return false;
    }
}
