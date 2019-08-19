package com.ksw.simple_calender;

public class DateAttr {

    int year;
    int mouth;
    int day;
    int hour;
    int minute;

    public int getMouth() {
        return mouth;
    }

    public void setMouth(int mouth) {
        this.mouth = mouth;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public long getDateTime()
    {
        return year * 10000000 + mouth * 100000 + day * 1000 + hour * 10 + minute;
    }
}
