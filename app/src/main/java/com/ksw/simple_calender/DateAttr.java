package com.ksw.simple_calender;

public class DateAttr {

    int year;
    int mouth;
    int day;
    int hour;
    int minute;

    DateAttr(int year, int mouth, int day, int hour, int minute){
        this.year = year;
        this.mouth = mouth;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
    }

    DateAttr(long dateTime) {
        this.minute = (int)(dateTime % 100);
        dateTime /= 100;
        this.hour = (int)(dateTime % 100);
        dateTime /= 100;
        this.day = (int)(dateTime % 100);
        dateTime /= 100;
        this.mouth = (int)(dateTime % 100);
        dateTime /= 100;
        this.year = (int)dateTime;
    }

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
        return (long)year * 100000000 + mouth * 1000000 + day * 10000 + hour * 100 + minute;
    }
}
